package com.example.yuxiang.misturasdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE = 0x10;

    private WebView mWebView;
    private Myapplication mMyapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);

        mMyapplication = (Myapplication) getApplication();


        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.baidu.com");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        startService();
        //单一界面记录
        mMyapplication.getIRSSDK().onStart(MainActivity.this, getContentView(this));
        //多个界面调用需用以下方式，其中Main为场景名称，可以自定义其他名称
       // mMyapplication.getIRSSDK().onStart(MainActivity.this, getContentView(this), "Main");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //结束记录
        mMyapplication.getIRSSDK().onStop();
    }


    public ViewGroup getContentView(Activity ac) {
        ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        return (ViewGroup) content.getChildAt(0);
    }

    /**启动SMsdk*/
    private void directStartService() {
        mMyapplication.getIRSSDK().start();
    }

    private void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestOurPermissions();
        } else {
            directStartService();
        }
    }

    /**
     * 请求权限
     */
    private void requestOurPermissions() {
        ArrayList<String> requests = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            requests.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请READ_PHONE_STATE权限
            requests.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (requests.size() == 0) {
            directStartService();
        } else {
            String[] permissions = new String[requests.size()];
            ActivityCompat.requestPermissions(MainActivity.this, requests.toArray(permissions), REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CODE && grantResults.length != 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    requestOurPermissions();
                    return;
                }
            }
            //请求权限成功的调用，一般在此启动SDK
            directStartService();
        }
    }

}
