package com.squalle0nhart.hoctienganh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.ArrayList;

/**
 * Created by squalle0nhart on 4/4/2017.
 */

public class NotifyChangeWordReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WordInfo randomWord;
        WordDatabase mWordDatabase = WordDatabase.getInstance(context);
        ArrayList<WordInfo> listLearnedWord = mWordDatabase.getListLearnedWords();
        if (listLearnedWord != null && listLearnedWord.size() > 0) {
            randomWord = mWordDatabase.getListLearnedWords().get(0);
        } else {
            randomWord = mWordDatabase.getRandomWord();
        }
        if (randomWord != null) {
            AppUltis.showForegroundNotify(context, randomWord.getMeaning(), randomWord.getName(), AppUltis.getWordDetail(randomWord.getFullMean()));
        }
    }
}
