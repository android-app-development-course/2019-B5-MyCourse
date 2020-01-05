package com.android.mycourse.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.mycourse.R;
import com.android.mycourse.course.CourseHelper;

import java.util.ArrayList;

public class AssignmentEditActivity extends AppCompatActivity {

    public static final String ARG_MODE = "mode";   // 参数模式
    public static final int VAL_MODE_NEW = 0;       // 参数模式值添加
    public static final int VAL_MODE_EDIT = 1;      // 参数模式值修改

    private int mMode;          // 模式
    private int mIndex;
    private AssignmentBean mAssignment;

    private CourseHelper courseHelper;
    private AssignmentHelper assignmentHelper;
    private ArrayList<String> mSemesters;
    private ArrayList<String> mCourses;
    private ArrayAdapter<String> semesterAdapter;
    private ArrayAdapter<String> courseAdapter;
    // View
    private Spinner spnSemester;
    private Spinner spnCourse;
    private EditText etTitle;
    private EditText etTime;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_edit);
        initData();     // 初始化数据
        initView();     // 初始化控件
        initEvent();    // 初始化事件
    }

    // 创建工具栏菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_edit, menu);
        return true;
    }

    // 工具栏菜单选择事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit_ok) {     // 完成添加或修改
            if (mSemesters.size() > 0) {
                mAssignment.setSemester(mSemesters.get(spnSemester.getSelectedItemPosition()));
            }
            if (mCourses.size() > 0) {
                mAssignment.setCourse(mCourses.get(spnCourse.getSelectedItemPosition()));
            }
            mAssignment.setTitle(etTitle.getText().toString());
            mAssignment.setTime(etTime.getText().toString());
            mAssignment.setContent(etContent.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(AssignmentConstants.EXTRA_INDEX, mIndex);
            intent.putExtra(AssignmentConstants.EXTRA_ASSIGNMENT, mAssignment);
            if (mMode == VAL_MODE_NEW) {
                assignmentHelper.insertAssignment(mAssignment);
                setResult(AssignmentConstants.RESULT_INSERT, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.assignment_edit_ok_new), mAssignment.getTitle()),
                        Toast.LENGTH_SHORT).show();
            } else if (mMode == VAL_MODE_EDIT) {
                assignmentHelper.updateExperiment(mAssignment);
                setResult(AssignmentConstants.RESULT_UPDATE, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.assignment_edit_ok_modify), mAssignment.getTitle()),
                        Toast.LENGTH_SHORT).show();
            }
            finish();   // 结束
        }
        return super.onOptionsItemSelected(item);
    }

    // 回退键事件
    @Override
    public void onBackPressed() {
        onSupportNavigateUp();  // 调用导航键事件
    }

    // 导航键事件
    @Override
    public boolean onSupportNavigateUp() {
        // 询问是否取消添加或修改
        new AlertDialog.Builder(AssignmentEditActivity.this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(mMode == VAL_MODE_NEW ? R.string.assignment_edit_cancel_new :
                        R.string.assignment_edit_cancel_modify))
                .setPositiveButton(getString(R.string.btn_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();   // 返回
                            }
                        })
                .setNegativeButton(getString(R.string.btn_no), null)
                .show();
        return super.onSupportNavigateUp();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        mMode = intent != null ?
                intent.getIntExtra(ARG_MODE, VAL_MODE_NEW) : VAL_MODE_NEW;
        mIndex = intent != null ?
                intent.getIntExtra(AssignmentConstants.EXTRA_INDEX, -1) : -1;
        mAssignment = intent != null ?
                (AssignmentBean) intent.getParcelableExtra(AssignmentConstants.EXTRA_ASSIGNMENT) : null;
        if (mAssignment == null) {
            mAssignment = new AssignmentBean();
        }

        courseHelper = new CourseHelper(this);
        assignmentHelper = new AssignmentHelper(this);
        mSemesters = courseHelper.getAllSemesters();   // 获取学期数据

    }

    /**
     * 初始化控件
     */
    private void initView() {
        Toolbar toolbar = findViewById(R.id.tb_edit);
        spnSemester = findViewById(R.id.spn_assignment_semester);
        spnCourse = findViewById(R.id.spn_assignment_course);
        etTitle = findViewById(R.id.et_assignment_title);
        etTime = findViewById(R.id.et_assignment_time);
        etContent = findViewById(R.id.et_assignment_content);
        // 工具栏
        toolbar.setTitle(getString(mMode == VAL_MODE_NEW ? R.string.assignment_edit_title_new :
                R.string.assignment_edit_title_modify));
        setSupportActionBar(toolbar);
        // 学期选项
        semesterAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, mSemesters);
        semesterAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnSemester.setAdapter(semesterAdapter);
        // 课程名称选项
        courseAdapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        courseAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnCourse.setAdapter(courseAdapter);
        // 控件数据
        if (mMode == VAL_MODE_EDIT) {
            int index = mSemesters.indexOf(mAssignment.getSemester());
            spnSemester.setSelection(index != -1 ? index : 0);  // 选中该作业的学期
            etTitle.setText(mAssignment.getTitle());
            etTime.setText(mAssignment.getTime());
            etContent.setText(mAssignment.getContent());
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 选择学期后重装课程名称选项
        spnSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 显示所选学期的课程名称
                mCourses = courseHelper.searchNamesBySemester(mSemesters.get(i));
                courseAdapter.clear();
                courseAdapter.addAll(mCourses);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
