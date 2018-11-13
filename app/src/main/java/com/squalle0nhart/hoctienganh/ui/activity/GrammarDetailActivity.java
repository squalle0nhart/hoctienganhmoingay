package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.squalle0nhart.hoctienganh.Constants;
import com.squalle0nhart.hoctienganh.R;

/**
 * Created by squalleonhart on 3/18/2017.
 */

public class GrammarDetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);
        // Code xử lý đơn giản chỉ cần load htm ra webview
        int id = getIntent().getIntExtra(Constants.EXTRAS_GRAMMAR_CATEGORY, -1);
        WebView webView = (WebView) findViewById(R.id.wv_grammar_detail);
        webView.loadUrl("file:///android_asset/grammar/"+id+".htm");
    }
}
