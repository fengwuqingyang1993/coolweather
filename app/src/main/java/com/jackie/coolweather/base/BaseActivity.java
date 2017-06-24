package com.jackie.coolweather.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Administrator on 2017/6/23.
 */
public class BaseActivity  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitle();
    }

    private void hideTitle(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public void transActivity(Class<?> targetActivity){
        Intent intent = new Intent();
        intent.setClass(BaseActivity.this,targetActivity);
        BaseActivity.this.startActivity(intent);

    }

}
