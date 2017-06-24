package com.jackie.coolweather.base;

import android.app.Application;
import android.content.Context;

import com.jackie.coolweather.http.ResponseHandle;
import com.jackie.coolweather.http.choosearea.ChooseGbManager;

/**
 * Created by Administrator on 2017/6/23.
 */
public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        context.getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
