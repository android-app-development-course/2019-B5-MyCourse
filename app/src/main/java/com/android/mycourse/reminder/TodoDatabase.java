package com.android.mycourse.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabase extends SQLiteOpenHelper {
    public static final String TODO = "create table Todo ("
            + "id integer primary key autoincrement, "
            + "todotitle String, "
            + "tododsc String,"
            + "tododate String,"
            + "todotime String,"
            + "objectId String,"
            + "remindTime long,"
            + "imgId int,"
            + "isRepeat int )";

    TodoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists Todo");
        onCreate(db);
    }
}
