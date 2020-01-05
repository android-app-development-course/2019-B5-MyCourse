package com.android.mycourse.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    private Spinner spnSemester;     // 学期下拉选择框
    private ListView lvCourse;  // 课程列表

    private CourseHelper mCourseHelper;
    private ArrayList<String> mSemesters;
    private ArrayList<CourseBean> mAllCourses;
    private ArrayList<CourseBean> mCourses;
    private ArrayAdapter<String> semesterAdapter;
    private CourseAdapter mCourseAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        setHasOptionsMenu(true);    // 在工具栏显示此Fragment的菜单项
        mCourseHelper = new CourseHelper(getContext());
        initSemesterView();
        initListView(view);
        return view;
    }

    // 隐藏显示操作
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 隐藏工具栏中的周数选择框（不使用会占用空间的 INVISIBLE）
            spnSemester.setVisibility(View.GONE);
        } else {
            // 显示工具栏中的学期下拉选择框
            mSemesters = mCourseHelper.getAllSemesters();
            semesterAdapter.clear();
            semesterAdapter.addAll(mSemesters);
            spnSemester.setVisibility(View.VISIBLE);
            mAllCourses = mCourseHelper.getAllCourses();
            mCourses.clear();
            int i = spnSemester.getSelectedItemPosition();
            String semester = i >= 0 && i < mSemesters.size() ? mSemesters.get(i) : "";
            for (CourseBean courseBean : mAllCourses) {
                if (courseBean.getSemester().equals(semester)) {
                    mCourses.add(courseBean);
                }
            }
            mCourseAdapter.notifyDataSetChanged();
        }
    }

    // 设置工具栏菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();   // 清除工具栏中的原有的其他菜单项
        inflater.inflate(R.menu.menu_course, menu); // 向工具栏添加此Fragment的菜单项
    }

    // 工具栏菜单项选择事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.course_action_new) {        // 添加课程
            Intent intent = new Intent(getContext(), CourseEditActivity.class);
            intent.putExtra(CourseEditActivity.ARG_MODE, CourseEditActivity.VAL_MODE_NEW);
            int index = spnSemester.getSelectedItemPosition();
            if (index >= 0 && index < mSemesters.size()) {
                intent.putExtra(CourseConstants.EXTRA_SEMESTER, mSemesters.get(index));
            }
            startActivityForResult(intent, CourseConstants.REQUEST_NEW);
        }
        return super.onOptionsItemSelected(item);
    }

    // 处理添加或修改结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSemesters = mCourseHelper.getAllSemesters();
        semesterAdapter.clear();
        semesterAdapter.addAll(mSemesters);
        int index =
                data != null ? data.getIntExtra(CourseConstants.EXTRA_INDEX, -1) : -1;
        CourseBean course =
                data != null ? (CourseBean) data.getParcelableExtra(CourseConstants.EXTRA_COURSE) : null;
        if (requestCode == CourseConstants.REQUEST_NEW) {   // 新建课程
            if (resultCode == CourseConstants.RESULT_INSERT) {  // 添加课程信息
                if (course != null) {
                    mAllCourses.add(course);
                    mCourses.clear();
                    int i = spnSemester.getSelectedItemPosition();
                    String semester = i >= 0 && i < mSemesters.size() ? mSemesters.get(i) : "";
                    for (CourseBean courseBean : mAllCourses) {
                        if (courseBean.getSemester().equals(semester)) {
                            mCourses.add(courseBean);
                        }
                    }
                }
            }
        } else if (requestCode == CourseConstants.REQUEST_VIEW) {   // 查看课程
            if (resultCode == CourseConstants.RESULT_UPDATE) {  // 更新课程信息
                if (index >= 0 && index < mCourses.size() && course != null) {
                    mCourses.set(index, course);
                    for (int i = 0; i < mAllCourses.size(); i++) {
                        if (mAllCourses.get(i).getId() == course.getId()) {
                            mAllCourses.set(i, course);
                            break;
                        }
                    }
                }
            } else if (resultCode == CourseConstants.RESULT_DELETE) {   // 删除课程
                if (index >= 0 && index < mAllCourses.size() && course != null) {
                    mCourses.remove(index);
                    for (CourseBean courseBean : mAllCourses) {
                        if (courseBean.getId() == course.getId()) {
                            mAllCourses.remove(courseBean);
                        }
                    }
                }
            }
        }
        mCourseAdapter.notifyDataSetChanged();
    } // onActivityResult end

    /**
     * 初始化学期选择框
     */
    private void initSemesterView() {
        mSemesters = mCourseHelper.getAllSemesters();
        semesterAdapter = new ArrayAdapter<>(
                MainActivity.INSTANCE, R.layout.item_spinner_toolbar, mSemesters);
        semesterAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        AppBarLayout.LayoutParams lpSemester = new AppBarLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpSemester.leftMargin = 50;
        spnSemester = new Spinner(getContext());
        spnSemester.setLayoutParams(lpSemester);
        spnSemester.setAdapter(semesterAdapter);
        spnSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCourses.clear();
                for (CourseBean course : mAllCourses) {
                    if (course.getSemester().equals(mSemesters.get(i))) {
                        mCourses.add(course);
                    }
                }
                mCourseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MainActivity.INSTANCE.mTbMain.addView(spnSemester);      // 添加学期下拉选择框到工具栏中
    }

    /**
     * 初始化课程列表
     */
    private void initListView(View view) {
        mAllCourses = mCourseHelper.getAllCourses();
        mCourses = new ArrayList<>(mAllCourses);
        mCourseAdapter = new CourseAdapter(getContext(), mCourses);
        lvCourse = view.findViewById(R.id.lv_course);
        lvCourse.setAdapter(mCourseAdapter);
        // 点击课程列表进入查看课程界面
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CourseViewActivity.class);
                intent.putExtra(CourseConstants.EXTRA_INDEX, i);
                intent.putExtra(CourseConstants.EXTRA_COURSE, mCourses.get(i));
                startActivityForResult(intent, CourseConstants.REQUEST_VIEW);
            }
        });
        // 长按课程列表删除课程
        lvCourse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                // 询问是否删除课程
                new AlertDialog.Builder(MainActivity.INSTANCE)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(
                                getString(R.string.course_delete_ask), mCourses.get(index).getName()))
                        .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), String.format(
                                        getString(R.string.course_delete_finish),
                                        mCourses.get(index).getName()), Toast.LENGTH_SHORT).show();
                                mCourseHelper.deleteCourse(mCourses.get(index));
                                mCourses.remove(index);
                                mCourseAdapter.notifyDataSetChanged();
                                mAllCourses = mCourseHelper.getAllCourses();
                            }
                        })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                return true;
            }
        });
    }
}