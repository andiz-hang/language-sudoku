package com.bignerdranch.android.vocabularysudoku.view;

import android.animation.ObjectAnimator;
import android.graphics.Color;
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
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

public class GridLayoutUI {
    private GridLayout mLayout;
    private ButtonUI[][] mButtonUIs;
    private int mSize;


    public GridLayoutUI(GridLayout layout, int size) {
        mSize = size;
        layout.setRowCount(size);
        layout.setColumnCount(size);
        mLayout = layout;
        mButtonUIs = new ButtonUI[mSize][mSize];
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                mButtonUIs[i][j] = new ButtonUI();
            }
        }
    }

    public GridLayoutUI(GridLayout layout, int rows, int cols) {
        mSize = sSize;
        layout.setRowCount(rows);
        layout.setColumnCount(cols);
        mLayout = layout;
        mButtonUIs = new ButtonUI[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mButtonUIs[i][j] = new ButtonUI();
            }
        }
    }

    // Change the object's property to value over duration frames
    public void animate(String property, Float value, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mLayout, property, value);
        animation.setDuration(duration);
        animation.start();
    }

    public ButtonUI[][] getButtonUIs() {
        return mButtonUIs;
    }

    public void addButtonUI(ButtonUI button, int index) {
        int y = index / mSize;
        int x = index % mSize;
        mButtonUIs[y][x] = button;
    }

    public void addButtonUI(ButtonUI button, int yIndex, int xIndex) {
        mButtonUIs[yIndex][xIndex] = button;
    }

    public ButtonUI getButtonUI(int index) {
        int y = index / mSize;
        int x = index % mSize;
        return mButtonUIs[y][x];
    }

    private ButtonUI getButtonUI(int yIndex, int xIndex) {
        return mButtonUIs[yIndex][xIndex];
    }

    public GridLayout getLayout() {
        return mLayout;
    }

    /*
    // Return a button with its text updated
    public Button FillLockedCellByMode(int index, int newValue) {
        Button button = getButtonUI(index).getButton();
        if (sIsMode1) button.setText(sLanguage2.getWord(newValue));
        else button.setText(sLanguage1.getWord(newValue));
        button.setTextColor(Color.BLUE);
        return button;
    }
    */

    // Returns a SudokuCell array with possibly conflicting cells highlighted red
    private void setRowCellsRed(int rowNum) {
        for (int i = 0; i < mSize; i++) {
            //if (getButtonUI(cellIndex / mSize * mSize + i).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex / mSize * mSize + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI(rowNum * mSize + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    private void setColumnCellsRed(int colNum) {
        for (int i = 0; i < mSize; i++) {
            //Log.d("Test", "Column: " + i);
            //if (getButtonUI(cellIndex % mSize + i * mSize).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex % mSize + i * mSize).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI(colNum + i * mSize).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    private void setBoxCellsRed(int boxNum) {
        for (int i = 0; i < mSize; i++) {
            int boxWidth = (int)ceil(sqrt(sSize));
            int boxHeight = (int)floor(sqrt(sSize));

            //if (getButtonUI(cellIndex / mSize /3*27 + cellIndex%mSize/3*3 + i%3 + i/3*mSize).getButton().getBackground().getConstantState() == res.getDrawable(R.drawable.bg_btn).getConstantState())
            //getButtonUI(cellIndex / mSize /3*27 + cellIndex%mSize/3*3 + i%3 + i/3*mSize).getButton().setBackgroundResource(R.drawable.bg_btn_red);
            getButtonUI((boxNum / boxHeight) * (sSize * boxHeight) + (boxNum % boxHeight) * boxWidth + i % boxWidth + i / boxWidth * sSize).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    public void toNumbers() {
        for(int i = 0;i < mSize; i++){
            for(int j = 0; j < mSize; j++){
                String buttonText = getButtonUI(j,i).getText();
                for(int k = 1; k <= mSize; k++){
                    if ((buttonText.equals(sLanguage1.getWord(k))) || (buttonText.equals(sLanguage2.getWord(k)))){
                        getButtonUI(j,i).setText(Integer.toString(k));
                    }
                }
            }
        }
    }

    public void toWords(SudokuGrid grid) {
        for(int i = 0;i < mSize; i++){
            for(int j = 0; j < mSize; j++){
                String buttonText = getButtonUI(j,i).getText();
                for(int k = 1; k <= mSize; k++){
                    if (buttonText.equals(Integer.toString(k))){
                        if (grid.getSudokuCell(i,j).isLock()) getButtonUI(j,i).setText(sLanguage1.getWord(k));
                        else getButtonUI(j,i).setText(sLanguage2.getWord(k));
                    }
                }
            }
        }
    }
    public void defaultButtonColors(){
        for (int y=0;y<mSize*mSize;y++) {
            getButtonUI(y).getButton().setBackgroundResource(R.drawable.bg_btn);
        }
    }
    public void highlightWrongCells(SudokuCell mGrid[][], boolean mWrongRows[], boolean mWrongCols[], boolean mWrongBoxes[]){
        for(int i = 0; i < mSize; i++){
            if(mWrongRows[i]) setRowCellsRed(i);
            if(mWrongCols[i]) setColumnCellsRed(i);
            if(mWrongBoxes[i]) setBoxCellsRed(i);
        }
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                if (mGrid[i][j].isConflicting())
                    getButtonUI(i, j).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
                if (mGrid[i][j].isLock())
                    getButtonUI(i, j).setText(sLanguage1.getWord(mGrid[i][j].getValue()));
                else {
                    getButtonUI(i, j).getButton().setTextColor(Color.BLUE);
                    getButtonUI(i, j).setText(sLanguage2.getWord(mGrid[i][j].getValue()));
                }
                if (mGrid[i][j].getValue() == 0){
                    getButtonUI(i, j).getButton().setTextColor(Color.BLUE);
                    getButtonUI(i, j).setText(" ");

                }
            }
        }
    }
    //private void setButtonValue(int value){
        // Fills current cell's button with input value
        //mSudokuLayout.getButtonUI(sCurrentCell).setButton(mSudokuLayout.FillLockedCellByMode(sCurrentCell, value));
    //}
}

