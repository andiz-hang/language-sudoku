package com.bignerdranch.android.vocabularysudoku;

import android.widget.Button;

public class SudokuCell {
    //scope when you want to make your variable/function visible in all classes that extend current class AND its parent classes
    protected Button Button;
    private boolean Lock;

    public SudokuCell(){
        Lock = false;
    }
    public void setLock(boolean lock) {
        this.Lock = lock;
    }
    public boolean isLock() {
        return Lock;
    }
}


