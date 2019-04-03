package com.bignerdranch.android.vocabularysudoku.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;

import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sSize;
import static com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity.sDifficulty;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class SudokuGrid {

    private SudokuCell[][] mGrid;
    private boolean mIsZoomed = false;

    private boolean[] mWrongRows;
    private boolean[] mWrongCols;
    private boolean[] mWrongBoxes;

    private int[] mAnswers;
    //private Resources mRes;
    private GridLayoutUI mSudokuLayout;
    private int mSavedPuzzleNumber;
    private int mSize;
    private int mCurrent=-1;

    // Methods

    public SudokuGrid(int N, int puzzleNum, int size, InputStream is) throws IOException {

        mGrid = new SudokuCell[N][N];
        mWrongRows = new boolean[N];
        mWrongCols = new boolean[N];
        mWrongBoxes = new boolean[N];
        mAnswers = new int[N * N];
        mSize = size;

        int[] mInitialValues = new int[N * N];
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        int count = 0;
        // Reads lines until line puzzleNum is reached. Then, put the values into the puzzle
        while ((line = reader.readLine()) != null && count <= puzzleNum) {
            String[] tokens = line.split(",");

            if (count == puzzleNum) {
                for (int i = 0; i < (mSize * mSize); i++) {
                    mInitialValues[i] = Integer.parseInt(tokens[i]);
                    mAnswers[i] = Integer.parseInt(tokens[i + (mSize * mSize)]);
                }
            }
            count++;
        }
        mSavedPuzzleNumber = puzzleNum;

        initializePuzzle(mInitialValues);
    }
    public SudokuGrid(int N, int puzzleNum, InputStream is) throws IOException {

        mGrid = new SudokuCell[N][N];
        mWrongRows = new boolean[N];
        mWrongCols = new boolean[N];
        mWrongBoxes = new boolean[N];
        mAnswers = new int[N * N];
        mSize = sSize;

        int[] mInitialValues = new int[N * N];
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        int count = 0;
        // Reads lines until line puzzleNum is reached. Then, put the values into the puzzle
        while ((line = reader.readLine()) != null && count <= puzzleNum) {
            String[] tokens = line.split(",");

            if (count == puzzleNum) {
                for (int i = 0; i < (mSize * mSize); i++) {
                    mInitialValues[i] = Integer.parseInt(tokens[i]);
                    mAnswers[i] = Integer.parseInt(tokens[i + (mSize * mSize)]);
                }
            }
            count++;
        }
        mSavedPuzzleNumber = puzzleNum;

        initializePuzzle(mInitialValues);
    }


    public int getPuzzleNum(){ return mSavedPuzzleNumber;}

    // Initialize both the grid and the answer array
    private void initializePuzzle(int[] initialValues) {

        // Initialize the Sudoku Grid Array
        //String initialValues = mRes.getStringArray(R.array.puzz)[randInt];
        mGrid = new SudokuCell[mSize][mSize];
        int[] newValues=difficultyAdjust(initialValues);
        for(int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                mGrid[i][j] = new SudokuCell();
                int newValue = newValues[i * mSize + j];
                if (newValue != 0){
                    mGrid[i][j].setValue(newValue);
                    mGrid[i][j].setLock(true);
                }
            }
        }
    }
    private int[] difficultyAdjust(int[] initialValues){
        Random rand;
        int count = 0,randInt,diff;
        int[] newValues=new int[mSize*mSize];
        diff=(int)round(mSize*mSize*((10-sDifficulty)*.07+.15));
        for (int i=0;i<mSize*mSize;i++){
            if (initialValues[i]!=0){
                count+=1;
            }
            newValues[i]=initialValues[i];
        }
        while (count<=diff){
            rand = new Random();
            randInt = rand.nextInt(mSize*mSize);
            Log.d("Test",Integer.toString(count)+" "+Integer.toString(diff));
            if (newValues[randInt]==0){
                newValues[randInt]=mAnswers[randInt];
                count+=1;
            }
        }
        return  newValues;
    }
    public SudokuCell getSudokuCell(int x, int y) {
        return mGrid[y][x];
    }

    public SudokuCell getSudokuCell(int index){
        int y = index / mSize;
        int x = index % mSize;
        return mGrid[y][x];
    }

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    boolean cellConflictInColumn(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex % mSize + distanceIndex * mSize;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInRow(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / mSize * mSize + distanceIndex;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInBox(int currentCellIndex, int popupIndex, int distanceIndex){
        int boxWidth = (int)ceil(sqrt(mSize));
        int boxHeight = (int)floor(sqrt(mSize));
        int firstElementInCol = currentCellIndex / mSize / boxHeight * (boxHeight * mSize);
        int columnOffset = currentCellIndex % mSize / boxWidth * boxWidth;
        int distanceOffset = distanceIndex % boxWidth + (distanceIndex / boxWidth * mSize);
        int targetCellIndex = firstElementInCol + columnOffset + distanceOffset;
        //int targetCellIndex = currentCellIndex / mSize / (int)sqrt(mSize) * (mSize * (int)sqrt(mSize)) + currentCellIndex % mSize / (int)sqrt(mSize) * (int)sqrt(mSize) + distanceIndex % (int)sqrt(mSize) + distanceIndex / (int)sqrt(mSize) * mSize;

        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    public void setWrongRows(int index, boolean value){
        int rowNum = index / mSize;
        mWrongRows[rowNum] = value;
        setCellConflicting(index, value);
    }
    public void setWrongCols(int index, boolean value){
        int colNum = index % mSize;
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
        int boxWidth = (int)ceil(sqrt(mSize));
        int boxHeight = (int)floor(sqrt(mSize));

        int boxColumn = (index % mSize) / boxWidth;    // 0~2
        int boxRow = (index / mSize) / boxHeight;       // 0~2
        int sum = (boxRow * boxHeight) + boxColumn;
        return sum;        // 0~mSize - 1
    }

    private void setCellConflicting(int index, boolean value){
        int y = index / mSize;
        int x = index % mSize;
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

    public void setSudokuLayout(GridLayoutUI layoutUI) {
        mSudokuLayout = layoutUI;
    }


    public boolean updateSudokuModel(int value, int CurrentCell){
        // If a cell isn't locked, set its text to the chosen word and show any conflicts
        if(!getSudokuCell(CurrentCell).isLock()){
            getSudokuCell(CurrentCell).setValue(value);

            return updateConflicts();
                //Toast.makeText(context, "Congrats! You Win!", Toast.LENGTH_LONG).show();
//            if (value != 0) setButtonValue(value);
//            resetButtonImage();
        }
        return false;
    }

    public boolean updateConflicts(){
        int count=0,correct;
        for(int i = 0; i < mSize * mSize; i++){
            setWrongRows(i, false);
            setWrongCols(i, false);
            setWrongBoxes(i, false);
        }
        for (int x=0;x<mSize * mSize;x++) {
            correct = findConflictAtIndex(x);
            if (correct == 1 && getSudokuCell(x).getValue() != 0)
                count+=1;
        }
        if (count==mSize * mSize)
            return true;
        return false;
    }
    public void setSelected(int index){
        if (index==-1) {
            mCurrent = -1;
        }
        else {
            mCurrent = index;
        }
    }

    int findConflictAtIndex(int cellIndex){
        boolean row=false,column=false,box=false;
        // Compares current cell to all potentially conflicting cells
        // If a conflict is found, set that cell to wrong
        // Iterates through all cells in the same row, column, and box as the current cell

        for (int j=0;j<mSize;j++) {
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

    public void sendModelToView() {
        mSudokuLayout.defaultButtonColors();
        mSudokuLayout.highlightWrongCells(mWrongRows, mWrongCols, mWrongBoxes);
        mSudokuLayout.displayNewText(mGrid);
        mSudokuLayout.highlightSelected(mCurrent);
    }
}

