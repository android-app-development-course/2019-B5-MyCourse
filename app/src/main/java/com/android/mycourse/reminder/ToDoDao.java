package com.android.mycourse.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoDao {
    private TodoDatabase dbHelper;
    private SQLiteDatabase db;

    ToDoDao(Context context) {
        dbHelper = new TodoDatabase(
                context.getApplicationContext(), "Data.db", null, 2);
    }

    private void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    /**
     * 创建成功，返回记录的ID
     *
     * @param todos
     */
    void create(Todos todos) {

        open();
        ContentValues values = new ContentValues();
        values.put("todotitle", todos.getTitle());
        values.put("tododsc", todos.getDesc());
        values.put("tododate", todos.getDate());
        values.put("todotime", todos.getTime());
        values.put("remindTime", todos.getRemindTime());
        values.put("isRepeat", todos.getIsRepeat());
        values.put("imgId", todos.getImgId());
        long id = db.insert("Todo", null, values);
        close();
    }

    /**
     * 获取所有task
     *
     * @return
     */
    List<Todos> getAllTask() {
        open();
        List<Todos> todosList = new ArrayList<Todos>();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo", null);
        while (cursor.moveToNext()) {
            Todos data = new Todos();
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
            data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
            data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
            data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
            data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
            data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
            data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            todosList.add(data);
        }
        // make sure to close the cursor

        cursor.close();
        close();
        Log.i("ToDoDao", "查询到本地的任务个数：" + todosList.size());
        return todosList;
    }

    /**
     * 获取单个待办事项
     *
     * @param id
     * @return
     */
    public Todos getTask(int id) {
        open();
        Todos data = new Todos();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo WHERE id =" + id, null);
        if (cursor.moveToNext()) {
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
            data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
            data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
            data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
            data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
            data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
            data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
        }
        cursor.close();
        close();
        return data;
    }
}
