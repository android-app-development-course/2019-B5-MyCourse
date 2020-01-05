package com.android.mycourse.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.android.mycourse.view.ScrollListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_TITLE = "title";        // 参数对话框标题
    private static final String ARG_MAX_CLASS = "maxClass"; // 参数最大节数
    private static final String ARG_OLD_TIME = "oldTime";   // 参数已选时间
    private static final int MAX_CLASS = 11;                // 参数默认最大节数

    private String mTitle;          // 对话框标题
    private int mMaxClass;          // 最大节数
    private String mOldTime;        // 已选时间
    String[] mDays;                 // 星期
    private List<String> mTimes;    // 选择的时间

    private NumberPicker npDay;         // 星期
    private NumberPicker npFirstLesson; // 第一节
    private NumberPicker npLastLesson;  // 最后一节
    private ScrollListView slvTime;     // 选择的时间列表
    private Button btnAdd;              // 添加按钮
    private Button btnCancel;           // 取消按钮
    private Button btnOK;               // 确定按钮
    ArrayAdapter<String> timeAdapter;

    private TimeSelectedListener mTimeSelectedListener;     // 监听器

    /**
     * 监听器接口
     * 点击确定时调用方法向调用者传送参数值
     * 调用者通过重写方法从参数获取参数值
     */
    interface TimeSelectedListener {
        void onTimeSelected(String newTime);
    }

    /**
     * 设置监听器，在调用者中使用
     *
     * @param timeSelectedListener 监听器
     */
    void setTimeSelectedListener(TimeSelectedListener timeSelectedListener) {
        mTimeSelectedListener = timeSelectedListener;
    }

    /**
     * 创建对话框实例
     *
     * @param title 对话框标题
     * @return 对话框实例
     */
    static TimeDialogFragment newInstance(String title, int maxClass, String oldTime) {
        TimeDialogFragment fragment = new TimeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putInt(ARG_MAX_CLASS, maxClass);
        bundle.putString(ARG_OLD_TIME, oldTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_course_time, null);
        initView(view);     // 初始化控件
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        return builder.create();
    }

    /**
     * 初始化控件
     *
     * @param view 布局
     */
    private void initView(View view) {

        Bundle bundle = getArguments();
        mTitle = bundle != null ? bundle.getString(ARG_TITLE) : "";
        mMaxClass = bundle != null ? bundle.getInt(ARG_MAX_CLASS) : MAX_CLASS;
        mOldTime = bundle != null ? bundle.getString(ARG_OLD_TIME) : "";

        npDay = view.findViewById(R.id.np_dialog_course_time_day);
        npFirstLesson = view.findViewById(R.id.np_dialog_course_time_first);
        npLastLesson = view.findViewById(R.id.np_dialog_course_time_last);
        slvTime = view.findViewById(R.id.lv_dialog_course_time_time);
        btnAdd = view.findViewById(R.id.btn_dialog_course_time_add);
        btnCancel = view.findViewById(R.id.btn_dialog_course_time_cancel);
        btnOK = view.findViewById(R.id.btn_dialog_course_time_ok);

        ((TextView) view.findViewById(R.id.tv_dialog_course_time_title)).setText(mTitle);  // 设置标题
        // NumberPicker的项数由MaxValue-MinValue决定，且总是取数组的前N项而自动剔除后项，故MaxValue需取数组长度
        // 初始化星期选项
        mDays = CourseUtils.getDays(MainActivity.INSTANCE);
        npDay.setDisplayedValues(mDays);
        npDay.setMinValue(1);               // 第一项
        npDay.setMaxValue(mDays.length);    // 最后一项
        npDay.setValue(1);                  // 默认选择项
        npDay.setWrapSelectorWheel(false);  // 禁止循环滚动
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  // 禁止编辑
        // 初始化第一节选项
        npFirstLesson.setMinValue(1);
        npFirstLesson.setMaxValue(mMaxClass);
        npFirstLesson.setValue(1);
        npFirstLesson.setWrapSelectorWheel(false);
        npFirstLesson.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        // 初始化最后一节选项
        npLastLesson.setMinValue(1);
        npLastLesson.setMaxValue(mMaxClass);
        npLastLesson.setValue(1);
        npLastLesson.setWrapSelectorWheel(false);
        npLastLesson.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        // 监听选择第一节课
        npFirstLesson.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                npLastLesson.setMinValue(i1);   // 只能选择第一节之后的节数
                npLastLesson.setValue(i1);      // 设置和第一节的节数相同
                npLastLesson.setWrapSelectorWheel(false);   // 禁止自动恢复可滚动模式
            }
        });
        // 初始化已选时间列表
        mTimes = new ArrayList<>();
        if (!mOldTime.equals("")) {
            String[] times = mOldTime.split("\n");
            mTimes.addAll(Arrays.asList(times));
        }
        timeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_list_normal, mTimes);
        slvTime.setAdapter(timeAdapter);
        // 监听长按已选时间列表项
        slvTime.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTimes.remove(i);    // 移除已选时间
                timeAdapter.notifyDataSetChanged();
                return true;
            }
        });
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);

    }

    /**
     * 控件点击事件
     *
     * @param view 被点击的控件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 添加时间
            case R.id.btn_dialog_course_time_add:
                String day = mDays[npDay.getValue() - 1];
                int firstLesson = npFirstLesson.getValue();
                int lastLesson = npLastLesson.getValue();
                String timeToAdd = CourseUtils.formatTime(MainActivity.INSTANCE,
                        npDay.getValue() - 1, firstLesson, lastLesson);
//                String timeToAdd = day + ": " + String.format(getString(R.string.dialog_course_time_time),
//                        "" + (firstLesson == lastLesson ? firstLesson : firstLesson + "-" + lastLesson));
                if (mTimes.contains(timeToAdd)) {
                    Toast.makeText(getContext(),
                            getString(R.string.dialog_course_time_exist), Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: 检查选择合法性

                    mTimes.add(timeToAdd);
                    timeAdapter.notifyDataSetChanged();
                    if (mTimes.size() == 1) {
                        Toast.makeText(getContext(),
                                getString(R.string.dialog_course_time_add), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            // 取消选择时间
            case R.id.btn_dialog_course_time_cancel:
                dismiss();  // 关闭对话框
                break;
            // 确定选择时间
            case R.id.btn_dialog_course_time_ok:
                // TODO: 检查选择合法性

                StringBuilder newTime = new StringBuilder();   // 选择的时间
                for (String time : mTimes) {
                    newTime.append(time).append("\n");
                }
                mTimeSelectedListener.onTimeSelected(newTime.toString().trim());   // 传回选择的时间
                dismiss();  // 关闭对话框
                break;
        }
    }
}
