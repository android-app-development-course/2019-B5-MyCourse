package com.android.mycourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.mycourse.address.AddressEditActivity;
import com.android.mycourse.address.AddressFragment;
import com.android.mycourse.assignment.AssignmentEditActivity;
import com.android.mycourse.assignment.AssignmentFragment;
import com.android.mycourse.course.CourseFragment;
import com.android.mycourse.experiment.ExperimentEditActivity;
import com.android.mycourse.experiment.ExperimentFragment;
import com.android.mycourse.notepad.NoteEditActivity;
import com.android.mycourse.notepad.NotepadFragment;
import com.android.mycourse.reminder.ReminderAddActivity;
import com.android.mycourse.reminder.ReminderFragment;
import com.android.mycourse.settings.SettingsFragment;
import com.android.mycourse.timetable.TimetableFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static MainActivity INSTANCE;
    public Toolbar mTbMain;                 // 工具栏，用于设置标题、监听菜单项选择事件等
    private DrawerLayout mDrawer;           // 抽屉布局，用于打开侧滑菜单
    private NavigationView mNavigationView; // 左侧的导航菜单，用于监听选择菜单项事件
    public Fragment mCurrentFragment;       // 当前Fragment，用于切换Fragment时隐藏
    TimetableFragment mTimetableFragment;   // 课表界面
    CourseFragment mCourseFragment;         // 课程界面
    ExperimentFragment mExperimentFragment; // 实验界面
    AssignmentFragment mAssignmentFragment; // 作业界面
    ReminderFragment mReminderFragment;     // 事件提醒界面
    NotepadFragment mNotepadFragment;       // 记事备忘界面
    AddressFragment mAddressFragment;       // 网址收藏界面
    SettingsFragment mSettingsFragment;     // 设置界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE = this;    // 保存此Activity实例，以便在Fragment中使用
        initView();         // 初始化控件
        initEvent();        // 初始化事件
    }

    /**
     * 初始化控件
     */
    private void initView() {

        mTbMain = findViewById(R.id.tb_main);
        mNavigationView = findViewById(R.id.nav_view);
        mDrawer = findViewById(R.id.drawer_layout);

        // 设置左侧菜单的日期
        ((TextView) (mNavigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_date)))
                .setText(String.format("%1$tF  %1$tA", new Date()));
        // 显示默认界面课表
        mTimetableFragment = new TimetableFragment();
        mCurrentFragment = mTimetableFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_main, mCurrentFragment).commit();

        mTbMain.setTitle(getString(R.string.main_menu_timetable));
        setSupportActionBar(mTbMain);       // 设置工具栏

    } // initView end

    /**
     * 初始化事件
     */
    private void initEvent() {

        // 点击工具栏导航按钮时，滑出左侧菜单
        mTbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);     // 打开左侧菜单
            }
        });

        // 选择左侧菜单的菜单项，切换相应的界面
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (mCurrentFragment != null) {      // 隐藏当前即将被切换的Fragment
                    getSupportFragmentManager().beginTransaction().hide(mCurrentFragment).commit();
                }
                switch (menuItem.getItemId()) {
                    // 课表
                    case R.id.nav_timetable:
                        if (mTimetableFragment == null) {
                            mTimetableFragment = new TimetableFragment();
                        }
                        mCurrentFragment = mTimetableFragment;
                        break;
                    // 课程
                    case R.id.nav_course:
                        if (mCourseFragment == null) {
                            mCourseFragment = new CourseFragment();
                        }
                        mCurrentFragment = mCourseFragment;
                        break;
                    // 实验
                    case R.id.nav_experiment:
                        if (mExperimentFragment == null) {
                            mExperimentFragment = new ExperimentFragment();
                        }
                        mCurrentFragment = mExperimentFragment;
                        break;
                    // 作业
                    case R.id.nav_assignment:
                        if (mAssignmentFragment == null) {
                            mAssignmentFragment = new AssignmentFragment();
                        }
                        mCurrentFragment = mAssignmentFragment;
                        break;
                    // 事件提醒
                    case R.id.nav_reminder:
                        if (mReminderFragment == null) {
                            mReminderFragment = new ReminderFragment();
                        }
                        mCurrentFragment = mReminderFragment;
                        break;
                    // 记事备忘
                    case R.id.nav_notepad:
                        if (mNotepadFragment == null) {
                            mNotepadFragment = new NotepadFragment();
                        }
                        mCurrentFragment = mNotepadFragment;
                        break;
                    // 网址收藏
                    case R.id.nav_address:
                        if (mAddressFragment == null) {
                            mAddressFragment = new AddressFragment();
                        }
                        mCurrentFragment = mAddressFragment;
                        break;
                    // 设置
                    case R.id.nav_settings:
                        if (mSettingsFragment == null) {
                            mSettingsFragment = new SettingsFragment();
                        }
                        mCurrentFragment = mSettingsFragment;
                        break;
                    default:
                        return false;
                }
                mTbMain.setTitle(menuItem.getTitle());  // 默认用菜单的标题作为切换界面后工具栏的标题
                if (!mCurrentFragment.isAdded()) {          // 如果未添加，则先添加
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content_main, mCurrentFragment).commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .show(mCurrentFragment).commit();   // 显示被切换的界面
                mDrawer.closeDrawer(GravityCompat.START);   // 自动关闭左侧菜单
                return true;
            } // onNavigationItemSelected end
        }); // setNavigationItemSelectedListener end

    } // initEvent end

    // 创建工具栏菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 选择工具栏菜单项事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main_action_new_experiment:
                // TODO: 新建实验
                intent = new Intent(this, ExperimentEditActivity.class);
                intent.putExtra(ExperimentEditActivity.ARG_MODE, 0);
                startActivity(intent);
                break;
            case R.id.main_action_new_assignment:
                // TODO: 新建作业
                intent = new Intent(this, AssignmentEditActivity.class);
                intent.putExtra(AssignmentEditActivity.ARG_MODE, 0);
                startActivity(intent);
                break;
            case R.id.main_action_new_reminder:
                // TODO: 新建提醒
                intent = new Intent(this, ReminderAddActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.main_action_new_note:
                // TODO: 新建备忘
                intent = new Intent(this, NoteEditActivity.class);
                intent.putExtra(NoteEditActivity.ARG_MODE, NoteEditActivity.VAL_MODE_NEW);
                startActivity(intent);
                break;
            case R.id.main_action_new_address:
                // TODO: 新建网址
                intent = new Intent(this, AddressEditActivity.class);
                intent.putExtra(AddressEditActivity.ARG_MODE, AddressEditActivity.VAL_MODE_NEW);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 回退键事件
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);   // 如果打开了左侧菜单，则只关闭左侧菜单
            return;
        }
        // TODO: 根据用户设置判断是否立即退出APP
//        moveTaskToBack(true);   // 不立即退出APP
        super.onBackPressed();    // 立即退出APP
    }

}