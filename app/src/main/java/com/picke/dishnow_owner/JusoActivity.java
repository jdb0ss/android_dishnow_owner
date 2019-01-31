package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


public class JusoActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private Handler handler;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_webview();
        handler = new Handler();
        id = getIntent().getStringExtra("o_id");

    }
    public void init_webview() {
        setContentView(R.layout.activity_juso);
        webView = findViewById(R.id.juso_webview);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setSupportMultipleWindows(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new AndroidBridge(),"TestApp");
        webView.loadUrl("http://claor123.cafe24.com/daumjuso.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(JusoActivity.this,ResSignupActivity.class);
                    intent.putExtra("juso1",arg2);
                    intent.putExtra("juso2",arg3);
                    intent.putExtra("o_id",id);
                    startActivity(intent);
                    finish();
                    init_webview();
                }
            });
        }
    }
}
