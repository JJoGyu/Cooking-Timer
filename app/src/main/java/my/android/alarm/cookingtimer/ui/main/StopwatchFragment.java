package my.android.alarm.cookingtimer.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.concurrent.TimeUnit;
import my.android.alarm.cookingtimer.R;

public class StopwatchFragment extends Fragment {

    private TextView mTextView1;
    private TextView mTextView2;
    private Button mButton;
    private static final String ARG_HOURS = "hours";
    private static final String ARG_MINUTES = "minutes";
    private static final String SAVED_MILLS = "sMills";
    int mHours;
    int mMinutes;
    int mMinutesForStopwatch;
    int mMillsUntilFinished = 0;
    private AdView mAdView;
    public SharedPreferences timePreferences;
    CountDownTimer countDownTimer;

    public StopwatchFragment() {
    }

    public static StopwatchFragment newInstance(int hours, int minutes) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_HOURS, hours);
        args.putSerializable(ARG_MINUTES, minutes);
        StopwatchFragment fragment = new StopwatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        timePreferences = new SharedPreferences();
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMillsUntilFinished = savedInstanceState.getInt(SAVED_MILLS);
        }
        else
        {
            mHours = getArguments().getInt(ARG_HOURS);
            mMinutes = getArguments().getInt(ARG_MINUTES);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle saveInstanceState)
    {
        View v = inflater.inflate(R.layout.stopwatch_fragment, container, false);

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(mMillsUntilFinished == 0)
        {
            mMinutesForStopwatch = mMinutes * 60000 + mHours * 60 * 60000;
        }
        else
        {
            mMinutesForStopwatch = mMillsUntilFinished;
        }

        mTextView1 = (TextView) v.findViewById(R.id.textView);
        mTextView1.setText("Cooking...\nAlarm will get on in:");

        mTextView2 = (TextView) v.findViewById(R.id.textView2);
        countDownTimer = new CountDownTimer(mMinutesForStopwatch, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillsUntilFinished = (int) millisUntilFinished;
                if(TimeUnit.MILLISECONDS.toHours(millisUntilFinished) > 0 &&
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) > 0))
                {
                    mTextView2.setText("" + String.format("%d h, %d min, %d sec",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    int timeToSet = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    timePreferences.setLastLength(getContext(), timeToSet);
                }
                else if(TimeUnit.MILLISECONDS.toHours(millisUntilFinished) <= 0 &&
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) > 0))
                {
                    mTextView2.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    int timeToSet = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    timePreferences.setLastLength(getContext(), timeToSet);
                }
                else if(TimeUnit.MILLISECONDS.toHours(millisUntilFinished) <= 0 &&
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) <= 0))
                {
                    mTextView2.setText("" + String.format("%d sec",
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
            }
            @Override
            public void onFinish() {
                mTextView2.setText("Done");
                try {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences.setAlarmOn(getContext(), false);

                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }


        }.start();


        mButton = (Button) v.findViewById(R.id.stopwatch_button);
        mButton.setText("Stop");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmService.stopServiceAlarm(getContext());
                getContext().stopService(new Intent(getContext(), AlarmService.class));
                try {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences.setAlarmOn(getContext(), false);

                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SAVED_MILLS, mMillsUntilFinished - 1000);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        countDownTimer.cancel();

    }

}
