package com.android.mycourse.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.mycourse.R;

import java.util.ArrayList;

public class TypeDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_TITLE = "title";        // 参数对话框标题
    private static final String ARG_TYPES = "types";        // 参数已添加的类型

    private String mTitle;              // 参数值对话框标题
    private ArrayList<String> mTypes;   // 已添加的类型
    private boolean mTypeAdded;         // 是否添加

    private EditText etTypeName;        // 类型名称输入框
    private Button btnCancel;           // 取消按钮
    private Button btnOK;               // 确定按钮

    private TypeAddedListener mTypeAddedListener;     // 监听器

    /**
     * 监听器接口
     * 点击确定时调用方法向调用者传送参数值
     * 调用者通过重写方法从参数获取参数值
     */
    interface TypeAddedListener {
        void onTypeAdded(String type);  // 添加
        void onTypeNotAdded();          // 不添加
    }

    /**
     * 设置监听器，在调用者中使用
     *
     * @param typeAddedListener 监听器
     */
    void setTypeAddedListener(TypeAddedListener typeAddedListener) {
        mTypeAddedListener = typeAddedListener;
    }

    /**
     * 创建对话框实例
     *
     * @param title 对话框标题
     * @return 对话框实例
     */
    static TypeDialogFragment newInstance(String title, ArrayList<String> types) {
        TypeDialogFragment fragment = new TypeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putStringArrayList(ARG_TYPES, types);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_course_type, null);
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
            mTitle = bundle.getString(ARG_TITLE);
            mTypes = bundle.getStringArrayList(ARG_TYPES);
        } else {
            mTitle = "";
            mTypes = null;
        }
        mTypeAdded = false;

        etTypeName = view.findViewById(R.id.et_dialog_course_type_name);
        btnCancel = view.findViewById(R.id.btn_dialog_course_type_cancel);
        btnOK = view.findViewById(R.id.btn_dialog_course_type_ok);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.tv_dialog_course_type_title)).setText(mTitle);

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
            case R.id.btn_dialog_course_type_cancel:
                dismiss();  // 关闭对话框
                break;
            // 确定
            case R.id.btn_dialog_course_type_ok:
                String type = etTypeName.getText().toString().trim();
                // TODO: 检查输入合法性

                // 检查类型是否已存在
                for (String str : mTypes) {
                    if (str.equals(type)) {
                        Toast.makeText(getContext(), String.format(
                                getString(R.string.dialog_course_type_existed), type),
                                Toast.LENGTH_SHORT).show();
                        return;     // 类型已存在，直接返回
                    }
                }
                mTypeAddedListener.onTypeAdded(type);
                mTypeAdded = true;
                dismiss();  // 关闭对话框
                break;
        }
    }

    // 销毁事件
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (!mTypeAdded) {
            mTypeAddedListener.onTypeNotAdded();    // 调用未添加方法通知调用者
        }
        super.onDismiss(dialog);    // 销毁对话框，需放在最后
    }
}
