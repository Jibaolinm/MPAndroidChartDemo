package com.petterp.guosai;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author Petterp on 2019/5/8
 * Summary:全局Context类，需要在AndroidMaifest中配置
 * 例如 <application
 *          android:name="com.petterp.guosai.MyApplication"
 * 邮箱：1509492795@qq.com
 */
@SuppressLint("StaticFieldLeak")
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
