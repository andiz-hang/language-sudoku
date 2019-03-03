package com.bignerdranch.android.vocabularysudoku.View;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;

import static android.support.v4.graphics.drawable.IconCompat.getResources;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sIsMode1;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sLanguage1;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sLanguage2;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sSize;

public class GridLayoutUI {
    private GridLayout mLayout;
    private ButtonUI[][] mButtonUIs;
    Resources res;


    public GridLayoutUI(GridLayout layout, Resources resources) {
        Log.d("Test", "GridLayoutUI Created");
        res = resources;
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

    // Return a button with its text updated
    public Button FillLockedCellByMode(int index, int newValue){
        Button button = getButtonUI(index).getButton();
        if (sIsMode1) button.setText(sLanguage2.getWord(newValue));
        else button.setText(sLanguage1.getWord(newValue));
        button.setTextColor(Color.BLUE);
        return button;
    }
    // Returns a SudokuCell array with possibly conflicting cells highlighted red
    public void SetRowCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {

            if (getButtonUI(cellIndex / 9 * 9 + i).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
                getButtonUI(cellIndex / 9 * 9 + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    public void SetColumnCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {
            if (getButtonUI(cellIndex % 9 + i * 9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
                getButtonUI(cellIndex % 9 + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    public void SetBoxCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {
            if (getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
                getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
}
