package my.android.alarm.cookingtimer.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import java.util.Timer;

import my.android.alarm.cookingtimer.R;

public class AlarmReceiver extends BroadcastReceiver {
    
    private int repeat = 0;
    private boolean isStreamSilent = false;
    private boolean isSilentModeOn = false;
    int play = 0;
    Context context;

    @Override
    public void onReceive (final Context context, Intent intent)
    {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioManager audioManager1 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if ((audioManager1.isStreamMute(AudioManager.STREAM_NOTIFICATION)) || (audioManager1.isStreamMute(AudioManager.STREAM_RING))) {

                SharedPreferences.setIfSoundIsMute(context, true);
                isStreamSilent = true;

                if ((audioManager1.getRingerMode() == AudioManager.RINGER_MODE_SILENT)) {
                    isSilentModeOn = true;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    audioManager1.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
                    //audioManager1.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
                    //audioManager1.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
                    audioManager1.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                    audioManager1.setStreamVolume(AudioManager.STREAM_RING, (audioManager1.getStreamMaxVolume(AudioManager.STREAM_RING) * 2 / 3), 0);
                    audioManager1.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
                } else {
                    audioManager1.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    //audioManager1.setStreamMute(AudioManager.STREAM_ALARM, false);
                    //audioManager1.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    audioManager1.setStreamMute(AudioManager.STREAM_RING, false);
                    audioManager1.setStreamVolume(AudioManager.STREAM_RING, (audioManager1.getStreamMaxVolume(AudioManager.STREAM_RING) * 2 / 3), 0);
                    audioManager1.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                }

            }
        }

        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.not4);
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);

        try {
            mp.setDataSource(context, uri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(play < 2)
                    {
                        mp.start();
                        play++;
                    }
                    else
                    {
                        mp.release();
                    }
                }
            });
        } catch (Exception e) {
        }

        final Timer mTimer = new Timer();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isStreamSilent == true) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        AudioManager alarmManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                            //alarmManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
                            //alarmManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                            alarmManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                            alarmManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
                        } else {
                            alarmManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                            //alarmManager.setStreamMute(AudioManager.STREAM_ALARM, true);
                            //alarmManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                            alarmManager.setStreamMute(AudioManager.STREAM_RING, true);
                            alarmManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                        }
                        SharedPreferences.setAlarmOn(context, false);
                    }
                }, 10000);
            }
        }
        context.stopService(new Intent(context, AlarmService.class));

    }

}
