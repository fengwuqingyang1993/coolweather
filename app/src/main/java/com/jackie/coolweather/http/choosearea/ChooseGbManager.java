package com.jackie.coolweather.http.choosearea;

import android.content.Context;
import android.text.TextUtils;

import com.jackie.coolweather.db.DBUtil;
import com.jackie.coolweather.http.HttpConfig;
import com.jackie.coolweather.http.HttpUtil;
import com.jackie.coolweather.http.ResponseHandle;
import com.jackie.coolweather.model.GbAddress;
import com.jackie.coolweather.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */
public class ChooseGbManager {
//http://v.juhe.cn/weather/citys?key=503cb5c21e2c9322b13607658364c6db

    private Context context;
    public static boolean isFirst = true;
    /**
     * 向网络发送获取全国城市列表的请求
     * @param context 上下文对象
     * @param handle 请求返回结果的处理者
     */
    public void loadGb(Context context,ResponseHandle handle){
        this.context = context;
        StringBuilder builder = new StringBuilder(HttpConfig.BASE_URL);
        builder.append("citys?");
        builder.append("key=");
        builder.append(HttpConfig.APP_KEY);
        HttpUtil httpUtil = new HttpUtil("GET");
        httpUtil.sendHttpRequest(builder.toString(),handle);
    }

    /**
     * 解析JSON数据 ，把解析的结果存入数据库表中
     * @param jsonData  网络请求返回的全国城市列表的JSON 数据
     */
    public boolean handleGbResponse(String jsonData){
        if (isFirst){
            LogUtil.d("handleGbResponse ",isFirst+"");
            GbAddress address = new GbAddress();
            DBUtil dbUtil = DBUtil.getDbUtilInstance(context);
            if (!TextUtils.isEmpty(jsonData)){
                try {
                    JSONObject object = new JSONObject(jsonData);
                    jsonData = object.getString("result");
                    JSONArray array = new JSONArray(jsonData);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        address.setId(obj.getInt("id"));
                        address.setProvince(obj.getString("province"));
                        address.setCitys(obj.getString("city"));
                        address.setDistrict(obj.getString("district"));
                        // 把一个地区数据存入数据库
                        dbUtil.saveGb(address);
                    }
                    isFirst = false;
                    return true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
