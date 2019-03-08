package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;

//import static android.support.v4.graphics.drawable.IconCompat.getResources;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sIsMode1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage2;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sSize;

public class GridLayoutUI {
    private GridLayout mLayout;
    private ButtonUI[][] mButtonUIs;


    public GridLayoutUI(GridLayout layout) {
        Log.d("Test", "GridLayoutUI Created");
        mLayout = layout;
        mButtonUIs = new ButtonUI[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                mButtonUIs[i][j] = new ButtonUI();
            }
        }
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

    // Return a button with its text updated
    public Button FillLockedCellByMode(int index, int newValue){
        Button button = getButtonUI(index).getButton();
        if (sIsMode1) button.setText(sLanguage2.getWord(newValue));
        else button.setText(sLanguage1.getWord(newValue));
        button.setTextColor(Color.BLUE);
        return button;
    }
    // Returns a SudokuCell array with possibly conflicting cells highlighted red
    public void SetRowCellsRed(int rowNum) {
        for (int i = 0; i < 9; i++) {
            //if (getButtonUI(cellIndex / 9 * 9 + i).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex / 9 * 9 + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI(rowNum * 9 + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    public void SetColumnCellsRed(int colNum) {
        for (int i = 0; i < 9; i++) {
            //if (getButtonUI(cellIndex % 9 + i * 9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex % 9 + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI(colNum + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    public void SetBoxCellsRed(int boxNum) {
        for (int i = 0; i < 9; i++) {
            //if (getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI((boxNum/3)*27 + (boxNum%3)*3 + i%3 + i/3*9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
}

