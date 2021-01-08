package com.kh.delivery_project.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kh.delivery_project.R;
import com.kh.delivery_project.util.Keys;

public class Activity_WebView extends AppCompatActivity implements Keys {

    private WebView browser;

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            int index = data.indexOf(",");
            String address = data.substring(index + 2);

            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", address);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        browser = findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        browser.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("page", "started");
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                Log.d("page", "commit");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("page", "finished");
                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        browser.loadUrl(IP + "/util/daumAddress");
    }
}
