package com.android.mycourse.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mycourse.R;

import java.util.Date;

public class NoteEditActivity extends AppCompatActivity {

    public static final String ARG_MODE = "mode";   // 启动模式
    public static final int VAL_MODE_NEW = 0;       // 新建备忘
    public static final int VAL_MODE_EDIT = 1;      // 编辑备忘

    private TextView tvTime;
    private EditText etTitle;
    private EditText etContent;

    private int mMode;
    private int mIndex;
    private NoteBean mNote;
    private NoteHelper mNoteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_edit);
        initView();     // 初始化控件
    }

    // 初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notepad_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 菜单项选择事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 取消新建或编辑备忘
            case android.R.id.home:
                onSupportNavigateUp();  // 调用导航键事件
                return true;
            // 完成新建或编辑备忘
            case R.id.action_note_edit_ok:
                mNote.setTime(tvTime.getText().toString());
                mNote.setTitle(etTitle.getText().toString());
                mNote.setContent(etContent.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(NotepadConstants.EXTRA_INDEX, mIndex);
                intent.putExtra(NotepadConstants.EXTRA_NOTE, mNote);
                if (mMode == VAL_MODE_NEW) {
                    mNoteHelper.insertNote(mNote);
                    setResult(NotepadConstants.RESULT_INSERT, intent);  // 添加备忘
                    Toast.makeText(this, String.format(
                            getString(R.string.notepad_edit_ok_new), mNote.getTitle()),
                            Toast.LENGTH_SHORT).show();
                } else if (mMode == VAL_MODE_EDIT) {
                    mNoteHelper.updateNote(mNote);
                    setResult(NotepadConstants.RESULT_UPDATE, intent);  // 更新备忘
                    Toast.makeText(this, String.format(
                            getString(R.string.notepad_edit_ok_edit), mNote.getTitle()),
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
                        ? getString(R.string.notepad_edit_cancel_new)
                        : getString(R.string.notepad_edit_cancel_edit))
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
                intent.getIntExtra(NotepadConstants.EXTRA_INDEX, -1) : -1;
        mNote = intent != null ?
                (NoteBean) intent.getParcelableExtra(NotepadConstants.EXTRA_NOTE) : null;
        if (mNote == null) {
            mNote = new NoteBean();
        }
        mNoteHelper = new NoteHelper(this);

        tvTime = findViewById(R.id.tv_note_edit_time);
        etTitle = findViewById(R.id.et_note_edit_title);
        etContent = findViewById(R.id.et_note_edit_content);

        if (mMode == VAL_MODE_NEW) {
            tvTime.setText(String.format("%1$tF %1$tR", new Date()));
        }
        if (mMode == VAL_MODE_EDIT) {
            tvTime.setText(mNote.getTime());
            etTitle.setText(mNote.getTitle());
            etContent.setText(mNote.getContent());
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mMode == VAL_MODE_NEW ? getString(R.string.notepad_edit_title_new) :
                    getString(R.string.notepad_edit_title_edit));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().
                    getDrawable(R.drawable.ic_close_white_24dp));    // 设置导航键图标
        }

    }
}
