package com.android.mycourse.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mycourse.R;

public class AddressEditActivity extends AppCompatActivity {

    public static final String ARG_MODE = "mode";   // 启动模式
    public static final int VAL_MODE_NEW = 0;       // 新建网址
    public static final int VAL_MODE_EDIT = 1;      // 编辑网址

    private int mMode;
    private int mIndex;
    private AddressBean mAddress;
    private AddressHelper mAddressHelper;

    private EditText etTitle;
    private EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        initView();     // 初始化控件
    }

    // 初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 菜单项选择事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 取消新建或编辑网址
            case android.R.id.home:
                onSupportNavigateUp();  // 调用导航键事件
                return true;
            // 完成新建或编辑网址
            case R.id.action_address_edit_ok:
                mAddress.setTitle(etTitle.getText().toString());
                mAddress.setAddress(etAddress.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(AddressConstants.EXTRA_INDEX, mIndex);
                intent.putExtra(AddressConstants.EXTRA_ADDRESS, mAddress);
                if (mMode == VAL_MODE_NEW) {
                    mAddressHelper.insertAddress(mAddress);
                    setResult(AddressConstants.RESULT_INSERT, intent);  // 添加网址
                    Toast.makeText(this, String.format(
                            getString(R.string.address_edit_ok_new), mAddress.getTitle()),
                            Toast.LENGTH_SHORT).show();
                } else if (mMode == VAL_MODE_EDIT) {
                    mAddressHelper.updateAddress(mAddress);
                    setResult(AddressConstants.RESULT_UPDATE, intent);  // 更新网址
                    Toast.makeText(this, String.format(
                            getString(R.string.address_edit_ok_edit), mAddress.getTitle()),
                            Toast.LENGTH_SHORT).show();
                }
                finish();
        } // switch end
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected end

    // 回退键事件
    @Override
    public void onBackPressed() {
        onSupportNavigateUp();  // 调用导航键事件
    }

    // 导航键事件
    @Override
    public boolean onSupportNavigateUp() {
        // 询问是否退出
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(mMode == VAL_MODE_NEW
                        ? getString(R.string.address_edit_cancel_new)
                        : getString(R.string.address_edit_cancel_edit))
                .setPositiveButton(getString(R.string.btn_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.btn_no), null)
                .show();
        return super.onSupportNavigateUp();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        Intent intent = getIntent();
        mMode = intent != null ?
                intent.getIntExtra(ARG_MODE, VAL_MODE_NEW) : VAL_MODE_NEW;
        mIndex = intent != null ?
                intent.getIntExtra(AddressConstants.EXTRA_INDEX, -1) : -1;
        mAddress = intent != null ?
                (AddressBean) intent.getParcelableExtra(AddressConstants.EXTRA_ADDRESS) : null;
        if (mAddress == null) {
            mAddress = new AddressBean();
        }
        mAddressHelper = new AddressHelper(this);

        etTitle = findViewById(R.id.et_address_edit_title);
        etAddress = findViewById(R.id.et_address_edit_address);

        if (mMode == VAL_MODE_EDIT) {
            etTitle.setText(mAddress.getTitle());
            etAddress.setText(mAddress.getAddress());
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mMode == VAL_MODE_NEW ? getString(R.string.address_edit_title_new) :
                    getString(R.string.address_edit_title_edit));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().
                    getDrawable(R.drawable.ic_close_white_24dp));    // 设置导航键图标
        }

    }

}
