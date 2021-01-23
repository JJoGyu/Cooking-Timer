package my.android.alarm.cookingtimer.ui.main;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferences {

    private static final String PREF_TIME_LENGTH = "timeLength";
    public static final String PREF_IS_SOUND_MUTE = "soundMode";
    public static final String PREF_IS_ALARM_ON = "alarmOn";

    public static int getLastLength (Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_TIME_LENGTH, 0);
    }

    public static void setLastLength (Context context, int lastLength)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_TIME_LENGTH, lastLength)
                .apply();
    }

    public static boolean checkIsSoundMute(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_SOUND_MUTE, false);
    }

    public static void setIfSoundIsMute(Context context, boolean result)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_SOUND_MUTE, result)
                .apply();
    }

    public static boolean getAlarmOnState(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean result)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, result)
                .apply();
    }
}
