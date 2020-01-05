package com.android.mycourse.experiment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.mycourse.R;
import com.android.mycourse.course.CourseHelper;

import java.util.ArrayList;

public class ExperimentEditActivity extends AppCompatActivity {

    public static final String ARG_MODE = "mode";   // 参数模式
    public static final int VAL_MODE_NEW = 0;       // 参数模式值添加
    public static final int VAL_MODE_EDIT = 1;      // 参数模式值修改

    private int mMode;          // 模式
    private int mIndex;
    private ExperimentBean mExperiment;
    private CourseHelper mCourseHelper;
    private ExperimentHelper mExperimentHelper;
    private ArrayList<String> mSemesters;
    private ArrayList<String> mCourses;
    private ArrayAdapter<String> mSemesterAdapter;
    private ArrayAdapter<String> mCourseAdapter;
    // View
    private Spinner spnSemester;
    private Spinner spnCourse;
    private EditText etName;
    private EditText etTime;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_edit);
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
                mExperiment.setSemester(mSemesters.get(spnSemester.getSelectedItemPosition()));
            }
            if (mCourses.size() > 0) {
                mExperiment.setCourse(mCourses.get(spnCourse.getSelectedItemPosition()));
            }
            mExperiment.setName(etName.getText().toString());
            mExperiment.setTime(etTime.getText().toString());
            mExperiment.setContent(etContent.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(ExperimentConstants.EXTRA_INDEX, mIndex);
            intent.putExtra(ExperimentConstants.EXTRA_EXPERIMENT, mExperiment);
            if (mMode == VAL_MODE_NEW) {
                mExperimentHelper.insertExperiment(mExperiment);
                setResult(ExperimentConstants.RESULT_INSERT, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.experiment_edit_ok_new), mExperiment.getName()),
                        Toast.LENGTH_SHORT).show();
            } else if (mMode == VAL_MODE_EDIT) {
                mExperimentHelper.updateExperiment(mExperiment);
                setResult(ExperimentConstants.RESULT_UPDATE, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.experiment_edit_ok_modify), mExperiment.getName()),
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
        new AlertDialog.Builder(ExperimentEditActivity.this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(mMode == VAL_MODE_NEW ? R.string.experiment_edit_cancel_new :
                        R.string.experiment_edit_cancel_modify))
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
                intent.getIntExtra(ExperimentConstants.EXTRA_INDEX, -1) : -1;
        mExperiment = intent != null ?
                (ExperimentBean) intent.getParcelableExtra(ExperimentConstants.EXTRA_EXPERIMENT) : null;
        if (mExperiment == null) {
            mExperiment = new ExperimentBean();
        }

        mCourseHelper = new CourseHelper(this);
        mExperimentHelper = new ExperimentHelper(this);
        mSemesters = mCourseHelper.getAllSemesters();   // 获取学期数据

    }

    /**
     * 初始化控件
     */
    private void initView() {
        Toolbar toolbar = findViewById(R.id.tb_edit);
        spnSemester = findViewById(R.id.spn_experiment_semester);
        spnCourse = findViewById(R.id.spn_experiment_course);
        etName = findViewById(R.id.et_experiment_name);
        etTime = findViewById(R.id.et_experiment_time);
        etContent = findViewById(R.id.et_experiment_content);
        // 工具栏
        toolbar.setTitle(getString(mMode == VAL_MODE_NEW ? R.string.experiment_edit_title_new :
                R.string.experiment_edit_title_modify));
        setSupportActionBar(toolbar);
        // 学期选项
        mSemesterAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, mSemesters);
        mSemesterAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnSemester.setAdapter(mSemesterAdapter);
        // 课程名称选项
        mCourseAdapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        mCourseAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnCourse.setAdapter(mCourseAdapter);
        // 控件数据
        if (mMode == VAL_MODE_EDIT) {
            int index = mSemesters.indexOf(mExperiment.getSemester());
            spnSemester.setSelection(index != -1 ? index : 0);  // 选中该实验的学期
            etName.setText(mExperiment.getName());
            etTime.setText(mExperiment.getTime());
            etContent.setText(mExperiment.getContent());
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
                mCourses = mCourseHelper.searchNamesBySemester(mSemesters.get(i));
                mCourseAdapter.clear();
                mCourseAdapter.addAll(mCourses);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
