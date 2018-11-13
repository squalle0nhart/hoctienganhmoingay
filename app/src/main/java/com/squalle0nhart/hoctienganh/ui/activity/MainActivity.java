package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.service.LockScreenService;
import com.squalle0nhart.hoctienganh.service.NotificationService;
import com.squalle0nhart.hoctienganh.ultis.AppPreference;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import test.activity.TestActivity;

/**
 * Created by squalle0nhart on 9/3/2017.
 */

public class MainActivity extends Activity {
    Context mContext;
    LinearLayout llLearnWord, llLearnPhase, llTest, llConversation, llGrammar;
    LinearLayout llShortWord, llSetting, llNotify, llIrregularVerb, llLockScreen;
    List<PharseInfo> mListApophthegm;
    WordDatabase mWordDatabase;
    TextView tvTitle;
    ImageView ivTranslate, ivLearnNotify, ivLockScreen;
    AppPreference mAppPreference;
    int wordLearned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mAppPreference = AppPreference.getInstance(mContext);
        initView();
        MobileAds.initialize(mContext, getString(R.string.admob_id));
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(2)
                .setRemindInterval(1)
                .setShowLaterButton(true)
                .setOnClickButtonListener(new OnClickButtonListener() {
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int random = AppUltis.randomInt(0, mListApophthegm.size() - 1);
        final PharseInfo temp = mListApophthegm.get(random);
        wordLearned = mWordDatabase.getListLearnedWords().size();

        tvTitle.setText(temp.getText());
        ivTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvTitle.getText().toString().equals(temp.getText())) {
                    tvTitle.setText(temp.getMean());
                } else {
                    tvTitle.setText(temp.getText());
                }
            }
        });
    }

    private void initView() {
        llLearnWord = (LinearLayout) findViewById(R.id.ll_learn_word);
        llLearnPhase = (LinearLayout) findViewById(R.id.ll_learn_phase);
        llTest = (LinearLayout) findViewById(R.id.ll_test);
        llConversation = (LinearLayout) findViewById(R.id.ll_conversation);
        llShortWord = (LinearLayout) findViewById(R.id.ll_short_word);
        llGrammar = (LinearLayout) findViewById(R.id.ll_grammar);
        llIrregularVerb = (LinearLayout) findViewById(R.id.ll_irregular_verb);
        llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        llLockScreen = (LinearLayout) findViewById(R.id.ll_lock_screen);
        ivTranslate = (ImageView) findViewById(R.id.iv_main_translate);
        ivLearnNotify = (ImageView) findViewById(R.id.iv_learn_notify);
        ivLockScreen = (ImageView) findViewById(R.id.iv_lock_screen);
        mWordDatabase = WordDatabase.getInstance(mContext);
        mListApophthegm = mWordDatabase.getListQuote();
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llNotify = (LinearLayout) findViewById(R.id.ll_learn_notify);
        wordLearned = mWordDatabase.getListLearnedWords().size();

        Intent notifyIntent = new Intent(mContext, NotificationService.class);
        if (mAppPreference.isLearnNotify()) {
            startService(notifyIntent);
            ivLearnNotify.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
        } else {
            ivLearnNotify.setBackgroundResource(R.drawable.ic_notifications_none_black_24dp);
            stopService(notifyIntent);
        }

        llNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NotificationService.class);
                if (mAppPreference.isLearnNotify()) {
                    mAppPreference.setLearnNotify(false);
                    stopService(intent);
                    ivLearnNotify.setBackgroundResource(R.drawable.ic_notifications_none_black_24dp);
                } else {
                    mAppPreference.setLearnNotify(true);
                    ivLearnNotify.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                    startService(intent);
                }
            }
        });


        final Intent lockIntent = new Intent(mContext, LockScreenService.class);
        if (mAppPreference.isLockScreenEnable()) {
            startService(lockIntent);
            ivLockScreen.setBackgroundResource(R.drawable.ic_lock_24px);
        } else {
            ivLockScreen.setBackgroundResource(R.drawable.ic_lock_open_24px);
            stopService(lockIntent);
        }

        llLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppPreference.isLockScreenEnable()) {
                    mAppPreference.setLockScreen(false);
                    stopService(lockIntent);
                    ivLockScreen.setBackgroundResource(R.drawable.ic_lock_open_24px);
                } else {
                    mAppPreference.setLockScreen(true);
                    ivLockScreen.setBackgroundResource(R.drawable.ic_lock_24px);
                    startService(lockIntent);
                }
            }
        });

        llLearnWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LearnWordActivity.class);
                startActivity(intent);
            }
        });

        llLearnPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LearnPhaseCategoryActivity.class);
                startActivity(intent);
            }
        });

        llGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GrammarActivity.class);
                startActivity(intent);
            }
        });

        llConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ConversationListActivity.class);
                startActivity(intent);
            }
        });

        llIrregularVerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, IrregularVerbActivity.class);
                startActivity(intent);
            }
        });

        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordLearned < 10) {
                    Toast.makeText(mContext, getString(R.string.test_not_enough_word), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(mContext, TestActivity.class);
                    startActivity(intent);
                }
            }
        });

        llShortWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShortWordActivity.class);
                startActivity(intent);
            }
        });

        llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
