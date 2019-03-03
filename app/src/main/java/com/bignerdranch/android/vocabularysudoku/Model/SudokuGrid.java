package com.bignerdranch.android.vocabularysudoku.Model;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.View.GridLayoutUI;

import java.util.Random;

import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sCurrentCell;
import static com.bignerdranch.android.vocabularysudoku.Controller.SudokuActivity.sSize;

public class SudokuGrid {

    private SudokuCell[][] mGrid;
    private boolean mIsZoomed = false;

    private boolean[] mWrongRows;
    private boolean[] mWrongCols;
    private boolean[] mWrongBoxes;

    private int[] mAnswers;
    private Resources mRes;
    private GridLayoutUI mSudokuLayout;

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

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    public boolean CellConflictInColumn(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex % 9 + distanceIndex * 9;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    public boolean CellConflictInRow(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 * 9 + distanceIndex;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    public boolean CellConflictInBox(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 /3*27 + currentCellIndex%9/3*3 + distanceIndex%3 + distanceIndex/3*9;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    public void setWrongRows(int index){
        int rowNum = index / sSize;
        mWrongRows[rowNum] = true;
        setCellConflicting(index, true);
    }
    public void setWrongCols(int index){
        int colNum = index % sSize;
        mWrongCols[colNum] = true;
        setCellConflicting(index, true);
    }
    public void setWrongBoxes(int index){
        int boxNum = convertIndexToBoxNum(index);
        mWrongRows[boxNum] = true;
        setCellConflicting(index, true);
    }

    private int convertIndexToBoxNum(int index){
        int boxColumn = (index % sSize) / 3;    // 0~2
        int boxRow = (index / sSize) / 3;       // 0~2
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

    public boolean IsZoomed() {
        return mIsZoomed;
    }

    public void setSudokuLayout(GridLayoutUI layoutUI){
        mSudokuLayout = layoutUI;
    }

    private void setButtonValue(int index){
        // Fills current cell's button with input value
        mSudokuLayout.getButtonUI(sCurrentCell).setButton(mSudokuLayout.FillLockedCellByMode(sCurrentCell, index+1));
    }
    private void resetButtonImage(){
        for (int y=0;y<81;y++) {
            mSudokuLayout.getButtonUI(y).getButton().setBackgroundResource(R.drawable.bg_btn);
        }
    }

    public void updateSudokuModel(int index){
        // If a cell isn't locked, set its text to the chosen word and show any conflicts
        if(!getSudokuCell(sCurrentCell).isLock()){
            getSudokuCell(sCurrentCell).setValue(index+1);

            int count=0,correct;

            for (int x=0;x<81;x++) {
                correct = FindConflictAtIndex(x);
                if (correct == 1 && getSudokuCell(x).getValue() != 0)
                    count+=1;
            }
//            if (count==81)
//                Toast.makeText(getApplicationContext(),"Congrats! You win!",Toast.LENGTH_SHORT).show();
        }
        setButtonValue(index);
        resetButtonImage();
    }

    public int FindConflictAtIndex(int cellIndex){
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
                if (CellConflictInColumn(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    column=true;
                    setWrongCols(cellIndex);
                }
                if (CellConflictInRow(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    row=true;
                    setWrongRows(cellIndex);
                }
                if (CellConflictInBox(cellIndex, getSudokuCell(cellIndex).getValue()-1, j)){
                    box=true;
                    setWrongBoxes(cellIndex);
                }
            }
        }
        //if (row)    mSudokuLayout.SetRowCellsRed(cellIndex);
        //if (column) mSudokuLayout.SetColumnCellsRed(cellIndex);
        //if (box)    mSudokuLayout.SetBoxCellsRed(cellIndex);
        //if (box||column||row)mSudokuLayout.getButtonUI(cellIndex).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
        if (box||column||row) return 0;
        else return 1;
    }

    public void sendModelToView(){
        // Colour Columns
        for(int i = 0; i < sSize; i++){
            if(getWrongRow(i)) mSudokuLayout.SetRowCellsRed(i);
            if(getWrongCol(i)) mSudokuLayout.SetColumnCellsRed(i);
            if(getWrongBox(i)) mSudokuLayout.SetBoxCellsRed(i);
        }
        // Colour Conflicting Buttons
        for(int i = 0; i < sSize; i++){
            for(int j = 0; j < sSize; j++){
                if (mGrid[i][j].isConflicting()){
                    mSudokuLayout.getButtonUI(i,j).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
                }
            }
        }
    }
}
