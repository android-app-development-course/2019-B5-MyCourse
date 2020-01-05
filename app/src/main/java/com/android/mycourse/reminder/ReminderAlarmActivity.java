package com.android.mycourse.reminder;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mycourse.R;

public class ReminderAlarmActivity extends AppCompatActivity {
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_alarm);
        String message = this.getIntent().getStringExtra(AlarmManagerUtils.TIPS);
        showDialogInBroadcastReceiver(message);
    }

    private void showDialogInBroadcastReceiver(String message) {
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(new long[]{100, 10, 100, 600}, 0);
        }
        final SimpleDialog dialog = new SimpleDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle("提醒");
        dialog.setMessage(message);
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v || dialog.bt_cancel == v) {
                    vibrator.cancel();
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }
}
