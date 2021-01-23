package my.android.alarm.cookingtimer.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import my.android.alarm.cookingtimer.R;

public class MainFragment extends Fragment {

    int JOB_ID = 1000;
    private MainViewModel mViewModel;
    private TextView mMessage;
    private TextView mTimeTextView;
    private Button mAddHourButton;
    private Button mAdd10MinutesButton;
    private Button mAdd1MinuteButton;
    private Button mStartButton;
    private Button mClearButton;
    private int mTime = 0;
    private int mHours = 0;
    private int mMinutes = 0;
    Time time = new Time();
    private static final String EXTRA_HOURS = "com.android.maris.cookingTimer.hours";
    private static final String EXTRA_MINUTES = "com.android.maris.cookingTimer.minutes";

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);

        mMessage = (TextView) v.findViewById(R.id.message);

        mTimeTextView = (TextView) v.findViewById(R.id.time);

        mAddHourButton = (Button) v.findViewById(R.id.button_add_hour);
        mAddHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime += 60;
                time.setTime(mTime);
                updateTime();
            }
        });

        mAdd10MinutesButton = (Button) v.findViewById(R.id.button_add_10min);
        mAdd10MinutesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime +=10;
                time.setTime(mTime);
                updateTime();
            }
        });

        mAdd1MinuteButton = (Button) v.findViewById(R.id.button_add_1min);
        mAdd1MinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime += 1;
                time.setTime(mTime);
                updateTime();
            }
        });

        mStartButton = (Button) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().startForegroundService(new Intent(getActivity(), AlarmService.class));
                }
                else {
                    getActivity().startService(new Intent(getActivity(), AlarmService.class));
                }
                SharedPreferences.setAlarmOn(getContext(), true);

                Intent i = new Intent(getActivity(), StopWatchActivity.class);
                i.putExtra(EXTRA_HOURS, mHours);
                i.putExtra(EXTRA_MINUTES, mMinutes);
                startActivity(i);

            }
        });

        mClearButton = (Button) v.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime = 0;
                mHours = 0;
                mMinutes = 0;
                updateTime();
            }
        });

        return v;
    }

    public void updateTime ()
    {
        if(mTime >= 60)
        {
            mHours = mTime / 60;
            mMinutes = mTime - (60 * mHours);
            mTimeTextView.setText(mHours + " h " + mMinutes + " min");
        }
        else {
            if(mTime == 0)
            {
                mTimeTextView.setText(mMinutes + " min");
            }
            else {
                    mMinutes = mTime - (60 * mHours);
                    mTimeTextView.setText(mMinutes + " min");
            }
        }
        SharedPreferences.setLastLength(getContext(), mTime);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

}
