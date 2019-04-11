package com.bignerdranch.android.vocabularysudoku.model;

import android.os.SystemClock;
import android.widget.Chronometer;

public class Timer {
    Chronometer mTimer;

    public Timer(Chronometer chronometer) {
        mTimer = chronometer;
    }

    public void startTimer(long lastPaused) {
        if (lastPaused == 0) {
            mTimer.setBase(SystemClock.elapsedRealtime());
        } else {
            mTimer.setBase(mTimer.getBase() + SystemClock.elapsedRealtime() - lastPaused);
        }
        mTimer.start();
    }

    // Returns the elapsed time of the system clock
    public long pauseTimer() {
        mTimer.stop();
        return SystemClock.elapsedRealtime();
    }
}
