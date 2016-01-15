package com.waka.workspace.smalldianping.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.waka.workspace.smalldianping.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WWC on 2016/1/8.
 */
public class CommodityDB {
    private DBHelper DBHelper;
    private SQLiteDatabase DB;
    private Context context;
    private String storeID;
    private String commodityID;
    public static DBHelper dbHelper;
    int sold;
    public List<CommodityInformation> commodityList = new ArrayList<CommodityInformation>();

    //用于初始化ListView对应的List对应的构造函数
    public CommodityDB(Context context, String storeID) {
        this.context = context;
        this.storeID = storeID;
    }

    //用于更新销售量（sold）对应的构造函数
    public CommodityDB(Context context, String commodityID, int sold) {
        this.context = context;
        this.commodityID = commodityID;
        this.sold = sold;
    }

    //初始化链表
    public void initIntroduction() {
        DBHelper = new DBHelper(context);
        DB = DBHelper.getWritableDatabase();
        Cursor cursor = DB.query(Constant.TABLE_NAME_COMMODITY, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_STOREID)).equals(storeID)) {
                byte[] picture = cursor.getBlob(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_PICTURE));
                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                commodityList.add(new CommodityInformation(
                        cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_STOREID)),//商店ID
                        cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_COMMODITYID)),//商品ID
                        cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_NAME)),//商品名称
                        bitmap, //商品图片
                        cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_PRICE)), //商品价格
                        cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_SOLD))//商品销售量
                ));
                Log.i("!!!!!!!!!!!", String.valueOf(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_SOLD))));

                Log.i("ID", cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_COMMODITYID)));
            }
        }
        DB.close();
    }

    //更新销售量
    public void updateSold() {
        DBHelper = new DBHelper(context);
        DB = DBHelper.getWritableDatabase();
        String sql = "UPDATE "
                + Constant.TABLE_NAME_COMMODITY
                + " SET "
                + Constant.COLUMN_NAME_COMMODITY_SOLD
                + "="
                + Constant.COLUMN_NAME_COMMODITY_SOLD
                + "+"
                + sold
                + " WHERE "
                + Constant.COLUMN_NAME_COMMODITY_COMMODITYID
                + "='"
                + commodityID + "'";
        Log.i("sqllllllllllll", sql);
        DB.execSQL(sql);
        DB.close();
    }

    public static String getGoodsName(String goodsid) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME_COMMODITY, null, Constant.COLUMN_NAME_COMMODITY_COMMODITYID + "=?", new String[]{goodsid}, null, null, null, null);
        if (cursor.moveToNext()) {
            String str = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_NAME));
            cursor.close();
            return str;
        }
        return null;
    }

    /**
     * 得到物品图像
     *
     * @param goodsid
     * @return
     */
    public static Bitmap getGoodsBitmap(String goodsid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME_COMMODITY, null, Constant.COLUMN_NAME_COMMODITY_COMMODITYID + "=?", new String[]{goodsid}, null, null, null, null);
        if (cursor.moveToNext()) {
            Bitmap bitmap = null;
            try {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Constant.COLUMN_NAME_COMMODITY_PICTURE));
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            } finally {
                cursor.close();
                db.close();
            }
        }
        return null;
    }
}
