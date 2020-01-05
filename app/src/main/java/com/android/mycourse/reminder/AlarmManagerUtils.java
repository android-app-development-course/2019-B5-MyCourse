package com.android.mycourse.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

class AlarmManagerUtils {
    static final String ID = "ID";
    static final String TIPS = "TIPS";
    private static final String INTERVAL_MILLIS = "INTERVAL_MILLIS";

    static void setAlarm(Context context, long remindTime, int id, String tips) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = 0;
        Intent intent = new Intent(AlarmService.ACTION);
        intent.putExtra(ID, id);
        intent.putExtra(TIPS, tips);
        intent.putExtra(INTERVAL_MILLIS, intervalMillis);
        PendingIntent sender = PendingIntent.getService(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (am != null) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, remindTime, sender);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(remindTime, sender);
            if (am != null) {
                am.setAlarmClock(alarmClockInfo, sender);
            }
        } else {
            if (am != null) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, remindTime, intervalMillis, sender);// 可能存在不精确的问题
            }
        }
    }
}
