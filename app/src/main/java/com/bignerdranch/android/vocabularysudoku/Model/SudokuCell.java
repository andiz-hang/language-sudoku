package com.bignerdranch.android.vocabularysudoku.Model;

import android.widget.Button;

public class SudokuCell {
    //scope when you want to make your variable/function visible in all classes that extend current class AND its parent classes
    private Button mButton;
    private boolean mLock = false;
    private int mValue = 0;

    public void setValue(int value) {
        this.mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public void setLock(boolean lock) {
        this.mLock = lock;
    }

    public boolean isLock() {
        return mLock;
    }

    public Button getButton() {
        return mButton;
    }

    public void setButton(Button newButton) {
        mButton = newButton;
    }
}
