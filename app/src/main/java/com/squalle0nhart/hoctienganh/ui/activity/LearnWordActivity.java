package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.ExpandWordAdapter;

import java.util.List;


/**
 * Created by squalleonhart on 3/14/2017.
 */
public class LearnWordActivity extends Activity {
    RecyclerView mRecycleView;
    ExpandWordAdapter mWordAdapter;
    Context mContext;
    WordDatabase mWordDatabase;
    SearchView mSearchView;
    List<WordInfo> mListWord;
    LinearLayout llNoWord;
    Button btAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListWord.clear();
        mListWord = mWordDatabase.getListLearnedWords();
        mWordAdapter = new ExpandWordAdapter(mContext, mListWord);
        mRecycleView.setAdapter(mWordAdapter);
        if (mListWord.size() > 0) {
            llNoWord.setVisibility(View.GONE);
        }
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

        mListWord = mWordDatabase.getListLearnedWords();
        llNoWord = (LinearLayout) findViewById(R.id.ll_no_word);
        if (mListWord.size() == 0) {
            llNoWord.setVisibility(View.VISIBLE);
        }
        mWordAdapter = new ExpandWordAdapter(mContext, mListWord);
        mRecycleView.setAdapter(mWordAdapter);
        mWordAdapter.notifyDataSetChanged();

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
                mListWord = mWordDatabase.getListLearnedWordSearch(newText);
                mWordAdapter = new ExpandWordAdapter(mContext, mListWord);
                mRecycleView.setAdapter(mWordAdapter);
                return false;
            }
        });

        btAdd = (Button) findViewById(R.id.bt_add_word);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AllWordActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

}
