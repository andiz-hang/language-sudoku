package com.bignerdranch.android.vocabularysudoku;

import android.widget.Button;

class SudokuCell {
    //scope when you want to make your variable/function visible in all classes that extend current class AND its parent classes
    Button Button;
    private boolean Lock;
    private int Value=0;
    int getValue(){
        return Value;
    }
    void setValue(int value){
        this.Value=value;
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


