package com.android.mycourse.experiment;

public interface ExperimentConstants {

    String EXTRA_INDEX = "index";   // 实验在列表中的索引
    String EXTRA_EXPERIMENT = ExperimentBean.class.getCanonicalName();

    /**
     * REQUEST对应Activity中startActivityForResult、onActivityResult方法中的requestCode值
     * RESULT对应Activity中setResult、onActivityResult方法中的resultCode值
     */
    int REQUEST_NEW = 100;
    int RESULT_INSERT = 110;

    int REQUEST_VIEW = 200;
    int RESULT_DELETE = 210;

    int REQUEST_EDIT = 300;
    int RESULT_UPDATE = 310;
}
