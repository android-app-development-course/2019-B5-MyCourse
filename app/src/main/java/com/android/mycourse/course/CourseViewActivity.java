package com.android.mycourse.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.mycourse.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CourseViewActivity extends AppCompatActivity {

    private int mIndex;
    private CourseBean mCourse;

    private TextView tvName;
    private TextView tvType;
    private TextView tvSemester;
    private TextView tvClassWeek;
    private TextView tvClassTime;
    private TextView tvClassPlace;
    private TextView tvClassTeacher;
    private TextView tvCourseRemarks;
    private TextView tvExperimentWeek;
    private TextView tvExperimentTime;
    private TextView tvExperimentPlace;
    private TextView tvExperimentTeacher;
    private TextView tvExperimentRemarks;
    private ImageButton ibExperimentAdd;
    private TextView tvAssignmentRemarks;
    private ImageButton ibAssignmentAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        initView();     // 初始化控件
        updateView();   // 控件装入数据
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 返回主界面
            case android.R.id.home:
                finish();
                break;
            // 编辑课程
            case R.id.action_course_view_edit:
                Intent intent = new Intent(this, CourseEditActivity.class);
                intent.putExtra(CourseEditActivity.ARG_MODE, CourseEditActivity.VAL_MODE_EDIT);
                intent.putExtra(CourseConstants.EXTRA_COURSE, mCourse);
                startActivityForResult(intent, CourseConstants.REQUEST_EDIT);  // 进入编辑界面
                break;
            // 删除课程
            case R.id.action_course_view_delete:
                // 询问是否删除课程
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(getString(R.string.course_delete_ask),
                                mCourse.getName()))
                        .setPositiveButton(getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent();
                                        intent.putExtra(CourseConstants.EXTRA_INDEX, mIndex);
                                        intent.putExtra(CourseConstants.EXTRA_COURSE, mCourse);
                                        setResult(CourseConstants.RESULT_DELETE, intent);
                                        Toast.makeText(CourseViewActivity.this, String.format(
                                                getString(R.string.course_delete_finish),
                                                mCourse.getName()), Toast.LENGTH_SHORT).show();
                                        finish();   // 返回主界面删除课程
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
        if (requestCode == CourseConstants.REQUEST_EDIT) {    // 编辑课程
            if (resultCode == CourseConstants.RESULT_UPDATE) {   // 更新课程
                mCourse = data != null ?
                        (CourseBean) data.getParcelableExtra(CourseConstants.EXTRA_COURSE) : null;
                if (mCourse != null) {
                    updateView();   // 更新控件数据
                    // 回传数据
                    Intent intent = new Intent();
                    intent.putExtra(CourseConstants.EXTRA_INDEX, mIndex);
                    intent.putExtra(CourseConstants.EXTRA_COURSE, mCourse);
                    setResult(CourseConstants.RESULT_UPDATE, intent);
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
                intent.getIntExtra(CourseConstants.EXTRA_INDEX, -1) : -1;
        mCourse = intent != null ?
                (CourseBean) intent.getParcelableExtra(CourseConstants.EXTRA_COURSE) : null;
        // 工具栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.course_view_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 标签页
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.course_information));
        titles.add(getString(R.string.course_experiment));
        titles.add(getString(R.string.course_assignment));
        LayoutInflater mLayoutInflater = getLayoutInflater();
        ArrayList<View> pages = new ArrayList<>();
        pages.add(mLayoutInflater.inflate(R.layout.page_course_view_information, null));
        pages.add(mLayoutInflater.inflate(R.layout.page_course_view_experiment, null));
        pages.add(mLayoutInflater.inflate(R.layout.page_course_view_assignment, null));
        CoursePagerAdapter pagerAdapter = new CoursePagerAdapter(titles, pages);
        ViewPager viewPager = findViewById(R.id.vp_course_view);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_course_view);
        tabs.setupWithViewPager(viewPager);
        // 课程信息
        tvName = pages.get(0).findViewById(R.id.tv_course_information_name);
        tvType = pages.get(0).findViewById(R.id.tv_course_information_type);
        tvSemester = pages.get(0).findViewById(R.id.tv_course_information_semester);
        tvClassWeek = pages.get(0).findViewById(R.id.tv_course_information_week);
        tvClassTime = pages.get(0).findViewById(R.id.tv_course_information_time);
        tvClassPlace = pages.get(0).findViewById(R.id.tv_course_information_place);
        tvClassTeacher = pages.get(0).findViewById(R.id.tv_course_information_teacher);
        tvCourseRemarks = pages.get(0).findViewById(R.id.tv_course_information_remarks);
        tvExperimentWeek = pages.get(1).findViewById(R.id.tv_course_experiment_week);
        tvExperimentTime = pages.get(1).findViewById(R.id.tv_course_experiment_time);
        tvExperimentPlace = pages.get(1).findViewById(R.id.tv_course_experiment_place);
        tvExperimentTeacher = pages.get(1).findViewById(R.id.tv_course_experiment_teacher);
        tvExperimentRemarks = pages.get(1).findViewById(R.id.tv_course_experiment_remarks);
//        ibExperimentAdd = pages.get(1).findViewById(R.id.ib_course_experiment_add);
        tvAssignmentRemarks = pages.get(2).findViewById(R.id.tv_course_assignment_remarks);
//        ibAssignmentAdd = pages.get(2).findViewById(R.id.ib_course_assignment_add);
    }

    /**
     * 更新控件数据
     */
    private void updateView() {
        if (mCourse != null) {
            tvName.setText(mCourse.getName());
            tvType.setText(mCourse.getType());
            tvSemester.setText(mCourse.getSemester());
            tvClassWeek.setText(mCourse.getClassWeek());
            tvClassTime.setText(mCourse.getClassTime());
            tvClassPlace.setText(mCourse.getClassPlace());
            tvClassTeacher.setText(mCourse.getClassTeacher());
            tvCourseRemarks.setText(mCourse.getCourseRemarks());
            tvExperimentWeek.setText(mCourse.getExperimentWeek());
            tvExperimentTime.setText(mCourse.getExperimentTime());
            tvExperimentPlace.setText(mCourse.getExperimentPlace());
            tvExperimentTeacher.setText(mCourse.getExperimentTeacher());
            tvExperimentRemarks.setText(mCourse.getExperimentRemarks());
            tvExperimentRemarks.setVisibility(
                    tvExperimentRemarks.getText().equals("") ? View.GONE : View.VISIBLE);
            tvAssignmentRemarks.setText(mCourse.getAssignmentRemarks());
            tvAssignmentRemarks.setVisibility(
                    tvAssignmentRemarks.getText().equals("") ? View.GONE : View.VISIBLE);
        }
    }
}
