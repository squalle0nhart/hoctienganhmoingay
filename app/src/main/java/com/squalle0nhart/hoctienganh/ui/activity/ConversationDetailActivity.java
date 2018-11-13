package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.ConversationInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.ConversationDetailAdapter;
import com.squalle0nhart.hoctienganh.ui.view.EqualSpaceItemDecoration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by squalleonhart on 3/19/2017.
 */

public class ConversationDetailActivity extends Activity {
    Context mContext;
    RecyclerView mRecycleView;
    ConversationDetailAdapter mConversationListAdapter;
    ArrayList<ConversationInfo> mListPharse;
    int mID;
    MediaPlayer mMediaPlayer;
    ImageView ivPlay, ivReplay;
    MaterialProgressBar pbProgress;

    Handler mHandler;
    public int mStartTime, mEndTime;
    public static boolean sIsPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);
        mContext = this;
        mID = getIntent().getIntExtra(Constants.EXTRAS_CONVERSATION, -1);
        int i = mContext.getResources().getIdentifier("lesson_"+(mID+1),"raw", mContext.getPackageName());
        mMediaPlayer = MediaPlayer.create(mContext, i);
        mHandler = new Handler();
        mEndTime = mMediaPlayer.getDuration();
        initView();
    }

    /***********************************************************************************************
     *  Dải phân cách
     **********************************************************************************************/

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.rv_grammar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new EqualSpaceItemDecoration(10));
        mListPharse = loadConversationFromXML();
        mConversationListAdapter = new ConversationDetailAdapter(mContext, mListPharse);
        mRecycleView.setAdapter(mConversationListAdapter);
        pbProgress = (MaterialProgressBar) findViewById(R.id.pb_progress);
        pbProgress.setMax(mMediaPlayer.getDuration());
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivReplay = (ImageView) findViewById(R.id.iv_replay);

        /**
         * xử lý sự kiện nút play
         */
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sIsPlaying) {
                    startMediaPlayer();
                    sIsPlaying = true;
                    mMediaPlayer.start();
                    ivPlay.setImageResource(R.drawable.ic_pause);
                } else {
                    stopMediaPlayer();
                    sIsPlaying = false;
                    mMediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.ic_play);
                }
            }
        });

        /**
         * Xử lý sự kiện nút replay
         */
        ivReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sIsPlaying) {
                    mMediaPlayer.seekTo(0);
                    mStartTime = 0;
                    pbProgress.setProgress(0);
                } else {
                    mMediaPlayer.seekTo(0);
                    sIsPlaying = true;
                    mStartTime = 0;
                    mMediaPlayer.start();
                    startMediaPlayer();
                    ivPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        });
    }

    /**
     * Lấy nội dung câu từ file XML
     */
    private ArrayList<ConversationInfo> loadConversationFromXML() {
        ArrayList<ConversationInfo> listPharse = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.lang_vi);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            Element root = document.getDocumentElement();
            Element element = (Element) root.getElementsByTagName("lesson").item(mID);
            NodeList list = element.getElementsByTagName("item");
            for (int i = 0; i < list.getLength(); i++) {
                Node note = list.item(i);
                if (note instanceof Element) {
                    Element temp = (Element) note;
                    String enText = temp.getAttribute("en_text");
                    String viText = temp.getAttribute("local_text");
                    String person = temp.getAttribute("person");
                    listPharse.add(new ConversationInfo(person, enText, viText));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listPharse;
    }


    // Cập nhật lại giao diện đang phát nhạc (thời gian, thanh seekbar...) mỗi 1s
    void startMediaPlayer() {
        mHandler.postDelayed(mUpdateSongTime, 1000);
    }

    // Dừng việc cập nhật giao diện phát nhạc : Khi chuyển bài, pause
    void stopMediaPlayer() {
        mHandler.removeCallbacks(mUpdateSongTime);
    }

    /**
     * Runnable cập nhật thời gian bài đọc mỗi giây lên giao diện
     */
    Runnable mUpdateSongTime = new Runnable() {
        @Override
        public void run() {
            try {
                if (sIsPlaying) {
                    mStartTime = mStartTime + 1000;
                    if (mStartTime >= mEndTime) {
                        mStartTime = 0;
                        pbProgress.setProgress(0);
                        sIsPlaying = false;
                    } else {
                        pbProgress.setProgress(mStartTime);
                    }
                }
            } finally {
                // 100% chạy đoạn này kể cả gặp exception
                mHandler.postDelayed(mUpdateSongTime, 1000);
            }
        }
    };
}

