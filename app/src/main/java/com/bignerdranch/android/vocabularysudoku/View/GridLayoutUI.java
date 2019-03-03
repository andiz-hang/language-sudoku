package com.bignerdranch.android.vocabularysudoku.View;

import android.animation.ObjectAnimator;
import android.text.Layout;
import android.view.View;
import android.widget.GridLayout;

import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sSize;

public class GridLayoutUI {
    private GridLayout mLayout;
    private ButtonUI[][] mButtonUIs;

    public GridLayoutUI(GridLayout layout) {
        mLayout = layout;
    }

    // Change the object's property to value over duration frames
    public void Animate(String property, Float value, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mLayout, property, value);
        animation.setDuration(duration);
        animation.start();
    }

    public ButtonUI[][] getButtonUIs(){
        return mButtonUIs;
    }

    public void addButtonUI(ButtonUI button, int index){
        int y = index / sSize;
        int x = index % sSize;
        mButtonUIs[y][x] = button;
    }

    public void addButtonUI(ButtonUI button, int yIndex, int xIndex){
        mButtonUIs[yIndex][xIndex] = button;
    }

    public ButtonUI getButtonUI(int index){
        int y = index / sSize;
        int x = index % sSize;
        return mButtonUIs[y][x];
    }
    public ButtonUI getButtonUI(int yIndex, int xIndex){
        return mButtonUIs[yIndex][xIndex];
    }

    public GridLayout getLayout(){
        return mLayout;
    }
}
