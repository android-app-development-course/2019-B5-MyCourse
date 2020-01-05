package com.android.mycourse.reminder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.mycourse.R;

public class SimpleDialog extends Dialog {
    protected static int default_width = WindowManager.LayoutParams.WRAP_CONTENT; // 默认宽度
    protected static int default_height = WindowManager.LayoutParams.WRAP_CONTENT;// 默认高度
    public static int TYPE_TWO_BT = 2;
    public static int TYPE_NO_BT = 0;
    private TextView dialog_title;
    private EditText dialog_message;
    Button bt_cancel, bt_confirm;
    private View customView;
    //	@Bind(R.id.icon)
    private ImageView icon;


    SimpleDialog(Context context, int style) {
        super(context, R.style.FullScreenDialog);
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_simple, null);

        icon = customView.findViewById(R.id.icon);

        LinearLayout ll_button = customView.findViewById(R.id.ll_button);
        dialog_title = customView.findViewById(R.id.dialog_title);
        setTitle("提示信息");
        dialog_message = customView.findViewById(R.id.dialog_message);
        dialog_message.clearFocus();
        bt_confirm = customView.findViewById(R.id.dialog_confirm);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(customView);
        //ButterKnife  view绑定
        //ButterKnife.bind(this,customView);
    }

    void setClickListener(View.OnClickListener listener) {
        bt_confirm.setOnClickListener(listener);
    }

    void setMessage(String message) {
        dialog_message.setText(message);
    }

    public SimpleDialog setTitle(String title) {
        dialog_title.setText(title);
        return this;
    }

    public SimpleDialog setIcon(int iconResId) {
        dialog_title.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(iconResId);

        return this;
    }
}
