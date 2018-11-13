package test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppPreference;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.Collections;
import java.util.Locale;

import test.TestUltis;

/**
 * Created by squalle0nhart on 22/3/2017.
 */

public class TestActivity extends Activity implements TextToSpeech.OnInitListener {
    ImageView ivSpeak;
    TextView tvWordTest;
    Button[] mListChoiceButton = new Button[4];
    Button btNext;
    Context mContext;
    WordDatabase mWordDatabase;
    WordInfo mTestWord;
    int randomRightChoice;
    TextToSpeech mTextToSpeech;
    boolean isTTSReady;
    int nextWord;
    AppPreference mAppPreference;
    float mSpeakSpeed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_test_default);
        mAppPreference = AppPreference.getInstance(mContext);
        mSpeakSpeed = mAppPreference.getSpeakSpeed();
        mTextToSpeech = new TextToSpeech(mContext, TestActivity.this);
        nextWord = getIntent().getIntExtra(Constants.EXTRAS_TEST, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextToSpeech == null) {
            mTextToSpeech = new TextToSpeech(mContext, TestActivity.this);
        }
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
     * Hàm xử lý chính của activity
     */
    private void initView() {
        mWordDatabase = WordDatabase.getInstance(mContext);
        if (TestUltis.sListLearnedWord == null || TestUltis.sListLearnedWord.size() == 0) {
            TestUltis.sListLearnedWord = mWordDatabase.getListLearnedWords();
        }

        if (TestUltis.sListAllWord == null || TestUltis.sListAllWord.size() == 0) {
            TestUltis.sListAllWord = mWordDatabase.getListWords();
        }

        ivSpeak = (ImageView) findViewById(R.id.iv_test_speak);
        tvWordTest = (TextView) findViewById(R.id.tv_word_test);
        btNext = (Button) findViewById(R.id.bt_test_next);
        mListChoiceButton[0] = (Button) findViewById(R.id.bt_choice_1);
        mListChoiceButton[1] = (Button) findViewById(R.id.bt_choice_2);
        mListChoiceButton[2] = (Button) findViewById(R.id.bt_choice_3);
        mListChoiceButton[3] = (Button) findViewById(R.id.bt_choice_4);

        for (int i = 0; i < 4; i++) {
            mListChoiceButton[i].setOnClickListener(new ButtonChoiceOnClick());
        }

        // Trộn mảng từ đã học
        Collections.shuffle(TestUltis.sListLearnedWord);

        mTestWord = TestUltis.sListLearnedWord.get(nextWord);
        tvWordTest.setText(mTestWord.getName());

        String[] listWrongChoice = TestUltis.getRandomAnswer(mTestWord).split("::");

        // Lấy đáp án ngẫu nhiên
        randomRightChoice = AppUltis.randomInt(0, 3);
        mListChoiceButton[randomRightChoice].setText(mTestWord.getMeaning().replace("&AMP", ""));
        int temp = 0;
        for (int i = 0; i < 4; i++) {
            if (i != randomRightChoice) {
                mListChoiceButton[i].setText(listWrongChoice[temp]);
                temp++;
            }
        }

        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTTSReady) {
                    mTextToSpeech = new TextToSpeech(mContext, TestActivity.this);
                } else {
                    mTextToSpeech.speak(mTestWord.getName()
                            , TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        // Chuyển sang từ khác
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextWord < TestUltis.sListLearnedWord.size() - 1) {
                    nextWord++;
                    Intent intent = new Intent(mContext, TestWriteActivity.class);
                    intent.putExtra(Constants.EXTRAS_TEST, nextWord);
                    startActivity(intent);
                    finish();
                } else {
                    TestUltis.showDialogComplete(mContext);
                }
            }
        });
    }

    /**
     * Sự kiện click khi chọn 1 đáp án
     */
    private class ButtonChoiceOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int position = -1;
            for (int i = 0; i < 4; i++) {
                if (view.getId() == mListChoiceButton[i].getId()) {
                    position = i;
                    break;
                }
            }
            if (position == randomRightChoice) {
                // Chọn đúng
                mListChoiceButton[randomRightChoice].setTextColor(Color.BLUE);
                mListChoiceButton[randomRightChoice].setText(mListChoiceButton[randomRightChoice].getText()
                        + " - " + getString(R.string.right_choice));
            } else {
                // Chọn sai
                mListChoiceButton[position].setTextColor(Color.RED);
                mListChoiceButton[position].setText(mListChoiceButton[position].getText()
                        + " - " + getString(R.string.wrong_choice));

                mListChoiceButton[randomRightChoice].setTextColor(Color.BLUE);
                mListChoiceButton[randomRightChoice].setText(mListChoiceButton[randomRightChoice].getText()
                        + " - " + getString(R.string.right_choice));
            }

            for (int i = 0; i < 4; i++) {
                mListChoiceButton[i].setClickable(false);
            }
            btNext.setVisibility(View.VISIBLE);
        }
    }
}
