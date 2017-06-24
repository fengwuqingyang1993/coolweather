package com.jackie.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jackie.coolweather.model.City;
import com.jackie.coolweather.model.District;
import com.jackie.coolweather.model.GbAddress;
import com.jackie.coolweather.model.Province;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */
public class DBUtil {

    private GbOpenHelper helper;
    private String tableName;
    private Context context;
    private static DBUtil dbUtilInstance;

    private DBUtil(Context context) {
        this.context = context;
        this.helper = new GbOpenHelper(context);
    }

    public static DBUtil getDbUtilInstance(Context context){
        if (null ==dbUtilInstance){
            dbUtilInstance = new DBUtil(context);
        }

        return dbUtilInstance;
    }

    /**
     * 把一个地区数据存入数据库表中
     * @param gb 地区的实体类
     */
    public void saveGb(GbAddress gb){
        SQLiteDatabase database = helper.getWritableDatabase();
        if (null != gb){
            ContentValues values = new ContentValues();
            values.put("_id",gb.getId());
            values.put("province_name",gb.getProvince());
            values.put("city_name",gb.getCitys());
            values.put("district_name",gb.getDistrict());

            database.insert("gb",null,values);
        }
    }

    /**
     * 在总表中查询全国所有省份
     * @return 全国所有省份的列表
     */
    public ArrayList<Province> loadProvince(){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Province> provinces = new ArrayList<Province>();
        Cursor cursor = null;
        //运用了数据库的去重复查询方法
        cursor = db.query(true,"gb",new String[]{"_id","province_name"},null,null,
                "province_name", null,null,null);
        while (cursor.moveToNext()){
            Province province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            province.setProvinceName(cursor.getString(
                    cursor.getColumnIndex("province_name")));
            provinces.add(province);
        }
        cursor.close();
        db.close();
        return provinces;
    }

    /**
     * 根据省份，查询该省分的所有城市
     * @param provinceName  要查询的省份名称
     * @return  该省份的所有城市
     */
    public ArrayList<City> loadCity(String provinceName){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<City> cities = new ArrayList<City>();
        Cursor cursor = null;
        cursor = db.query(true,"gb",new String[]{"_id","province_name","city_name"},
                "province_name=?",new String[]{provinceName},"city_name",null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            cities.add(city);
        }
        cursor.close();
        db.close();
        return cities;
    }

    /**
     * 根据所给的省份和城市，查询城市所有的区县
     * @param provinceName 省份的名称
     * @param cityName 城市的名称
     * @return  该城市的所有区县
     */
    public ArrayList<District> loadDistrict(String provinceName,String cityName){
        ArrayList<District> districts = new ArrayList<District>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(true,"gb",null,"province_name=? and city_name=?",
                new String[]{provinceName,cityName},null,null,null,null);
        while (cursor.moveToNext()){
            District district = new District();
            district.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            district.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            district.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            district.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
            districts.add(district);
        }
        cursor.close();
        db.close();
        return districts;
    }

    /**
     * 删除数据库的所有信息
     */
    public void deleteAll(){
        SQLiteDatabase db  = helper.getWritableDatabase();
        db.delete("gb",null,null);
        db.close();
    }

}
