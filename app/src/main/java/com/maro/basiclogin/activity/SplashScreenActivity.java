package com.maro.basiclogin.activity;

import android.content.Intent;
import android.os.Bundle;

import com.maro.basiclogin.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Splash Screen Activity
 */
public class SplashScreenActivity extends AbstractActivity {

    // Set period for using (10 seconds to be sure that it will be execute only one time
    private static final int TIMER_PERIOD = 10000;
    // Delay before starting the timerTask the first time in ms
    private static final int TIMER_DELAY = 2500;
    private Timer updateProgressTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Wait 2.5 seconds then launch the second screen
        updateProgressTimer = new Timer();
        updateProgressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // If the user is logged in, skip the Login screen
                        Intent i = new Intent(getApplicationContext(), session.isUserLoggedIn()? MainActivity.class : LoginActivity.class );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        updateProgressTimer.cancel();
                        updateProgressTimer.purge();
                        mActivity.finish();
                    }
                });
            }
        }, TIMER_DELAY, TIMER_PERIOD);
    }
}
