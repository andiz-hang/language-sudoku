package com.bignerdranch.android.vocabularysudoku.Controller;

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
import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.Model.Language;
import com.bignerdranch.android.vocabularysudoku.Model.SudokuGrid;
import com.bignerdranch.android.vocabularysudoku.View.ButtonUI;
import com.bignerdranch.android.vocabularysudoku.View.GridLayoutUI;

public class SudokuActivity extends AppCompatActivity {

    // Variable Naming Convention:
    // m: member variable
    // c: constant
    // p: pointer
    // s: static
    // i: index

    // The number of correct, filled in cells
    public static int      sSize = 9;
    static int      sCorrectCellCount;
    static boolean  sPopUpOnScreen = false;// for pop-up-screen
    static int      sCurrentCell;
    public static int sScreenWidth, sScreenHeight;

    Resources       res = getResources();

    DisplayMetrics  mDisplayMetrics = new DisplayMetrics();
    int[]           mSudokuValues = new int[81];
    boolean         mIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    boolean         mIsLanguage1 = false; // determines whether the first language is the toggled language or not
    Language        mLanguage1 = new Language("English","one", "two","three","four","five","six","seven","eight","nine");
    Language        mLanguage2 = new Language("Mandarin","一", "二","三","四","五","六","七","八","九");
    //Language mLanguage3 = new Language("French","un", "deux","trois","quatre","cinq","six","sept","huit","neuf");

    SudokuGrid      mSudokuGrid = new SudokuGrid(sSize, res);

    GridLayoutUI    mSudokuLayout;
    GridLayoutUI    mPopupMenu;
    ButtonUI[]      mPopUpButtons = new ButtonUI[9];
    ButtonUI        mClearButtonUI;  // unimplemented
    ButtonUI        mToggleButtonUI; // unimplemented
    ButtonUI        mHintButtonUI;   // unimplemented


    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        Log.d("Test", "SudokuLayoutUI");
        GridLayout gridLayout = findViewById(R.id.sudoku_grid);
        mSudokuLayout = new GridLayoutUI(gridLayout);

        // Get width and height of screen
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        sScreenHeight = mDisplayMetrics.heightPixels;
        sScreenWidth = mDisplayMetrics.widthPixels;

        // Get popup layout
        Log.d("Test", "popUpGrid");
        GridLayout popUpGrid = findViewById(R.id.pop_up_layout);
        mPopupMenu = new GridLayoutUI(popUpGrid);
        mPopupMenu.getLayout().setTranslationY(sScreenHeight);

        Log.d("Test", "Create Popup Buttons");
        // Create Popup Buttons
        // which fill in sudoku cells and show conflicts when pressed
        for(int i = 0; i<9; i++) {
            // Final index ii allows inner functions to access index i
            final int ii = i;//0~8
            mPopUpButtons[i].setButton(new Button(this));
            mPopUpButtons[i].setText(mLanguage2.getWord(i + 1));
            mPopUpButtons[i].getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Zoom out once a word is selected from popup menu
                    OnClickZoom(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mPopUpButtons[ii].getButton());
                    // Change Cell text and check if puzzle is finished.
                    changeSudokuCellText(ii);
                }
            });
            // Create and set parameters for button, then add button with parameters to Popup Grid
            GridLayout.LayoutParams layoutParams = CreatePopUpButtonParameters();
            mPopupMenu.getLayout().addView(mPopUpButtons[i].getButton(), layoutParams);
        }

        Log.d("Test", "Create Sudoku Buttons");
        // Create Sudoku Buttons
        // Fill mSudokuValues[data] with Sudoku values
        for(int i = 0; i < sSize; i++){
            for(int j = 0; j < sSize; j++){
                int index = i * sSize + j;
                final int ii = index;
                // Initalize new sudoku cell, give it a button, and add it to SudokuCells
                Button   button = new Button(this);
                ButtonUI buttonUI = new ButtonUI(button, mSudokuLayout.getLayout(), i, j);
                mSudokuLayout.addButtonUI(buttonUI, index);

                // Create Listener for Button
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        OnClickZoom(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(ii).getButton());
                        sCurrentCell=ii;
                    }
                });
            }
        }

        // Menu Button Actions
        Button clearButton = findViewById(R.id.clear_button);//clean the filled in word
        mClearButtonUI = new ButtonUI(clearButton);
        mClearButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mSudokuCells[sCurrentCell].isLock()) {
//                    mSudokuCells[sCurrentCell].getButton().setText("");
//                    mSudokuCells[sCurrentCell].setValue(0);
//                }
                if (!mSudokuGrid.getSudokuCell(sCurrentCell).isLock()){
                    mSudokuLayout.getButtonUI(sCurrentCell).setText("");
                    mSudokuGrid.getSudokuCell(sCurrentCell).setValue(0);
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

        // Setup toggleButton
        Button toggleButton = findViewById(R.id.toggle_button);//only toggle pop up buttons' language
        mToggleButtonUI = new ButtonUI(toggleButton);
        mToggleButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipLanguage();
            }
        });
        //final String fullAnsw = res.getStringArray(R.array.answ)[randInt];

        // Setup hintButton
        Button hintButton = findViewById(R.id.hint_button);// highlight right answer of pop up buttons
        mHintButtonUI = new ButtonUI(hintButton);
        mHintButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need CHANGE !
//                int valueForPopUpHint = Character.getNumericValue((fullAnsw.charAt(sCurrentCell)));
//                mPopUpButtons[valueForPopUpHint].setBackgroundResource(R.drawable.bg_btn_yellow);
            }
        });
        //initially hide the buttons
        mClearButtonUI.getButton().setTranslationX(sScreenHeight/5f);
        mToggleButtonUI.getButton().setTranslationX(sScreenHeight/5f);
        mHintButtonUI.getButton().setTranslationX(sScreenHeight/5f);
    }//end of onCreate






    // METHODS

    void flipLanguage(){
        for (int i = 0; i < 9; i++) {
            if (mIsLanguage1)
                mPopUpButtons[i].setText(mLanguage2.getWord(i + 1));
            else
                mPopUpButtons[i].setText(mLanguage1.getWord(i + 1));
        }
        mIsLanguage1 = !mIsLanguage1;
    }
    // Return an array of potential conflicts
    int UpdateWrongArray(int cellIndex){
        boolean row=false,column=false,box=false;
        // Compares current cell to all potentially conflicting cells
        // If a conflict is found, set that cell to wrong
        // Iterates through all cells in the same row, column, and box as the current cell
        for (int j=0;j<9;j++) {
            // value           = 1-9 corresponding to the answer being put into the current cell
            // sCurrentCell = index of the current cell
            // If the current cell was correct, but the new value conflicts with a cell in the same row, column, or box:
            //     Label this cell as wrong in the mWrong array

            if (mSudokuGrid.getSudokuCell(cellIndex).getValue()!=0){
                if (CellConflictInColumn(cellIndex, mSudokuGrid.getSudokuCell(cellIndex).getValue()-1, j)){
                    column=true;
                }
                if (CellConflictInRow(cellIndex, mSudokuGrid.getSudokuCell(cellIndex).getValue()-1, j)){
                    row=true;
                }
                if (CellConflictInBox(cellIndex, mSudokuGrid.getSudokuCell(cellIndex).getValue()-1, j)){
                    box=true;
                }
            }
        }
        if (row)SetRowCellsRed(cellIndex);
        if (column)SetColumnCellsRed(cellIndex);
        if (box)SetBoxCellsRed(cellIndex);
        if (box||column||row)mSudokuLayout.getButtonUI(cellIndex).getButton().setBackgroundResource(R.drawable.bg_btn_ex_red);
        else return 1;
        return 0;
    }
    // Returns a SudokuCell array with possibly conflicting cells highlighted red
    void SetRowCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {

            if (mSudokuLayout.getButtonUI(cellIndex / 9 * 9 + i).getButton().getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_btn).getConstantState())
                mSudokuLayout.getButtonUI(cellIndex / 9 * 9 + i).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    void SetColumnCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {
            if (mSudokuLayout.getButtonUI(cellIndex % 9 + i * 9).getButton().getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_btn).getConstantState())
                mSudokuLayout.getButtonUI(cellIndex % 9 + i * 9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }
    void SetBoxCellsRed(int cellIndex) {
        for (int i = 0; i < 9; i++) {
            if (mSudokuLayout.getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_btn).getConstantState())
                mSudokuLayout.getButtonUI(cellIndex / 9 /3*27 + cellIndex%9/3*3 + i%3 + i/3*9).getButton().setBackgroundResource(R.drawable.bg_btn_red);
        }
    }

    // Return a button with its text updated
    Button FillLockedCellByMode(int index, int newValue){
        Button button = mSudokuLayout.getButtonUI(index).getButton();
        if (mIsMode1) button.setText(mLanguage2.getWord(newValue));
        else button.setText(mLanguage1.getWord(newValue));
        button.setTextColor(Color.BLUE);
        return button;
    }

    // Returns true IF (the cell in question is in the same column as the current cell)
    //                 AND (the cell in question isn't the current cell)
    boolean CellConflictInColumn(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex % 9 + distanceIndex * 9;
        return popupIndex + 1 == mSudokuGrid.getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same row as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean CellConflictInRow(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 * 9 + distanceIndex;
        return popupIndex + 1 == mSudokuGrid.getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Returns true IF (the cell in question is in the same box as the current cell)
    //                 AND(the cell in question isn't the current cell)
    boolean CellConflictInBox(int currentCellIndex, int popupIndex, int distanceIndex){
        int targetCellIndex = currentCellIndex / 9 /3*27 + currentCellIndex%9/3*3 + distanceIndex%3 + distanceIndex/3*9;
        return popupIndex + 1 == mSudokuGrid.getSudokuCell(targetCellIndex).getValue() && targetCellIndex != currentCellIndex;
    }

    // Creates and returns layout parameters for popup button
    GridLayout.LayoutParams CreatePopUpButtonParameters(){
        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        layoutParameters.width = sScreenWidth/4;
        layoutParameters.height = sScreenHeight/13;
        layoutParameters.bottomMargin = 0;
        return layoutParameters;
    }

//    // Create and return layout parameters for a sudokucell
//    GridLayout.LayoutParams CreateSudokuCellParameters(int indexI, int indexJ){
//        GridLayout.LayoutParams layoutParameters = new GridLayout.LayoutParams();
//
//        layoutParameters.width = sScreenWidth/13;
//        layoutParameters.height = sScreenWidth/13;
//        layoutParameters.setMargins(sScreenWidth / 72,sScreenHeight / 128,sScreenWidth / 72,sScreenHeight / 128);
//        if (indexI==3 || indexI==6){
//            layoutParameters.setMargins(layoutParameters.leftMargin,sScreenHeight / 77,layoutParameters.rightMargin,layoutParameters.bottomMargin);
//        }
//        if (indexJ==3 || indexJ==6){
//            layoutParameters.setMargins(sScreenWidth / 43,layoutParameters.topMargin,layoutParameters.rightMargin,layoutParameters.bottomMargin);
//        }
//
//        return layoutParameters;
//    }


//    // Create and return a SudokuCell with text set based on current puzzle
//    SudokuCell SetCellValue(SudokuCell sudokuCell, int[] values, int index){
//        sudokuCell.getButton().setBackgroundResource(R.drawable.bg_btn);
//        if (values[index]==0){
//            sudokuCell.getButton().setText("");
//        }
//        else {
//            sCorrectCellCount+=1;
//            String word = mLanguage1.getWord(values[index]);
//            sudokuCell.getButton().setText(word);
//            sudokuCell.setLock(true);
//            sudokuCell.setValue(mSudokuValues[index]);
//        }
//        sudokuCell.getButton().setTextSize(8);
//        sudokuCell.getButton().setPadding(0,0,0,0);
//        return sudokuCell;
//    }

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
                int value = mSudokuGrid.getSudokuCell(i).getValue();
                FillLockedCellByMode(i, value);
                if(!mSudokuGrid.getSudokuCell(i).isLock()){//clean other filled cells
                    mSudokuLayout.getButtonUI(i).getButton().setText("");
                    mSudokuGrid.getSudokuCell(i).setValue(0);
                }
            }
            for (int j = 0; j < 9; j++) {
                if (!mIsMode1) mPopUpButtons[j].setText(mLanguage2.getWord(j + 1));
                else mPopUpButtons[j].setText(mLanguage1.getWord(j + 1));
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
            Animate(pop_up_view, "translationY", sScreenHeight/13*3f, 500);
            // Move buttons off screen
            Animate(findViewById(R.id.clear_button), "translationX", sScreenHeight/5f,300);
            Animate(findViewById(R.id.toggle_button), "translationX", sScreenHeight/5f,400);
            Animate(findViewById(R.id.hint_button), "translationX", sScreenHeight/5f,500);
            // Zoom out
            Animate(sudoku_view, "scaleX", 1f, 500);
            Animate(sudoku_view, "scaleY", 1f, 500);
            sPopUpOnScreen = false;
        }

        // Move Onscreen
        else {
            // Pan to the selected button
            Animate(sudoku_view, "translationX",  sudoku_view.getWidth()*1f - button.getX() * (sudoku_view.getWidth()*2/(sScreenWidth*71/80f)),500);
            Animate(sudoku_view, "translationY",  sudoku_view.getHeight()*1f - button.getY()*((sudoku_view.getHeight()+sScreenWidth*6/12)/(sScreenWidth*71/80f)),500);
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

    void changeSudokuCellText(int index){
        // If a cell isn't locked, set its text to the chosen word and show any conflicts
        if(!mSudokuGrid.getSudokuCell(sCurrentCell).isLock()){
            // Fills current cell's button with input value
            mSudokuLayout.getButtonUI(sCurrentCell).setButton(FillLockedCellByMode(sCurrentCell, index+1));
            mSudokuGrid.getSudokuCell(sCurrentCell).setValue(index+1);

            int count=0,correct;
            for (int y=0;y<81;y++)
                mSudokuLayout.getButtonUI(y).getButton().setBackgroundResource(R.drawable.bg_btn);
            for (int x=0;x<81;x++) {
                correct = UpdateWrongArray(x);
                if (correct == 1 && mSudokuGrid.getSudokuCell(x).getValue() != 0)
                    count+=1;
            }
            if (count==81)
                Toast.makeText(getApplicationContext(),"Congrats! You win!",Toast.LENGTH_SHORT).show();
        }
    }

    // Change the object's property to value over duration frames
    // REMOVE ME
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