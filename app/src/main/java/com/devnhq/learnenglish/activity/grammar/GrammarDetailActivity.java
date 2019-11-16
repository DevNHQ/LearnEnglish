package com.devnhq.learnenglish.activity.grammar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.custom.CustomWebViewClient;

public class GrammarDetailActivity extends AppCompatActivity {
    String id;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);
        id = getIntent().getExtras().getString("id");
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(id);
        webView.setWebViewClient(new CustomWebViewClient());
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}
