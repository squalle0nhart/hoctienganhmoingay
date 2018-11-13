package com.squalle0nhart.hoctienganh.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.PharseDatabase;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.ultis.AppPreference;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by squalleonhart on 3/17/2017.
 */

public class PharseAdapter extends RecyclerView.Adapter<PharseAdapter.PharseViewHolder> implements TextToSpeech.OnInitListener {
    ArrayList<PharseInfo> mListPharse;
    Context mContext;
    private TextToSpeech mTextToSpeech;

    public TextToSpeech getmTextToSpeech() {
        return mTextToSpeech;
    }

    public void setmTextToSpeech(TextToSpeech mTextToSpeech) {
        this.mTextToSpeech = mTextToSpeech;
    }

    boolean isTTSReady;
    AppPreference mAppPreference;
    float mSpeakSpeed;


    public PharseAdapter(Context context, ArrayList<PharseInfo> list) {
        mContext = context;
        mListPharse = list;
        mTextToSpeech = new TextToSpeech(mContext, this);
        mAppPreference = AppPreference.getInstance(mContext);
        mSpeakSpeed = mAppPreference.getSpeakSpeed();
    }

    @Override
    public PharseAdapter.PharseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_pharse_layout, null);
        return new PharseAdapter.PharseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PharseAdapter.PharseViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mListPharse.size();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.ENGLISH);
            mTextToSpeech.setSpeechRate(mSpeakSpeed);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                mContext.startActivity(installIntent);
            } else {
                isTTSReady = true;
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
            isTTSReady = false;
        }
    }

    /**
     * Class viewholder
     */
    public class PharseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvText, tvMean;
        ImageView ivSpeak, ivLearnPharse;

        public PharseViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_pharse_text);
            tvMean = (TextView) itemView.findViewById(R.id.tv_pharse_mean);
            ivSpeak = (ImageView) itemView.findViewById(R.id.iv_pharse_speak);
            ivSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isTTSReady) {
                        mTextToSpeech = new TextToSpeech(mContext, PharseAdapter.this);
                    } else {
                        mTextToSpeech.speak(mListPharse.get(getAdapterPosition()).getText().replaceAll("\\s+$", "")
                                , TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });

            ivLearnPharse = (ImageView) itemView.findViewById(R.id.iv_learn_pharse);
            ivLearnPharse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    PharseInfo item = mListPharse.get(position);
                    if (item.getIsRead() == 0) {
                        item.setIsRead(1);
                        ivLearnPharse.setBackgroundResource(R.drawable.ic_quote_24px);
                    } else {
                        item.setIsRead(0);
                        ivLearnPharse.setBackgroundResource(R.drawable.ic_star_border_24px);
                    }
                    PharseDatabase.getInstance(mContext).updatePharseInfo(item);
                }
            });
        }

        public void bindView() {
            PharseInfo info = mListPharse.get(getAdapterPosition());
            tvText.setText(info.getText());
            tvMean.setText(info.getMean());
            if (info.getIsRead() == 1) {
                ivLearnPharse.setBackgroundResource(R.drawable.ic_quote_24px);
            } else {
                ivLearnPharse.setBackgroundResource(R.drawable.ic_star_border_24px);
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
