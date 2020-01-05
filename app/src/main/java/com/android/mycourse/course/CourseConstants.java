package com.android.mycourse.course;

public interface CourseConstants {

    String EXTRA_INDEX = "index";
    String EXTRA_COURSE = CourseBean.class.getCanonicalName();
    String EXTRA_SEMESTER = "semester";

    int REQUEST_NEW = 100;
    int RESULT_INSERT = 110;

    int REQUEST_VIEW = 200;
    int RESULT_DELETE = 210;

    int REQUEST_EDIT = 300;
    int RESULT_UPDATE = 310;
}
