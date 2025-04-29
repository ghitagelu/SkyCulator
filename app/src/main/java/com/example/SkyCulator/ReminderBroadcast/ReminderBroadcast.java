package com.example.SkyCulator.ReminderBroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.SkyCulator.R;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Notification description
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.ic_pie_chart_default_24dp)
                .setContentTitle("NotificationTesting")
                .setContentText("testing this shit")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());
    }
}
