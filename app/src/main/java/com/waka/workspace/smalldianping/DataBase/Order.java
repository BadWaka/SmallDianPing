package com.waka.workspace.smalldianping.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.waka.workspace.smalldianping.Constant;

import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class Order {
    private int order_id=-1;//订单ID
    private String user_id;//订单客户的ID
    private String store_id;//订单商家ID
    private String goods_id;//订单商品的ID
    private int    goods_num;//订单中商品的数量
    private double order_total_cost;//订单的总金额
    private String order_key;//订单使用码
    private Date order_pay_datetime;//订单付款时间
    private String str_order_pay_datetime;//字符串型 订单付款时间
    private String str_evaluation_datetime;//字符串型 订单评论时间

    private String order_evaluation;//订单评论
    private Date order_evaluation_datetime;//订单评论时间
    private double order_rating;//订单评价星级
    private int order_state;//订单状态（未评价，已完成）

    public static DBHelper dbhelper;//数据库连接


    //生成订单时使用的构造方法
    public Order(String user_id,String store_id,String goods_id,int goods_num,double order_total_cost){
        this.user_id=user_id;
        this.store_id=store_id;
        this.goods_id=goods_id;
        this.goods_num=goods_num;
        this.order_total_cost=order_total_cost;
        this.order_key="1001 0510 2356 5518";
        this.order_pay_datetime=new Date(System.currentTimeMillis());//获取当前时间

        this.order_evaluation_datetime=new Date(System.currentTimeMillis());
        this.order_evaluation="";
        this.order_rating=0;
        this.order_state=0;

    }

    public Order(String order_id){
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME_ORDER, null, Constant.COLUMN_NAME_ORDER_ID_IN_ORDER+"=?", new String[]{order_id}, null, null, null);
        if(cursor.moveToNext()){
            this.user_id=cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_USER_ID_IN_ORDER));
            this.order_key=cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_KEY_IN_ORDER));
            this.order_id = cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_ID_IN_ORDER));
            this.store_id = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_STORE_ID_IN_ORDER));
            this.goods_id=cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_GOODS_ID_IN_ORDER));
            this.goods_num = cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_GOODS_NUM_IN_ORDER));
            this.order_total_cost = cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_TOTAL_COST_IN_ORDER));
            this.str_order_pay_datetime = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_PAY_DATETIME_IN_ORDER));
            this.order_evaluation=cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_EVAULATION_IN_ORDER));
            this.str_evaluation_datetime=cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_EVAULATION_DATETIME_IN_ORDER));
            this.order_rating=cursor.getDouble(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_RATING_IN_ORDER));
            this.order_state=cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER));
        }
        cursor.close();
        db.close();
    }

    public long addOrder(){

        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_NAME_USER_ID_IN_ORDER, user_id);
        values.put(Constant.COLUMN_NAME_STORE_ID_IN_ORDER, store_id);
        values.put(Constant.COLUMN_NAME_GOODS_ID_IN_ORDER, goods_id);
        values.put(Constant.COLUMN_NAME_GOODS_NUM_IN_ORDER, goods_num);
        values.put(Constant.COLUMN_NAME_ORDER_TOTAL_COST_IN_ORDER, order_total_cost);
        values.put(Constant.COLUMN_NAME_ORDER_KEY_IN_ORDER, order_key);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_pay_datetime = format.format(order_pay_datetime);
        values.put(Constant.COLUMN_NAME_ORDER_PAY_DATETIME_IN_ORDER, str_pay_datetime);
        values.put(Constant.COLUMN_NAME_ORDER_EVAULATION_IN_ORDER, order_evaluation);
        values.put(Constant.COLUMN_NAME_ORDER_EVAULATION_DATETIME_IN_ORDER, "");
        values.put(Constant.COLUMN_NAME_ORDER_RATING_IN_ORDER, order_rating);
        values.put(Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER, order_state);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        long ret= db.insert(Constant.TABLE_NAME_ORDER, null, values);
        db.close();
        return  ret;
    }

    public static List<Order> getUserAllOrdersByUserID(String user_id){
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        List<Order> listOrder=new ArrayList<Order>();
        Cursor cursor=db.query(Constant.TABLE_NAME_ORDER, null, Constant.COLUMN_NAME_USER_ID_IN_ORDER+"=?", new String[]{user_id}, null, null, null);
        while(cursor.moveToNext()){
            listOrder.add(new Order(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_ID_IN_ORDER))));
        }
        cursor.close();
        db.close();
        return listOrder;
    }

    public static List<Order> getUserAllOrdersByStoreID(String store_id){
        List<Order> listOrder=new ArrayList<Order>();
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor cursor=db.query(Constant.TABLE_NAME_ORDER, null, Constant.COLUMN_NAME_STORE_ID_IN_ORDER + "=?", new String[]{store_id}, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER))==1)//是否有评论
                listOrder.add(new Order(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_ID_IN_ORDER))));
        }
        cursor.close();
        db.close();
        return listOrder;
    }

    public static List<Order> getUserAllOrdersByGoodsID(String goods_id){
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        List<Order> listOrder=new ArrayList<Order>();
        Cursor cursor=db.query(Constant.TABLE_NAME_ORDER, null, Constant.COLUMN_NAME_GOODS_ID_IN_ORDER+"=?", new String[]{goods_id}, null, null, null);
        while(cursor.moveToNext()){
            listOrder.add(new Order(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_ORDER_ID_IN_ORDER))));
        }
        cursor.close();
        db.close();
        return listOrder;
    }
    public static void addOrderevaluation(int order_id,String order_evaluation,double order_rating)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String str_pay_datetime = format.format(date);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER,1);
        cv.put(Constant.COLUMN_NAME_ORDER_EVAULATION_IN_ORDER,order_evaluation);
        cv.put(Constant.COLUMN_NAME_ORDER_EVAULATION_DATETIME_IN_ORDER,str_pay_datetime);
        cv.put(Constant.COLUMN_NAME_ORDER_RATING_IN_ORDER, order_rating);
        db.update(Constant.TABLE_NAME_ORDER, cv, Constant.COLUMN_NAME_ORDER_ID_IN_ORDER + "=?", new String[]{String.valueOf(order_id)});
        db.close();
    }

    public int getOrder_id(){return order_id;}
    public String getStore_id(){return store_id;}
    public int getOrder_state(){return order_state;}
    public double getOrder_total_cost(){return order_total_cost;}
    public Date getOrder_pay_datetime(){return order_pay_datetime;}
    public String getStr_order_pay_datetime(){return str_order_pay_datetime;}
    public String str_evaluation_datetime(){return str_evaluation_datetime;}
    public double get_order_rating(){return order_rating;}
    public String get_order_evaluation(){return order_evaluation;}
    public String getGoods_id(){return goods_id;}
    public int getGoods_num(){return this.goods_num;}
    public String getOrder_key(){return this.order_key;}
    public String getStr_evaluation_datetime(){return str_evaluation_datetime;}
    public String getUser_id(){return this.user_id;}
}
