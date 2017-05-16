package com.himmiractivity.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.himmiractivity.App;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.view.HomeDialog;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

/**
 * 使用帮助页面
 */

public class HelpActivity extends BaseBusActivity {
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.help;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("使用帮助");
        initTitleBar();
        // webView加载对话框
        webView.setWebViewClient(new WebViewClient() {
            HomeDialog.DialogView dialog;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog = new HomeDialog.DialogView(HelpActivity.this);
                dialog.show();
                dialog.setMessage("加载中");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ToastUtil.show(HelpActivity.this, "url" + url);
                return false;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        // webView.loadUrl("file:///android_asset/test.html");
        webView.loadUrl(Configuration.URL_HELP);
        // webView.loadUrl("javascript:resetFontSize('20px')");
        // install callback function
//        webView.addJavascriptInterface(new AndroidBridge(), "android");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        } else {
            this.finish();
        }
        super.onBackPressed();
    }
}
