package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.squalle0nhart.hoctienganh.R;

/**
 * Created by squalle0nhart on 23/3/2017.
 */

public class ShortWordActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);
        // Code xử lý đơn giản chỉ cần load htm ra webview
        WebView webView = (WebView) findViewById(R.id.wv_grammar_detail);
        webView.loadUrl("file:///android_asset/shortword.html");
    }
}
