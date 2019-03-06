package com.bignerdranch.android.vocabularysudoku.controller;

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

import com.bignerdranch.android.vocabularysudoku.model.Language;
import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.model.SudokuCell;
import com.bignerdranch.android.vocabularysudoku.model.SudokuGrid;
import com.bignerdranch.android.vocabularysudoku.view.ButtonUI;
import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;


import java.util.Random;


public class SudokuActivity extends AppCompatActivity {

    // Variable Naming Convention:
    // m: member variable
    // c: constant
    // p: pointer
    // s: static
    // i: index

    Resources res;// = getResources();

    // Holds location of incorrect cells
    boolean[] mWrong = new boolean[81];
    int mScreenWidth, mScreenHeight;
    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    SudokuCell[] mSudokuCells = new SudokuCell[81];
    ButtonUI[] mPopUpButtons = new ButtonUI[9];
    //    Button[]        mPopUpButtons = new Button[9];
    int[] mSudokuValues = new int[81];
    Button mClearButton;  // unimplemented
    Button mToggleButton; // unimplemented
    Button mHintButton;   // unimplemented


    public static int sSize = 9;
    static int sCorrectCellCount;
    static boolean sPopUpOnScreen = false;// for pop-up-screen
    public static int sCurrentCell;
    public static int sScreenWidth, sScreenHeight;

    public static boolean sIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    public static Language sLanguage1 = new Language("English", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    public static Language sLanguage2 = new Language("Mandarin", "一", "二", "三", "四", "五", "六", "七", "八", "九");
    //public static Language sLanguage3 = new Language("French","un", "deux","trois","quatre","cinq","six","sept","huit","neuf");
    SudokuGrid mSudokuGrid;
    boolean mIsLanguage1 = false; // determines whether the first language is the toggled language or not

    GridLayoutUI mSudokuLayout;
    GridLayoutUI mPopupMenu;
    ButtonUI mClearButtonUI;  // unimplemented
    ButtonUI mToggleButtonUI; // unimplemented
    ButtonUI mHintButtonUI;   // unimplemented

    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        res = getResources();
        Random rand = new Random();
        int randInt = rand.nextInt(75);
        String answerKey = res.getStringArray(R.array.answ)[randInt];
        String initialValues = res.getStringArray(R.array.puzz)[randInt];
        mSudokuGrid = new SudokuGrid(sSize, answerKey, initialValues);
        //mSudokuGrid.sendModelToView();

        Log.d("Test", "SudokuLayoutUI");
        GridLayout gridLayout = findViewById(R.id.sudoku_grid);
        mSudokuLayout = new GridLayoutUI(gridLayout);
        mSudokuGrid.setSudokuLayout(mSudokuLayout);

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
        for (int i = 0; i < 9; i++) {
            // Final index ii allows inner functions to access index i
            final int ii = i;//0~8
            mPopUpButtons[i] = new ButtonUI(new Button(this));
            mPopUpButtons[i].setButton(new Button(this));
            mPopUpButtons[i].setText(sLanguage2.getWord(i + 1));
            mPopUpButtons[i].getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Zoom out once a word is selected from popup menu
                    OnClickZoom(findViewById(R.id.sudoku_grid), mPopUpButtons[ii].getButton());
                    // Change Cell text and check if puzzle is finished.
                    mSudokuGrid.updateSudokuModel(ii);
                    mSudokuGrid.sendModelToView();
                }
            });
            // Create and set parameters for button, then add button with parameters to Popup Grid
            GridLayout.LayoutParams layoutParams = mPopUpButtons[i].CreatePopUpButtonParameters();
            mPopupMenu.getLayout().addView(mPopUpButtons[i].getButton(), layoutParams);
        }

        Log.d("Test", "Create Sudoku Buttons");
        // Create Sudoku Buttons
        // Fill mSudokuValues[data] with Sudoku values
        for (int i = 0; i < sSize; i++) {
            for (int j = 0; j < sSize; j++) {
                int index = i * sSize + j;
                final int ii = index;
                // Initalize new sudoku cell, give it a button, and add it to SudokuCells
                Button button = new Button(this);
                ButtonUI buttonUI = new ButtonUI(button, mSudokuLayout.getLayout(), i, j);
                mSudokuLayout.addButtonUI(buttonUI, index);
                // Create Listener for Button
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(ii).getButton());
                        sCurrentCell = ii;
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
                if (!mSudokuGrid.getSudokuCell(sCurrentCell).isLock()) {
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
//                        mWrong = FindConflictAtIndex(mWrong,mSudokuCells,i, mSudokuCells[i].getValue());
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
        mClearButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
        mToggleButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
        mHintButtonUI.getButton().setTranslationX(sScreenHeight / 5f);


        //Log.d("Test","");
        mSudokuGrid.sendModelToView();
    }
    //end of onCreate

    // METHODS

    void flipLanguage() {
        for (int i = 0; i < 9; i++) {
            if (mIsLanguage1)
                mPopUpButtons[i].setText(sLanguage2.getWord(i + 1));
            else
                mPopUpButtons[i].setText(sLanguage1.getWord(i + 1));
        }
        mIsLanguage1 = !mIsLanguage1;
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
        if (id == R.id.ChangeMode) {
            for (int i = 0; i < 81; i++) {
                int value = mSudokuGrid.getSudokuCell(i).getValue();
                mSudokuLayout.FillLockedCellByMode(i, value);
                if (!mSudokuGrid.getSudokuCell(i).isLock()) {//clean other filled cells
                    mSudokuLayout.getButtonUI(i).getButton().setText("");
                    mSudokuGrid.getSudokuCell(i).setValue(0);
                }
            }
            for (int j = 0; j < 9; j++) {
                if (!sIsMode1) mPopUpButtons[j].setText(sLanguage2.getWord(j + 1));
                else mPopUpButtons[j].setText(sLanguage1.getWord(j + 1));
            }
            sIsMode1 = !sIsMode1;
        }
        return super.onOptionsItemSelected(item);
    }


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    // Zooms in on the selected button
    private void OnClickZoom(View sudoku_view, Button button) {
        float zoom_scale = 3;
        // Move Offscreen
        if (sPopUpOnScreen) {
            // Pan to middle of sudoku
            mSudokuLayout.Animate("translationX", 0f, 500);
            mSudokuLayout.Animate("translationY", 0f, 500);
            // Move pop up view offscreen
            mPopupMenu.Animate("translationY", sScreenHeight / 13 * 3f, 500);
            // Move buttons off screen
            mClearButtonUI.Animate("translationX", sScreenHeight / 5f, 300);
            mToggleButtonUI.Animate("translationX", sScreenHeight / 5f, 400);
            mHintButtonUI.Animate("translationX", sScreenHeight / 5f, 500);
            // Zoom out
            mSudokuLayout.Animate("scaleX", 1f, 500);
            mSudokuLayout.Animate("scaleY", 1f, 500);
            sPopUpOnScreen = false;
        }
        // Move Onscreen
        else {
            // Pan to the selected button
            mSudokuLayout.Animate("translationX", sudoku_view.getWidth() * 1f - button.getX() * (sudoku_view.getWidth() * 2 / (sScreenWidth * 71 / 80f)), 500);
            mSudokuLayout.Animate("translationY", sudoku_view.getHeight() * 1f - button.getY() * ((sudoku_view.getHeight() + sScreenWidth * 6 / 12) / (sScreenWidth * 71 / 80f)), 500);
            // Move the pop up view on screen
            mPopupMenu.Animate("translationY", 0f, 500);
            // Zoom in
            mSudokuLayout.Animate("scaleX", zoom_scale, 500);
            mSudokuLayout.Animate("scaleY", zoom_scale, 500);
            // Move buttons on screen

            mClearButtonUI.Animate("translationX", 0f, 300);
            mToggleButtonUI.Animate("translationX", 0f, 400);
            mHintButtonUI.Animate("translationX", 0f, 500);

            sPopUpOnScreen = true;
        }
    }

}



