package com.project.facerecognitiondemo.application;

import android.app.Application;
import android.content.Context;

import com.project.facerecognitiondemo.util.SharedPrefrenceUtils;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/14 15:02
 * Description: It's use to ...
 */
public class MyApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPrefrenceUtils.SharedPrefrenceHelper(mContext);
    }

    public static Context getmContext() {
        return mContext;
    }
}
