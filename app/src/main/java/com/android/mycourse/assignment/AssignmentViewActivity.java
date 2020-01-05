package com.android.mycourse.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mycourse.R;

public class AssignmentViewActivity extends AppCompatActivity {

    private int mIndex;
    private AssignmentBean mAssignment;

    private TextView tvSemester;
    private TextView tvCourse;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);
        initView();     // 初始化控件
        updateView();   // 控件装入数据
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assignment_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 返回主界面
            case android.R.id.home:
                finish();
                break;
            // 编辑作业
            case R.id.action_assignment_view_edit:
                Intent intent = new Intent(this, AssignmentEditActivity.class);
                intent.putExtra(AssignmentEditActivity.ARG_MODE, AssignmentEditActivity.VAL_MODE_EDIT);
                intent.putExtra(AssignmentConstants.EXTRA_ASSIGNMENT, mAssignment);
                startActivityForResult(intent, AssignmentConstants.REQUEST_EDIT);  // 进入编辑界面
                break;
            // 删除作业
            case R.id.action_assignment_view_delete:
                // 询问是否删除作业
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(getString(R.string.assignment_delete_ask),
                                mAssignment.getTitle()))
                        .setPositiveButton(getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AssignmentHelper experimentHelper =
                                                new AssignmentHelper(AssignmentViewActivity.this);
                                        experimentHelper.deleteAssignment(mAssignment);
                                        Intent intent = new Intent();
                                        intent.putExtra(AssignmentConstants.EXTRA_INDEX, mIndex);
                                        setResult(AssignmentConstants.RESULT_DELETE, intent);
                                        Toast.makeText(AssignmentViewActivity.this, String.format(
                                                getString(R.string.assignment_delete_finish),
                                                mAssignment.getTitle()), Toast.LENGTH_SHORT).show();
                                        finish();   // 返回删除主界面的作业
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
        if (requestCode == AssignmentConstants.REQUEST_EDIT) {    // 编辑作业
            if (resultCode == AssignmentConstants.RESULT_UPDATE) {   // 更新作业
                mAssignment = data != null ? (AssignmentBean)
                        data.getParcelableExtra(AssignmentConstants.EXTRA_ASSIGNMENT) : null;
                if (mAssignment != null) {
                    updateView();   // 更新控件数据
                    // 回传数据
                    Intent intent = new Intent();
                    intent.putExtra(AssignmentConstants.EXTRA_INDEX, mIndex);
                    intent.putExtra(AssignmentConstants.EXTRA_ASSIGNMENT, mAssignment);
                    setResult(AssignmentConstants.RESULT_UPDATE, intent);
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
                intent.getIntExtra(AssignmentConstants.EXTRA_INDEX, -1) : -1;
        mAssignment = intent != null ? (AssignmentBean)
                intent.getParcelableExtra(AssignmentConstants.EXTRA_ASSIGNMENT) : null;

        tvSemester = findViewById(R.id.tv_assignment_view_semester);
        tvCourse = findViewById(R.id.tv_assignment_view_course);
        tvTitle = findViewById(R.id.tv_assignment_view_title);
        tvTime = findViewById(R.id.tv_assignment_view_time);
        tvContent = findViewById(R.id.tv_assignment_view_content);
        // 工具栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.assignment_view_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * 更新控件数据
     */
    private void updateView() {
        if (mAssignment != null) {
            tvSemester.setText(mAssignment.getSemester());
            tvCourse.setText(mAssignment.getCourse());
            tvTime.setText(mAssignment.getTime());
            tvTitle.setText(mAssignment.getTitle());
            tvContent.setText(mAssignment.getContent());
        }
    }
}
