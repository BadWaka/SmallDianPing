package com.waka.workspace.smalldianping.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.waka.workspace.smalldianping.Constant;

/**
 * 数据库帮助类
 * Created by wang_xiaojie,waka on 2015/12/25.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";// log标记

    //创建数据库表的SQL语句
    public static final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE " + Constant.TABLE_NAME_SEARCH_HISTORY + " (" + Constant.COLUMN_NAME_HISTORY_IN_SEARCH_HISTORY + " TEXT PRIMARY KEY)";//创建搜索历史表
    public static final String CREATE_TABLE_STORE = "CREATE TABLE " + Constant.TABLE_NAME_STORE
            + " (" + Constant.COLUMN_NAME_STORE_ID_IN_STORE + " TEXT PRIMARY KEY,"
            + Constant.COLUMN_NAME_STORE_RATING_IN_STORE + " DOUBLE,"
            + Constant.COLUMN_NAME_STORE_AVERAGE_COST_IN_STORE + " DOUBLE,"
            + Constant.COLUMN_NAME_STORE_IMAGE_IN_STORE + " BLOB,"
            + Constant.COLUMN_NAME_STORE_NAME_IN_STORE + " TEXT,"
            + Constant.COLUMN_NAME_STORE_CITY_IN_STORE + " TEXT,"
            + Constant.COLUMN_NAME_STORE_ADDRESS_IN_STORE + " TEXT,"
            + Constant.COLUMN_NAME_STORE_INTRODUCTION_IN_STORE + " TEXT,"
            + Constant.COLUMN_NAME_STORE_DETAIL_IN_STORE + " TEXT"
            + ")";//创建商家信息表
    public static final String CREATE_TABLE_ORDER = "CREATE TABLE " + Constant.TABLE_NAME_ORDER
            + " (" + Constant.COLUMN_NAME_ORDER_ID_IN_ORDER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Constant.COLUMN_NAME_USER_ID_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_STORE_ID_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_GOODS_ID_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_GOODS_NUM_IN_ORDER + " INTEGER,"
            + Constant.COLUMN_NAME_ORDER_TOTAL_COST_IN_ORDER + " DOUBLE,"
            + Constant.COLUMN_NAME_ORDER_KEY_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_ORDER_PAY_DATETIME_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_ORDER_EVAULATION_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_ORDER_EVAULATION_DATETIME_IN_ORDER + " TEXT,"
            + Constant.COLUMN_NAME_ORDER_RATING_IN_ORDER + " DOUBLE,"
            + Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER + " INTEGER"
            + ")";
    //创建商品信息表
    public static final String CREATE_TABLE_COMMODITY= "CREATE TABLE "+Constant.TABLE_NAME_COMMODITY+" ("
            + Constant.COLUMN_NAME_COMMODITY_COMMODITYID +    " TEXT     ,"   //商品ID
            + Constant.COLUMN_NAME_COMMODITY_STOREID     +    " TEXT     ,"   //商店ID
            + Constant.COLUMN_NAME_COMMODITY_NAME        +    " TEXT     ,"   //商品名称
            + Constant.COLUMN_NAME_COMMODITY_PRICE       +    " INTEGER  ,"   //商品价格
            + Constant.COLUMN_NAME_COMMODITY_SOLD        +    " INTEGER  ,"   //已售商品数量
            + Constant.COLUMN_NAME_COMMODITY_PICTURE     +    " BLOB     )";   //商品图片

    //创建用户表
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + Constant.TABLE_NAME_USER
            + " (" + Constant.COLUMN_NAME_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Constant.COLUMN_NAME_USER_NAME + " TEXT,"
            + Constant.COLUMN_NAME_USER_PWD + " TEXT"
            +")";
    //创建触发器
    public static final String CREATE_TRIGGER_UPDATE_ORDER_EVALUATION="CREATE TRIGGER Update_Order_Evaluation " +
            "AFTER UPDATE ON "+Constant.TABLE_NAME_ORDER +" "+
            "FOR EACH ROW BEGIN "+
            "UPDATE "+Constant.TABLE_NAME_STORE+" SET "+Constant.COLUMN_NAME_STORE_RATING_IN_STORE+"=("+
            "SELECT AVG("+Constant.COLUMN_NAME_ORDER_RATING_IN_ORDER+") FROM "+Constant.TABLE_NAME_ORDER+" "+
            "WHERE "+Constant.COLUMN_NAME_STORE_ID_IN_ORDER+"=NEW."+Constant.COLUMN_NAME_STORE_ID_IN_ORDER+" AND "+Constant.COLUMN_NAME_ORDER_STATE_IN_ORDER +"=1) "+
            "WHERE "+Constant.COLUMN_NAME_STORE_ID_IN_STORE+"=NEW."+Constant.COLUMN_NAME_STORE_ID_IN_ORDER+
            "; END";
    private Context mContext;

    /**
     * 一个参数的构造方法，用它调用四个参数的构造方法
     *
     * @param context
     */
    public DBHelper(Context context) {
        this(context, Constant.DATA_BASE_NAME, null, 1);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     * 创建数据库
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
        db.execSQL(CREATE_TABLE_STORE);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_COMMODITY);

        db.execSQL(CREATE_TRIGGER_UPDATE_ORDER_EVALUATION);
        new InitializationTable(mContext,db);
    }

    /**
     * 升级数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
