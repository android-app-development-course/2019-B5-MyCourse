package com.android.mycourse.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.mycourse.R;

public class NoteViewActivity extends AppCompatActivity {

    private TextView tvTime;
    private TextView tvTitle;
    private TextView tvContent;
    private int mIndex;
    private NoteBean mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_view);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notepad_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 返回主界面
            case android.R.id.home:
                finish();
                break;
            // 编辑备忘
            case R.id.action_notepad_view_edit:
                Intent intent = new Intent(this, NoteEditActivity.class);
                intent.putExtra(NoteEditActivity.ARG_MODE, NoteEditActivity.VAL_MODE_EDIT);
                intent.putExtra(NotepadConstants.EXTRA_NOTE, mNote);
                startActivityForResult(intent, NotepadConstants.REQUEST_EDIT);  // 进入编辑界面
                break;
            // 删除备忘
            case R.id.action_notepad_view_delete:
                // 询问是否删除备忘
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(getString(R.string.notepad_delete_ask),
                                mNote.getTitle()))
                        .setPositiveButton(getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        NoteHelper noteHelper = new NoteHelper(NoteViewActivity.this);
                                        noteHelper.deleteNote(mNote);
                                        Intent intent = new Intent();
                                        intent.putExtra(NotepadConstants.EXTRA_INDEX, mIndex);
                                        setResult(NotepadConstants.RESULT_DELETE, intent);
                                        Toast.makeText(NoteViewActivity.this, String.format(
                                                getString(R.string.notepad_delete_finish),
                                                mNote.getTitle()), Toast.LENGTH_SHORT).show();
                                        finish();   // 返回删除主界面的备忘
                                    }
                                })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                break;
        } // switch end
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NotepadConstants.REQUEST_EDIT) {    // 编辑备忘
            if (resultCode == NotepadConstants.RESULT_UPDATE) {   // 更新备忘
                mNote = data != null ?
                        (NoteBean) data.getParcelableExtra(NotepadConstants.EXTRA_NOTE) : null;
                if (mNote != null) {
                    updateView();   // 更新控件数据
                    // 回传数据
                    Intent intent = new Intent();
                    intent.putExtra(NotepadConstants.EXTRA_INDEX, mIndex);
                    intent.putExtra(NotepadConstants.EXTRA_NOTE, mNote);
                    setResult(NotepadConstants.RESULT_UPDATE, intent);
                }
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        Intent intent = getIntent();
        mIndex = intent != null ?
                intent.getIntExtra(NotepadConstants.EXTRA_INDEX, -1) : -1;
        mNote = intent != null ?
                (NoteBean) intent.getParcelableExtra(NotepadConstants.EXTRA_NOTE) : null;

        tvTime = findViewById(R.id.tv_note_view_time);
        tvTitle = findViewById(R.id.tv_note_view_title);
        tvContent = findViewById(R.id.tv_note_view_content);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.notepad_view_title));
            actionBar.setDisplayHomeAsUpEnabled(true);  // 显示导航键
        }
        updateView();

    }

    /**
     * 更新控件数据
     */
    private void updateView() {
        if (mNote != null) {
            tvTime.setText(mNote.getTime());
            tvTitle.setText(mNote.getTitle());
            tvContent.setText(mNote.getContent());
        }
    }
}
