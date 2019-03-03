package com.bignerdranch.android.vocabularysudoku.Model;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.bignerdranch.android.vocabularysudoku.R;
import java.util.Random;

import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sSize;

public class SudokuGrid {

    private SudokuCell[][] mGrid;
    private boolean mIsZoomed = false;

    private boolean[] mWrongRows;
    private boolean[] mWrongCols;
    private boolean[] mWrongBoxes;

    private int[] mAnswers;
    private Resources mRes;

    // Methods

    public SudokuGrid(int N, Resources res) {
        Log.d("Test", "SudokuGrid getting initialized");
        mGrid = new SudokuCell[N][N];
        mWrongRows = new boolean[N];
        mWrongCols = new boolean[N];
        mWrongBoxes = new boolean[N];
        mAnswers = new int[N * N];
        mRes = res;

        initializePuzzle();
    }

    // Initialize both the grid and the answer array
    private void initializePuzzle() {
        Random rand = new Random();
        int randInt = rand.nextInt(75);

        // Initialize the Answer Array
        String answerKey = mRes.getStringArray(R.array.answ)[randInt];
        for(int i = 0; i < 81; i++) {
            mAnswers[i] = Character.getNumericValue(answerKey.charAt(i));
        }

        // Initialize the Sudoku Grid Array
        String initialValues = mRes.getStringArray(R.array.puzz)[randInt];
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mGrid[i][j].setValue(Character.getNumericValue(initialValues.charAt(i * 9 + j)));
            }
        }
    }

    public SudokuCell getSudokuCell(int x, int y) {
        return mGrid[y][x];
    }

    public SudokuCell getSudokuCell(int index){
        int y = index / sSize;
        int x = index % sSize;
        return mGrid[y][x];
    }

    public boolean getWrongRow(int index) {
        return mWrongRows[index];
    }

    public boolean getWrongCol(int index) {
        return mWrongCols[index];
    }

    public boolean getWrongBox(int index) {
        return mWrongBoxes[index];
    }

    public boolean IsZoomed() {
        return mIsZoomed;
    }


}
