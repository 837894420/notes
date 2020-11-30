package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MyDataBaseHelper extends SQLiteOpenHelper{     //对数据库进行创建

    public static  final String CREATE_NOTE = "create table Note("
            + "id integer primary key autoincrement,"
            + "title text not null,"
            + "author text not null,"
            + "content text,"
            + "date datetime not null default current_time,"
            + "image BLOB )";

    // 重写构造方法
    private Context mContext;
    public MyDataBaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }



    @Override
    // 创建数据库的方法
    public void  onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NOTE);
        //初始化

    }
    // 更新数据库的方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

