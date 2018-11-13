package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.ui.activity.ConversationDetailActivity;

import java.util.ArrayList;

import static com.squalle0nhart.hoctienganh.Constants.EXTRAS_CONVERSATION;

/**
 * Created by squalleonhart on 3/19/2017.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>  {
    ArrayList<PharseInfo> mListPharse;
    Context mContext;
    public ConversationListAdapter(Context context, ArrayList<PharseInfo> list) {
        mContext = context;
        mListPharse = list;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_pharse_layout, null);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mListPharse.size();
    }



    /**
     * Class viewholder
     */
    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvMean;
        ImageView ivSpeak, ivLearn;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_pharse_text);
            tvMean = (TextView) itemView.findViewById(R.id.tv_pharse_mean);
            ivSpeak = (ImageView) itemView.findViewById(R.id.iv_pharse_speak);
            ivSpeak.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ConversationDetailActivity.class);
                    intent.putExtra(EXTRAS_CONVERSATION, getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });

            ivLearn = (ImageView) itemView.findViewById(R.id.iv_learn_pharse);
            ivLearn.setVisibility(View.GONE);
        }

        public void bindView() {
            PharseInfo info = mListPharse.get(getAdapterPosition());
            tvText.setText(getAdapterPosition()+1 + ". "+ info.getText());
            tvMean.setText(info.getMean());
        }

    }
}
