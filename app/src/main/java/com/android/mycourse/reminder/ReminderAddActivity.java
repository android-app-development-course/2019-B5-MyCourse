package com.android.mycourse.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.mycourse.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.EventListener;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class ReminderAddActivity extends AppCompatActivity implements EventListener {

    private Toolbar toolbar;
    private int mYear, mMonth, mDay;//当前日期
    private int mHour, mMin;//当前时间
    private FloatingActionButton fab_ok;
    private TextView nv_todo_date, nv_todo_time;
    private EditText nv_todo_title, nv_todo_dsc;
    private Switch nv_repeat;
    private FABProgressCircle fabProgressCircle;
    private String todoDate = null, todoTime = null;
    private String todoTitle, todoDsc;
    private long remindTime;
    private static final String TAG = "time";
    private Todos todos;
    private int isRepeat = 0;
    private int imgId;
    private static int[] imageArray = new int[]{R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,};
    private ImageView new_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_reminder_add);
        initView();
        initEvent();
    }

    // 初始化控件
    private void initView() {
        // 初始化变量
        toolbar = findViewById(R.id.new_toolbar);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);
        fab_ok = findViewById(R.id.fab_ok);
        nv_todo_title = findViewById(R.id.new_todo_title);
        nv_todo_dsc = findViewById(R.id.new_todo_dsc);
        nv_todo_date = findViewById(R.id.new_todo_date);
        nv_todo_time = findViewById(R.id.new_todo_time);
        nv_repeat = findViewById(R.id.repeat);
        fabProgressCircle = findViewById(R.id.fabProgressCircle);
        new_bg = findViewById(R.id.new_background);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    // 初始化事件
    private void initEvent() {
        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoDate == null) {
                    Toasty.info(ReminderAddActivity.this, "没有设置日期",
                            Toast.LENGTH_SHORT, true).show();
                } else if (todoTime == null) {
                    Toasty.info(ReminderAddActivity.this, "没有设置提醒时间",
                            Toast.LENGTH_SHORT, true).show();

                } else {
                    fabProgressCircle.show();
                    todoTitle = nv_todo_title.getText().toString();
                    todoDsc = nv_todo_dsc.getText().toString();
                    Calendar calendarTime = Calendar.getInstance();
                    calendarTime.setTimeInMillis(System.currentTimeMillis());
                    calendarTime.set(Calendar.YEAR, mYear);
                    calendarTime.set(Calendar.MONTH, mMonth);
                    calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
                    calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
                    calendarTime.set(Calendar.MINUTE, mMin);
                    calendarTime.set(Calendar.SECOND, 0);
                    remindTime = calendarTime.getTimeInMillis();
                    Log.i(TAG, "时间是" + remindTime);
                    //插入数据
                    todos = new Todos();
                    todos.setTitle(todoTitle);
                    todos.setDesc(todoDsc);
                    todos.setDate(todoDate);
                    todos.setTime(todoTime);
                    todos.setRemindTime(remindTime);
                    todos.setIsRepeat(isRepeat);
                    todos.setImgId(imgId);
                    new ToDoDao(getApplicationContext()).create(todos);
                    Log.i("DB", "保存到本地数据库成功 ");
                    AlarmManagerUtils.setAlarm(ReminderAddActivity.this,
                            todos.getRemindTime(), 0, todos.getDesc());
                    finish();
                }
            }
        });

        nv_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ReminderAddActivity.this, onDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(true);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.show();

            }
        });

        nv_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ReminderAddActivity.this, onTimeSetListener, mHour, mMin, true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nv_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isRepeat = 1;
                } else {
                    isRepeat = 0;
                }

            }
        });

        Random random = new Random();
        imgId = imageArray[random.nextInt(8)];
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);
        Glide.with(getApplicationContext())
                .load(imgId)
                .apply(options)
                .into(new_bg);
    }


    private void setStatusBar() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 日期选择器对话框监听
     */
    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            todoDate = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
            nv_todo_date.setText(todoDate);
        }
    };

    /**
     * 时间选择对话框监听
     */
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mHour = hour;
            mMin = minute;
            if (minute < 10) {
                todoTime = hour + ":" + "0" + minute;
            } else {
                todoTime = hour + ":" + minute;
            }
            nv_todo_time.setText(todoTime);
        }
    };

}
