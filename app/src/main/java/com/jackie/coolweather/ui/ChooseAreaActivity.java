package com.jackie.coolweather.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.coolweather.R;
import com.jackie.coolweather.base.BaseActivity;
import com.jackie.coolweather.db.DBUtil;
import com.jackie.coolweather.http.HttpUtil;
import com.jackie.coolweather.http.ResponseHandle;
import com.jackie.coolweather.http.choosearea.ChooseGbManager;
import com.jackie.coolweather.model.City;
import com.jackie.coolweather.model.District;
import com.jackie.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class ChooseAreaActivity extends BaseActivity {

    private TextView areaTitle;
    private ListView areaList;
    private ArrayAdapter<String> adapter;
    private List<String> dataList;
    private ArrayList<Province> provinces;
    private ArrayList<City> cities;
    private ArrayList<District> districts;
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_DISTRICT = 2;
    private  int  currentLevel;
    private ChooseGbManager manager;
    private Province selectionProvince;
    private City selectionCity;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (result) {
                if ("province".equals(msg.obj)){
                    queryProvince();
                }else if ("city".equals(msg.obj)){
                    queryCity();
                }else if ("district".equals(msg.obj)){
                    queryDistrict();
                }
            }else {
                Toast.makeText(ChooseAreaActivity.this,"查询有误，请检查",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        manager = new ChooseGbManager();
        areaTitle = (TextView) findViewById(R.id.area_title);
        areaList = (ListView) findViewById(R.id.area_list);
        dataList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        areaList.setAdapter(adapter);
        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectionProvince = provinces.get(position);
                    queryCity();
                }else if (currentLevel == LEVEL_CITY){
                    selectionCity = cities.get(position);
                    queryDistrict();
                }else {
                    //点击区县，暂无反应
                }

            }
        });
        queryProvince();
    }

    /**
     * 查询全国所有省份，优先从本地数据库中读取，如果没有查询到再去服务器获取
     */
    private void queryProvince() {
        DBUtil dbUtil = DBUtil.getDbUtilInstance(this);
        provinces =  dbUtil.loadProvince();
        if (provinces != null&&provinces.size()>0){
            dataList.clear();
            for (Province province:provinces){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            areaList.setSelection(0);
            areaTitle.setText("全国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer("province");
        }

    }

    /**
     * 查询选中省份的所有城市，优先从本地数据库中读取，如果没有查询到再去服务器获取
     */
    public void queryCity(){
        DBUtil dbUtil = DBUtil.getDbUtilInstance(this);
        cities  = dbUtil.loadCity(selectionProvince.getProvinceName());
        if (cities != null && cities.size()>0){
            dataList.clear();
            for (City city:cities){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            areaTitle.setText(selectionProvince.getProvinceName());
            areaList.setSelection(0);
            currentLevel = LEVEL_CITY;

        }else {
            queryFromServer("city");
        }
    }

    /**
     * 查询选中城市的所有县区，优先从本地数据库中读取，如果没有查询到再去服务器获取
     */
    private void queryDistrict(){
        DBUtil dbUtil = DBUtil.getDbUtilInstance(this);
        districts = dbUtil.loadDistrict(selectionCity.getProvinceName(),
                selectionCity.getCityName());
        if (districts != null&&districts.size()>0){
            dataList.clear();
            for (District district:districts){
                dataList.add(district.getDistrictName());
            }
            adapter.notifyDataSetChanged();
            areaList.setSelection(0);
            areaTitle.setText(selectionCity.getCityName());
            currentLevel = LEVEL_DISTRICT;
        }else {
            queryFromServer("district");
        }
    }

    /**
     * 访问服务器后，解析处理数据是否成功
     */
    private boolean result;

    /**
     * 根据传入的类型从服务器上查询省市县的数据
     * @param type  查询的类型
     */
    private void queryFromServer(final String type) {

       manager.loadGb(this, new ResponseHandle() {
           @Override
           public void onFinish(String jsonData) {
           result = manager.handleGbResponse(jsonData);
           Message msg = new Message();
           msg.obj  = type;
           handler.sendMessage(msg);
       }
           @Override
           public void onError(int errorCode) {

           }
       });
    }

    /**
     * 根据当前列表的省市区级别，判断当按下BACK键时，应返回哪个界面
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_DISTRICT){
            queryCity();
        }  else if (currentLevel == LEVEL_CITY){
            queryProvince();
        } else {
            finish();
        }
    }
}
