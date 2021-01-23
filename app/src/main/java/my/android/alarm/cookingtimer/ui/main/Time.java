package my.android.alarm.cookingtimer.ui.main;

import java.io.Serializable;

public class Time implements Serializable {

    private int hour;
    private int min;
    private int time;

    public Time ()
    {
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
