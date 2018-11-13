package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.ConversationInfo;

import java.util.ArrayList;

/**
 * Created by squalleonhart on 3/19/2017.
 */

public class ConversationDetailAdapter extends RecyclerView.Adapter<ConversationDetailAdapter.ConversationViewHolder> {
    ArrayList<ConversationInfo> mListPharse;
    Context mContext;

    public ConversationDetailAdapter(Context context, ArrayList<ConversationInfo> list) {
        mContext = context;
        mListPharse = list;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_conversation, null);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mListPharse.size();
    }


    /**
     * Class viewholder
     */
    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTextLeft, tvMeanLeft, tvTextRight, tvMeanRight;
        RelativeLayout rlLeft, rlRight;
        TextView tvPersonLeft, tvPersonRight;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            rlRight = (RelativeLayout) itemView.findViewById(R.id.rl_conversation_right);
            tvTextRight = (TextView) itemView.findViewById(R.id.tv_text_right);
            tvMeanRight = (TextView) itemView.findViewById(R.id.tv_mean_right);
            rlLeft = (RelativeLayout) itemView.findViewById(R.id.rl_conversation_left);
            tvTextLeft = (TextView) itemView.findViewById(R.id.tv_text_left);
            tvMeanLeft = (TextView) itemView.findViewById(R.id.tv_mean_left);
            tvPersonLeft = (TextView) itemView.findViewById(R.id.tv_person_left);
            tvPersonRight = (TextView) itemView.findViewById(R.id.tv_person_right);
        }

        public void bindView(int position) {
            Log.e("TAG","POS" + position);
            ConversationInfo temp = mListPharse.get(position);
            if (position % 2 == 0) {
                rlRight.setVisibility(View.VISIBLE);
                rlLeft.setVisibility(View.GONE);
                tvTextRight.setText(temp.getPerson()+": "+temp.getText());
                tvMeanRight.setText(temp.getMean());
            } else {
                rlLeft.setVisibility(View.VISIBLE);
                rlRight.setVisibility(View.GONE);
                tvTextLeft.setText(temp.getPerson()+": "+temp.getText());
                tvMeanLeft.setText(temp.getMean());
            }
        }
    }
}
