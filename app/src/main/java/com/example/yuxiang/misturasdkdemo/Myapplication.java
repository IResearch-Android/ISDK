package com.example.yuxiang.misturasdkdemo;

import android.app.Application;
import android.util.Log;

import cn.com.iresearch.phonemonitor.irssdk.IRSSDK;
import cn.com.iresearch.phonemonitor.irssdk.callback.NetWorkCallback;

/**
 * Created by chenbiao on 2017/3/31.
 */

public class Myapplication extends Application {

    private static final String TAG = "IRSDK";
    private IRSSDK mIRSSDK;


    @Override
    public void onCreate() {
        super.onCreate();

    }
    /**构建初始化设置*/
    private void buildSDK() {
        //具体的API可以参照文档说明操作，以下仅为参考。
        mIRSSDK = new IRSSDK.Builder(getApplicationContext())
                .setGps(12.11d, 12.15d) //设置GPS
                .setMccMnc("460", "001")//设置运营商代码
                .setIsCollectAppInfo(true)
                .isDebug(true)//设置是否打印
                .setNetWorkCallback(new NetWorkCallback() {
                    @Override
                    public void sendSuccess(String s) {
                        // 数据发送成功回调
                        Log.d(TAG, "sendSuccess: " + s);
                    }

                    @Override
                    public void sendFail(String s, int i) {
                        // 数据发送失败回调
                        Log.d(TAG, "sendFail: " + s + "----" + i);
                    }

                    @Override
                    public void preSend(String s) {
                        // 数据即将发送回调
                        Log.d(TAG, "preSend: " + s);
                    }

                    @Override
                    public void sendFinished() {
                        // 数据即将发送回调
                        Log.d(TAG, "sendFinished: ");
                    }
                })
                .build();
    }

    public IRSSDK getIRSSDK() {
        if (mIRSSDK == null) {
            buildSDK();
        }
        return mIRSSDK;
    }

}
