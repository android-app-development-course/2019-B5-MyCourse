package com.android.mycourse.experiment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExperimentHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "experiment.db";
    private static final int DATABASE_VERSION = 1;
    // 实验信息表
    private static final String TABLE_EXPERIMENT = "experiment";
    private static final String COLUMN_EXPERIMENT_ID = "id";
    private static final String COLUMN_EXPERIMENT_SEMESTER = "semester";
    private static final String COLUMN_EXPERIMENT_COURSE = "course";
    private static final String COLUMN_EXPERIMENT_NAME = "name";
    private static final String COLUMN_EXPERIMENT_TIME = "time";
    private static final String COLUMN_EXPERIMENT_CONTENT = "content";

    public ExperimentHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建实验信息表
        String sqlExperiment = "CREATE TABLE " + TABLE_EXPERIMENT + "(" +
                COLUMN_EXPERIMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EXPERIMENT_SEMESTER + " TEXT," +
                COLUMN_EXPERIMENT_COURSE + " TEXT," +
                COLUMN_EXPERIMENT_NAME + " TEXT," +
                COLUMN_EXPERIMENT_TIME + " TEXT," +
                COLUMN_EXPERIMENT_CONTENT + " TEXT" +
                ")";
        db.execSQL(sqlExperiment);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 读取所有实验数据
     *
     * @return 所有实验数据
     */
    public ArrayList<ExperimentBean> getAllExperiments() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPERIMENT, null, null, null,
                null, null, null);
        ArrayList<ExperimentBean> experiments = new ArrayList<>();
        ExperimentBean experiment;
        while (cursor.moveToNext()) {
            experiment = new ExperimentBean();
            experiment.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_EXPERIMENT_ID)));
            experiment.setSemester(cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIMENT_SEMESTER)));
            experiment.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIMENT_COURSE)));
            experiment.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIMENT_NAME)));
            experiment.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIMENT_TIME)));
            experiment.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_EXPERIMENT_CONTENT)));
            experiments.add(0, experiment);
        }
        cursor.close();
        db.close();
        return experiments;
    }

    /**
     * 插入实验数据
     *
     * @param experiment 待插入的实验信息
     */
    public void insertExperiment(ExperimentBean experiment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPERIMENT_SEMESTER, experiment.getSemester());
        values.put(COLUMN_EXPERIMENT_COURSE, experiment.getCourse());
        values.put(COLUMN_EXPERIMENT_NAME, experiment.getName());
        values.put(COLUMN_EXPERIMENT_TIME, experiment.getTime());
        values.put(COLUMN_EXPERIMENT_CONTENT, experiment.getContent());
        long id = db.insert(TABLE_EXPERIMENT, null, values);
        experiment.setId(id);
        db.close();
    }

    /**
     * 修改实验信息
     *
     * @param experiment 待修改的实验信息
     */
    public void updateExperiment(ExperimentBean experiment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPERIMENT_SEMESTER, experiment.getSemester());
        values.put(COLUMN_EXPERIMENT_COURSE, experiment.getCourse());
        values.put(COLUMN_EXPERIMENT_NAME, experiment.getName());
        values.put(COLUMN_EXPERIMENT_TIME, experiment.getTime());
        values.put(COLUMN_EXPERIMENT_CONTENT, experiment.getContent());
        db.update(TABLE_EXPERIMENT, values,
                COLUMN_EXPERIMENT_ID + "=?", new String[]{String.valueOf(experiment.getId())});
        db.close();
    }

    /**
     * 删除实验信息
     *
     * @param experiment 待删除的实验信息
     */
    public void deleteExperiment(ExperimentBean experiment) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXPERIMENT,
                COLUMN_EXPERIMENT_ID + "=?", new String[]{String.valueOf(experiment.getId())});
        db.close();
    }
}
