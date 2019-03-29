package com.bignerdranch.android.vocabularysudoku.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;

import java.util.Random;

import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sCurrentCell;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage1;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sLanguage2;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sSize;

public class SudokuGrid {

    private SudokuCell[][] mGrid;
    private boolean mIsZoomed = false;

    private boolean[] mWrongRows;
    private boolean[] mWrongCols;
    private boolean[] mWrongBoxes;

    private int[] mAnswers;
    //private Resources mRes;
    private GridLayoutUI mSudokuLayout;
    private String mInitialValues;
    private String mAnswerKey;
    private int mSavedPuzzleNumber;

    // Methods

    public SudokuGrid(Context context, int N, int puzzleNum) {

        mGrid = new SudokuCell[N][N];
        mWrongRows = new boolean[N];
        mWrongCols = new boolean[N];
        mWrongBoxes = new boolean[N];
        mAnswers = new int[N * N];

        Resources res = context.getResources();

        mSavedPuzzleNumber = puzzleNum;
        mAnswerKey = res.getStringArray(R.array.answ)[puzzleNum];
        mInitialValues = res.getStringArray(R.array.puzz)[puzzleNum];
        //mRes = res;
        initializePuzzle(mAnswerKey, mInitialValues);
    }


    public int getPuzzleNum(){ return mSavedPuzzleNumber;}
    // Initialize both the grid and the answer array
    private void initializePuzzle(String answerKey, String initialValues) {
        // Initialize the Answer Array
        //String answerKey = mRes.getStringArray(R.array.answ)[randInt];
        for(int i = 0; i < 81; i++) {
            mAnswers[i] = Character.getNumericValue(answerKey.charAt(i));
        }
        // Initialize the Sudoku Grid Array
        //String initialValues = mRes.getStringArray(R.array.puzz)[randInt];
        mGrid = new SudokuCell[9][9];
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mGrid[i][j] = new SudokuCell();
                int newValue = Character.getNumericValue(initialValues.charAt(i * 9 + j));
                if (newValue != 0){
                    mGrid[i][j].setValue(newValue);
                    mGrid[i][j].setLock(true);
                }
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

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    boolean cellConflictInColumn(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex % 9 + distanceIndex * 9;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInRow(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 * 9 + distanceIndex;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInBox(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 /3*27 + currentCellIndex%9/3*3 + distanceIndex%3 + distanceIndex/3*9;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    public void setWrongRows(int index, boolean value){
        int rowNum = index / sSize;
        mWrongRows[rowNum] = value;
        setCellConflicting(index, value);
    }
    public void setWrongCols(int index, boolean value){
        int colNum = index % sSize;
        mWrongCols[colNum] = value;
        setCellConflicting(index, value);
    }
    public void setWrongBoxes(int index, boolean value){
        int boxNum = convertIndexToBoxNum(index);
        mWrongBoxes[boxNum] = value;
        setCellConflicting(index, value);
    }

    public int getAnswers(int index){
        return mAnswers[index];
    }

    void setAnswers(int index, int answer) {
        mAnswers[index] = answer;
    }

    private int convertIndexToBoxNum(int index){
        int boxColumn = (index % sSize) / 3;    // 0~2
        int boxRow = (index / sSize) / 3;       // 0~2
        int sum = (boxRow * 3) + boxColumn;
        return (boxRow * 3) + boxColumn;        // 0~8
    }

    private void setCellConflicting(int index, boolean value){
        int y = index / sSize;
        int x = index % sSize;
        mGrid[y][x].setConflicting(value);
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

    boolean isZoomed() {
        return mIsZoomed;
    }

    void setZoomed(boolean zoom) {
        mIsZoomed=zoom;
    }

    public void setSudokuLayout(GridLayoutUI layoutUI) {
        mSudokuLayout = layoutUI;
    }


    public void updateSudokuModel(int value, int CurrentCell){
        // If a cell isn't locked, set its text to the chosen word and show any conflicts
        if(!getSudokuCell(CurrentCell).isLock()){
            getSudokuCell(CurrentCell).setValue(value);

            int count=0,correct;
            for(int i = 0; i < 81; i++){
                setWrongRows(i, false);
                setWrongCols(i, false);
                setWrongBoxes(i, false);
            }
            for (int x=0;x<81;x++) {
                correct = findConflictAtIndex(x);
                if (correct == 1 && getSudokuCell(x).getValue() != 0)
                    count+=1;
            }
//            if (count==81)
//                Toast.makeText(getApplicationContext(),"Congrats! You win!",Toast.LENGTH_SHORT).show();
            //if (value != 0) setButtonValue(value);
            //resetButtonImage();
        }
    }



    int findConflictAtIndex(int cellIndex){
        boolean row=false,column=false,box=false;
        // Compares current cell to all potentially conflicting cells
        // If a conflict is found, set that cell to wrong
        // Iterates through all cells in the same row, column, and box as the current cell

        for (int j=0;j<9;j++) {
            // value           = 1-9 corresponding to the answer being put into the current cell
            // sCurrentCell = index of the current cell
            // If the current cell was correct, but the new value conflicts with a cell in the same row, column, or box:
            //     Label this cell as wrong in the mWrong array
            if (getSudokuCell(cellIndex).getValue()!=0){
                getSudokuCell(cellIndex).setConflicting(false);
                if (cellConflictInColumn(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    column=true;
                    setWrongCols(cellIndex, true);
                }
                if (cellConflictInRow(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    row=true;
                    setWrongRows(cellIndex, true);
                }
                if (cellConflictInBox(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    box=true;
                    setWrongBoxes(cellIndex, true);
                }
            }
        }
        //if (row)    mSudokuLayout.SetRowCellsRed(cellIndex);
        //if (column) mSudokuLayout.SetColumnCellsRed(cellIndex);
        //if (box)    mSudokuLayout.SetBoxCellsRed(cellIndex);
        //if (box||column||row)mSudokuLayout.getButtonUI(cellIndex).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
        if (box||column||row) {
            if (!getSudokuCell(cellIndex).isLock()) getSudokuCell(cellIndex).setConflicting(true);
            return 0;
        }
        else return 1;
    }

    public void sendModelToView(Language language2) {
        mSudokuLayout.defaultButtonColors();
        mSudokuLayout.highlightWrongCells(mGrid, mWrongRows, mWrongCols, mWrongBoxes, language2);

    }

    public void sendModelToView() {
        mSudokuLayout.defaultButtonColors();
        mSudokuLayout.highlightWrongCells(mGrid, mWrongRows, mWrongCols, mWrongBoxes);
    }
}

