package com.android.mycourse.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.mycourse.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class CourseEditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ARG_MODE = "mode";   // 参数模式
    public static final int VAL_MODE_NEW = 0;       // 参数模式值添加
    public static final int VAL_MODE_EDIT = 1;      // 参数模式值修改
    private static final int MAX_WEEK = 20;         // 默认最大周数
    private static final int MAX_CLASS = 11;        // 默认最大节数

    private int mMode;  // 启动模式
    private int mIndex;
    private String mSemester;
    private CourseBean mCourse;
    private CourseHelper mCourseHelper;
    // Course
    private EditText etName;            // 课程名称
    private Spinner spnType;            // 课程类型选择框
    private Spinner spnSemester;        // 学期控件选择框
    private EditText etClassWeek;       // 上课周数输入框
    private EditText etClassTime;       // 上课时间输入框
    private EditText etClassPlace;
    private EditText etClassTeacher;
    private EditText etCourseRemarks;
    // Experiment
    private EditText etExperimentWeek;  // 实验周数输入框
    private EditText etExperimentTime;  // 实验时间输入框
    private EditText etExperimentPlace;
    private EditText etExperimentTeacher;
    private EditText etExperimentRemarks;
    private ImageButton ibExperimentAdd;    // 添加实验按钮
    // Assignment
    private EditText etAssignmentRemarks;
    private ImageButton ibAssignmentAdd;    // 添加作业按钮

    private ArrayAdapter<String> mTypeAdapter;      // 课程类型适配器
    private ArrayAdapter<String> mSemesterAdapter;  // 学期适配器
    private ArrayList<String> mTypes;     // 课程类型选项
    private ArrayList<String> mSemesters; // 学期选项
    private int mMaxWeek;                       // 最大周数
    private int mMaxClass;                      // 最大节数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
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
            // TODO: 检查输入的信息是否和已有的课程信息冲突

            // 检查课程名称是否为空
            if (etName.getText().toString().equals("")) {
                Toast.makeText(this,
                        getString(R.string.course_edit_ok_name), Toast.LENGTH_SHORT).show();
                return true;
            }
            mCourse.setName(etName.getText().toString());
            mCourse.setType(mTypes.get(spnType.getSelectedItemPosition()));
            mCourse.setSemester(mSemesters.get(spnSemester.getSelectedItemPosition()));
            mCourse.setClassWeek(etClassWeek.getText().toString());
            mCourse.setClassTime(etClassTime.getText().toString());
            mCourse.setClassPlace(etClassPlace.getText().toString());
            mCourse.setClassTeacher(etClassTeacher.getText().toString());
            mCourse.setCourseRemarks(etCourseRemarks.getText().toString());
            mCourse.setExperimentWeek(etExperimentWeek.getText().toString());
            mCourse.setExperimentTime(etExperimentTime.getText().toString());
            mCourse.setExperimentPlace(etExperimentPlace.getText().toString());
            mCourse.setExperimentTeacher(etExperimentTeacher.getText().toString());
            mCourse.setExperimentRemarks(etExperimentRemarks.getText().toString());
            mCourse.setAssignmentRemarks(etAssignmentRemarks.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(CourseConstants.EXTRA_INDEX, mIndex);
            intent.putExtra(CourseConstants.EXTRA_COURSE, mCourse);
            if (mMode == VAL_MODE_NEW) {
                mCourseHelper.insertCourse(mCourse);
                setResult(CourseConstants.RESULT_INSERT, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.course_edit_ok_new), mCourse.getName()),
                        Toast.LENGTH_SHORT).show();
            } else if (mMode == VAL_MODE_EDIT) {
                mCourseHelper.updateCourse(mCourse);
                setResult(CourseConstants.RESULT_UPDATE, intent);
                Toast.makeText(this, String.format(
                        getString(R.string.course_edit_ok_modify), mCourse.getName()),
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
        new AlertDialog.Builder(CourseEditActivity.this)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(mMode == VAL_MODE_NEW ?
                        R.string.course_edit_cancel_new : R.string.course_edit_cancel_modify))
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
                intent.getIntExtra(CourseConstants.EXTRA_INDEX, -1) : -1;
        mSemester = intent != null ?
                intent.getStringExtra(CourseConstants.EXTRA_SEMESTER) : "";
        mCourse = intent != null ?
                (CourseBean) intent.getParcelableExtra(CourseConstants.EXTRA_COURSE) : null;
        if (mCourse == null) {
            mCourse = new CourseBean();
        }
        mCourseHelper = new CourseHelper(this);
        // 读取课程类型
        mTypes = mCourseHelper.getAllTypes();
        // 倒数第二项为“-”，表示无类型；最后一项为新建类型
        mTypes.addAll(Arrays.asList("-", getString(R.string.course_information_type_new)));
        // 读取学期
        mSemesters = mCourseHelper.getAllSemesters();
        // 倒数第二项为“-”，表示无类型；最后一项为新建类型
        mSemesters.addAll(Arrays.asList("-", getString(R.string.course_information_semester_new)));
        // TODO: 读取周数
        mMaxWeek = MAX_WEEK;   // 读取失败时设为默认值
        // TODO: 读取节数
        mMaxClass = MAX_CLASS;  // 读取失败时设为默认值

    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 工具栏
        Toolbar toolbar = findViewById(R.id.tb_edit);
        toolbar.setTitle(getString(mMode == VAL_MODE_NEW ? R.string.course_edit_title_new :
                R.string.course_edit_title_modify));
        setSupportActionBar(toolbar);
        // 标签页
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.course_information));
        titles.add(getString(R.string.course_experiment));
        titles.add(getString(R.string.course_assignment));
        LayoutInflater mLayoutInflater = getLayoutInflater();
        ArrayList<View> pages = new ArrayList<>();
        pages.add(mLayoutInflater.inflate(R.layout.page_course_edit_information, null));
        pages.add(mLayoutInflater.inflate(R.layout.page_course_edit_experiment, null));
        pages.add(mLayoutInflater.inflate(R.layout.page_course_edit_assignment, null));
        CoursePagerAdapter pagerAdapter = new CoursePagerAdapter(titles, pages); // 标签页适配器
        ViewPager viewPager = findViewById(R.id.vp_course_edit);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_course_edit);
        tabs.setupWithViewPager(viewPager);
        // 子控件
        etName = pages.get(0).findViewById(R.id.et_course_information_name);
        spnType = pages.get(0).findViewById(R.id.spn_course_information_type);
        spnSemester = pages.get(0).findViewById(R.id.spn_course_information_semester);
        etClassWeek = pages.get(0).findViewById(R.id.et_course_information_week);
        etClassTime = pages.get(0).findViewById(R.id.et_course_information_time);
        etClassPlace = pages.get(0).findViewById(R.id.et_course_information_place);
        etClassTeacher = pages.get(0).findViewById(R.id.et_course_information_teacher);
        etCourseRemarks = pages.get(0).findViewById(R.id.et_course_information_remarks);
        etExperimentWeek = pages.get(1).findViewById(R.id.et_course_experiment_week);
        etExperimentTime = pages.get(1).findViewById(R.id.et_course_experiment_time);
        etExperimentPlace = pages.get(1).findViewById(R.id.et_course_experiment_place);
        etExperimentTeacher = pages.get(1).findViewById(R.id.et_course_experiment_teacher);
        etExperimentRemarks = pages.get(1).findViewById(R.id.et_course_experiment_remarks);
//        ibExperimentAdd = pages.get(1).findViewById(R.id.ib_course_experiment_add);
        etAssignmentRemarks = pages.get(2).findViewById(R.id.et_course_assignment_remarks);
//        ibAssignmentAdd = pages.get(2).findViewById(R.id.ib_course_assignment_add);
        // 设置监听器
        etClassWeek.setOnClickListener(this);
        etClassTime.setOnClickListener(this);
        etExperimentWeek.setOnClickListener(this);
        etExperimentTime.setOnClickListener(this);
//        ibExperimentAdd.setOnClickListener(this);
//        ibAssignmentAdd.setOnClickListener(this);
        // 课程类型
        mTypeAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, mTypes);
        mTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnType.setAdapter(mTypeAdapter);
        // 学期
        mSemesterAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, mSemesters);
        mSemesterAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spnSemester.setAdapter(mSemesterAdapter);
        int index;
        if (mMode == VAL_MODE_NEW) {
            index = mSemesters.indexOf(mSemester);
            spnSemester.setSelection(index != -1 ? index : 0);  // 选中编辑课程界面所显示的学期
        } else if (mMode == VAL_MODE_EDIT) {
            index = mTypes.indexOf(mCourse.getType());
            spnType.setSelection(index != -1 ? index : 0);  // 选中编辑课程界面所显示的课程类型
            index = mSemesters.indexOf(mCourse.getSemester());
            spnSemester.setSelection(index != -1 ? index : 0);  // 选中编辑课程界面所显示的学期
            etName.setText(mCourse.getName());
            etClassWeek.setText(mCourse.getClassWeek());
            etClassTime.setText(mCourse.getClassTime());
            etClassPlace.setText(mCourse.getClassPlace());
            etClassTeacher.setText(mCourse.getClassTeacher());
            etCourseRemarks.setText(mCourse.getCourseRemarks());
            etExperimentWeek.setText(mCourse.getExperimentWeek());
            etExperimentTime.setText(mCourse.getExperimentTime());
            etExperimentPlace.setText(mCourse.getExperimentPlace());
            etExperimentTeacher.setText(mCourse.getExperimentTeacher());
            etExperimentRemarks.setText(mCourse.getExperimentRemarks());
            etAssignmentRemarks.setText(mCourse.getAssignmentRemarks());
        }

    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        // 选择课程类型
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 选择最后一项时弹出添加新类型对话框
                if (i == mTypes.size() - 1) {
                    TypeDialogFragment dlgType = TypeDialogFragment.newInstance(
                            getString(R.string.dialog_course_type_title), mTypes);
                    dlgType.setTypeAddedListener(new TypeDialogFragment.TypeAddedListener() {
                        @Override
                        public void onTypeAdded(String type) {
                            mTypes.add(mTypes.size() - 2, type);  // 添加到已有类型末尾
                            mTypeAdapter.notifyDataSetChanged();
                            spnType.setSelection(mTypes.size() - 3);    // 选中新添加的类型
                            mCourseHelper.insertType(type);
                            Toast.makeText(CourseEditActivity.this, String.format(
                                    getString(R.string.course_edit_type_added), type),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTypeNotAdded() {
                            spnType.setSelection(mTypes.size() - 2);   // 未添加，选中-
                        }
                    });
                    dlgType.show(getSupportFragmentManager(), null);    // 显示添加类型对话框
                } // if end
            } // onItemSelected end

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // spnType.setOnItemSelectedListener end

        // 选择学期
        spnSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 选择最后一项时弹出添加新学期对话框
                if (i == mSemesters.size() - 1) {
                    SemesterDialogFragment dlgSemester = SemesterDialogFragment.newInstance(mSemesters);
                    dlgSemester.setSemesterAddedListener(new SemesterDialogFragment.SemesterAddedListener() {
                        @Override
                        public void onSemesterAdded(String semester) {
                            mSemesters.add(0, semester);  // 添加到已有学期顶端
                            mSemesterAdapter.notifyDataSetChanged();
                            spnSemester.setSelection(0);     // 选择新添加的学期
                            mCourseHelper.deleteAllSemesters();
                            mCourseHelper.insertSemesters(
                                    new ArrayList<>(mSemesters.subList(0, mSemesters.size() - 2)));
                            Toast.makeText(CourseEditActivity.this, String.format(
                                    getString(R.string.course_edit_semester_added), semester),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSemesterNotAdded() {
                            spnSemester.setSelection(mSemesters.size() - 2);   // 未添加，选中“-”
                        }
                    });
                    dlgSemester.show(getSupportFragmentManager(), null);    // 显示添加新学期对话框
                } // setSemesterAddedListener end
            } // onItemSelected end

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // spnSemester.setOnItemSelectedListener end

    } // initView end

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 选择上课周数
            case R.id.et_course_information_week:
                WeekDialogFragment dlgClassWeek = WeekDialogFragment.newInstance(
                        getString(R.string.dialog_course_week_title_class), mMaxWeek,
                        etClassWeek.getText().toString());
                dlgClassWeek.setWeekSelectedListener(new WeekDialogFragment.WeekSelectedListener() {
                    @Override
                    public void onWeekSelected(String newWeek) {
                        etClassWeek.setText(newWeek);    // 填入选择的上课周数
                    }
                });
                dlgClassWeek.show(getSupportFragmentManager(), null); // 显示选择周数对话框
                break;
            // 选择上课时间
            case R.id.et_course_information_time:
                TimeDialogFragment dlgClassTime = TimeDialogFragment.newInstance(
                        getString(R.string.dialog_course_time_title_class), mMaxClass,
                        etClassTime.getText().toString());
                dlgClassTime.setTimeSelectedListener(new TimeDialogFragment.TimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String newTime) {
                        etClassTime.setText(newTime);      // 填入选择的上课时间
                    }
                });
                dlgClassTime.show(getSupportFragmentManager(), null);  // 显示选择时间对话框
                break;
            // 选择实验周数
            case R.id.et_course_experiment_week:
                WeekDialogFragment dlgExperimentWeek = WeekDialogFragment.newInstance(
                        getString(R.string.dialog_course_week_title_experiment), mMaxWeek,
                        etExperimentWeek.getText().toString());
                dlgExperimentWeek.setWeekSelectedListener(new WeekDialogFragment.WeekSelectedListener() {
                    @Override
                    public void onWeekSelected(String newWeek) {
                        etExperimentWeek.setText(newWeek);   // 填入选择的实验周数
                    }
                });
                dlgExperimentWeek.show(getSupportFragmentManager(), null);  // 显示选择周数对话框
                break;
            // 选择实验时间
            case R.id.et_course_experiment_time:
                TimeDialogFragment dlgExperimentTime = TimeDialogFragment.newInstance(
                        getString(R.string.dialog_course_time_title_experiment), MAX_CLASS,
                        etExperimentTime.getText().toString());
                dlgExperimentTime.setTimeSelectedListener(new TimeDialogFragment.TimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String newTime) {
                        etExperimentTime.setText(newTime);     // 填入选择的实验时间
                    }
                });
                dlgExperimentTime.show(getSupportFragmentManager(), null);  // 显示选择时间对话框
                break;
//            // 添加实验
//            case R.id.ib_course_experiment_add:
//                // TODO: 添加实验
//
//                break;
//            // 添加作业
//            case R.id.ib_course_assignment_add:
//                // TODO: 添加作业
//
//                break;
        }
    }
}
