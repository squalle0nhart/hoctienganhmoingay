package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.PharseCategoryInfo;
import com.squalle0nhart.hoctienganh.ui.activity.LearnPharseActivity;

import java.util.ArrayList;

import static com.squalle0nhart.hoctienganh.Constants.EXTRAS_PHARSE_CATEGORY;

/**
 * Created by squalleonhart on 3/17/2017.
 */

public class PharseCategoryAdapter extends RecyclerView.Adapter<PharseCategoryAdapter.PharseViewHolder> {
    ArrayList<PharseCategoryInfo> mListCategory;
    Context mContext;

    public PharseCategoryAdapter(Context context, ArrayList<PharseCategoryInfo> list) {
        mContext = context;
        mListCategory = list;
    }

    @Override
    public PharseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_pharse_category_layout, null);
        return new PharseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PharseViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mListCategory.size();
    }

    /**
     * Class viewholder
     */
    public class PharseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivCategory;

        public PharseViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_pharse_title);
            ivCategory = (ImageView) itemView.findViewById(R.id.iv_pharse_catergory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, LearnPharseActivity.class);
                    intent.putExtra(EXTRAS_PHARSE_CATEGORY, mListCategory.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }

        public void bindView() {
            PharseCategoryInfo info = mListCategory.get(getAdapterPosition());
            tvTitle.setText(info.getText());
            switch (info.getId()) {
                case 0:
                    ivCategory.setBackgroundResource(R.drawable.ic_star_border_24px);
                    break;
                case 1:
                    ivCategory.setBackgroundResource(R.drawable.ic_basic);
                    break;
                case 2:
                    ivCategory.setBackgroundResource(R.drawable.ic_sos);
                    break;
                case 3:
                    ivCategory.setBackgroundResource(R.drawable.ic_basic_conversation);
                    break;
                case 4:
                    ivCategory.setBackgroundResource(R.drawable.ic_time);
                    break;
                case 5:
                    ivCategory.setBackgroundResource(R.drawable.ic_weather);
                    break;
                case 6:
                    ivCategory.setBackgroundResource(R.drawable.ic_home);
                    break;
                case 7:
                    ivCategory.setBackgroundResource(R.drawable.ic_travel);
                    break;
                case 8:
                    ivCategory.setBackgroundResource(R.drawable.ic_hotel);
                    break;
                case 9:
                    ivCategory.setBackgroundResource(R.drawable.ic_eat);
                    break;
                case 10:
                    ivCategory.setBackgroundResource(R.drawable.ic_shopping);
                    break;
                case 11:
                    ivCategory.setBackgroundResource(R.drawable.ic_city);
                    break;
                case 12:
                    ivCategory.setBackgroundResource(R.drawable.ic_free_time);
                    break;
                case 13:
                    ivCategory.setBackgroundResource(R.drawable.ic_heath);
                    break;
                case 14:
                    ivCategory.setBackgroundResource(R.drawable.ic_work);
                    break;
                case 15:
                    ivCategory.setBackgroundResource(R.drawable.ic_phone);
                    break;
                case 16:
                    ivCategory.setBackgroundResource(R.drawable.ic_mail);
                    break;
            }
        }
    }
}
