package com.android.mycourse.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.mycourse.experiment.ExperimentBean;

import java.util.ArrayList;

public class AssignmentHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TITLE = "assignment.db";
    private static final int DATABASE_VERSION = 1;
    // 作业信息表
    private static final String TABLE_ASSIGNMENT = "assignment";
    private static final String COLUMN_ASSIGNMENT_ID = "id";
    private static final String COLUMN_ASSIGNMENT_SEMESTER = "semester";
    private static final String COLUMN_ASSIGNMENT_COURSE = "course";
    private static final String COLUMN_ASSIGNMENT_TITLE = "title";
    private static final String COLUMN_ASSIGNMENT_TIME = "time";
    private static final String COLUMN_ASSIGNMENT_CONTENT = "content";

    AssignmentHelper(@Nullable Context context) {
        super(context, DATABASE_TITLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建作业信息表
        String sqlExperiment = "CREATE TABLE " + TABLE_ASSIGNMENT + "(" +
                COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ASSIGNMENT_SEMESTER + " TEXT," +
                COLUMN_ASSIGNMENT_COURSE + " TEXT," +
                COLUMN_ASSIGNMENT_TITLE + " TEXT," +
                COLUMN_ASSIGNMENT_TIME + " TEXT," +
                COLUMN_ASSIGNMENT_CONTENT + " TEXT" +
                ")";
        db.execSQL(sqlExperiment);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 读取所有作业数据
     *
     * @return 所有作业数据
     */
    public ArrayList<AssignmentBean> getAllAssignments() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ASSIGNMENT, null, null, null,
                null, null, null);
        ArrayList<AssignmentBean> assignments = new ArrayList<>();
        AssignmentBean assignment;
        while (cursor.moveToNext()) {
            assignment = new AssignmentBean();
            assignment.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ASSIGNMENT_ID)));
            assignment.setSemester(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_SEMESTER)));
            assignment.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_COURSE)));
            assignment.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_TITLE)));
            assignment.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_TIME)));
            assignment.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_CONTENT)));
            assignments.add(0, assignment);
        }
        cursor.close();
        db.close();
        return assignments;
    }

    /**
     * 插入作业数据
     *
     * @param assignment 待插入的作业信息
     */
    public void insertAssignment(AssignmentBean assignment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASSIGNMENT_SEMESTER, assignment.getSemester());
        values.put(COLUMN_ASSIGNMENT_COURSE, assignment.getCourse());
        values.put(COLUMN_ASSIGNMENT_TITLE, assignment.getTitle());
        values.put(COLUMN_ASSIGNMENT_TIME, assignment.getTime());
        values.put(COLUMN_ASSIGNMENT_CONTENT, assignment.getContent());
        long id = db.insert(TABLE_ASSIGNMENT, null, values);
        assignment.setId(id);
        db.close();
    }

    /**
     * 修改作业信息
     *
     * @param assignment 待修改的作业信息
     */
    public void updateExperiment(AssignmentBean assignment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASSIGNMENT_SEMESTER, assignment.getSemester());
        values.put(COLUMN_ASSIGNMENT_COURSE, assignment.getCourse());
        values.put(COLUMN_ASSIGNMENT_TITLE, assignment.getTitle());
        values.put(COLUMN_ASSIGNMENT_TIME, assignment.getTime());
        values.put(COLUMN_ASSIGNMENT_CONTENT, assignment.getContent());
        db.update(TABLE_ASSIGNMENT, values,
                COLUMN_ASSIGNMENT_ID + "=?", new String[]{String.valueOf(assignment.getId())});
        db.close();
    }

    /**
     * 删除作业信息
     *
     * @param assignment 待删除的作业信息
     */
    public void deleteAssignment(AssignmentBean assignment) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ASSIGNMENT,
                COLUMN_ASSIGNMENT_ID + "=?", new String[]{String.valueOf(assignment.getId())});
        db.close();
    }
}
