package com.ct7ct7ct7.androidvimeoplayer.view;

import android.os.Handler;
import android.util.Log;

public class TimeCounterFullScreen {
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning;
    private int timeInSeconds;
    private static TimeCounterFullScreen instance;
    private DataCallbackFullScreen callback;

    public static TimeCounterFullScreen getInstance() {
        if (instance == null) {
            instance = new TimeCounterFullScreen();
        }
        return instance;
    }


    public void initialize() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timeInSeconds++;
                handler.postDelayed(this, 1000);
                if (timeInSeconds == 30){
                    callback.onDataReceivedFullScreen(timeInSeconds);
                }
            }
        };
    }

    public void startTimer() {
        if (!isRunning) {
            handler.postDelayed(runnable, 1000);
            isRunning = true;
        }
    }

    public void pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    public void resetTimer() {
        timeInSeconds = 0;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Integer oldTime) {
        timeInSeconds = oldTime;
    }

    public void setDataCallbackFullScreen(DataCallbackFullScreen callback) {
        this.callback = callback;
    }

}
