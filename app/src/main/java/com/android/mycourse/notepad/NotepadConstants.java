package com.android.mycourse.notepad;

public interface NotepadConstants {

    String EXTRA_INDEX = "index";   // 备忘在列表中的索引
    String EXTRA_NOTE = NoteBean.class.getCanonicalName();

    /**
     * REQUEST对应Activity中startActivityForResult、onActivityResult方法中的requestCode值
     * RESULT对应Activity中setResult、onActivityResult方法中的resultCode值
     */
    int REQUEST_NEW = 100;      // 新建备忘
    int RESULT_INSERT = 110;    // 插入备忘数据

    int REQUEST_VIEW = 200;     // 查看备忘
    int RESULT_DELETE = 210;    // 删除备忘数据

    int REQUEST_EDIT = 300;     // 编辑备忘
    int RESULT_UPDATE = 310;    // 更新备忘数据

}
