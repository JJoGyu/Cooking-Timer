package my.android.alarm.cookingtimer.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import my.android.alarm.cookingtimer.R;

public class StopWatchActivity extends AppCompatActivity implements Serializable {

    private static final String EXTRA_HOURS = "com.android.maris.cookingTimer.hours";
    private static final String EXTRA_MINUTES = "com.android.maris.cookingTimer.minutes";

    public static Intent newIntent (Context context)
    {
        Intent intent = new Intent(context, StopWatchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        int mHours = getIntent().getIntExtra(EXTRA_HOURS, 0);
        int mMinutes = getIntent().getIntExtra(EXTRA_MINUTES, 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StopwatchFragment.newInstance(mHours, mMinutes))
                    .commitNow();
        }
    }
}
