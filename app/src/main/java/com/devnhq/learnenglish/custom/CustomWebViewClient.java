package com.devnhq.learnenglish.custom;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading
            (WebView view, String url) {
        // here you can check the url
        // (whitelist / blacklist)
        return true;
        // will NOT load the link
        // use "return false;" to allow it to load
    }
}
