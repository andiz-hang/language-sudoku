package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenHeight;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenWidth;

public class ButtonUI {
    private Button mButton;
    private int mIndexX;
    private int mIndexY;

    public ButtonUI(){

    }

    public ButtonUI(Button button){
        mButton = button;
    }

    public ButtonUI(Button button, GridLayout grid, int x, int y) {
        mButton = button;
        mButton.setTextSize(12);
        //mButton.setMinHeight(0);
        //mButton.setMinWidth(0);
        //mButton.setIncludeFontPadding(false);
        mButton.setPadding(0,0,0,0);
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
    private GridLayout.LayoutParams CreateSudokuCellButtonParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

            // In portrait mode
            if (sScreenHeight > sScreenWidth) {
                layoutParameters.width = sScreenWidth / 10;
                layoutParameters.height = sScreenWidth / 10;

                layoutParameters.setMargins(0,0, 0, 0);
                if (indexI==3 || indexI==6){
                    layoutParameters.setMargins(layoutParameters.leftMargin,sScreenHeight / 77,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }
                if (indexJ==3 || indexJ==6){
                    layoutParameters.setMargins(sScreenWidth / 43,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }

            } else { // In landscape mode
                layoutParameters.width = sScreenHeight / 11;
                layoutParameters.height = sScreenHeight / 11;

                layoutParameters.setMargins(0,0,0,0);
                if (indexI==3 || indexI==6){
                    layoutParameters.setMargins(layoutParameters.leftMargin,sScreenHeight / 200,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }
                if (indexJ==3 || indexJ==6){
                    layoutParameters.setMargins(sScreenWidth / 75,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }
            }

        return layoutParameters;
    }
    // Creates and returns layout parameters for a Popup button
    public GridLayout.LayoutParams CreatePopUpButtonParameters(){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        if (sScreenHeight > sScreenWidth) {
            layoutParameters.width = sScreenWidth / 4;
            layoutParameters.height = sScreenHeight / 13;
        } else {
            layoutParameters.width = sScreenWidth / 10;
            layoutParameters.height = sScreenHeight / 13;
        }
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }
}

