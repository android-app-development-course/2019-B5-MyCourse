package com.android.mycourse.reminder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service
{
    public static final String ACTION = "com.android.mycourse.alarm";

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Context context = getApplicationContext();
        Intent clockIntent = new Intent(context, ReminderAlarmActivity.class);
        clockIntent.putExtra(AlarmManagerUtils.ID, intent.getIntExtra(AlarmManagerUtils.ID, 0));
        clockIntent.putExtra(AlarmManagerUtils.TIPS, intent.getStringExtra(AlarmManagerUtils.TIPS));
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(clockIntent);
        return super.onStartCommand(intent, flags, startId);
    }
}
