package com.jackie.coolweather.http;

/**
 * Created by Administrator on 2017/6/23.
 */
public interface ResponseHandle {

    public void onFinish(String address);
    public void onError(int errorCode);
}
