package com.squalle0nhart.hoctienganh.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by squalleonhart on 3/17/2017.
 */

public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpaceHeight;

    public EqualSpaceItemDecoration(int mSpaceHeight) {
        this.mSpaceHeight = mSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mSpaceHeight;
        outRect.top = mSpaceHeight;
        outRect.left = mSpaceHeight;
        outRect.right = mSpaceHeight;
    }
}