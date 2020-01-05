package com.android.mycourse.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

import com.android.mycourse.R;

import java.util.ArrayList;
import java.util.List;

public class WeekDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_TITLE = "title";                // 对话框标题
    private static final String ARG_MAX_WEEK = "maxWeek";           // 最大周数
    private static final String ARG_OLD_WEEK = "oldWeek";           // 已选周数
    private static final int MAX_WEEK = 20;                         // 默认最大周数
    private static final int MAX_WEEK_ALLOWED = 30;                 // 允许输入的最大周数

    private String mTitle;          // 参数值对话框标题
    private int mMaxWeek;           // 参数值最大周数
    private String mOldWeek;        // 参数值已选周数

    private EditText etMaxWeek;     // 最大周数输入框
    private CheckBox cbAllWeek;     // 全部周数选择框
    private CheckBox cbOddWeek;     // 单周周数选择框
    private CheckBox cbEvenWeek;    // 双周周数选择框
    private GridLayout layoutWeek;  // 周数布局
    private List<CheckBox> cbWeeks; // 周数选择框
    private Button btnCancel;       // 取消按钮
    private Button btnOK;           // 确定按钮

    private WeekSelectedListener mWeekSelectedListener;     // 监听器

    /**
     * 监听器接口
     * 点击确定时调用方法向调用者传送参数值
     * 调用者通过重写方法从参数获取参数值
     */
    interface WeekSelectedListener {
        void onWeekSelected(String newWeek);
    }

    /**
     * 设置监听器，在调用者中使用
     *
     * @param weekSelectedListener 监听器
     */
    void setWeekSelectedListener(WeekSelectedListener weekSelectedListener) {
        mWeekSelectedListener = weekSelectedListener;
    }

    /**
     * 创建对话框实例
     *
     * @param title   对话框标题
     * @param maxWeek 最大周数，用于创建相应数量的周数选项
     * @param oldWeek 已选周数，用于选择上一次的周数选项
     * @return 对话框实例
     */
    static WeekDialogFragment newInstance(String title, int maxWeek, String oldWeek) {
        WeekDialogFragment fragment = new WeekDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putInt(ARG_MAX_WEEK, maxWeek);
        bundle.putString(ARG_OLD_WEEK, oldWeek);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_course_week, null);
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
        mTitle = bundle != null ? bundle.getString(ARG_TITLE) : "";         // 对话框标题
        mMaxWeek = bundle != null ? bundle.getInt(ARG_MAX_WEEK) : MAX_WEEK; // 最大周数
        mOldWeek = bundle != null ? bundle.getString(ARG_OLD_WEEK) : "";    // 之前已选周数

        etMaxWeek = view.findViewById(R.id.et_max_week);
        cbAllWeek = view.findViewById(R.id.cb_week_all);
        cbOddWeek = view.findViewById(R.id.cb_week_odd);
        cbEvenWeek = view.findViewById(R.id.cb_week_even);
        layoutWeek = view.findViewById(R.id.layout_week_number);
        btnCancel = view.findViewById(R.id.btn_dialog_course_week_cancel);
        btnOK = view.findViewById(R.id.btn_dialog_course_week_ok);
        cbWeeks = new ArrayList<>();

        ((TextView) view.findViewById(R.id.tv_name)).setText(mTitle);  // 设置标题
        // 监听输入的最大周数
        etMaxWeek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {   // 动态改变周数选项
                int value;
                // 转换输入值
                try {
                    value = Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    return;     // 输入值无效时不改变周数选项
                }
                // 检查输入的最大周数是否合法
                if (value < 1 || value > MAX_WEEK_ALLOWED) {
                    Toast.makeText(getContext(),
                            String.format(getString(R.string.course_week_overflow), MAX_WEEK_ALLOWED),
                            Toast.LENGTH_SHORT).show();     // 弹出提示
                    etMaxWeek.setText(String.valueOf(mMaxWeek));     // 重置最大周数
                    etMaxWeek.setSelection(etMaxWeek.getText().length());   // 光标置末
                }
                // 清除原来的周数选项
                cbAllWeek.setChecked(false);
                cbOddWeek.setChecked(false);
                cbEvenWeek.setChecked(false);
                cbWeeks.clear();
                layoutWeek.removeAllViews();
                // 创建周数选项
                for (int i = 1; i <= Integer.parseInt(etMaxWeek.getText().toString()); i++) {
                    CheckBox cbWeek = new CheckBox(getContext());
                    cbWeek.setText(String.valueOf(i));
                    cbWeeks.add(cbWeek);
                    GridLayout.LayoutParams lpWeek = new GridLayout.LayoutParams();
                    lpWeek.width = 0;  // 均分列宽
                    lpWeek.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // 等列宽权重
                    layoutWeek.addView(cbWeek, lpWeek);
                }
            } // afterTextChanged end
        }); // etMaxWeek.addTextChangedListener end
        etMaxWeek.setText(String.valueOf(mMaxWeek)); // 初始化最大周数值，放在监听器之后可立即触发事件
        etMaxWeek.setSelection(etMaxWeek.getText().length());   // 光标置末

        // 选中已选周数选项
        if (!mOldWeek.equals("")) {
            try {
                String[] weeks = mOldWeek.split(",");   // 分割非连续周数
                int numberChecked = 0;      // 已选周数数
                boolean isOdd = true;       // 是否全为单周周数
                boolean isEven = true;      // 是否全为双周周数
                for (String week : weeks) {
                    String[] numbers = week.split("-"); // 分割连续周数
                    if (numbers.length == 1) {      // 分割失败，处理单个周数
                        cbWeeks.get(Integer.parseInt(numbers[0]) - 1).setChecked(true);
                        numberChecked += 1;
                        if (Integer.parseInt(numbers[0]) % 2 == 0) {
                            isOdd = false;  // 含双周周数，不选中单周
                        } else {
                            isEven = false; // 含单周周数，不选中双周
                        }
                    } else {        // 分割成功，处理起止周数
                        for (int i = Integer.parseInt(numbers[0]) - 1; i < Integer.parseInt(numbers[1]); i++) {
                            cbWeeks.get(i).setChecked(true);
                            numberChecked += 1;
                            isOdd = isEven = false;
                        }
                    }
                }
                cbAllWeek.setChecked(numberChecked == cbWeeks.size());                  // 选中全部
                cbOddWeek.setChecked(numberChecked == cbWeeks.size() / 2 && isOdd);     // 选中单周
                cbEvenWeek.setChecked(numberChecked == cbWeeks.size() / 2 && isEven);   // 选中双周
            } catch (Exception ignored) {   // 若传入的已选周数字符串不合法，则不做相应的初始化
            }
        } // if end
        cbAllWeek.setOnClickListener(this);
        cbOddWeek.setOnClickListener(this);
        cbEvenWeek.setOnClickListener(this);
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
            // 选择全部周数
            case R.id.cb_week_all:
                cbOddWeek.setChecked(false);
                cbEvenWeek.setChecked(false);
                for (CheckBox cbWeek : cbWeeks) {
                    cbWeek.setChecked(cbAllWeek.isChecked());
                }
                break;
            // 选择单周周数
            case R.id.cb_week_odd:
                cbAllWeek.setChecked(false);
                cbEvenWeek.setChecked(false);
                for (CheckBox cbWeek : cbWeeks) {
                    cbWeek.setChecked(cbOddWeek.isChecked() &&
                            Integer.parseInt(cbWeek.getText().toString()) % 2 != 0);
                }
                break;
            // 选择双周周数
            case R.id.cb_week_even:
                cbAllWeek.setChecked(false);
                cbOddWeek.setChecked(false);
                for (CheckBox cbWeek : cbWeeks) {
                    cbWeek.setChecked(cbEvenWeek.isChecked() &&
                            Integer.parseInt(cbWeek.getText().toString()) % 2 == 0);
                }
                break;
            // 取消
            case R.id.btn_dialog_course_week_cancel:
                dismiss();      // 关闭对话框
                break;
            // 确定
            case R.id.btn_dialog_course_week_ok:
                // TODO: 检查选择合法性

                int start = -1, end = -1;   // 子起止周数
                StringBuilder newWeek = new StringBuilder();      // 选择的周数
                for (CheckBox cbWeek : cbWeeks) {
                    if (cbWeek.isChecked()) {   // 当前周数被选择，先判断是否插入分隔符，然后修改新的子起止周数
                        // 如果已插入一条子起止周数数据并且上一个周数未选择，则添加逗号作为分隔符
                        newWeek.append(newWeek.length() > 0 && start == -1 ? "," : "");
                        start = start == -1 ? Integer.parseInt(cbWeek.getText().toString()) : start;
                        end = Integer.parseInt(cbWeek.getText().toString());
                    } else {    // 当前周数未选择，则将上一条子起止周数数据插入（如果有）
                        if (end != -1) {    // 插入上一条子起止周数数据
                            newWeek.append(start == end ? "" + end : start + "-" + end);
                        }
                        start = end = -1;   // 修改子起止周数，避免重复插入数据
                    }
                }
                if (end != -1) {    // 当最后一个周数被选择时，需从此处添加最后一条子起止周数数据
                    newWeek.append(start == end ? "" + end : start + "-" + end);
                }
                mWeekSelectedListener.onWeekSelected(newWeek.toString());     // 传回选择的周数
                dismiss();      // 关闭对话框
                break;
        }
    }
}
