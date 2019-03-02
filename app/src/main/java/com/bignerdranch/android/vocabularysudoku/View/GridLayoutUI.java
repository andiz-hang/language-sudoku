package com.bignerdranch.android.vocabularysudoku.View;

import android.animation.ObjectAnimator;
import android.text.Layout;
import android.view.View;
import android.widget.GridLayout;

public class GridLayoutUI {
    private GridLayout mLayout;

    public GridLayoutUI(GridLayout layout) {
        mLayout = layout;
    }

    // Change the object's property to value over duration frames
    public void Animate(String property, Float value, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mLayout, property, value);
        animation.setDuration(duration);
        animation.start();
    }

    public GridLayout getLayout(){
        return mLayout;
    }
}
