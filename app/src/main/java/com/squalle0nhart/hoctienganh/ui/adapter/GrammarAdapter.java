package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.ui.activity.GrammarDetailActivity;

import java.util.ArrayList;

import static com.squalle0nhart.hoctienganh.Constants.EXTRAS_GRAMMAR_CATEGORY;

/**
 * Created by ThangBK on 13/3/2017.
 */

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> {
    ArrayList<String> mListGrammar;
    Context mContext;


    public GrammarAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListGrammar = list;
    }

    @Override
    public GrammarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_grammar_layout, null);
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GrammarAdapter.GrammarViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mListGrammar.size();
    }


    /**
     * Class viewholder
     */
    public class GrammarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvText;

        public GrammarViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_grammar_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GrammarDetailActivity.class);
                    intent.putExtra(EXTRAS_GRAMMAR_CATEGORY, getAdapterPosition() + 1);
                    mContext.startActivity(intent);
                }
            });
        }

        public void bindView() {
            String info = mListGrammar.get(getAdapterPosition());
            tvText.setText(info);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
