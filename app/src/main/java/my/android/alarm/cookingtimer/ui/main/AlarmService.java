package my.android.alarm.cookingtimer.ui.main;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import my.android.alarm.cookingtimer.R;

public class AlarmService extends Service {

    private NotificationManager mNM;
    private static final int DELAY_INTERVAL = 1000 * 60;
    public static final int FOREGROUND_ID = 1976;

    public class AlarmBinder extends Binder
    {
        AlarmService getService()
        {
            return AlarmService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= 26) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmService.class), 0);

        String NOTIFICATION_CHANNEL_ID = "my.android.maris.CookingTimer";
        String channelName = getString(R.string.channel_name);
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setDescription("<Notification channel for posting information about active service of Cooking Timer application>");
        chan.enableLights(true);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Cooking Timer")
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentText("Cooking Timer Service Running")
                .setContentText("Cooking Timer is running")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .build();

            startForeground(FOREGROUND_ID, notification);
        }else{
            startForeground(FOREGROUND_ID, new Notification());
        }

        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                SharedPreferences.getLastLength(getApplicationContext()) * DELAY_INTERVAL, pi);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //not in use
    public static void setServiceAlarm (Context context)
    {
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                SharedPreferences.getLastLength(context) * DELAY_INTERVAL, pi);
    }
    //not in use end

    public static void stopServiceAlarm (Context context)
    {
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        context.stopService(new Intent(context, AlarmService.class));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

}
