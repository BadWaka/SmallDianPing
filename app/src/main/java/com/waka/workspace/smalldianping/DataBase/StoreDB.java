package com.waka.workspace.smalldianping.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.waka.workspace.smalldianping.Constant;

/**
 * Created by wang_xiaojie on 2015/12/25 0025.
 */
public class StoreDB implements PoiSearch.OnPoiSearchListener {
    private PoiItem poiItem;//poi信息，包括id，名称，地址等等
    private String store_id;
    private String store_name;//商家名
    private String store_address;//商家地址
    private String store_city;//商家所在城市
    private String store_introduction;//商家简介
    private String store_detail;//商家具体介绍
    private float distanceToLocation = -1;
    private double store_rating = -1;//商家星级
    private double store_avgcost = -1;//人均消费
    private byte[] store_image = null;//商家照片
    private SQLiteDatabase db;//数据库连接

    public StoreDB(PoiItem poiItem, SQLiteDatabase db) {
        this.poiItem = poiItem;
        this.db = db;
        this.store_name=poiItem.getTitle();
        this.store_address=poiItem.getSnippet();
        store_id=poiItem.getPoiId();

        Cursor cursor = db.query("store", null, "_store_id=?", new String[]{poiItem.getPoiId()}, null, null, null);
        if (cursor.moveToNext()) {
            this.store_rating = cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_RATING_IN_STORE));
            this.store_avgcost = cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_AVERAGE_COST_IN_STORE));
            this.store_image = cursor.getBlob(cursor.getColumnIndex("_store_image"));
            this.store_name = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_NAME_IN_STORE));
            this.store_city = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_CITY_IN_STORE));
            this.store_address = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_ADDRESS_IN_STORE));
            this.store_introduction = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_INTRODUCTION_IN_STORE));
            this.store_detail = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_DETAIL_IN_STORE));
        }
        cursor.close();
    }

    public StoreDB(String id, SQLiteDatabase db) {
        this.db = db;
        store_id=id;
        Cursor cursor = db.query("store", null, "_store_id=?", new String[]{id}, null, null, null);
        if (cursor.moveToNext()) {
            this.store_rating = cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_RATING_IN_STORE));
            this.store_avgcost = cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_AVERAGE_COST_IN_STORE));
            this.store_image = cursor.getBlob(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_IMAGE_IN_STORE));
            this.store_name = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_NAME_IN_STORE));
            this.store_city = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_CITY_IN_STORE));
            this.store_address = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_ADDRESS_IN_STORE));
            this.store_introduction = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_INTRODUCTION_IN_STORE));
            this.store_detail = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_DETAIL_IN_STORE));
        }
        cursor.close();

    }

    public void setDistanceToLocation(float dis) {
        distanceToLocation = dis;
    }

    public float getDistanceToLocation() {
        return distanceToLocation;
    }

    public String getStoreID() {
        return this.store_id;
    }

    public String getStoreSnippet() {
        return this.store_address;
    }

    public double getStoreRating() {
        return this.store_rating;
    }

    public double getStoreAvgcost() {
        return this.store_avgcost;
    }

    public String getStoreName() {
        return store_name;
    }

    public String getStoreType() {
        return poiItem.getTypeDes();
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public byte[] getImageBytes() {
        return this.store_image;
    }

    public String getStore_addresss(){return this.store_address;};//商家地址

    public String getStore_detail(){return store_detail;};//商家具体介绍


    public static long addStore(PoiItem poiItem, double avgcost, byte[] bytesImage, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_NAME_STORE_ID_IN_STORE, poiItem.getPoiId());
        values.put(Constant.COLUMN_NAME_STORE_NAME_IN_STORE, poiItem.getTitle());
        values.put(Constant.COLUMN_NAME_STORE_CITY_IN_STORE, poiItem.getCityName());
        values.put(Constant.COLUMN_NAME_STORE_ADDRESS_IN_STORE, poiItem.getSnippet());
        values.put(Constant.COLUMN_NAME_STORE_INTRODUCTION_IN_STORE, poiItem.getTypeDes());
        values.put(Constant.COLUMN_NAME_STORE_DETAIL_IN_STORE, "暂无商家介绍");
        values.put(Constant.COLUMN_NAME_STORE_RATING_IN_STORE, 0.0);
        values.put(Constant.COLUMN_NAME_STORE_AVERAGE_COST_IN_STORE, avgcost);
        values.put(Constant.COLUMN_NAME_STORE_IMAGE_IN_STORE, bytesImage);
        return db.insert("store", null, values);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }
}
