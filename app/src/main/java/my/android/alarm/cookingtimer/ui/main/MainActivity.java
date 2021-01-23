package my.android.alarm.cookingtimer.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import my.android.alarm.cookingtimer.R;

public class MainActivity extends AppCompatActivity {

    public static Intent newIntent (Context context)
    {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        if (savedInstanceState == null) {
            if(SharedPreferences.getAlarmOnState(getApplicationContext()) == false) {
                SharedPreferences.setIfSoundIsMute(getApplicationContext(), false);
            }
            else
            {
                SharedPreferences.setAlarmOn(getApplicationContext(), false);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

}
