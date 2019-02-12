package com.picke.dishnow_owner.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.Registration_RestaurantActivity;


public class JusoActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private Handler handler;
    private UserInfoClass userInfoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        init_webview();
        handler = new Handler();
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
                    userInfoClass.setResadd_num(arg1);
                    userInfoClass.setResaddress(arg2);
                    userInfoClass.setResadd_detail(arg3);
                    startActivity(new Intent(JusoActivity.this,Registration_RestaurantActivity.class));
                    finish();
                    init_webview();
                }
            });
        }
    }
}
