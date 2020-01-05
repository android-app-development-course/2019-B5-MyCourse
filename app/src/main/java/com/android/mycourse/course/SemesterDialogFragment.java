package com.android.mycourse.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.mycourse.R;

import java.util.ArrayList;

public class SemesterDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_SEMESTERS = "semesters";    // 参数已添加的学期

    private ArrayList<String> mSemesters;   // 已添加的学期
    private boolean mSemesterAdded; // 是否添加

    private EditText etStartYear;   // 起始年输入框
    private EditText etEndYear;     // 结束年输入框
    private EditText etSemester;    // 学期输入框
    private Button btnCancel;       // 取消按钮
    private Button btnOK;           // 确定按钮

    private SemesterAddedListener mSemesterAddedListener;     // 监听器

    /**
     * 监听器接口
     * 点击确定时调用方法向调用者传送参数值
     * 调用者通过重写方法从参数获取参数值
     */
    interface SemesterAddedListener {
        void onSemesterAdded(String semester);  // 添加
        void onSemesterNotAdded();              // 不添加
    }

    /**
     * 设置监听器，在调用者中使用
     *
     * @param semesterAddedListener 监听器
     */
    void setSemesterAddedListener(SemesterAddedListener semesterAddedListener) {
        mSemesterAddedListener = semesterAddedListener;
    }

    /**
     * 创建对话框实例
     *
     * @return 对话框实例
     */
    static SemesterDialogFragment newInstance(ArrayList<String> semesters) {
        SemesterDialogFragment fragment = new SemesterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ARG_SEMESTERS, semesters);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_course_semester, null);
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
        if (bundle != null) {
            mSemesters = bundle.getStringArrayList(ARG_SEMESTERS);
        }
        mSemesterAdded = false;

        etStartYear = view.findViewById(R.id.et_dialog_course_semester_start_year);
        etEndYear = view.findViewById(R.id.et_dialog_course_semester_end_year);
        etSemester = view.findViewById(R.id.et_dialog_course_semester_semester);
        btnCancel = view.findViewById(R.id.btn_dialog_course_semester_cancel);
        btnOK = view.findViewById(R.id.btn_dialog_course_semester_ok);

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
            // 取消
            case R.id.btn_dialog_course_semester_cancel:
                dismiss();  // 关闭对话框
                break;
            // 确定
            case R.id.btn_dialog_course_semester_ok:
                String startYear = etStartYear.getText().toString().trim();
                String endYear = etEndYear.getText().toString().trim();
                String semester = etSemester.getText().toString().trim();
                // TODO: 检查输入合法性

                String semesterToAdd = startYear;
                if (!endYear.equals(startYear) && !endYear.equals("")) {
                    semesterToAdd += "-" + endYear;
                }
                if (!semester.equals("")) {
                    semesterToAdd += "(" + semester + ")";
                }
                // 检查学期是否已存在
                for (String str : mSemesters) {
                    if (str.equals(semesterToAdd)) {
                        Toast.makeText(getContext(), String.format(
                                getString(R.string.dialog_course_semester_existed), semesterToAdd),
                                Toast.LENGTH_SHORT).show();
                        return;     // 不允许添加，直接返回
                    }
                }
                mSemesterAddedListener.onSemesterAdded(semesterToAdd);  // 通知允许添加
                mSemesterAdded = true;
                dismiss();  // 关闭对话框
                break;
        }
    }

    // 销毁事件
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (!mSemesterAdded) {
            mSemesterAddedListener.onSemesterNotAdded();    // 调用未添加方法通知调用者
        }
        super.onDismiss(dialog);    // 销毁对话框，需放在最后
    }
}
