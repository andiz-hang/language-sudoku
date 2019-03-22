package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.model.Language;
import com.bignerdranch.android.vocabularysudoku.model.SudokuCell;
import com.bignerdranch.android.vocabularysudoku.model.SudokuGrid;

//import static android.support.v4.graphics.drawable.IconCompat.getResources;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sCurrentCell;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sIsMode1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage2;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sSize;

public class GridLayoutUI {
    private GridLayout mLayout;
    private ButtonUI[][] mButtonUIs;


    public GridLayoutUI(GridLayout layout) {
        mLayout = layout;
        mButtonUIs = new ButtonUI[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
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

    public ButtonUI[][] getButtonUIs() {
        return mButtonUIs;
    }

    public void addButtonUI(ButtonUI button, int index) {
        int y = index / sSize;
        int x = index % sSize;
        mButtonUIs[y][x] = button;
    }

    public void addButtonUI(ButtonUI button, int yIndex, int xIndex) {
        mButtonUIs[yIndex][xIndex] = button;
    }

    public ButtonUI getButtonUI(int index) {
        int y = index / sSize;
        int x = index % sSize;
        return mButtonUIs[y][x];
    }

    public ButtonUI getButtonUI(int yIndex, int xIndex) {
        return mButtonUIs[yIndex][xIndex];
    }

    public GridLayout getLayout() {
        return mLayout;
    }

    // Return a button with its text updated
    public Button FillLockedCellByMode(int index, int newValue) {
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
            //Log.d("Test", "Column: " + i);
            //if (getButtonUI(cellIndex % 9 + i * 9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex % 9 + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI(colNum + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    public void SetBoxCellsRed(int boxNum) {
        for (int i = 0; i < 9; i++) {
            //if (getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI((boxNum / 3) * 27 + (boxNum % 3) * 3 + i % 3 + i / 3 * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    public void ToNumbers() {
        for(int i = 0;i < 9; i++){
            for(int j = 0; j < 9; j++){
                String buttonText = getButtonUI(j,i).getText();
                for(int k = 1; k <= 9; k++){
                    if ((buttonText == sLanguage1.getWord(k)) || (buttonText == sLanguage2.getWord(k))){
                        getButtonUI(j,i).setText(Integer.toString(k));
                    }
                }
            }
        }
    }

    public void ToWords(SudokuGrid grid) {
        for(int i = 0;i < 9; i++){
            for(int j = 0; j < 9; j++){
                String buttonText = getButtonUI(j,i).getText();
                for(int k = 1; k <= 9; k++){
                    if (buttonText == Integer.toString(k)){
                        if (grid.getSudokuCell(i,j).isLock()) getButtonUI(j,i).setText(sLanguage1.getWord(k));
                        else getButtonUI(j,i).setText(sLanguage2.getWord(k));
                    }
                }
            }
        }
    }
    public void defaultButtonColors(){
        for (int y=0;y<81;y++) {
            getButtonUI(y).getButton().setBackgroundResource(R.drawable.bg_btn);
        }
    }
    public void highlightWrongCells(SudokuCell mGrid[][], boolean mWrongRows[], boolean mWrongCols[], boolean mWrongBoxes[], Language language2){
        for(int i = 0; i < sSize; i++){
            if(mWrongRows[i]) SetRowCellsRed(i);
            if(mWrongCols[i]) SetColumnCellsRed(i);
            if(mWrongBoxes[i]) SetBoxCellsRed(i);
        }
        for (int i = 0; i < sSize; i++) {
            for (int j = 0; j < sSize; j++) {
                if (mGrid[i][j].isConflicting())
                    getButtonUI(i, j).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
                if (mGrid[i][j].isLock())
                    getButtonUI(i, j).setText(sLanguage1.getWord(mGrid[i][j].getValue()));
                else
                    getButtonUI(i, j).setText(language2.getWord(mGrid[i][j].getValue()));
                if (mGrid[i][j].getValue() == 0) getButtonUI(i, j).setText(" ");
            }
        }
    }
    public void highlightWrongCells(SudokuCell mGrid[][], boolean mWrongRows[], boolean mWrongCols[], boolean mWrongBoxes[]){
        for(int i = 0; i < sSize; i++){
            if(mWrongRows[i]) SetRowCellsRed(i);
            if(mWrongCols[i]) SetColumnCellsRed(i);
            if(mWrongBoxes[i]) SetBoxCellsRed(i);
        }
        for (int i = 0; i < sSize; i++) {
            for (int j = 0; j < sSize; j++) {
                if (mGrid[i][j].isConflicting())
                    getButtonUI(i, j).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
                if (mGrid[i][j].isLock())
                    getButtonUI(i, j).setText(sLanguage1.getWord(mGrid[i][j].getValue()));
                else
                    getButtonUI(i, j).setText(sLanguage2.getWord(mGrid[i][j].getValue()));
                if (mGrid[i][j].getValue() == 0) getButtonUI(i, j).setText(" ");
            }
        }
    }
    //private void setButtonValue(int value){
        // Fills current cell's button with input value
        //mSudokuLayout.getButtonUI(sCurrentCell).setButton(mSudokuLayout.FillLockedCellByMode(sCurrentCell, value));
    //}
}

