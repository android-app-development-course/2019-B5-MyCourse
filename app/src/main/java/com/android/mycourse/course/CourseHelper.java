package com.android.mycourse.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CourseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course.db";
    private static final int DATABASE_VERSION = 1;
    // 课程信息表
    private static final String TABLE_COURSE = "course";
    private static final String COLUMN_COURSE_ID = "id";
    private static final String COLUMN_COURSE_NAME = "name";
    private static final String COLUMN_COURSE_TYPE = "type";
    private static final String COLUMN_COURSE_SEMESTER = "semester";
    private static final String COLUMN_COURSE_CLASS_WEEK = "class_week";
    private static final String COLUMN_COURSE_CLASS_TIME = "class_time";
    private static final String COLUMN_COURSE_CLASS_PLACE = "class_place";
    private static final String COLUMN_COURSE_CLASS_TEACHER = "class_teacher";
    private static final String COLUMN_COURSE_COURSE_REMARKS = "course_remarks";
    private static final String COLUMN_COURSE_EXPERIMENT_WEEK = "experiment_week";
    private static final String COLUMN_COURSE_EXPERIMENT_TIME = "experiment_time";
    private static final String COLUMN_COURSE_EXPERIMENT_PLACE = "experiment_place";
    private static final String COLUMN_COURSE_EXPERIMENT_TEACHER = "experiment_teacher";
    private static final String COLUMN_COURSE_EXPERIMENT_REMARKS = "experiment_remarks";
    // 课程类型表
    private static final String TABLE_TYPE = "type";
    private static final String COLUMN_TYPE_NAME = "name";
    // 学期表
    private static final String TABLE_SEMESTER = "semester";
    private static final String COLUMN_SEMESTER_NAME = "name";

    public CourseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建课程信息表
        String sqlCourse = "CREATE TABLE " + TABLE_COURSE + "(" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COURSE_NAME + " TEXT," +
                COLUMN_COURSE_TYPE + " TEXT," +
                COLUMN_COURSE_SEMESTER + " TEXT," +
                COLUMN_COURSE_CLASS_WEEK + " TEXT," +
                COLUMN_COURSE_CLASS_TIME + " TEXT," +
                COLUMN_COURSE_CLASS_PLACE + " TEXT," +
                COLUMN_COURSE_CLASS_TEACHER + " TEXT," +
                COLUMN_COURSE_COURSE_REMARKS + " TEXT," +
                COLUMN_COURSE_EXPERIMENT_WEEK + " TEXT," +
                COLUMN_COURSE_EXPERIMENT_TIME + " TEXT," +
                COLUMN_COURSE_EXPERIMENT_PLACE + " TEXT," +
                COLUMN_COURSE_EXPERIMENT_TEACHER + " TEXT," +
                COLUMN_COURSE_EXPERIMENT_REMARKS + " TEXT" +
                ")";
        db.execSQL(sqlCourse);
        // 创建课程类型表
        String sqlType = "CREATE TABLE " + TABLE_TYPE + "(" +
                COLUMN_TYPE_NAME + " TEXT PRIMARY KEY" +
                ")";
        db.execSQL(sqlType);
        // 创建学期表
        String sqlSemester = "CREATE TABLE " + TABLE_SEMESTER + "(" +
                COLUMN_SEMESTER_NAME + " TEXT PRIMARY KEY" +
                ")";
        db.execSQL(sqlSemester);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 读取所有课程数据
     *
     * @return 所有课程数据
     */
    public ArrayList<CourseBean> getAllCourses() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSE, null, null, null,
                null, null, null);
        ArrayList<CourseBean> courses = new ArrayList<>();
        CourseBean course;
        while (cursor.moveToNext()) {
            course = new CourseBean();
            course.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_COURSE_ID)));
            course.setName(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME)));
            course.setType(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_TYPE)));
            course.setSemester(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_SEMESTER)));
            course.setClassWeek(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CLASS_WEEK)));
            course.setClassTime(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CLASS_TIME)));
            course.setClassPlace(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CLASS_PLACE)));
            course.setClassTeacher(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CLASS_TEACHER)));
            course.setCourseRemarks(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_COURSE_REMARKS)));
            course.setExperimentWeek(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_EXPERIMENT_WEEK)));
            course.setExperimentTime(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_EXPERIMENT_TIME)));
            course.setExperimentPlace(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_EXPERIMENT_PLACE)));
            course.setExperimentTeacher(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_EXPERIMENT_TEACHER)));
            course.setCourseRemarks(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_EXPERIMENT_REMARKS)));
            courses.add(course);
        }
        cursor.close();
        db.close();
        return courses;
    }

    /**
     * 插入课程数据
     *
     * @param course 待插入的课程信息
     */
    public void insertCourse(CourseBean course) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_TYPE, course.getType());
        values.put(COLUMN_COURSE_SEMESTER, course.getSemester());
        values.put(COLUMN_COURSE_CLASS_WEEK, course.getClassWeek());
        values.put(COLUMN_COURSE_CLASS_TIME, course.getClassTime());
        values.put(COLUMN_COURSE_CLASS_PLACE, course.getClassPlace());
        values.put(COLUMN_COURSE_CLASS_TEACHER, course.getClassTeacher());
        values.put(COLUMN_COURSE_COURSE_REMARKS, course.getCourseRemarks());
        values.put(COLUMN_COURSE_EXPERIMENT_WEEK, course.getExperimentWeek());
        values.put(COLUMN_COURSE_EXPERIMENT_TIME, course.getExperimentTime());
        values.put(COLUMN_COURSE_EXPERIMENT_PLACE, course.getExperimentPlace());
        values.put(COLUMN_COURSE_EXPERIMENT_TEACHER, course.getExperimentTeacher());
        values.put(COLUMN_COURSE_EXPERIMENT_REMARKS, course.getExperimentRemarks());
        long id = db.insert(TABLE_COURSE, null, values);
        course.setId(id);
        db.close();
    }

    /**
     * 修改课程信息
     *
     * @param course 待修改的课程信息
     */
    public void updateCourse(CourseBean course) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_TYPE, course.getType());
        values.put(COLUMN_COURSE_SEMESTER, course.getSemester());
        values.put(COLUMN_COURSE_CLASS_WEEK, course.getClassWeek());
        values.put(COLUMN_COURSE_CLASS_TIME, course.getClassTime());
        values.put(COLUMN_COURSE_CLASS_PLACE, course.getClassPlace());
        values.put(COLUMN_COURSE_CLASS_TEACHER, course.getClassTeacher());
        values.put(COLUMN_COURSE_COURSE_REMARKS, course.getCourseRemarks());
        values.put(COLUMN_COURSE_EXPERIMENT_WEEK, course.getExperimentWeek());
        values.put(COLUMN_COURSE_EXPERIMENT_TIME, course.getExperimentTime());
        values.put(COLUMN_COURSE_EXPERIMENT_PLACE, course.getExperimentPlace());
        values.put(COLUMN_COURSE_EXPERIMENT_TEACHER, course.getExperimentTeacher());
        values.put(COLUMN_COURSE_EXPERIMENT_REMARKS, course.getExperimentRemarks());
        db.update(TABLE_COURSE, values,
                COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(course.getId())});
        db.close();
    }

    /**
     * 删除课程信息
     *
     * @param course 待删除的课程信息
     */
    public void deleteCourse(CourseBean course) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_COURSE,
                COLUMN_COURSE_ID + "=?", new String[]{String.valueOf(course.getId())});
        db.close();
    }

    /**
     * 按学期查询课程名称
     *
     * @param semester 学期
     * @return 课程名称
     */
    public ArrayList<String> searchNamesBySemester(String semester) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSE, new String[]{COLUMN_COURSE_NAME},
                COLUMN_COURSE_SEMESTER + "=?", new String[]{semester}, null,
                null, null, null);
        ArrayList<String> courseNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            courseNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME)));
        }
        cursor.close();
        db.close();
        return courseNames;
    }

    /**
     * 读取所有课程类型
     *
     * @return 所有课程类型
     */
    public ArrayList<String> getAllTypes() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TYPE, null, null, null,
                null, null, null);
        ArrayList<String> types = new ArrayList<>();
        while (cursor.moveToNext()) {
            types.add(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)));
        }
        cursor.close();
        db.close();
        return types;
    }

    /**
     * 添加课程类型数据
     *
     * @param type 待添加的课程类型
     */
    public void insertType(String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_NAME, type);
        db.insert(TABLE_TYPE, null, values);
        db.close();
    }

    /**
     * 添加多个课程类型数据
     *
     * @param types 待添加的多个课程类型
     */
    public void insertTypes(ArrayList<String> types) {
        SQLiteDatabase db = getWritableDatabase();
        for (String type : types) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TYPE_NAME, type);
            db.insert(TABLE_TYPE, null, values);
        }
        db.close();
    }

    /**
     * 删除所有课程类型
     */
    public void deleteAllTypes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TYPE, null, null);
        db.close();
    }

    /**
     * 读取所有学期数据
     *
     * @return 所有学期数据
     */
    public ArrayList<String> getAllSemesters() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SEMESTER, null, null, null,
                null, null, null);
        ArrayList<String> semesters = new ArrayList<>();
        while (cursor.moveToNext()) {
            semesters.add(cursor.getString(cursor.getColumnIndex(COLUMN_SEMESTER_NAME)));
        }
        cursor.close();
        db.close();
        return semesters;
    }

    /**
     * 添加多个学期数据
     *
     * @param semesters 待添加的多个学期
     */
    public void insertSemesters(ArrayList<String> semesters) {
        SQLiteDatabase db = getWritableDatabase();
        for (String semester : semesters) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SEMESTER_NAME, semester);
            db.insert(TABLE_SEMESTER, null, values);
        }
        db.close();
    }

    /**
     * 删除学期
     *
     * @param semester 待删除的学期
     */
    public void deleteSemester(String semester) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SEMESTER,
                COLUMN_SEMESTER_NAME + "=?", new String[]{semester});
        db.close();
    }

    /**
     * 删除所有学期
     */
    public void deleteAllSemesters() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SEMESTER, null, null);
        db.close();
    }
}
