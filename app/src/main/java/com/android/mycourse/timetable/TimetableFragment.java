package com.android.mycourse.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.android.mycourse.course.CourseBean;
import com.android.mycourse.course.CourseConstants;
import com.android.mycourse.course.CourseHelper;
import com.android.mycourse.course.CourseUtils;
import com.android.mycourse.course.CourseViewActivity;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class TimetableFragment extends Fragment {

    private static final int MAX_WEEK = 20;
    private static final int MAX_CLASS = 11;

    private Spinner spnWeek;        // 周数下拉选择框，用于保存动态创建的控件
    private Boolean mShowAllDays;   // 是否显示一周7天
    private int mWeekNumber;        // 周数，用于创建周数下拉选择框的选项数
    private int mCurrentWeek;       // 当前周数，用于选择周数下拉选择框的选项
    private int mClassNumber;       // 节数，用于创建节数视图、设置课程表网格行数
    private int mGridHeight;        // 课程表网格高度，用于设置一节课的高度
    private ArrayList<CourseBean> mCourses;
    private CourseHelper mCourseHelper;
    private GridLayout lClass;
    private ArrayList<TextView> tvDays;
    private ArrayList<TextView> tvClasses;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        setHasOptionsMenu(true);
        initData();
        initWeekNumberView();   // 初始化周数下拉选择框控件
        initDayView(view);          // 初始化日期视图
        initClassNumberView(view);  // 初始化节数视图
        initClassView(view);        // 初始化课程表控件
        return view;
    }

    /**
     * Fragment 被 show()、hide() 时调用
     * Fragment被首次创建时即使 hidden == false 也不调用，故不能在此初始化控件
     *
     * @param hidden Fragment 是否可见
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            spnWeek.setVisibility(View.GONE);       // 隐藏工具栏中的周数选择框
        } else {
            spnWeek.setVisibility(View.VISIBLE);    // 显示工具栏中的周数选择框
            mCourses = mCourseHelper.getAllCourses();
            initClassView(getView());
        }
    }

    // 设置工具栏菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timetable, menu); // 向工具栏添加此Fragment的菜单项
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_timetable_fold) {   // 收缩无课程的列
            mShowAllDays = !mShowAllDays;
            for (int i = 0; i < lClass.getColumnCount(); i++) { // 遍历每一列
                // 恢复显示日期
                TextView tvDay = tvDays.get(i);
                tvDay.setVisibility(View.VISIBLE);
                boolean isEmpty = true;
                // 第一次遍历确定该列是否有课程，同时恢复布局
                for (int j = 0; j < mClassNumber; j++) {
                    TextView tvClass = tvClasses.get(i + j * lClass.getColumnCount());
                    GridLayout.LayoutParams lpClass = (GridLayout.LayoutParams) tvClass.getLayoutParams();
                    lpClass.columnSpec = GridLayout.spec(i, 1f);
                    lpClass.setMargins(1, 1, 0, 0);
                    tvClass.setLayoutParams(lpClass);
                    if (tvClass.getId() != -1) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {  // 当天无课程，收缩该列
                    // 隐藏该列上方的日期
                    tvDay.setVisibility(mShowAllDays ? View.VISIBLE : View.GONE);
                    for (int j = 0; j < mClassNumber; j++) {    // 第二次遍历收缩单元格
                        // 收缩该列的单元格
                        TextView tvClass = tvClasses.get(i + j * lClass.getColumnCount());
                        GridLayout.LayoutParams lpClass = (GridLayout.LayoutParams) tvClass.getLayoutParams();
                        lpClass.columnSpec = GridLayout.spec(i, mShowAllDays ? 1f : 0f);
                        lpClass.setMargins(mShowAllDays ? 1 : 0, mShowAllDays ? 1 : 0, 0, 0);
                        tvClass.setLayoutParams(lpClass);
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CourseBean course = data != null ?
                (CourseBean) data.getParcelableExtra(CourseConstants.EXTRA_COURSE) : null;
        if (requestCode == CourseConstants.REQUEST_VIEW) {
            if (resultCode == CourseConstants.RESULT_UPDATE) {
                if (course != null) {
                    for (int i = 0; i < mCourses.size(); i++) {
                        if (mCourses.get(i).getId() == course.getId()) {
                            if (!course.getClassTime().equals(mCourses.get(i).getClassTime())) {
                                removeClass(mCourses.get(i));     // 移除原来的课程
                                String courseExisted = addClass(course); // 重新添加课程
                                if (courseExisted == null) {    // 添加成功
                                    Toast.makeText(getContext(), String.format(
                                            getString(R.string.timetable_added_class), course.getName()),
                                            Toast.LENGTH_SHORT).show();
                                } else {    // 添加失败
                                    Toast.makeText(getContext(), String.format(
                                            getString(R.string.timetable_add_existed), courseExisted),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                int[] indexes = CourseUtils.parseTime(MainActivity.INSTANCE,
                                        mCourses.get(i).getClassTime());
                                TextView tvClass = tvClasses.get((indexes[1] - 1) *
                                        lClass.getColumnCount() + indexes[0] - 1);
                                // 更新单元格内容
                                tvClass.setText(String.format(getString(R.string.timetable_class),
                                        course.getName(), course.getClassPlace()));
                            }
                            mCourses.set(i, course);     // 更新课程信息
                            break;
                        }
                    }
                }
            } else if (resultCode == CourseConstants.RESULT_DELETE) {
                if (course != null) {
                    mCourseHelper.deleteCourse(course);
                    removeClass(course);
                }
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCourseHelper = new CourseHelper(getContext());
        mCourses = mCourseHelper.getAllCourses();       // 读取所有课程数据
        // TODO: 读取并初始化以下变量值

        mWeekNumber = MAX_WEEK;
        mCurrentWeek = 1;
        mClassNumber = MAX_CLASS;
        mGridHeight = 115;
        mShowAllDays = true;

    }

    /**
     * 初始化周数下拉选择框控件
     * 读取周数，根据周数创建周数下拉选择框并放置在工具栏中
     * 默认的周数值为20
     */
    private void initWeekNumberView() {
        // 生成周数列表
        ArrayList<String> mWeekNumbers = new ArrayList<>();
        for (int i = 1; i <= mWeekNumber; i++) {
            mWeekNumbers.add(String.format(getString(R.string.timetable_week_number), i));
        }
        ArrayAdapter<String> weekNumberAdapter = new ArrayAdapter<>(
                MainActivity.INSTANCE, R.layout.item_spinner_toolbar, mWeekNumbers);
        weekNumberAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        AppBarLayout.LayoutParams lpWeekNumber = new AppBarLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpWeekNumber.leftMargin = 90;
        spnWeek = new Spinner(getContext());     // 创建周数下拉选择框
        spnWeek.setAdapter(weekNumberAdapter);
        spnWeek.setSelection(mCurrentWeek - 1);    // 初始化当前周数
        spnWeek.setLayoutParams(lpWeekNumber);
        MainActivity.INSTANCE.mTbMain.addView(spnWeek);      // 添加周数下拉选择框到工具栏中
    }

    /**
     * 初始化日期视图
     * 创建包含星期和日的文本并显示在顶部
     *
     * @param view 容器
     */
    private void initDayView(View view) {
        // 取出标题以便在循环中使用
        int[] days = {R.string.timetable_day_mon, R.string.timetable_day_tue,
                R.string.timetable_day_wed, R.string.timetable_day_thu,
                R.string.timetable_day_fri, R.string.timetable_day_sat,
                R.string.timetable_day_sun};
        // 获取当周的第一天
        Calendar calendar = Calendar.getInstance();     // 获取当前日期
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);   // 获取当天是当周的第几天，值从1开始
        if (calendar.getFirstDayOfWeek() == Calendar.SUNDAY && dayOfWeek == 1) {    // 如果第一天是周日
            dayOfWeek = 8;      // 把周日放到最后一天
        }
        calendar.add(Calendar.DATE, (calendar.getFirstDayOfWeek() == Calendar.SUNDAY
                ? 2 : 1) - dayOfWeek);    // 将 calendar 设为当周的第一天
        // 创建日期视图
        LinearLayout lDay = view.findViewById(R.id.layout_day);     // 获取布局文件中的日期布局
        tvDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LinearLayout.LayoutParams lpDay =
                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lpDay.weight = 1;   // 均分宽度
            TextView tvDay = new TextView(getContext());
            tvDay.setLayoutParams(lpDay);
            tvDay.setGravity(Gravity.CENTER);
            tvDay.setTextColor(getResources().getColor(R.color.textGray));
            tvDay.setText(String.format(getString(days[i]), calendar.get(Calendar.DATE)));  // 设置日期
            tvDays.add(tvDay);
            lDay.addView(tvDay);    // 向日期布局加入日期视图
            calendar.add(Calendar.DATE, 1);     // 取下一天
        }
    }

    /**
     * 初始化节数视图
     * 读取节数，根据节数创建左侧的节数视图
     * 默认的节数值为11
     *
     * @param view 容器
     */
    private void initClassNumberView(View view) {
        LinearLayout lClassNo = view.findViewById(R.id.layout_class_no);    // 获取布局文件中的节数布局
        for (int i = 1; i <= mClassNumber; i++) {
            LinearLayout.LayoutParams lpClassNo = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mGridHeight);
            lpClassNo.topMargin = 1;
            TextView tvClassNo = new TextView(getContext());
            tvClassNo.setLayoutParams(lpClassNo);
            tvClassNo.setGravity(Gravity.CENTER);
            tvClassNo.setBackgroundColor(getResources().getColor(R.color.lightBackground));   // 设置白色背景
            tvClassNo.setTextColor(getResources().getColor(R.color.textGray));
            tvClassNo.setText(String.valueOf(i));   // 节数编号
            lClassNo.addView(tvClassNo);    // 添加节数到布局中
        }
    }

    /**
     * 初始化课程表控件
     * 根据节数创建网格布局，列数固定为7，分别对应一周中的7天，并创建文本控件填满布局
     * 读取上课信息，根据需要移除网格中原有的控件，然后创建课程控件，根据需要设置列数再放入网格
     *
     * @param view 容器
     */
    private void initClassView(View view) {
        lClass = view.findViewById(R.id.grid_timetable);    // 获取布局文件中的课程表布局
        lClass.setRowCount(mClassNumber);   // 根据节数设置网格行数
        lClass.removeAllViews();
        tvClasses = new ArrayList<>();
        // 初始化网格布局，用空白单元格填充网格
        for (int i = 0; i < mClassNumber; i++) {
            for (int j = 0; j < lClass.getColumnCount(); j++) {
                GridLayout.LayoutParams lpClass = new GridLayout.LayoutParams();
                lpClass.setMargins(1, 1, 0, 0); // 显出网格“线”
                lpClass.width = 0;  // 均分列宽
                lpClass.height = mGridHeight;   // 初始化高度
                lpClass.rowSpec = GridLayout.spec(i, 1, 1f);    // 第i行，占1行，高度权重1
                lpClass.columnSpec = GridLayout.spec(j, 1, 1f); // 第j行，占1行，宽度权重1
                lpClass.setGravity(Gravity.FILL);   // 必须放在Spec之后
                TextView tvClass = new TextView(getContext());
                tvClass.setLayoutParams(lpClass);
                tvClass.setPadding(10, 10, 10, 10);
                tvClass.setClickable(true);     // 需设置可点击属性才能应用点击效果
                tvClass.setBackgroundResource(R.drawable.timetable_class);   // 设置点击样式
                tvClass.setGravity(Gravity.CENTER);
                tvClass.setId(-1);  // 用课程Id作为Id，-1表示此单元格所在位置无课程
                tvClasses.add(tvClass);
                lClass.addView(tvClass);   // 添加控件后才能实现列宽均分
            }
        }
        // 添加课程到课表中
        for (CourseBean course : mCourses) {
            addClass(course);
        }
    }

    /**
     * 添加课程到课表中
     *
     * @param course 待添加的课程
     * @return 为null表示添加成功，不为null表示课表中已存在的课程的单元格内容
     */
    private String addClass(CourseBean course) {
        String[] times = course.getClassTime().split("\n");
        for (String time : times) {
            int[] indexes = CourseUtils.parseTime(MainActivity.INSTANCE, time);
            if (indexes[0] == -1) {
                return null;
            }
            // 检查待使用的单元格是否已有课程
            for (int i = indexes[1] - 1; i < indexes[2]; i++) {
                TextView tvRemoved = tvClasses.get(i * lClass.getColumnCount() + indexes[0] - 1);
                if (tvRemoved.getId() != -1) {
                    for (CourseBean courseExisted : mCourses) {
                        if (courseExisted.getId() == tvRemoved.getId()) {
                            return courseExisted.getName(); // 返回已占用单元格的课程名称
                        }
                    }
                }
            }
            // 移出多余的空白单元格
            for (int j = indexes[1]; j < indexes[2]; j++) {
                TextView tvRemoved = tvClasses.get(j * lClass.getColumnCount() + indexes[0] - 1);
                tvRemoved.setId((int) course.getId());     // 用课程Id标记被移出的空白单元格
                lClass.removeView(tvRemoved);   // 移出多余单元格以免课程单元格添加后影响布局效果
            }
            // 获取待设置课程信息的单元格
            TextView tvClass =
                    tvClasses.get((indexes[1] - 1) * lClass.getColumnCount() + indexes[0] - 1);
            // 获取布局属性
            GridLayout.LayoutParams lpClass = (GridLayout.LayoutParams) tvClass.getLayoutParams();
            // 设置行数
            lpClass.rowSpec = GridLayout.spec(indexes[1] - 1, indexes[2] - indexes[1] + 1);
            lpClass.setGravity(Gravity.FILL);   // 需重新设置，必须放在Spec之后
            tvClass.setLayoutParams(lpClass);   // 重设布局属性
            // 设置单元格内容
            tvClass.setText(String.format(
                    getString(R.string.timetable_class), course.getName(), course.getClassPlace()));
            tvClass.setId((int) course.getId());   // 用课程Id作为Id，便于区分空白单元格和课程单元格
            // 监听课程单元格点击事件
            tvClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (CourseBean course : mCourses) {
                        if (course.getId() == view.getId()) {
                            Intent intent = new Intent(getActivity(), CourseViewActivity.class);
                            intent.putExtra(CourseConstants.EXTRA_COURSE, course);
                            startActivityForResult(intent, CourseConstants.REQUEST_VIEW);   // 查看课程
                        }
                    }
                }
            });
        }
        String[] times2 = course.getExperimentTime().split("\n");
        for (String time : times2) {
            int[] indexes = CourseUtils.parseTime(MainActivity.INSTANCE, time);
            if (indexes[0] == -1) {
                return null;
            }
            // 检查待使用的单元格是否已有课程
            for (int i = indexes[1] - 1; i < indexes[2]; i++) {
                TextView tvRemoved = tvClasses.get(i * lClass.getColumnCount() + indexes[0] - 1);
                if (tvRemoved.getId() != -1) {
                    for (CourseBean courseExisted : mCourses) {
                        if (courseExisted.getId() == tvRemoved.getId()) {
                            return courseExisted.getName(); // 返回已占用单元格的课程名称
                        }
                    }
                }
            }
            // 移出多余的空白单元格
            for (int j = indexes[1]; j < indexes[2]; j++) {
                TextView tvRemoved = tvClasses.get(j * lClass.getColumnCount() + indexes[0] - 1);
                tvRemoved.setId((int) course.getId());     // 用课程Id标记被移出的空白单元格
                lClass.removeView(tvRemoved);   // 移出多余单元格以免课程单元格添加后影响布局效果
            }
            // 获取待设置课程信息的单元格
            TextView tvClass =
                    tvClasses.get((indexes[1] - 1) * lClass.getColumnCount() + indexes[0] - 1);
            // 获取布局属性
            GridLayout.LayoutParams lpClass = (GridLayout.LayoutParams) tvClass.getLayoutParams();
            // 设置行数
            lpClass.rowSpec = GridLayout.spec(indexes[1] - 1, indexes[2] - indexes[1] + 1);
            lpClass.setGravity(Gravity.FILL);   // 需重新设置，必须放在Spec之后
            tvClass.setLayoutParams(lpClass);   // 重设布局属性
            // 设置单元格内容
            tvClass.setText(String.format(
                    getString(R.string.timetable_experiment), course.getName(), course.getExperimentPlace()));
            tvClass.setId((int) course.getId());   // 用课程Id作为Id，便于区分空白单元格和课程单元格
            // 监听课程单元格点击事件
            tvClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (CourseBean course : mCourses) {
                        if (course.getId() == view.getId()) {
                            Intent intent = new Intent(getActivity(), CourseViewActivity.class);
                            intent.putExtra(CourseConstants.EXTRA_COURSE, course);
                            startActivityForResult(intent, CourseConstants.REQUEST_VIEW);   // 查看课程
                        }
                    }
                }
            });
        }
        return null;
    } // addClass end

    /**
     * 移除课表中的课程
     *
     * @param course 待移除的课程
     */
    private void removeClass(CourseBean course) {
        String[] times = course.getClassTime().split("\n");
        for (String time : times) {
            int[] indexes = CourseUtils.parseTime(MainActivity.INSTANCE, time);
            // 重置课程单元格为空白单元格
            TextView tvClass = tvClasses.get((indexes[1] - 1) * lClass.getColumnCount() + indexes[0] - 1);
            GridLayout.LayoutParams lpClass = (GridLayout.LayoutParams) tvClass.getLayoutParams();
            lpClass.rowSpec = GridLayout.spec(indexes[1] - 1, 1);
            lpClass.setGravity(Gravity.FILL);
            tvClass.setLayoutParams(lpClass);
            tvClass.setText("");
            tvClass.setId(-1);
            // 重新添加空白单元格
            for (int i = indexes[1]; i < indexes[2]; i++) {
                TextView tvRemoved = tvClasses.get(i * lClass.getColumnCount() + indexes[0] - 1);
                // 获取布局属性
                GridLayout.LayoutParams lpAdded = (GridLayout.LayoutParams) tvRemoved.getLayoutParams();
                // 设置行数
                lpAdded.rowSpec = GridLayout.spec(i, 1);
                lpAdded.setGravity(Gravity.FILL);   // 需重新设置，必须放在Spec之后
                tvRemoved.setLayoutParams(lpAdded);   // 重设布局属性
                tvRemoved.setId(-1);
                lClass.addView(tvRemoved);
            }
        }
    } // removeClass end
}