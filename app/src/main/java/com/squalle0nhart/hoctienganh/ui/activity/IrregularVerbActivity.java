package com.squalle0nhart.hoctienganh.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.squalle0nhart.hoctienganh.R;

/**
 * Created by ThangBK on 14/3/2017.
 */

public class IrregularVerbActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);
        // load file nội dung ra webview
        WebView webView = (WebView) findViewById(R.id.wv_grammar_detail);
        webView.loadUrl("file:///android_asset/irregular_verb.htm");
    }
}
