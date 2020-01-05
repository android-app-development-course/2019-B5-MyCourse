package com.android.mycourse.address;

public interface AddressConstants {
    
    String EXTRA_INDEX = "index";   // 网址在列表中的索引
    String EXTRA_ADDRESS = AddressBean.class.getCanonicalName();

    /**
     * REQUEST对应Activity中startActivityForResult、onActivityResult方法中的requestCode值
     * RESULT对应Activity中setResult、onActivityResult方法中的resultCode值
     */
    int REQUEST_NEW = 100;      // 新建网址
    int RESULT_INSERT = 110;    // 插入网址

    int REQUEST_EDIT = 300;     // 编辑网址
    int RESULT_UPDATE = 310;    // 更新网址
}
