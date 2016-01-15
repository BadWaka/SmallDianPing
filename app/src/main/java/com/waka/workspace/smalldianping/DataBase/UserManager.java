package com.waka.workspace.smalldianping.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.waka.workspace.smalldianping.Constant;

/**
 * Created by dong on 2016/1/12.
 */
public class UserManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public UserManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里

    }

    /**
     * add persons
     * @param user
     */
    public void add(User user) {
        db = helper.getWritableDatabase();
        //创建存放数据的ContentValues对象
        ContentValues values = new ContentValues();
        //像ContentValues中存放数据
        values.put(Constant.COLUMN_NAME_USER_NAME,user.getName());
        values.put(Constant.COLUMN_NAME_USER_PWD,user.getPwd());

        //数据库执行插入命令
        long insert = db.insert(Constant.TABLE_NAME_USER, null, values);
        user.setId((int) insert);
        db.close();
    }

    /**
     * delete
     *
     */
    public void delete(User user) {

    }

    /**
     * query
     */
    public User query(String pwd,String name) {
        db = helper.getWritableDatabase();
        //创建游标对象
        Cursor cursor = db.query(Constant.TABLE_NAME_USER,null,null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            String userName = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_USER_NAME));
            String userPWD= cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME_USER_PWD));
            int userID= cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_NAME_USERID));
            if(pwd.equals(userPWD)&&name.equals(userName)){
                cursor.close();
                db.close();
                return new User(userID,userName,userPWD);

            }

        }
        cursor.close();
        db.close();
        return null;
    }

    /**
     * close database
     */
    public void closeDB() {
        if(db!=null&&db.isOpen())
        db.close();
    }
}
