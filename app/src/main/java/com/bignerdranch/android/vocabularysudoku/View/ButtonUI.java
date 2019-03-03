package com.bignerdranch.android.vocabularysudoku.View;

import android.animation.ObjectAnimator;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sScreenHeight;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sScreenWidth;

public class ButtonUI {
    private Button mButton;
    private int mIndexX;
    private int mIndexY;

    public ButtonUI(Button button){
        Log.d("Test", "ButtonUI Created");
        mButton = button;
    }

    public ButtonUI(Button button, GridLayout grid, int x, int y) {
        Log.d("Test", "ButtonUI Created");
        mButton = button;
        GridLayout.LayoutParams buttonUIParams = CreateSudokuCellButtonParameters(x,y);
        grid.addView(button,buttonUIParams);
    }

    public Button getButton(){
        return mButton;
    }
    public void setButton(Button button){
        mButton = button;
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

    // Create and return layout parameters for a Sudokucell
    GridLayout.LayoutParams CreateSudokuCellButtonParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

        layoutParameters.width = sScreenWidth/13;
        layoutParameters.height = sScreenWidth/13;
        layoutParameters.setMargins(sScreenWidth / 72,sScreenHeight / 128,sScreenWidth / 72,sScreenHeight / 128);
        if (indexI==3 || indexI==6){
            layoutParameters.setMargins(layoutParameters.leftMargin,sScreenHeight / 77,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }
        if (indexJ==3 || indexJ==6){
            layoutParameters.setMargins(sScreenWidth / 43,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }

        return layoutParameters;
    }
    // Creates and returns layout parameters for a Popup button
    public GridLayout.LayoutParams CreatePopUpButtonParameters(){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        layoutParameters.width = sScreenWidth/4;
        layoutParameters.height = sScreenHeight/13;
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }
}
