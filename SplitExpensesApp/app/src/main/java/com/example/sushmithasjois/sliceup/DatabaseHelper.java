package com.example.sushmithasjois.sliceup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;


/**
 * Created by Sushmitha S Jois on 4/3/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context, "project.db", null, 1);
        db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists user(username TEXT PRIMARY KEY,password TEXT)");
        db.execSQL("Insert into user values('sush@gmail.com','sush')");
        db.execSQL("Insert into user values('fida@gmail.com','fida')");



}
    public int login(String name,String pass){
        //StringBuffer buffer=new StringBuffer();

        Cursor c= db.rawQuery("select password from user where username='"+name+"';'",null);
        c.moveToFirst();
        if(c.moveToFirst()){
            if(pass.equals(c.getString(0))){
            return 1;
        }}
        return -1; }


    /*if(c.getCount()==0)    {
            buffer.append(1);
            return(buffer);     }
        while(c.moveToNext()){
            buffer.append(c.getString(0));    }
        return(buffer); */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
