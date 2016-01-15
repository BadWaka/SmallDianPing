package com.waka.workspace.smalldianping;

/**
 * 常量类
 * Created by waka on 2015/12/22.
 */
public class Constant {

    //数据库库名
    public static final String DATA_BASE_NAME = "SmallDianPing.db";//数据库全名，包括后缀名
    //搜索历史表
    public static final String TABLE_NAME_SEARCH_HISTORY = "search_history";//表名
    public static final String TABLE_NAME_ORDER = "orders";//订单表名
    public static final String TABLE_NAME_STORE = "store";//商家信息表名
    public static final String TABLE_NAME_USER = "user";//用户信息表名

    public static final String COLUMN_NAME_HISTORY_IN_SEARCH_HISTORY = "_history";//历史记录,TEXT PRIMARY KEY

    //商家信息表

    public static final String COLUMN_NAME_STORE_ID_IN_STORE = "_store_id";//商家id,TEXT PRIMARY KEY
    public static final String COLUMN_NAME_STORE_RATING_IN_STORE = "_store_rating";//商家评分,DOUBLE
    public static final String COLUMN_NAME_STORE_AVERAGE_COST_IN_STORE = "_store_avgcost";//人均花费,DOUBLE
    public static final String COLUMN_NAME_STORE_IMAGE_IN_STORE = "_store_image";//商家图片,BLOB
    public static final String COLUMN_NAME_STORE_NAME_IN_STORE = "_store_name";//商家名
    public static final String COLUMN_NAME_STORE_ADDRESS_IN_STORE = "_store_address";//商家具体地址
    public static final String COLUMN_NAME_STORE_CITY_IN_STORE = "_store_city";//商家所在城市
    public static final String COLUMN_NAME_STORE_DETAIL_IN_STORE = "_store_detail";//商家具体介绍
    public static final String COLUMN_NAME_STORE_INTRODUCTION_IN_STORE = "_store_introduction";//商家简介

    //订单信息表
    public static final String COLUMN_NAME_ORDER_ID_IN_ORDER = "_order_id";//订单ID
    public static final String COLUMN_NAME_USER_ID_IN_ORDER = "_user_id";//订单客户ID
    public static final String COLUMN_NAME_STORE_ID_IN_ORDER = "_store_id";//订单商家ID
    public static final String COLUMN_NAME_GOODS_ID_IN_ORDER = "_goods_id";//订单商品ID
    public static final String COLUMN_NAME_GOODS_NUM_IN_ORDER = "_goods_num";//订单商品数量
    public static final String COLUMN_NAME_ORDER_TOTAL_COST_IN_ORDER = "_order_total_cost";//订单总价
    public static final String COLUMN_NAME_ORDER_KEY_IN_ORDER = "_order_key";//订单使用码
    public static final String COLUMN_NAME_ORDER_PAY_DATETIME_IN_ORDER = "_order_pay_datetime";//订单付款时间
    public static final String COLUMN_NAME_ORDER_EVAULATION_IN_ORDER = "_order_evaulation";//订单评论
    public static final String COLUMN_NAME_ORDER_EVAULATION_DATETIME_IN_ORDER = "_order_evaulation_datetime";//订单评论时间
    public static final String COLUMN_NAME_ORDER_RATING_IN_ORDER = "_order_rating";//订单评价星级
    public static final String COLUMN_NAME_ORDER_STATE_IN_ORDER = "_order_state";//订单状态

    //用户信息
    public static final String COLUMN_NAME_USERID= "_user_id";//用户id,INTEGER PRIMARY KEY
    public static final String COLUMN_NAME_USER_NAME = "_user_name";//用户名,TEXT
    public static final String COLUMN_NAME_USER_PWD = "_user_pwd";//用户密码,TEXT


    //RequestCode
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 1001;//从LoginActivity返回的RequestCode
    public static final int SEARCH_INPUT_ACTIVITY_REQUEST_CODE = 1002;//从SearchInputActivity返回的RequestCode
    public static final int RESULT_LIST_ACTIVITY_REQUEST_CODE = 1003;//从ResultListActivity返回的RequestCode
    public static final int RESULT_MAP_ACTIVITY_REQUEST_CODE = 1004;//从ResultMapActivity返回的RequestCode
    public static final int CITY_PICKER_DIALOG_ACTIVITY_REQUEST_CODE = 1005;//从CityPickerDialogActivity返回的RequestCode
    public static final int STORE_DETAIL_ACTIVITY_REQUEST_CODE = 1006;//从StoreDetailActivity返回的RequestCode
    public static final int ORDER_DETAIL_ACTIVITY_REQUEST_CODE = 1007;//从OrderEvaluateActivity返回的RequestCode
    public static final int ORDER_EVALUATE_ACTIVITY_REQUEST_CODE = 1008;//从OrderEvaluateActivity返回的RequestCode
    public static final int REGIST_ACTIVITY_REQUEST_CODE = 1009;
    public static final int CAPTURE_ACTIVITY_REQUEST_CODE=1010;
    //商品信息表
    public static final String  TABLE_NAME_COMMODITY="commoditytable";
    public static final String  COLUMN_NAME_COMMODITY_NAME="_commodityname"; //商品名称
    public static final String  COLUMN_NAME_COMMODITY_STOREID="_storeID";//商品对应的商店ID
    public static final String  COLUMN_NAME_COMMODITY_COMMODITYID="_commodityID";//商品ID
    public static final String  COLUMN_NAME_COMMODITY_PRICE="_price";//商品价格
    public static final String  COLUMN_NAME_COMMODITY_SOLD ="_sold";//商品销售量
    public static final String  COLUMN_NAME_COMMODITY_PICTURE ="_picture";//商品图片
}
