package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.ui.adapter.GrammarAdapter;
import com.squalle0nhart.hoctienganh.ui.view.EqualSpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by squalleonhart on 3/18/2017.
 */

public class GrammarActivity extends Activity {
    Context mContext;
    RecyclerView mRecycleView;
    GrammarAdapter mGrammarAdapter;
    ArrayList<String> mListGrammar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        mContext = this;
        initView();
    }

    //**********************************************************************************************
    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.rv_grammar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addItemDecoration(new EqualSpaceItemDecoration(10));
        mListGrammar = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.grammar)));
        mGrammarAdapter = new GrammarAdapter(mContext, mListGrammar);
        mRecycleView.setAdapter(mGrammarAdapter);
    }
}
