package com.bignerdranch.android.vocabularysudoku;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import java.util.Random;


public class SudokuActivity extends AppCompatActivity {
    
    // Variable Naming Convention:
    // m: member variable
    // c: constant
    // p: pointer
    // s: static
    // i: index

    // The number of correct, filled in cells
    static int       sCorrectCellCount;
    static boolean  sPopUpOnScreen = false;// for pop-up-screen
    static int      sCurrentCell;


    // Holds location of incorrect cells
    boolean[]       mWrong= new boolean[81];
    int             mScreenWidth, mScreenHeight;
    DisplayMetrics  mDisplayMetrics = new DisplayMetrics();
    SudokuCell[]    mSudokuCells = new SudokuCell[81];
    Button[]        mPopUpButtons = new Button[9];
    int[]           mSudokuValues = new int[81];
    boolean         mIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    boolean         mIsLanguage1 = false; // determines whether the first language is the toggled language or not
    Language        mLanguage1 = new Language("English","one", "two","three","four","five","six","seven","eight","nine");
    Language        mLanguage2 = new Language("Mandarin","一", "二","三","四","五","六","七","八","九");
    //Language mLanguage3 = new Language("French","un", "deux","trois","quatre","cinq","six","sept","huit","neuf");
    Button mClearButton;  // unimplemented
    Button mToggleButton; // unimplemented
    Button mHintButton;   // unimplemented


    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        // Get width and height of screen
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mScreenHeight = mDisplayMetrics.heightPixels;
        mScreenWidth = mDisplayMetrics.widthPixels;

        // Get popup layout
        GridLayout pop_up_grid=findViewById(R.id.pop_up_layout);
        pop_up_grid.setTranslationY(mScreenHeight);


        // Create popup buttons which fill in sudoku cells and show conflicts when pressed
        for(int i = 0; i<9; i++) {
            // Final index ii allows inner functions to access index i
            final int ii = i;//0~8
            mPopUpButtons[i] = new Button(this);
            mPopUpButtons[i].setText(mLanguage2.Words[i+1]);
            mPopUpButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Zoom out once a word is selected from popup menu
                    OnClickZoom(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mPopUpButtons[ii]);

                    // If a cell isn't locked, set its text to the chosen word and show any conflicts
                    if(! mSudokuCells[sCurrentCell].isLock()){
                        // Fills current cell's button with input value
                        mSudokuCells[sCurrentCell].Button = FillLockedCellByMode(sCurrentCell, ii+1);

                        boolean cellWasWrong = mWrong[sCurrentCell];
                        boolean cellWasBlank = false;
                        if (mSudokuCells[sCurrentCell].getValue()==0) cellWasBlank = true;
                        mSudokuCells[sCurrentCell].setValue(ii+1);

                        // Update all potentially conflicting cells in mWrong
                        mWrong = UpdateWrongArray(mWrong,mSudokuCells,sCurrentCell, ii);
                        // Update SudokuCells to show conflicts and tally CurrentCorrectCells
                        mSudokuCells = UpdateSudoku(mWrong, mSudokuCells, sCurrentCell, cellWasWrong, cellWasBlank);
                    }
                }
            });
            // Create and set parameters for button, then add button with parameters to Popup Grid
            GridLayout.LayoutParams layoutParams = CreatePopUpButtonParameters();
            pop_up_grid.addView(mPopUpButtons[i], layoutParams);
        }



        // Creates SudokuCells and adds them to SudokuCell array and Grid
        Random rand = new Random();
        int randInt = rand.nextInt(75);
        Resources res = getResources();
        // Gets string holding values for a random puzzle
        String full = res.getStringArray(R.array.puzz)[randInt];
        // Character.getNumericValue change string to int
        // Fill mSudokuValues[data] with Sudoku values
        for(int data = 0;data<81;data++) {mSudokuValues[data] = Character.getNumericValue(full.charAt(data));}
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                int index = i*9+j;
                final int ii = index;
                // Initalize new sudoku cell, give it a button, and add it to SudokuCells
                SudokuCell newCell = new SudokuCell();
                newCell.Button = new Button(this);
                mSudokuCells[index] = SetCellValue(newCell, mSudokuValues, index);
                mWrong[index]=false;

                // Create Listener for Button
                mSudokuCells[index].Button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        OnClickZoom(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mSudokuCells[ii].Button);
                        sCurrentCell=ii;
                    }
                });
                // Set button width, height, and margins in layoutParameters
                // Then add that button and its parameters to the Popup Menu Grid
                GridLayout gridLayout = findViewById(R.id.sudoku_grid);
                GridLayout.LayoutParams layoutParameters = CreateSudokuCellParameters(i, j);
                gridLayout.addView(mSudokuCells[index].Button, layoutParameters);
            }
        }
        // Menu Button Actions
        mClearButton = findViewById(R.id.clear_button);//clean the filled in word
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSudokuCells[sCurrentCell].isLock()) {
                    mSudokuCells[sCurrentCell].Button.setText("");
                    mSudokuCells[sCurrentCell].setValue(0); ;
                }
                //needs be figure out~~~~~~~~~~
//                for (int i=0; i<81; i++){
//                    if(! mSudokuCells[i].isLock()){
//                        boolean cellWasWrong = mWrong[i];
//                        boolean cellWasBlank = false;
//                        if (mSudokuCells[i].getValue()==0) cellWasBlank = true;
//                        // Update all potentially conflicting cells in mWrong
//                        mWrong = UpdateWrongArray(mWrong,mSudokuCells,i, mSudokuCells[i].getValue());
//                        // Update SudokuCells to show conflicts and tally CurrentCorrectCells
//                        mSudokuCells = UpdateSudoku(mWrong, mSudokuCells, i, cellWasWrong, cellWasBlank);
//                    }
//                }
            }
        });
        mToggleButton = findViewById(R.id.toggle_button);//only toggle pop up buttons' language
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 9; i++) {
                    if (mIsLanguage1)
                        mPopUpButtons[i].setText(mLanguage2.Words[i + 1]);
                    else
                        mPopUpButtons[i].setText(mLanguage1.Words[i + 1]);
                }
                mIsLanguage1 = !mIsLanguage1;
            }
        });
        final String fullAnsw = res.getStringArray(R.array.answ)[randInt];

        mHintButton = findViewById(R.id.hint_button);// highlight right answer of pop up buttons
        mHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //need CHANGE !
//                int valueForPopUpHint = Character.getNumericValue((fullAnsw.charAt(sCurrentCell)));
//                mPopUpButtons[valueForPopUpHint].setBackgroundResource(R.drawable.bg_btn_yellow);
            }
        });
        //initially hide the buttons
        mClearButton.setTranslationX(mScreenHeight/5f);
        mToggleButton.setTranslationX(mScreenHeight/5f);
        mHintButton.setTranslationX(mScreenHeight/5f);
    }//end of onCreate

    // METHODS

    // Return an array of potential conflicts
    boolean[] UpdateWrongArray(boolean[] wrong, SudokuCell[] sudoku, int cellIndex, int value){
        wrong[cellIndex] = false;
        // Compares current cell to all potentially conflicting cells
        // If a conflict is found, set that cell to wrong
        // Iterates through all cells in the same row, column, and box as the current cell
        for (int j=0;j<9;j++) {
            // value           = 1-9 corresponding to the answer being put into the current cell
            // sCurrentCell = index of the current cell
            // If the current cell was correct, but the new value conflicts with a cell in the same row, column, or box:
            //     Label this cell as wrong in the mWrong array
            if (!wrong[cellIndex]
                    &&(CellConflictInColumn(sudoku, cellIndex, value, j)
                    || CellConflictInRow(sudoku, cellIndex, value, j)
                    || CellConflictInBox(sudoku, cellIndex, value, j))) {
                wrong[cellIndex] = true;
            }
        }
        return wrong;
    }

    // Update the Sudoku array to show potential conflicts
    // Tally CorrectCellCount and Toast when sudoku is completed
    SudokuCell[] UpdateSudoku(boolean[] wrong, SudokuCell[] sudoku, int currentCellIndex, boolean cellWasWrong, boolean cellWasBlank){
        // If cell was wrong, but is now correct
        if ((cellWasWrong)&&(!wrong[currentCellIndex])){
            // Set no longer conflicting cells back to default color
            sudoku = UncolorFixedCells(sudoku, currentCellIndex);
        }
        // If the cell was blank or wrong, but now is correct: increment the tally of correct cells
        if ((cellWasBlank || cellWasWrong) && !wrong[currentCellIndex]) sCorrectCellCount++;
            // Otherwise, if cell was right, but now is wrong: decrement the tally of correct cells
        else if (!cellWasBlank && !cellWasWrong && wrong[currentCellIndex]) sCorrectCellCount--;
        Log.d("Test","CorrectCellCount : "+sCorrectCellCount);

        if (sCorrectCellCount==81){
            Toast.makeText(getApplicationContext(),"Congrats! You win!",Toast.LENGTH_SHORT).show();
        }
        // Colours all potentially conflicting cells red
        for (int x=0;x<81;x++) {
            if (CellAtIndexIsWrong(wrong, x)){
                sudoku = SetConflictingCellsRed(sudoku, x);
            }
        }
        return sudoku;
    }

    // Buttons coloured red previously are now uncoloured
    SudokuCell[] UncolorFixedCells(SudokuCell[] sudoku, int currentCellIndex){
        for (int x=0;x<9;x++) {
            sudoku[currentCellIndex / 9 / 3 * 27 + sCurrentCell % 9 / 3 * 3 + x % 3 + x / 3 * 9].Button.setBackgroundResource(R.drawable.bg_btn);
            sudoku[currentCellIndex % 9 + x * 9].Button.setBackgroundResource(R.drawable.bg_btn);
            sudoku[currentCellIndex / 9 * 9 + x].Button.setBackgroundResource(R.drawable.bg_btn);
        }
        return sudoku;
    }

    // Return a button with its text updated
    Button FillLockedCellByMode(int index, int newValue){
        Button button = mSudokuCells[index].Button;
        if (mIsMode1) button.setText(mLanguage2.Words[newValue]);
        else button.setText(mLanguage1.Words[newValue]);
        button.setTextColor(Color.BLUE);
        return button;
    }

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    boolean CellConflictInColumn(SudokuCell[] sudoku, int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex % 9 + distanceIndex * 9;
        return popupIndex + 1 == sudoku[targetCellIndex].getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean CellConflictInRow(SudokuCell[] sudoku, int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 * 9 + distanceIndex;
        return popupIndex + 1 == sudoku[targetCellIndex].getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean CellConflictInBox(SudokuCell[] sudoku, int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 /3*27 + currentCellIndex%9/3*3 + distanceIndex%3 + distanceIndex/3*9;
        return popupIndex + 1 == sudoku[targetCellIndex].getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true if the cell at index is incorrect
    boolean CellAtIndexIsWrong(boolean[] wrong, int index){
        return wrong[index];
    }

    // Returns a SudokuCell array with possibly conflicting cells highlighted red
    SudokuCell[] SetConflictingCellsRed(SudokuCell[] sudoku, int cellIndex){
        for (int i = 0; i < 9; i++) {
            if (!mWrong[cellIndex % 9 + i * 9]) {
                sudoku[cellIndex % 9 + i * 9].Button.setBackgroundResource(R.drawable.bg_btn_red);
            }
            if (!mWrong[cellIndex / 9 * 9 + i]) {
                sudoku[cellIndex / 9 * 9 + i].Button.setBackgroundResource(R.drawable.bg_btn_red);
            }
            if (!mWrong[cellIndex / 9 / 3 * 27 + cellIndex % 9 / 3 * 3 + i % 3 + i / 3 * 9]){
                sudoku[cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9].Button.setBackgroundResource(R.drawable.bg_btn_red);
            }
        }
        sudoku[cellIndex].Button.setBackgroundResource(R.drawable.bg_btn_ex_red);
        return sudoku;
    }

    // Creates and returns layout parameters for popup button
    GridLayout.LayoutParams CreatePopUpButtonParameters(){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        layoutParameters.width = mScreenWidth/4;
        layoutParameters.height = mScreenHeight/13;
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }

    // Create and return layout parameters for a sudokucell
    GridLayout.LayoutParams CreateSudokuCellParameters(int indexI, int indexJ){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();

        layoutParameters.width = mScreenWidth/13;
        layoutParameters.height = mScreenWidth/13;
        layoutParameters.setMargins(mScreenWidth / 72,mScreenHeight / 128,mScreenWidth / 72,mScreenHeight / 128);
        if (indexI==3 || indexI==6){
            layoutParameters.setMargins(layoutParameters.leftMargin,mScreenHeight / 77,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }
        if (indexJ==3 || indexJ==6){
            layoutParameters.setMargins(mScreenWidth / 43,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
        }

        return layoutParameters;
    }

    // Create and return a SudokuCell with text set based on current puzzle
    SudokuCell SetCellValue(SudokuCell sudokuCell, int[] values, int index){
        sudokuCell.Button.setBackgroundResource(R.drawable.bg_btn);
        if (values[index]==0){
            sudokuCell.Button.setText("");
        }
        else {
            sCorrectCellCount+=1;
            String word = mLanguage1.Words[values[index]];
            sudokuCell.Button.setText(word);
            sudokuCell.setLock(true);
            sudokuCell.setValue(mSudokuValues[index]);
        }
        sudokuCell.Button.setTextSize(8);
        sudokuCell.Button.setPadding(0,0,0,0);
        return sudokuCell;
    }

    // Create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Called when a Menu Button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        // When the LanguageToggle  button is clicked, switch language of all pop up menu buttons
        if(id == R.id.ChangeMode) {
            for (int i = 0; i < 81; i++) {
                int value = mSudokuCells[i].getValue();
                FillLockedCellByMode(i, value);
                if(!mSudokuCells[i].isLock()){//clean other filled cells
                    mSudokuCells[i].Button.setText("");
                    mSudokuCells[i].setValue(0);
                }
            }
            for (int j = 0; j < 9; j++) {
                if (!mIsMode1) mPopUpButtons[j].setText(mLanguage2.Words[j + 1]);
                else mPopUpButtons[j].setText(mLanguage1.Words[j + 1]);
            }
            mIsMode1 = !mIsMode1;
        }
        return super.onOptionsItemSelected(item);
    }



    // When a button is pressed this pulls up or pushes down the Pop Up Button
    // Zooms in on the selected button
    private void OnClickZoom(View pop_up_view, View sudoku_view, Button button){

        float zoom_scale = 3;
        // Move Offscreen
        if (sPopUpOnScreen) {
            // Pan to middle of sudoku
            Animate(sudoku_view, "translationX", 0f,500);
            Animate(sudoku_view, "translationY", 0f,500);
            // Move pop up view offscreen
            Animate(pop_up_view, "translationY", mScreenHeight/13*3f, 500);
            // Move buttons off screen
            Animate(findViewById(R.id.clear_button), "translationX", mScreenHeight/5f,300);
            Animate(findViewById(R.id.toggle_button), "translationX", mScreenHeight/5f,400);
            Animate(findViewById(R.id.hint_button), "translationX", mScreenHeight/5f,500);
            // Zoom out
            Animate(sudoku_view, "scaleX", 1f, 500);
            Animate(sudoku_view, "scaleY", 1f, 500);
            sPopUpOnScreen = false;
        }

        // Move Onscreen
        else {
            // Pan to the selected button
            Animate(sudoku_view, "translationX",  sudoku_view.getWidth()*1f - button.getX() * (sudoku_view.getWidth()*2/(mScreenWidth*71/80f)),500);
            Animate(sudoku_view, "translationY",  sudoku_view.getHeight()*1f - button.getY()*((sudoku_view.getHeight()+mScreenWidth*6/12)/(mScreenWidth*71/80f)),500);
            // Move the pop up view on screen
            Animate(pop_up_view, "translationY", 0f, 500);
            // Zoom in
            Animate(sudoku_view, "scaleX", zoom_scale, 500);
            Animate(sudoku_view, "scaleY", zoom_scale, 500);
            // Move buttons on screen
            Animate(findViewById(R.id.clear_button), "translationX", 0f,300);
            Animate(findViewById(R.id.toggle_button), "translationX", 0f,400);
            Animate(findViewById(R.id.hint_button), "translationX", 0f,500);

            sPopUpOnScreen = true;
        }
    }

    // Change the object's property to value over duration frames
    void Animate(Object obj, String property, Float value, int duration){
        ObjectAnimator animation = ObjectAnimator.ofFloat(obj, property, value);
        animation.setDuration(duration);
        animation.start();
    }
}


// How to make random numbers
// Random rand = new Random();
// int rand_int = rand.nextInt(10);

// Log.d("Test", "Log Message Reference")