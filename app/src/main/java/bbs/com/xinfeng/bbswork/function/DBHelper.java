package bbs.com.xinfeng.bbswork.function;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bbs.com.xinfeng.bbswork.utils.LogUtil;

/**
 * Created by dell on 2017/12/13.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mvphelper.db";
    public static final String TABLE_NAME = "crash";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //版本Version；渠道Channel；型号Model；系统System；时间Time；崩溃日志Log
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key, Version text, Channel text, Model text, System text, Time text, Log text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


    //插入方法
    public void insert(ContentValues values) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        //插入数据库中
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //查询方法
    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return c;

    }

    //根据唯一标识_id  来删除数据
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    //删除table中所有数据
    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_NAME, null, null);
        LogUtil.i("kkmm:" + delete);
    }


    //更新数据库的内容
    public void update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    //关闭数据库
    public void close() {
        if (db != null) {
            db.close();
        }
    }

    /*{DBHelper dbHelper = new DBHelper(provideActivity());
    Cursor cursor = dbHelper.query();
        while (cursor.moveToNext()) {
        if (cursor.isLast()){
            //版本Version；渠道Channel；型号Model；系统System；时间Time；崩溃日志Log
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            String version = cursor.getString(cursor.getColumnIndex("Version"));
            String channel = cursor.getString(cursor.getColumnIndex("Channel"));
            String model = cursor.getString(cursor.getColumnIndex("Model"));
            String system = cursor.getString(cursor.getColumnIndex("System"));
            String time = cursor.getString(cursor.getColumnIndex("Time"));
            String log = cursor.getString(cursor.getColumnIndex("Log"));
            LogUtil.i("crash:::", id + "__" + version + "__" + channel + "__" + model + "__" + system + "__" + time + "__" + log);

        }
    }
        cursor.close();
        dbHelper.deleteAll();
        dbHelper.close();}*/
}
