package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.controller.Mode;

import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.mIsPortraitMode;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sGameMode;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage2;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenHeight;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenWidth;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenXDPI;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sScreenYDPI;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sSize;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

public class ButtonUI {
    private Button mButton;
    private int mIndexX;
    private int mIndexY;

    ButtonUI(){

    }

    public ButtonUI(Button button){
        mButton = button;
    }

    public ButtonUI(Button button, GridLayout grid, int x, int y) {
        mButton = button;
        setTextSizeScaled(24);
        mButton.setPadding(0,0,0,0);
        mButton.setBackgroundResource(R.drawable.bg_btn);
        GridLayout.LayoutParams buttonUIParams = createSudokuCellButtonParameters(x,y);
        grid.addView(button,buttonUIParams);
    }

    public Button getButton(){
        return mButton;
    }

    public void setButton(Button button){
        mButton = button;
    }

    // Change the object's property to value over duration frames
    public void animate(String property, Float value, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mButton, property, value);
        animation.setDuration(duration);
        animation.start();
    }

    public void setText(String text) {
        mButton.setText(text);
    }

    // Setup the ui parameters of a popup button
    public void setupPopupButton(int index) {
        if (sGameMode== Mode.PLAY) setText(sLanguage2.getWord(index + 1));
        else setText(sLanguage1.getWord(index + 1));
        if (mIsPortraitMode) {
            setTextSizeScaled(36);
        } else {
            float screenWidthInches = sScreenWidth / sScreenXDPI;
            mButton.setTextSize(screenWidthInches * 4);
            setButtonSize(sScreenWidth / 8, sScreenHeight / ((sSize + 3 + 1) / 2) - 2);
        }
    }

    // setup the ui parameters of a menu button
    public void setupMenuButton() {
        if (mIsPortraitMode) {
            setButtonSize(sScreenWidth / 4, sScreenHeight / 13);
            float screenWidthInches = sScreenWidth / sScreenXDPI;
            mButton.setTextSize(screenWidthInches * 4);
        } else {
            setButtonSize(sScreenWidth / 8, sScreenHeight / ((sSize + 3 + 1) / 2));
            float screenHeightInches = sScreenHeight / sScreenYDPI;
            mButton.setTextSize(screenHeightInches * 4);
        }
    }

    public String getText(){
        return mButton.getText().toString();
    }

    private void setTextSizeScaled(float size) {
        if (mIsPortraitMode) {
            float screenWidthInches = sScreenWidth / sScreenXDPI;
            mButton.setTextSize(screenWidthInches * (size / sSize));

        } else {
            float screenHeightInches = sScreenHeight / sScreenYDPI;
            mButton.setTextSize(screenHeightInches * (size / sSize));
        }
    }

    private void setButtonSize(int width, int height) {
        mButton.setWidth(width);
        mButton.setHeight(height);
    }

    // Create and return layout parameters for a Sudokucell
    private GridLayout.LayoutParams createSudokuCellButtonParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

        // In portrait mode
        if (mIsPortraitMode) {
            layoutParameters.width = (int)(sScreenWidth / (sSize) * 0.85);
            layoutParameters.height = (int)(sScreenWidth / (sSize) * 0.85);
        } else { // In landscape mode
            layoutParameters.width = (int)(sScreenHeight / (sSize) * 0.85);
            layoutParameters.height = (int)(sScreenHeight / (sSize) * 0.85);
        }
        layoutParameters.setMargins(3,3, 3, 3);
        if (indexI % (int)floor(sqrt(sSize)) == 0) {
            layoutParameters.setMargins(layoutParameters.leftMargin,10,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }
        if (indexJ % (int)ceil(sqrt(sSize)) == 0) {
            layoutParameters.setMargins(10,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }
//        if (indexI==3 || indexI==6){
//            layoutParameters.setMargins(layoutParameters.leftMargin,15,layoutParameters.rightMargin,layoutParameters.bottomMargin);
//        }
//        if (indexJ==3 || indexJ==6){
//            layoutParameters.setMargins(15,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
//        }

        return layoutParameters;
    }

    // Creates and returns layout parameters for a Popup button
    public GridLayout.LayoutParams createPopupButtonParameters(){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        if (mIsPortraitMode) {
            if (sSize == 4) {
                layoutParameters.width = sScreenWidth * 3 / 4 / 2;
                layoutParameters.height = sScreenHeight * 3 / 13 / 2;
            } else {
                layoutParameters.width = sScreenWidth * 3 / 4 / (sSize / 3);
                layoutParameters.height = sScreenHeight / 13;
            }
        } else {
            layoutParameters.width = sScreenWidth / 8;
            layoutParameters.height = sScreenHeight / 6;
        }
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }
}

