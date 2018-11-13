package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.PharseDatabase;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.PharseAdapter;
import com.squalle0nhart.hoctienganh.ui.view.EqualSpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by squalleonhart on 3/17/2017.
 */

public class LearnPharseActivity extends Activity {
    int mID;
    Context mContext;
    ArrayList<PharseInfo> mListPharse;
    PharseDatabase mPharseDatabase;
    RecyclerView mRecycleView;
    PharseAdapter mPharseAdapter;
    SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_pharse);
        mID = getIntent().getIntExtra(Constants.EXTRAS_PHARSE_CATEGORY, -1);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPharseAdapter.getmTextToSpeech() != null) {
            mPharseAdapter.getmTextToSpeech().stop();
            mPharseAdapter.getmTextToSpeech().shutdown();
            mPharseAdapter.setmTextToSpeech(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPharseAdapter.getmTextToSpeech() != null) {
            mPharseAdapter.getmTextToSpeech().stop();
            mPharseAdapter.getmTextToSpeech().shutdown();
            mPharseAdapter.setmTextToSpeech(null);
        }
    }

    /**
     * Khởi tạo view
     */
    private void initView() {
        mPharseDatabase = PharseDatabase.getInstance(mContext);
        if (mID == 0) {
            mListPharse = mPharseDatabase.getListLearnedPharse();
        } else {
            mListPharse = mPharseDatabase.getListPharseByCategory(mID);
        }
        mPharseAdapter = new PharseAdapter(mContext, mListPharse);

        mRecycleView = (RecyclerView) findViewById(R.id.rv_pharse);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new EqualSpaceItemDecoration(10));
        mRecycleView.setAdapter(mPharseAdapter);

        mSearchView = (SearchView) findViewById(R.id.search_view_pharse);
        mSearchView.setIconified(false);
        mSearchView.setVisibility(View.VISIBLE);
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
                mListPharse.clear();
                mListPharse = mPharseDatabase.getListPharseByCategorySearch(mID, newText);
                mPharseAdapter = new PharseAdapter(mContext, mListPharse);
                mRecycleView.setAdapter(mPharseAdapter);
                return false;
            }
        });
    }
}
