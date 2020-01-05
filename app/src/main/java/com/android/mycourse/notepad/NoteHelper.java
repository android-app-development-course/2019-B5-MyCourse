package com.android.mycourse.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notepad.db";
    private static final int DATABASE_VERSION = 1;
    // 备忘表
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_NOTE_ID = "id";
    private static final String COLUMN_NOTE_TIME = "time";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_CONTENT = "content";

    NoteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建备忘表
        String sqlCreate = "CREATE TABLE " + TABLE_NOTE + "(" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOTE_TITLE + " TEXT," +
                COLUMN_NOTE_TIME + " TEXT," +
                COLUMN_NOTE_CONTENT + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * 读取备忘
     *
     * @return 所有备忘数据
     */
    ArrayList<NoteBean> getAllNotes() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE, null, null, null,
                null, null, null);
        ArrayList<NoteBean> notes = new ArrayList<>();
        NoteBean note;
        while (cursor.moveToNext()) {
            note = new NoteBean();
            note.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_ID)));
            note.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TIME)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
            note.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)));
            notes.add(0, note);
        }
        cursor.close();
        db.close();
        return notes;
    }

    /**
     * 添加备忘
     *
     * @param note 待添加的备忘
     */
    void insertNote(NoteBean note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TIME, note.getTime());
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        long id = db.insert(TABLE_NOTE, null, values);  // 插入数据后获取Id
        note.setId(id);     // 设置Id
        db.close();
    }

    /**
     * 保存备忘
     *
     * @param note 待保存的备忘
     */
    void updateNote(NoteBean note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TIME, note.getTime());
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        db.update(TABLE_NOTE, values,
                COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     * 删除备忘
     *
     * @param note 待删除的备忘
     */
    void deleteNote(NoteBean note) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTE,
                COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     * 删除全部备忘
     */
    void deleteAllNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTE, null, null);
        db.close();
    }
}
