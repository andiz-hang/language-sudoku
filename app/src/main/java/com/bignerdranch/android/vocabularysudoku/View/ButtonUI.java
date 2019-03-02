package com.bignerdranch.android.vocabularysudoku.View;

import android.animation.ObjectAnimator;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class ButtonUI {
    private Button mButton;
    private int mIndexX;
    private int mIndexY;

    public ButtonUI(Button button, GridLayout grid, int x, int y) {

        mButton = button;
        GridLayout.LayoutParams buttonUIParams = CreateSudokuCellParameters(x,y);
        grid.addView(button,buttonUIParams);
    }

    // Change the object's property to value over duration frames
    public void Animate(String property, Float value, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mButton, property, value);
        animation.setDuration(duration);
        animation.start();
    }

    public void setText(String text) {
        mButton.setText(text);
    }

    private void initializeButton(){

    }

    // Create and return layout parameters for a sudokucell
    GridLayout.LayoutParams CreateSudokuCellParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

        layoutParameters.width = mScreenWidth/13;
        layoutParameters.height = mScreenWidth/13;
        layoutParameters.setMargins(mScreenWidth / 72,mScreenHeight / 128,mScreenWidth / 72,mScreenHeight / 128);
        if (indexI==3 || indexI==6){
            layoutParameters.setMargins(layoutParameters.leftMargin,mScreenHeight / 77,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }
        if (indexJ==3 || indexJ==6){
            layoutParameters.setMargins(mScreenWidth / 43,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }

        return layoutParameters;
    }
}
