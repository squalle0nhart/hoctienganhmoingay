package test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;
import com.squalle0nhart.hoctienganh.database.WordDatabase;
import com.squalle0nhart.hoctienganh.model.WordInfo;

import java.util.Collections;

import test.TestUltis;

/**
 * Created by squalle0nhart on 23/3/2017.
 */

public class TestWriteActivity extends Activity {
    Button btNext, btCheck;
    Context mContext;
    WordDatabase mWordDatabase;
    WordInfo mTestWord;
    int nextWord;
    TextView tvQuestion, tvResult;
    EditText etAnswer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_test_write);
        nextWord = getIntent().getIntExtra(Constants.EXTRAS_TEST, 0);
        initView();
    }

    /**
     * Hàm xử lý chính của activity
     */
    private void initView() {
        mWordDatabase = WordDatabase.getInstance(mContext);
        btNext = (Button) findViewById(R.id.bt_test_next);
        btCheck = (Button) findViewById(R.id.bt_check);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        etAnswer = (EditText) findViewById(R.id.et_answer);
        tvResult = (TextView) findViewById(R.id.tv_result);

        // Trộn mảng từ đã học
        Collections.shuffle(TestUltis.sListLearnedWord);
        mTestWord = TestUltis.sListLearnedWord.get(nextWord);
        tvQuestion.setText(mTestWord.getMeaning());

        // Chuyển sang từ khác
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextWord < TestUltis.sListLearnedWord.size() - 1) {
                    nextWord++;
                    Intent intent = new Intent(mContext, TestActivity.class);
                    intent.putExtra(Constants.EXTRAS_TEST, nextWord);
                    startActivity(intent);
                    finish();
                } else {
                    TestUltis.showDialogComplete(mContext);
                }
            }
        });

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAnswer.getText().toString().equals("")) {
                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                } else {
                    tvResult.setVisibility(View.VISIBLE);
                    btNext.setVisibility(View.VISIBLE);
                    if (etAnswer.getText().toString().equals(mTestWord.getName())) {
                        tvResult.setTextColor(Color.BLUE);
                        tvResult.setText(getString(R.string.right_choice) + " - " + mTestWord.getName());
                    } else {
                        tvResult.setTextColor(Color.RED);
                        tvResult.setText(getString(R.string.wrong_choice) + " - " + mTestWord.getName());
                    }
                }
            }
        });
    }
}

