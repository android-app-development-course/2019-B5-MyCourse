package com.android.mycourse.address;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AddressHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "address.db";
    private static final int DATABASE_VERSION = 1;
    // 网址表
    private static final String TABLE_ADDRESS = "address";
    private static final String COLUMN_ADDRESS_ID = "id";
    private static final String COLUMN_ADDRESS_TITLE = "title";
    private static final String COLUMN_ADDRESS_ADDRESS = "address";

    public AddressHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建网址表
        String sqlCreate = "CREATE TABLE " + TABLE_ADDRESS + "(" +
                COLUMN_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ADDRESS_TITLE + " TEXT," +
                COLUMN_ADDRESS_ADDRESS + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 读取所有网址数据
     *
     * @return 所有网址数据
     */
    ArrayList<AddressBean> getAllAddresses() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESS, null, null, null,
                null, null, null);
        ArrayList<AddressBean> addresses = new ArrayList<>();
        AddressBean address;
        while (cursor.moveToNext()) {
            address = new AddressBean();
            address.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ADDRESS_ID)));
            address.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TITLE)));
            address.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_ADDRESS)));
            addresses.add(0, address);  // 倒插
        }
        cursor.close();
        db.close();
        return addresses;
    }

    /**
     * 添加网址
     *
     * @param address 待添加的网址
     */
    void insertAddress(AddressBean address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS_TITLE, address.getTitle());
        values.put(COLUMN_ADDRESS_ADDRESS, address.getAddress());
        long id = db.insert(TABLE_ADDRESS, null, values);  // 插入数据后获取Id
        address.setId(id);     // 设置Id
        db.close();
    }

    /**
     * 保存网址
     *
     * @param address 待保存的网址
     */
    void updateAddress(AddressBean address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS_TITLE, address.getTitle());
        values.put(COLUMN_ADDRESS_ADDRESS, address.getAddress());
        db.update(TABLE_ADDRESS, values,
                COLUMN_ADDRESS_ID + "=?", new String[]{String.valueOf(address.getId())});
        db.close();
    }

    /**
     * 删除网址
     *
     * @param address 待删除的网址
     */
    void deleteAddress(AddressBean address) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ADDRESS,
                COLUMN_ADDRESS_ID + "=?", new String[]{String.valueOf(address.getId())});
        db.close();
    }


    /**
     * 删除全部网址
     */
    void deleteAllAddresses() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ADDRESS, null, null);
        db.close();
    }
}
