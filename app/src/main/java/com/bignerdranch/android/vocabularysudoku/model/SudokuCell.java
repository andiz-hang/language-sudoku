package com.bignerdranch.android.vocabularysudoku.model;


public class SudokuCell {
    //scope when you want to make your variable/function visible in all classes that extend current class AND its parent classes
    //private Button mButton;
    private boolean mLock = false;
    private int mValue = 0;
    private boolean mConflicting = false;

    public SudokuCell(){
        mValue = 0;
    }

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

    public void setConflicting(boolean value){
        mConflicting = value;
    }

    public boolean isConflicting(){
        return mConflicting;
    }

//    public Button getButton() {
//        return mButton;
//    }
//
//    public void setButton(Button newButton) {
//        mButton = newButton;
//    }
}
