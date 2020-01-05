package com.android.mycourse.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.mycourse.R;

import java.util.Calendar;

public class ReminderEditActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;//当前日期
    private int mHour, mMin;//当前时间
    private TextView et_todo_title, et_todo_dsc, et_todo_date, et_todo_time;
    private Button ok, cancel;
    private String todoTitle, todoDsc, todoDate, todoTime;
    private String n_todoTitle, n_todoDsc;
    private String n_todoDate, n_todoTime;
    private TodoDatabase dbHelper;
    private long n_remindTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_edit);
        initView();
        initEvent();
    }

    private void initView() {
//        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMin = ca.get(Calendar.MINUTE);
        ok = findViewById(R.id.bt_edit_ok);
        cancel = findViewById(R.id.bt_edit_cancel);
        et_todo_title = findViewById(R.id.edit_todo_title);
        et_todo_dsc = findViewById(R.id.edit_todo_dsc);
        et_todo_date = findViewById(R.id.edit_todo_date);
        et_todo_time = findViewById(R.id.edit_todo_time);
        todoTitle = getIntent().getStringExtra("title");
        todoDsc = getIntent().getStringExtra("dsc");
        todoDate = getIntent().getStringExtra("date");
        todoTime = getIntent().getStringExtra("time");

//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.reminder_edit_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initEvent() {
        et_todo_title.setText(todoTitle);
        et_todo_dsc.setText(todoDsc);
        et_todo_date.setText(todoDate);
        et_todo_time.setText(todoTime);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                n_todoTitle = et_todo_title.getText().toString();
                n_todoDsc = et_todo_dsc.getText().toString();
                n_todoDate = et_todo_date.getText().toString();
                n_todoTime = et_todo_time.getText().toString();
                dbHelper = new TodoDatabase(ReminderEditActivity.this,
                        "Data.db", null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.setTimeInMillis(System.currentTimeMillis());
                calendarTime.set(Calendar.YEAR, mYear);
                calendarTime.set(Calendar.MONTH, mMonth);
                calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
                calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
                calendarTime.set(Calendar.MINUTE, mMin);
                calendarTime.set(Calendar.SECOND, 0);
                n_remindTime = calendarTime.getTimeInMillis();
                ContentValues values = new ContentValues();
                //更新数据库
                values.put("todotitle", n_todoTitle);
                values.put("tododsc", n_todoDsc);
                values.put("tododate", n_todoDate);
                values.put("todotime", n_todoTime);
                values.put("remindTime", n_remindTime);
//                values.put("isAlerted", 0);
                db.update("Todo", values, "todotitle = ?", new String[]{todoTitle});
//                Intent intent = new Intent(ReminderEditActivity.this, ReminderActivity.class);
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ReminderEditActivity.this, onDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(true);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.show();
            }
        });

        et_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ReminderEditActivity.this, onTimeSetListener, mHour, mMin, true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
            }
        });
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
            n_todoDate = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
            et_todo_date.setText(n_todoDate);
        }
    };

    /**
     * 时间选择对话框监听
     */
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            if (minute < 10) {
                n_todoTime = hour + ":" + "0" + minute;
            } else {
                n_todoTime = hour + ":" + minute;
            }
            et_todo_time.setText(n_todoTime);
        }
    };

}
