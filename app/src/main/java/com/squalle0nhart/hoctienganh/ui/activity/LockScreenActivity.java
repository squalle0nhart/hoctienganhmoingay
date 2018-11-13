package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ui.view.UnlockBar;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.Locale;

import test.activity.TestListenActivity;


/**
 * Created by squalle0nhart on 31/3/2017.
 */

public class LockScreenActivity extends Activity implements TextToSpeech.OnInitListener {
    Context mContext;
    RelativeLayout llRoot;
    ImageView ivBackGround, ivSpeak;
    UnlockBar unlockBar;
    TextView tvWord, tvMean, tvSpeak;
    TextToSpeech mTextToSpeech;
    boolean isTTSReady;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mTextToSpeech == null) {
            mTextToSpeech = new TextToSpeech(this, this);
        }
        mContext = this;
        setContentView(R.layout.activity_lock_screen);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
    }


    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        llRoot = (RelativeLayout) findViewById(R.id.ll_lock_screen_root);
        ivBackGround = (ImageView) findViewById(R.id.iv_lock_screen_background);
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        llRoot.setBackgroundDrawable(wallpaperDrawable);

        ivBackGround.setBackgroundColor(Color.argb(180,0,0,0));

        tvMean = (TextView) findViewById(R.id.tv_content);
        tvWord = (TextView) findViewById(R.id.tv_word);
        tvSpeak = (TextView) findViewById(R.id.tv_speak);
        ivSpeak = (ImageView) findViewById(R.id.iv_lock_speak);

        unlockBar = (UnlockBar) findViewById(R.id.unlock_bar);
        unlockBar.setOnUnlockListener(new UnlockBar.OnUnlockListener() {
            @Override
            public void onUnlock() {
                finish();
            }
        });

        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHardwareButtons = false;
        if (!hasMenuKey && !hasBackKey) {
            hasHardwareButtons = true;
        }

        if (hasNavBar(getResources()) || hasHardwareButtons) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(0, 0, 0, navHeight(this));
            unlockBar.setLayoutParams(params);
        }

        final WordInfo randomWord = AppUltis.randomWord(this);
        tvWord.setText(randomWord.getName());
        tvMean.setText(Html.fromHtml(AppUltis.getWordDetailLockScreen(randomWord.getFullMean())));
        tvSpeak.setText(randomWord.getRead());
        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextToSpeech.speak(randomWord.getName()
                        , TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    public boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public int navHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
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

}
