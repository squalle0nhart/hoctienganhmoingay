package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.PharseDatabase;
import com.squalle0nhart.hoctienganh.model.PharseCategoryInfo;
import com.squalle0nhart.hoctienganh.ui.adapter.PharseCategoryAdapter;
import com.squalle0nhart.hoctienganh.ui.view.EqualSpaceItemDecoration;

import java.util.ArrayList;


/**
 * Created by squalle0nhart on 14/3/2017.
 */
public class LearnPhaseCategoryActivity extends Activity {
    RecyclerView mRecycleView;
    Context mContext;
    ArrayList<PharseCategoryInfo> mListPharse;
    PharseCategoryAdapter mPharseAdapter;
    PharseDatabase mPharseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_pharse);
        mContext = this;
        initView();
    }

    private void initView() {
        mPharseDatabase = PharseDatabase.getInstance(mContext);
        mRecycleView = (RecyclerView) findViewById(R.id.rv_pharse);
        mRecycleView.setHasFixedSize(false);
        mRecycleView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecycleView.addItemDecoration(new EqualSpaceItemDecoration(10));

        mListPharse = mPharseDatabase.getListPharseCategory();
        mListPharse.add(0, new PharseCategoryInfo(getString(R.string.favorite),0));
        mPharseAdapter = new PharseCategoryAdapter(mContext, mListPharse);
        mRecycleView.setAdapter(mPharseAdapter);
        mPharseAdapter.notifyDataSetChanged();
    }

}
