package com.bignerdranch.android.vocabularysudoku.model;

import android.os.SystemClock;
import android.widget.Chronometer;

import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.mIsPortraitMode;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenHeight;

public class Timer {
    Chronometer mTimer;

    public Timer(Chronometer chronometer) {
        mTimer = chronometer;
        if (mIsPortraitMode) {
            mTimer.setTranslationY(sScreenHeight * (float)1 / 12);
        }
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

    // Returns the time on the timer in milliseconds
    public long stopTimer() {
        mTimer.stop();
        return SystemClock.elapsedRealtime() - mTimer.getBase();
    }
}
