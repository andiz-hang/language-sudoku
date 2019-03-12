package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;

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
        mButton.setTextSize(10);
        //mButton.setMinHeight(0);
        //mButton.setMinWidth(0);
        //mButton.setIncludeFontPadding(false);
        mButton.setPadding(0,0,0,0);
        mButton.setBackgroundResource(R.drawable.bg_btn);
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

    public String getText(){
        return mButton.getText().toString();
    }

    private void initializeButton(){

    }

    // Create and return layout parameters for a Sudokucell
    private GridLayout.LayoutParams CreateSudokuCellButtonParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

            // In portrait mode
            if (sScreenHeight > sScreenWidth) {
                layoutParameters.width = sScreenWidth / 11;
                layoutParameters.height = sScreenWidth / 11;

                layoutParameters.setMargins(5,5, 5, 5);
                if (indexI==3 || indexI==6){
                    layoutParameters.setMargins(layoutParameters.leftMargin,15,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }
                if (indexJ==3 || indexJ==6){
                    layoutParameters.setMargins(15,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }

            } else { // In landscape mode
                layoutParameters.width = sScreenHeight / 14;
                layoutParameters.height = sScreenHeight / 14;

                layoutParameters.setMargins(5,5, 5, 5);
                if (indexI==3 || indexI==6){
                    layoutParameters.setMargins(layoutParameters.leftMargin,15,layoutParameters.rightMargin,layoutParameters.bottomMargin);
                }
                if (indexJ==3 || indexJ==6){
                    layoutParameters.setMargins(15,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
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
        }
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }
}

