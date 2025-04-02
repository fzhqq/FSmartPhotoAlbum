package com.example.fsmartphotoalbum.app;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 为应用设置异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init();

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
