package com.waka.workspace.smalldianping.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.R;
import java.io.ByteArrayOutputStream;

/**
 * Created by WWC on 2016/1/5.
 */
public class InitializationTable {
    private Context context=null;
    SQLiteDatabase db=null;
    private String[] allstoreID={"B0FFFAHQ4Q"};
    private String[] allcommodityID={"0","1","2","3","4","5","6","7","8","9","10"};
    private String[] allcommodityname={"养生大拌菜","烤鱼","麻将大拉皮","金银馒头","金针魔芋丝","烤鱼","烤鱼","火爆鱿鱼花","烤鱼","烤鱼","小米青菜肥牛","干锅有机菜花","啤酒炝锅鱼","圆锅水晶粉","蛰皮白心菜","鱼米烩滑仔菇","小米青菜肥牛","火爆鱿鱼花"};
    private Double[] allprice={10.0,12.0,12.0,13.0,45.0,25.0,36.0,25.0,56.0,25.0,15.0,25.5,25.0,23.0,26.0,28.0,29.0,30.0};
    private int      allsold=0;
    public InitializationTable(Context context,SQLiteDatabase db){
        this.context=context;
        this.db=db;
        initDataBase();
    }
    public void insert(String commodityID,String storeID,String commodityname,Double price, int sold, byte[] picture)
    {
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_NAME_COMMODITY_COMMODITYID,commodityID);
        values.put(Constant.COLUMN_NAME_COMMODITY_STOREID,storeID);
        values.put(Constant.COLUMN_NAME_COMMODITY_NAME,commodityname);
        values.put(Constant.COLUMN_NAME_COMMODITY_PRICE,price);
        values.put(Constant.COLUMN_NAME_COMMODITY_SOLD,sold);
        values.put(Constant.COLUMN_NAME_COMMODITY_PICTURE,picture);
        db.insert(Constant.TABLE_NAME_COMMODITY, null, values);
    }
    private void initDataBase()
    {
        for(int i=0;i<11;i++) {
            byte[] picture = getpicture(i);
            insert(allcommodityID[i], allstoreID[0], allcommodityname[i], allprice[i], allsold, picture);
        }
    }
    //将drawable再转换成可以用来存储的byte[]类型
    private byte[] getpicture(int i)
    {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        switch (i) {
            case 0:
            BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic1)
                    .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 1:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic2)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 2:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic3)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 3:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic4)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 4:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic5)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 5:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic6)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 6:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic7)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 7:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic8)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 8:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic9)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 9:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic10)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
            case 10:
                BitmapFactory.decodeResource(context.getResources(), R.drawable.initpic11)
                        .compress(Bitmap.CompressFormat.PNG, 100, os);
                break;
        }
        return os.toByteArray();
    }
}
