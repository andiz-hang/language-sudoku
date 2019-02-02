package com.bignerdranch.android.vocabularysudoku;

import android.widget.Button;

public class SudokuCell {
    //scope when you want to make your variable/function visible in all classes that extend current class AND its parent classes
    protected Button Button;
    private boolean Lock;
    private int index=0;
    public int getIndex(){
        return index;
    }
    public void setIndex(int value){
        this.index=value;
    }
    SudokuCell(){
        Lock = false;
    }
    void setLock(boolean lock) {
        this.Lock = lock;
    }
    boolean isLock() {
        return Lock;
    }
}


