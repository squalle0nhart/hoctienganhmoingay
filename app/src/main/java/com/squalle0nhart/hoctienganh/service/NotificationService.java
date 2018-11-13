package com.squalle0nhart.hoctienganh.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppPreference;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by squalle0nhart on 31/3/2017.
 */

public class NotificationService extends Service implements TextToSpeech.OnInitListener {
    public Notification mNotification;
    public static String sNotifyWord;
    WordDatabase mWordDatabase;
    public static TextToSpeech sTextToSpeech;
    float mSpeakSpeed;
    Context mContext;
    AppPreference mAppPreference;
    Handler mHandler = new Handler();

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = sTextToSpeech.setLanguage(Locale.US);
            sTextToSpeech.setSpeechRate(mSpeakSpeed);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                mContext.startActivity(installIntent);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        stopSelf();
                    }
                });
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sTextToSpeech != null) {
            sTextToSpeech.stop();
            sTextToSpeech.shutdown();
            sTextToSpeech = null;
        }
        stopRepeatingTask();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mAppPreference = AppPreference.getInstance(mContext);
        mSpeakSpeed = mAppPreference.getSpeakSpeed();
        sTextToSpeech = new TextToSpeech(mContext, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WordInfo randomWord;
        mWordDatabase = WordDatabase.getInstance(this);
        ArrayList<WordInfo> listLearnedWord = mWordDatabase.getListLearnedWords();
        if (listLearnedWord != null && listLearnedWord.size() > 0) {
            randomWord = mWordDatabase.getListLearnedWords().get(0);
        } else {
            randomWord = mWordDatabase.getRandomWord();
        }
        if (randomWord != null) {
            sNotifyWord = randomWord.getName();
            mNotification = AppUltis.getForegroundNotify(this, randomWord.getMeaning(),
                    randomWord.getName(), AppUltis.getWordDetail(randomWord.getFullMean()));
            startForeground(Constants.NOTIFICATION_ID, mNotification);
        }
        startRepeatingTask();
        return START_STICKY;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                WordInfo randomWord = AppUltis.getRandomWord(
                        WordDatabase.getInstance(NotificationService.this)
                                .getListLearnedWords());
                if (randomWord != null) {
                    AppUltis.showForegroundNotify(NotificationService.this,
                            randomWord.getMeaning(), randomWord.getName(), AppUltis.getWordDetail(randomWord.getFullMean()));
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 5 * 60000);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

}
