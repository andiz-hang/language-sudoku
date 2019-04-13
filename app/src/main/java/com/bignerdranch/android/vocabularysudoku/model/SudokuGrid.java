package com.bignerdranch.android.vocabularysudoku.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
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
    private int mDifficulty;
    private int mCurrent = -1;
    private int[] mInitialValues;

    // Methods

    public SudokuGrid(int puzzleNum, int size, int difficulty, InputStream is) throws IOException {

        setupMembers(size,difficulty);
        mDifficulty = difficulty;
        mInitialValues = new int[size * size];
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

        mInitialValues = initializePuzzle(mInitialValues, false);
    }

    public SudokuGrid(int puzzleNum, int size, InputStream is, String initialValues) throws IOException {

        setupMembers(size,-1);

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

        mInitialValues = valueStringToArray(size, initialValues);

        mSavedPuzzleNumber = puzzleNum;

        mInitialValues = initializePuzzle(mInitialValues, true);
    }

    private int[] valueStringToArray(int size, String initialValues){
        String[] tokens = initialValues.split(" ");
        int[] values = new int[size*size];
        for(int i = 0; i < size * size; i++){
            values[i] = Integer.parseInt(tokens[i]);
        }
        return values;
    }

    public void setupMembers(int size, int difficulty){
        mGrid = new SudokuCell[size][size];
        mWrongRows = new boolean[size];
        mWrongCols = new boolean[size];
        mWrongBoxes = new boolean[size];
        mAnswers = new int[size * size];
        mSize = size;
        mInitialValues = new int[size * size];
        mDifficulty = difficulty;
    }


    public int getPuzzleNum() {
        return mSavedPuzzleNumber;
    }

    // Initialize both the grid and the answer array
    private int[] initializePuzzle(int[] initialValues, boolean isAdjusted) {

        // Initialize the Sudoku Grid Array
        mGrid = new SudokuCell[mSize][mSize];
        int[] newValues = initialValues;
        if (!isAdjusted) {
            newValues = difficultyAdjust(initialValues);
        }
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                mGrid[i][j] = new SudokuCell();
                int newValue = newValues[i * mSize + j];
                if (newValue != 0) {
                    mGrid[i][j].setValue(newValue);
                    mGrid[i][j].setLock(true);
                }
            }
        }
        return newValues;
    }


    private int[] difficultyAdjust(int[] initialValues) {
        Random rand;
        int count = 0, randInt, diff;
        int[] newValues = new int[mSize * mSize];
        diff = (int) round(mSize * mSize * ((10 - mDifficulty) * .07 + .15));
        for (int i = 0; i < mSize * mSize; i++) {
            if (initialValues[i] != 0) {
                count += 1;
            }
            newValues[i] = initialValues[i];
        }
        while (count <= diff) {
            rand = new Random();
            randInt = rand.nextInt(mSize * mSize);
            //Log.d("Test", Integer.toString(count) + " " + Integer.toString(diff));
            if (newValues[randInt] == 0) {
                newValues[randInt] = mAnswers[randInt];
                count += 1;
            }
        }
        return newValues;
    }

    public SudokuCell getSudokuCell(int x, int y) {
        return mGrid[y][x];
    }

    public SudokuCell getSudokuCell(int index) {
        int y = index / mSize;
        int x = index % mSize;
        return mGrid[y][x];
    }

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    boolean cellConflictInColumn(int currentCellIndex, int popupIndex, int distanceIndex) {
        int targetCellIndex = currentCellIndex % mSize + distanceIndex * mSize;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInRow(int currentCellIndex, int popupIndex, int distanceIndex) {
        int targetCellIndex = currentCellIndex / mSize * mSize + distanceIndex;
        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean cellConflictInBox(int currentCellIndex, int popupIndex, int distanceIndex) {
        int boxWidth = (int) ceil(sqrt(mSize));
        int boxHeight = (int) floor(sqrt(mSize));
        int firstElementInCol = currentCellIndex / mSize / boxHeight * (boxHeight * mSize);
        int columnOffset = currentCellIndex % mSize / boxWidth * boxWidth;
        int distanceOffset = distanceIndex % boxWidth + (distanceIndex / boxWidth * mSize);
        int targetCellIndex = firstElementInCol + columnOffset + distanceOffset;
        //int targetCellIndex = currentCellIndex / mSize / (int)sqrt(mSize) * (mSize * (int)sqrt(mSize)) + currentCellIndex % mSize / (int)sqrt(mSize) * (int)sqrt(mSize) + distanceIndex % (int)sqrt(mSize) + distanceIndex / (int)sqrt(mSize) * mSize;

        return popupIndex + 1 == getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    public void setWrongRows(int index, boolean value) {
        int rowNum = index / mSize;
        mWrongRows[rowNum] = value;
        setCellConflicting(index, value);
    }

    public void setWrongCols(int index, boolean value) {
        int colNum = index % mSize;
        mWrongCols[colNum] = value;
        setCellConflicting(index, value);
    }

    public void setWrongBoxes(int index, boolean value) {
        int boxNum = convertIndexToBoxNum(index);
        mWrongBoxes[boxNum] = value;
        setCellConflicting(index, value);
    }

    public int getAnswers(int index) {
        return mAnswers[index];
    }

    void setAnswers(int index, int answer) {
        mAnswers[index] = answer;
    }

    private int convertIndexToBoxNum(int index) {
        int boxWidth = (int) ceil(sqrt(mSize));
        int boxHeight = (int) floor(sqrt(mSize));

        int boxColumn = (index % mSize) / boxWidth;    // 0~2
        int boxRow = (index / mSize) / boxHeight;       // 0~2
        int sum = (boxRow * boxHeight) + boxColumn;
        return sum;        // 0~mSize - 1
    }

    private void setCellConflicting(int index, boolean value) {
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


    public boolean updateSudokuModel(int value, int CurrentCell) {
        // If a cell isn't locked, set its text to the chosen word and show any conflicts
        if (!getSudokuCell(CurrentCell).isLock()) {
            getSudokuCell(CurrentCell).setValue(value);

            return updateConflicts();
            //Toast.makeText(context, "Congrats! You Win!", Toast.LENGTH_LONG).show();
//            if (value != 0) setButtonValue(value);
//            resetButtonImage();
        }
        return false;
    }

    public boolean updateConflicts() {
        int count = 0, correct;
        for (int i = 0; i < mSize * mSize; i++) {
            setWrongRows(i, false);
            setWrongCols(i, false);
            setWrongBoxes(i, false);
        }
        for (int x = 0; x < mSize * mSize; x++) {
            correct = findConflictAtIndex(x);
            if (correct == 1 && getSudokuCell(x).getValue() != 0)
                count += 1;
        }
        if (count == mSize * mSize)
            return true;
        return false;
    }

    public void setSelected(int index) {
        mCurrent = index;
    }
    public int getSelected() {
        return mCurrent;
    }

    int findConflictAtIndex(int cellIndex) {
        boolean row = false, column = false, box = false;
        // Compares current cell to all potentially conflicting cells
        // If a conflict is found, set that cell to wrong
        // Iterates through all cells in the same row, column, and box as the current cell

        for (int j = 0; j < mSize; j++) {
            // value           = 1-9 corresponding to the answer being put into the current cell
            // sCurrentCell = index of the current cell
            // If the current cell was correct, but the new value conflicts with a cell in the same row, column, or box:
            //     Label this cell as wrong in the mWrong array
            if (getSudokuCell(cellIndex).getValue() != 0) {
                getSudokuCell(cellIndex).setConflicting(false);
                if (cellConflictInColumn(cellIndex, getSudokuCell(cellIndex).getValue() - 1, j)) {
                    column = true;
                    setWrongCols(cellIndex, true);
                }
                if (cellConflictInRow(cellIndex, getSudokuCell(cellIndex).getValue() - 1, j)) {
                    row = true;
                    setWrongRows(cellIndex, true);
                }
                if (cellConflictInBox(cellIndex, getSudokuCell(cellIndex).getValue() - 1, j)) {
                    box = true;
                    setWrongBoxes(cellIndex, true);
                }
            }
        }
        //if (row)    mSudokuLayout.SetRowCellsRed(cellIndex);
        //if (column) mSudokuLayout.SetColumnCellsRed(cellIndex);
        //if (box)    mSudokuLayout.SetBoxCellsRed(cellIndex);
        //if (box||column||row)mSudokuLayout.getButtonUI(cellIndex).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
        if (box || column || row) {
            if (!getSudokuCell(cellIndex).isLock()) getSudokuCell(cellIndex).setConflicting(true);
            return 0;
        } else return 1;
    }

    public void sendModelToView() {
        mSudokuLayout.defaultButtonColors();
        mSudokuLayout.highlightWrongCells(mWrongRows, mWrongCols, mWrongBoxes);
        mSudokuLayout.displayNewText(mGrid);
        mSudokuLayout.highlightSelected(getSelected(), this);
    }

    public String sudokuToString(){
        String sudokuString = "";
        for(int i = 0; i < sSize * sSize; i++){
            sudokuString += getSudokuCell(i).getValue();
        }
        return sudokuString;
    }

    public String sudokuAnswersToString(){
        String sudokuString = "";
        for(int i = 0; i < sSize * sSize; i++){
            sudokuString += mAnswers[i];
        }
        return sudokuString;
    }

    String valueArrayToString(int[] initialValues){
        String values = "";
        for(int i = 0; i < sSize * sSize; i++){
            values = values + Integer.toString(initialValues[i]);
            if (i < ((sSize * sSize)-1)) values = values + " ";
        }
        return values;
    }
    String getInputs(){
        String inputs = "";
        for(int i = 0; i < sSize; i++){
            for(int j = 0; j < sSize; j++){
                if(!mGrid[i][j].isLock()) {
                    inputs = inputs + Integer.toString(i*9+j);
                    inputs = inputs + ",";
                    inputs = inputs + Integer.toString(mGrid[i][j].getValue());
                    if (i < sSize - 1 || j < sSize - 1) inputs = inputs + " ";
                }
            }
        }
        return inputs;
    }

    public void applySavedInputs(SharedPreferences preferences){
        String inputs = preferences.getString("InputValues", "");
        String[] values = inputs.split(" ");
        for(int i = 0; i < values.length; i++){
            String[] pair = values[i].split(",");
            mGrid[Integer.parseInt(pair[0])/9][Integer.parseInt(pair[0])%9].setValue(Integer.parseInt(pair[1]));
        }
    }
    public void savePuzzle(SharedPreferences preferences, String uri, boolean listeningMode, boolean sampleFile, String wordOrder){
        SharedPreferences.Editor editor = preferences.edit();
        String sudokuCurrentString = sudokuToString();
        String sudokuAnswersString = sudokuAnswersToString();
        editor.putString("SudokuCurrent", sudokuCurrentString);
        editor.putInt("SudokuNum", mSavedPuzzleNumber);
        editor.putBoolean("SaveExists", true);
        editor.putBoolean("Listen", listeningMode);
        editor.putInt("Size", sSize);
        editor.putInt("Difficulty", sDifficulty);
        editor.putBoolean("SampleFile", sampleFile);
        editor.putString("Uri", uri);
        editor.putString("WordOrder", wordOrder);
        editor.putString("InitialValues", valueArrayToString(mInitialValues));
        editor.putString("InputValues", getInputs());
        Log.d("Test", "Saved Word Order: "+wordOrder);
        Log.d("Test", "Puzzle Number: "+mSavedPuzzleNumber);
        editor.commit();
    }

}