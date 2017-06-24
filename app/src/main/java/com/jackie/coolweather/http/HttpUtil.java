package com.jackie.coolweather.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jackie.coolweather.util.LogUtil;

/**
 * Created by Administrator on 2017/6/23.
 */
public class HttpUtil {

    /**
     * 网络访问方式，可选 "GET" or  "POST"
     */
       private String httpRequestMethod;

       public HttpUtil(String httpRequestMethod) {
           this.httpRequestMethod = httpRequestMethod;
        }

    /**
     * 使用该方法，向服务器发出一个访问请求,返回一个JSON数据
     * @param address 要访问的URL地址
     * @param handle  网络返回结果的处理者
     */
        public void sendHttpRequest(final String address, final ResponseHandle handle){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection conn = null;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(address);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod(httpRequestMethod);
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(8000);
                        int responseCode = conn.getResponseCode();
                        LogUtil.d("TAG","response code is: "+responseCode);
                        if (HttpURLConnection.HTTP_OK == responseCode){
                            InputStream in = conn.getInputStream();
                             reader = new BufferedReader(
                                    new InputStreamReader(in));
                            StringBuilder builder = new StringBuilder();
                            String line = null;
                            while (null !=(line = reader.readLine())){
                                builder.append(line);
                            }
                             handle.onFinish(builder.toString());
                        }else {
                            handle.onError(responseCode);
                        }
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }finally {
                        //记得最后关闭流操作
                        if (null !=reader){
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }


}
