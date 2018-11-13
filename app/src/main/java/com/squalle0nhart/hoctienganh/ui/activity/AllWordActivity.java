package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.ExpandWordAdapter;

import java.util.List;
import java.util.Random;

/**
 * Created by squalleonhart on 3/13/2017.
 */

public class AllWordActivity extends Activity {
    RecyclerView mRecycleView;
    Context mContext;
    WordDatabase mWordDatabase;
    SearchView mSearchView;
    List<WordInfo> mListWord;
    ExpandWordAdapter mWordAdapter;
    Button btAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        mContext = this;
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mWordAdapter.getmTextToSpeech() != null) {
                mWordAdapter.getmTextToSpeech().stop();
                mWordAdapter.getmTextToSpeech().shutdown();
                mWordAdapter.setmTextToSpeech(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mWordAdapter.getmTextToSpeech() != null) {
                mWordAdapter.getmTextToSpeech().stop();
                mWordAdapter.getmTextToSpeech().shutdown();
                mWordAdapter.setmTextToSpeech(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***********************************************************************************************
     *  Các hàm xử lý sự kiện click
     ***********************************************************************************************/

    /**
     * Khởi tạo view cho activity
     */
    private void initView() {
        mWordDatabase = WordDatabase.getInstance(mContext);
        mRecycleView = (RecyclerView) findViewById(R.id.rv_list_word);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecycleView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecycleView.addItemDecoration(mDividerItemDecoration);
        mListWord = mWordDatabase.getListWords();
        mWordAdapter = new ExpandWordAdapter(mContext, mListWord);
        mRecycleView.setAdapter(mWordAdapter);
        mWordAdapter.notifyDataSetChanged();

        btAdd = (Button) findViewById(R.id.bt_add_word);
        btAdd.setText(getString(R.string.add_random));
        btAdd.setOnClickListener(new ButtonAddRandomOnClick());

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setQuery("", false);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListWord.clear();
                mListWord = mWordDatabase.getListWordSearch(newText);
                mWordAdapter = new ExpandWordAdapter(mContext, mListWord);
                mRecycleView.setAdapter(mWordAdapter);
                return false;
            }
        });
    }

    /**
     * Sự kiện khi bấm nút thêm ngẫu nhiên
     */
    private class ButtonAddRandomOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int min = 0;
            int max = mListWord.size();
            Random r = new Random();
            int count = 0;
            int maxTry = 0;
            // Thử tối đa là 3000 lần mà ko tìm đủ từ ngẫu nhiên thì dừng
            while (count < 10 && maxTry < 3000) {
                maxTry++;
                int i = r.nextInt(max - min + 1) + min;
                WordInfo temp = mListWord.get(i);
                // Nếu random trùng thì random tiếp
                if (temp.getIsRead() == 0) {
                    count++;
                    temp.setIsRead(1);
                    mWordDatabase.updateWordInfo(temp);
                }
            }
            finish();
        }
    }
}
