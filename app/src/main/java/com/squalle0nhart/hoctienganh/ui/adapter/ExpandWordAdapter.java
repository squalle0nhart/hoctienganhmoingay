package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordExpandInfo;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppPreference;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.List;
import java.util.Locale;

/**
 * Created by squalleonhart on 3/15/2017.
 */

public class ExpandWordAdapter extends ExpandableRecyclerAdapter<WordInfo, WordExpandInfo, ExpandWordAdapter.MyParentViewHolder, ExpandWordAdapter.MyChildViewHolder> implements TextToSpeech.OnInitListener{
    Context mContext;
    private TextToSpeech mTextToSpeech;
    boolean isTTSReady;
    List<WordInfo> mListWordInfos;
    AppPreference mAppPreference;
    float mSpeakSpeed;

    public TextToSpeech getmTextToSpeech() {
        return mTextToSpeech;
    }

    public void setmTextToSpeech(TextToSpeech mTextToSpeech) {
        this.mTextToSpeech = mTextToSpeech;
    }

    public ExpandWordAdapter(Context context, @NonNull List<WordInfo> parentList) {
        super(parentList);
        mContext = context;
        mTextToSpeech = new TextToSpeech(mContext, this);
        mListWordInfos = parentList;
        mAppPreference = AppPreference.getInstance(mContext);
        mSpeakSpeed = mAppPreference.getSpeakSpeed();
    }

    @NonNull
    @Override
    public MyParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext,R.layout.item_word_layout, null);
        return new MyParentViewHolder(view);
    }

    @NonNull
    @Override
    public MyChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext,R.layout.item_word_expand_layout, null);
        return new MyChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull MyParentViewHolder myParentViewHolder, int i, @NonNull WordInfo wordInfo) {
        myParentViewHolder.bindView(wordInfo);
    }

    @Override
    public void onBindChildViewHolder(@NonNull MyChildViewHolder myChildViewHolder, int i, int i1, @NonNull WordExpandInfo wordExpandInfo) {
        myChildViewHolder.bindView(wordExpandInfo);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.ENGLISH);
            mTextToSpeech.setSpeechRate(mSpeakSpeed);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                mContext.startActivity(installIntent);
            } else {
                isTTSReady = true;
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
            isTTSReady = false;
        }
    }

    public class MyParentViewHolder extends ParentViewHolder implements View.OnClickListener{
        public TextView tvMeaning, tvWord, tvRead;
        public ImageView ivSpeak, ivLearnWord;

        public MyParentViewHolder(View itemView) {
            super(itemView);
            tvMeaning = (TextView) itemView.findViewById(R.id.tv_meaning);
            tvWord = (TextView) itemView.findViewById(R.id.tv_word);
            ivSpeak = (ImageView) itemView.findViewById(R.id.iv_speak);
            ivLearnWord = (ImageView) itemView.findViewById(R.id.iv_learn_word);
            tvRead = (TextView) itemView.findViewById(R.id.tv_read);

            // Xử lý khi bấm vào nút nghe tiếng
            ivSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isTTSReady) {
                        mTextToSpeech = new TextToSpeech(mContext, ExpandWordAdapter.this);
                    } else {
                        mTextToSpeech.speak(mListWordInfos.get(getAdapterPosition()).getName().replaceAll("\\s+$", "")
                                , TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });

            // Xử lý khi bấm nào nút học
            ivLearnWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    WordInfo item = mListWordInfos.get(position);
                    if (item.getIsRead() == 0) {
                        item.setIsRead(1);
                        ivLearnWord.setBackgroundResource(R.drawable.ic_quote_24px);
                    } else {
                        item.setIsRead(0);
                        ivLearnWord.setBackgroundResource(R.drawable.ic_star_border_24px);
                    }
                    WordDatabase.getInstance(mContext).updateWordInfo(item);
                }
            });

        }

        public void bindView(WordInfo wordInfo) {
            tvWord.setText(" " + wordInfo.getName().replaceAll("\\s+$", "")
                    + " " + wordInfo.getCategory());
            tvRead.setText("  " + wordInfo.getRead());
            tvMeaning.setText(wordInfo.getMeaning());
            if (wordInfo.getIsRead() == 1) {
                ivLearnWord.setBackgroundResource(R.drawable.ic_quote_24px);
            } else {
                ivLearnWord.setBackgroundResource(R.drawable.ic_star_border_24px);
            }
        }

        @Override
        public void onClick(View view) {
            //Bấm vào listview thì hiện chi tiết về từ
            if (isExpanded()) {
                collapseView();
            } else {
                expandView();
            }
        }
    }

    public class MyChildViewHolder extends ChildViewHolder<WordExpandInfo> {
        private TextView tvFullMean;

        public MyChildViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullMean = (TextView) itemView.findViewById(R.id.tv_full_mean);
        }

        public void bindView(WordExpandInfo wordInfo) {
            tvFullMean.setText(Html.fromHtml(AppUltis.getWordDetail(wordInfo.getText())));
        }


    }
}
