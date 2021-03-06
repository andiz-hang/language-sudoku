package com.bignerdranch.android.vocabularysudoku.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.vocabularysudoku.model.Language;
import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.model.SudokuGrid;
import com.bignerdranch.android.vocabularysudoku.model.Timer;
import com.bignerdranch.android.vocabularysudoku.model.WordPair;
import com.bignerdranch.android.vocabularysudoku.view.ButtonUI;
import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;
import com.bignerdranch.android.vocabularysudoku.view.NoticeUI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class SudokuActivity extends AppCompatActivity {

    // Variable Naming Convention:
    // m: member variable
    // c: constant
    // p: pointer
    // s: static
    // i: index

    Resources mRes;// = getResources();

    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    ButtonUI[] mPopupButtons;

    public static int sSize;
    public static int sDifficulty;
    static boolean sPopupOnScreen = false;// for pop-up-screen
    public static int sCurrentCell;
    public static int sScreenWidth, sScreenHeight;
    public static float sScreenXDPI, sScreenYDPI;
    public static boolean mIsPortraitMode;
    public boolean mListenMode;
    private SharedPreferences mSharedPreferences;
    public boolean mUseSampleFile;
    public static Mode sGameMode = Mode.PLAY;
    public String mWordOrder = "";
    public int mPuzzleNum;
    public String mUri;

    public static boolean sIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    public static Language sLanguage1;
    public static Language sLanguage2;
    SudokuGrid mSudokuGrid;
    boolean mSameLanguage = false; // whether puzzle language is same as input menu language
    boolean mWordListImported = false;
    public boolean mIsSquare;
    private float mPrevX;
    private float mPrevY;
    private boolean mScreenTapped = true;
    private int mPrevAction = 0;

    GridLayoutUI mSudokuLayout;
    GridLayoutUI mPopupMenu;
    ButtonUI mClearButtonUI;
    ButtonUI mToggleButtonUI;
    ButtonUI mHintButtonUI;

    Timer mTimer;
    long mLastPauseTime = 0;
    long mSetBase = 0;
    long mTimeToCompletePuzzle;

    TextToSpeech t1;
    TextToSpeech t2;

    public int sHint;

    List<WordPair> mWordPairs = new ArrayList<>();
    Uri csvUri;

    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        mSharedPreferences = getSharedPreferences("Sudoku", MODE_PRIVATE);

        // Get the size of the grid from main menu
        initializeIntents();

        setupTextToSpeech(Locale.TRADITIONAL_CHINESE);

        mRes = getResources();

        // Initialize language1 and language2
        initializeLanguages("English", "Mandarin");


        // Generating SudokuGrid
        Log.d("Test", "SudokuLayoutUI");
        if (savedInstanceState == null) { // First time opening the app
            fetchPuzzles();

            // Get the words from the imported file, if there is a file
            importWordsFromFile();
        } else { // Restore all saved values
            restoreGridState(savedInstanceState);
        }

        Log.d("Test", "Sudoku initialized successful");

        // Get Screen dimensions and pixel density
        getScreenInfo();

        // Initialize popup layout
        initializePopupMenu();

        Log.d("Test", "Create Popup Buttons");
        // Create Popup Buttons
        // which fill in sudoku cells and show conflicts when pressed
        for (int i = 0; i < sSize; i++) {
            // Final index ii allows inner functions to access index i
            final int ii = i;//0~8
            mPopupButtons[i] = new ButtonUI(new Button(this));

            // Set the ui of this button as a Popup Button
            mPopupButtons[i].setupPopupButton(i);

            mPopupButtons[i].getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Zoom out once a word is selected from popup menu
                    onClickZoom(findViewById(R.id.sudoku_grid), mPopupButtons[ii].getButton());
                    // Change Cell text and check if puzzle is finished.
                    if(mSudokuGrid.updateSudokuModel(ii + 1,sCurrentCell)) {
                        Toast.makeText(SudokuActivity.this, "Congrats! You Win!", Toast.LENGTH_LONG).show();
                        mTimeToCompletePuzzle = mTimer.stopTimer(); // Time to finish the puzzle is set here
                        uploadHighscore(mTimeToCompletePuzzle);
                    }
                    mSudokuGrid.sendModelToView();
                }
            });
            // Create and set parameters for button, then add button with parameters to Popup Grid
            if (mIsPortraitMode) {
                GridLayout.LayoutParams layoutParams = mPopupButtons[i].createPopupButtonParameters();
                mPopupMenu.getLayout().addView(mPopupButtons[i].getButton(), layoutParams);
            } else {
                mPopupMenu.getLayout().addView(mPopupButtons[i].getButton(), i);
            }
        }

        Log.d("Test", "Create Sudoku Buttons");
        // Create Sudoku Buttons
        // Fill mSudokuValues[data] with Sudoku values
        for (int i = 0; i < sSize; i++) {
            for (int j = 0; j < sSize; j++) {
                int index = i * sSize + j;
                final int ii = index;
                // Initialize new sudoku cell, give it a button, and add it to SudokuCells
                Button button = new Button(this);
                ButtonUI buttonUI = new ButtonUI(button, mSudokuLayout.getLayout(), i, j);

                mSudokuLayout.addButtonUI(buttonUI, index);
                // Create Listener for Button
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sGameMode==Mode.LISTEN && mSudokuGrid.getSudokuCell(ii).isLock()){
                            //String toSpeak = sLanguage2.getWord(mSudokuGrid.getSudokuCell(ii).getValue()); //sLanguage2.getWord(ii+1);
                            String toSpeak = sLanguage2.getWord(mSudokuGrid.getAnswers(ii));
                            Log.d("Test", "Language: "+sLanguage2.getName());
                            Log.d("Test", "Tospeak: "+toSpeak);
                            //Log.d("Test","Word: "+toSpeak);
                            t2.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (mScreenTapped) {
                            onClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(ii).getButton());
                            if (sPopupOnScreen)
                                mSudokuGrid.setSelected(ii);
                            else if (sCurrentCell != ii){
                                onClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(ii).getButton());
                                mSudokuGrid.setSelected(ii);
                            }
                            sCurrentCell = ii;
                            mSudokuGrid.sendModelToView();
                        }
                    }
                });
            }
        }

        // Menu Button Actions

        // Create a Menu LayoutParameters to resize the menu
        if (mIsPortraitMode) {
            fixMenu();
        }

        Button clearButton = findViewById(R.id.clear_button);//clean the filled in word

        mClearButtonUI = new ButtonUI(clearButton);
        mClearButtonUI.setupMenuButton();
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
                    mSudokuGrid.updateSudokuModel(0,sCurrentCell);
                    onClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(0).getButton());
                    mSudokuGrid.sendModelToView();
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
        mToggleButtonUI.setupMenuButton();
        mToggleButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipInputLanguage();
                if (sLanguage2.getName()=="English") setupTextToSpeech(Locale.ENGLISH);
                else if (sLanguage2.getName()=="Mandarin") setupTextToSpeech(Locale.TRADITIONAL_CHINESE);
            }
        });

        // Setup hintButton
        Button hintButton = findViewById(R.id.hint_button);// highlight right answer of pop up buttons

        mHintButtonUI = new ButtonUI(hintButton);
        mHintButtonUI.setupMenuButton();
        mHintButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Toast.makeText(getApplicationContext(), sLanguage1.getWord(mSudokuGrid.getAnswers(sCurrentCell)),Toast.LENGTH_SHORT).show();
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP, 0, 0);
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundResource(R.drawable.bg_btn_yellow);
                tv.setTextSize(30);
                tv.setPadding(10, 10, 15, 10);
                tv.setText(sLanguage1.getWord(mSudokuGrid.getAnswers(sCurrentCell)));
                toast.setView(tv);
                toast.show();
                sHint+=1;
                //need CHANGE !
//                int valueForPopupHint = Character.getNumericValue((fullAnsw.charAt(sCurrentCell)));
//                mPopupButtons[valueForPopupHint].setBackgroundResource(R.drawable.bg_btn_yellow);
            }
        });
        //initially hide the buttons if in Portrait mode
        if (mIsPortraitMode) {
            mClearButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
            mToggleButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
            mHintButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
        }

        //Log.d("Test","");
        Intent intent = getIntent();
        if(!intent.getBooleanExtra("new_game", true)){
            restoreTimer(mSharedPreferences);
                mSudokuGrid.applySavedInputs(mSharedPreferences);
        }
        mSudokuGrid.updateConflicts();
        mSudokuGrid.sendModelToView();

        // Create the timer as the LAST THING, after all other things have been created
        createTimer();

    } // END OF ONCREATE()

    @Override
    public boolean dispatchTouchEvent(MotionEvent e){
        Log.d("Test", "Language1 Word 1 :"+sLanguage1.getWord(1));
        Log.d("Test", "Language2 Word 1 :"+sLanguage2.getWord(1));
        mScreenTapped = false;
        if (mPrevAction == 0 && e.getAction()==1) mScreenTapped = true;

        if(e.getAction() == 2){
            //mSudokuLayout.getLayout().setTranslationX((mPrevX - e.getX()));
            //mSudokuLayout.getLayout().setTranslationY((mPrevY - e.getY()));

            float newPosX = mSudokuLayout.getLayout().getTranslationX() + (e.getX() - mPrevX);
            float newPosY = mSudokuLayout.getLayout().getTranslationY() + (e.getY() - mPrevY);

            float scale = 2;
            if (sPopupOnScreen) scale = 1f;

            if (newPosX > sScreenWidth/scale) newPosX = sScreenWidth/scale;
            if (newPosX < -sScreenWidth/scale) newPosX = -sScreenWidth/scale;
            if (newPosY > sScreenHeight/scale) newPosY = sScreenHeight/scale;
            if (newPosY < -sScreenHeight/scale) newPosY = -sScreenHeight/scale;

            mSudokuLayout.getLayout().setTranslationX(newPosX);
            mSudokuLayout.getLayout().setTranslationY(newPosY);
            //mSudokuLayout.animate();
        }
        //Toast.makeText(getApplicationContext(), "X = "+xVal,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "Y = "+yVal,Toast.LENGTH_SHORT).show();

        mPrevX = e.getX();
        mPrevY = e.getY();
        mPrevAction = e.getAction();
        super.dispatchTouchEvent(e);
        return true;
    }

    @Override
    public void onBackPressed(){
        savePuzzle();
        mLastPauseTime = mTimer.pauseTimer();
        saveTimer(mSharedPreferences);
        super.onBackPressed();
    }

    // When the app state changes (screen rotation), save all of the values of the app
    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<Integer> mSavedCellValues = new ArrayList<>();
        ArrayList<Integer> mSavedCellLocks = new ArrayList<>();
        ArrayList<Integer> mSavedCellConflicts = new ArrayList<>();

        for (int i = 0; i < sSize * sSize; i++) {
            mSavedCellValues.add(mSudokuGrid.getSudokuCell(i).getValue());
            if (mSudokuGrid.getSudokuCell(i).isLock()) mSavedCellLocks.add(1);
            else mSavedCellLocks.add(0);
            if (mSudokuGrid.getSudokuCell(i).isConflicting()) mSavedCellConflicts.add(1);
            else mSavedCellConflicts.add(0);
        }

        ArrayList<Integer> mSavedWrongRows = new ArrayList<>();
        ArrayList<Integer> mSavedWrongCols = new ArrayList<>();
        ArrayList<Integer> mSavedWrongBoxes = new ArrayList<>();

        ArrayList<String> mWordPairs1 = new ArrayList<>();
        ArrayList<String> mWordPairs2 = new ArrayList<>();
        for (int i = 0; i < sSize; i++) {
            if (mSudokuGrid.getWrongRow(i)) mSavedWrongRows.add(1);
            else mSavedWrongRows.add(0);
            if (mSudokuGrid.getWrongCol(i)) mSavedWrongCols.add(1);
            else mSavedWrongCols.add(0);
            if (mSudokuGrid.getWrongBox(i)) mSavedWrongBoxes.add(1);
            else mSavedWrongBoxes.add(0);

            mWordPairs1.add(sLanguage1.getWord(i + 1));
            mWordPairs2.add(sLanguage2.getWord(i + 1));
        }

        outState.putIntegerArrayList("SUDOKU_GRID_VALUES", mSavedCellValues);
        outState.putIntegerArrayList("SUDOKU_GRID_LOCKS", mSavedCellLocks);
        outState.putIntegerArrayList("SUDOKU_GRID_CONFLICTS", mSavedCellConflicts);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_ROWS", mSavedWrongRows);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_COLS", mSavedWrongCols);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_BOXES", mSavedWrongBoxes);
        outState.putInt("SUDOKU_PUZZLE_NUMBER", mSudokuGrid.getPuzzleNum());

        outState.putStringArrayList("SUDOKU_WORD_PAIRS_1", mWordPairs1);
        outState.putStringArrayList("SUDOKU_WORD_PAIRS_2", mWordPairs2);

        outState.putLong("SUDOKU_TIMER_LAST_PAUSED", mLastPauseTime);
        outState.putLong("SUDOKU_TIMER_BASE", mTimer.getBase());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }


    // Create an action bar menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Called when a Menu Button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exchange:
                Language holder1 = sLanguage1;
                sLanguage1 = sLanguage2;
                sLanguage2 = holder1;
                mSudokuGrid.sendModelToView();
                mSameLanguage = !mSameLanguage;
                if (mSameLanguage) flipInputLanguage();
                return true;
            case R.id.restart:
                for (int i = 0; i < sSize*sSize; i++) {
                    if(!mSudokuGrid.getSudokuCell(i).isLock()) {
                        mSudokuLayout.getButtonUI(i).setText("");
                        mSudokuGrid.getSudokuCell(i).setValue(0);
                    }
                }
                mSudokuGrid.updateConflicts();
                mSudokuGrid.sendModelToView();
                createTimer();
                if (mSameLanguage) flipInputLanguage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void savePuzzle(){
        mSudokuGrid.savePuzzle(mSharedPreferences, mUri, mListenMode, mUseSampleFile, mWordOrder);
    }
    public void uploadHighscore(long score){
        mSudokuGrid.uploadHighscore(mSharedPreferences,score);
    }
    // Called when the app is paused
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }

        mLastPauseTime = mTimer.pauseTimer();
        super.onPause();
    }

    // Called when the app is paused
    public void onResume(){
        // Changes the name of the action bar to the app name in Portrait mode
        // Hide the action bar in Landscape mode
        if (mIsPortraitMode) setActionBarName(getString(R.string.app_name));
        else {
            hideActionBar();
            hideStatusBar();
        }

        mTimer.startTimer(mLastPauseTime);
        super.onResume();
    }

    // METHODS

    // Restore the attributes of the grid on rotation
    void restoreGridState(Bundle savedInstanceState) {
        Log.d("Test", "WE SHOULDNT BE RESTORING");
        mPuzzleNum = savedInstanceState.getInt("SUDOKU_PUZZLE_NUMBER");
        InputStream is;
        if (sSize == 4) {
            is = getResources().openRawResource(R.raw.puzzles4);
        } else if (sSize == 6) {
            is = getResources().openRawResource(R.raw.puzzles6);
        } else if (sSize == 12) {
            is = getResources().openRawResource(R.raw.puzzles12);
        } else {
            is = getResources().openRawResource(R.raw.puzzles9);
        }
        try {
            Log.d("Test", "New Puzzle Num: "+mPuzzleNum);
            mSudokuGrid = new SudokuGrid(mPuzzleNum, sSize, sDifficulty, is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridLayout gridLayout = findViewById(R.id.sudoku_grid);
        mSudokuLayout = new GridLayoutUI(gridLayout, sSize);
        mSudokuGrid.setSudokuLayout(mSudokuLayout);

        for (int i = 0; i < sSize * sSize; i++) {
            mSudokuGrid.getSudokuCell(i).setValue(savedInstanceState.getIntegerArrayList("SUDOKU_GRID_VALUES").get(i));

            if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_LOCKS").get(i) == 1) {
                mSudokuGrid.getSudokuCell(i).setLock(true);
            } else {
                mSudokuGrid.getSudokuCell(i).setLock(false);
            }
            /*if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_CONFLICTS").get(i) == 1) {
                mSudokuGrid.getSudokuCell(i).setConflicting(true);
            } else {
                mSudokuGrid.getSudokuCell(i).setConflicting(false);
            }*/
            if (i < sSize) {/*
                if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_WRONG_ROWS").get(i) == 1) {
                    mSudokuGrid.setWrongRows(i, true);
                } else {
                    mSudokuGrid.setWrongRows(i, false);
                }
                if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_WRONG_COLS").get(i) == 1) {
                    mSudokuGrid.setWrongCols(i, true);
                } else {
                    mSudokuGrid.setWrongCols(i, false);
                }
                if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_WRONG_BOXES").get(i) == 1) {
                    mSudokuGrid.setWrongBoxes(i, true);
                } else {
                    mSudokuGrid.setWrongBoxes(i, false);
                }*/
                sLanguage1.setWord(savedInstanceState.getStringArrayList("SUDOKU_WORD_PAIRS_1").get(i), i + 1);
                sLanguage2.setWord(savedInstanceState.getStringArrayList("SUDOKU_WORD_PAIRS_2").get(i), i + 1);
            }
        }

        mLastPauseTime = savedInstanceState.getLong("SUDOKU_TIMER_LAST_PAUSED");
        mSetBase = savedInstanceState.getLong("SUDOKU_TIMER_BASE");
    }

    void fetchPuzzles() {
        Random rand = new Random();
        Intent intent = getIntent();
        if (intent.getBooleanExtra("new_game",true)){
            //mPuzzleNum = rand.nextInt(100);
        } else {
            mPuzzleNum = mSharedPreferences.getInt("SudokuNum", 0);
        }
        Log.d("Test", "Fetched SudokuNum: "+mPuzzleNum);
        InputStream is;
        if (sSize == 4) {
            is = getResources().openRawResource(R.raw.puzzles4);
        } else if (sSize == 6) {
            is = getResources().openRawResource(R.raw.puzzles6);
        } else if (sSize == 12) {
            is = getResources().openRawResource(R.raw.puzzles12);
        } else {
            is = getResources().openRawResource(R.raw.puzzles9);
        }
        try {
            Log.d("Test", "New Puzzle Num: "+mPuzzleNum);
            if (intent.getBooleanExtra("new_game",true)) {
                mSudokuGrid = new SudokuGrid(mPuzzleNum, sSize, sDifficulty, is);
            } else {
                String initialValues = mSharedPreferences.getString("InitialValues","");
                int difficulty = mSharedPreferences.getInt("difficulty",5);
                mSudokuGrid = new SudokuGrid(mPuzzleNum, sSize, difficulty, is, initialValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridLayout gridLayout = findViewById(R.id.sudoku_grid);
        mSudokuLayout = new GridLayoutUI(gridLayout, sSize);
        mSudokuGrid.setSudokuLayout(mSudokuLayout);
//            mSudokuGrid.sendModelToView();
    }

    // Initialize languages L1 and L2
    void initializeLanguages(String L1, String L2) {
        sLanguage1 = new Language(L1, sSize);
        sLanguage2 = new Language(L2, sSize);
    }

    // Initialize the popup menu
    void initializePopupMenu() {
        mPopupButtons = new ButtonUI[sSize];
        Log.d("Test", "popupGrid");
        final GridLayout popupGrid = findViewById(R.id.pop_up_layout);

        if (mIsPortraitMode) {
            if (sSize == 4) {
                mPopupMenu = new GridLayoutUI(popupGrid, 2, 2);
            } else {
                mPopupMenu = new GridLayoutUI(popupGrid, 3, sSize / 3);
            }
            mPopupMenu.getLayout().setTranslationY(sScreenHeight);
        } else {
            mPopupMenu = new GridLayoutUI(popupGrid, (sSize + 3 + 1) / 2, 2);
            mPopupMenu.getLayout().setTranslationX(sScreenWidth);
        }
    }

    // Get the user's device's screen information
    void getScreenInfo() {
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        sScreenHeight = mDisplayMetrics.heightPixels;
        sScreenWidth = mDisplayMetrics.widthPixels;
        sScreenXDPI = mDisplayMetrics.xdpi;
        sScreenYDPI = mDisplayMetrics.ydpi;

        mIsPortraitMode = (sScreenHeight > sScreenWidth);
    }

    // Gets the user inputted value of the grid size.
    // Determines if the grid is square or not
    // Default value is 9
    void getSizeFromSpinner() {
        Intent intent = getIntent();
        sSize = intent.getIntExtra("size", 9);

        // Grid is a square if the size is 4 or 9
        mIsSquare = (sSize == 4 || sSize == 9);
    }

    // Gets the user inputted difficulty.
    void getDifficultyFromBar() {
        Intent intent = getIntent();
        sDifficulty = intent.getIntExtra("diff_opt",5);
    }

    void setListenMode(){
        Intent intent = getIntent();
        if(intent.getBooleanExtra("listen_mode", false)){
            sGameMode = Mode.LISTEN;
        } else {
            sGameMode = Mode.PLAY;
        }

    }

    void initializeIntents(){
        Intent intent = getIntent();

        if (intent.getBooleanExtra("new_game",true)) {
            getSizeFromSpinner();
            getDifficultyFromBar();
            setListenMode();
            Random rand = new Random();
            mPuzzleNum = rand.nextInt(100);
            Log.d("Test", "Generated PuzzleNum: "+mPuzzleNum);
        }
        else {
            sDifficulty = mSharedPreferences.getInt("Difficulty", 5);
            sSize = mSharedPreferences.getInt("Size",9);
            mPuzzleNum = mSharedPreferences.getInt("SudokuNum", 0);
            Log.d("Test", "PuzzleNum at initialize: "+mPuzzleNum);
            mIsSquare = (sSize == 4 || sSize == 9);
            if (mSharedPreferences.getBoolean("Listen", false)){
                sGameMode = Mode.LISTEN;
            } else {
                sGameMode = Mode.PLAY;
            }
        }
    }

    // Gets the word pairs from the imported file, or the sample file,
    // and stores them into the language classes
    void importWordsFromFile() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("new_game",true)){
            Log.d("Test", "NEW GAME");
            mUri = intent.getStringExtra("uri_key");
            mUseSampleFile = intent.getBooleanExtra("use_sample_file", true);
            mListenMode = intent.getBooleanExtra("listen_mode", false); // MOVE ME
        } else {
            Log.d("Test", "CONTINUE GAME");
            mUri = mSharedPreferences.getString("Uri","");
            if (mUri == "") mUri = null;
            mUseSampleFile = mSharedPreferences.getBoolean("SampleFile", true);
            mListenMode = mSharedPreferences.getBoolean("Listen", false);
            Log.d("Test", "uri: "+mUri);
            Log.d("Test", "sample file: " + mUseSampleFile);
        }

        if (mUseSampleFile) {
            mWordListImported = true;
            fileToLanguage(R.raw.word_pairs, 15);
        } else  {
            Log.d("Test", "Import Not used");
            if (mUri != null) {
                Log.d("Test", "Uri not null");
                mWordListImported = true;
                try {
                    Log.d("Test", "try happens");
                    csvUri = Uri.parse(mUri);
                    readWordPairs(csvUri);
                    // Check if there are enough word pairs
                    if (mWordPairs.size() < sSize) {
                        notEnoughWordPairs();
                        Log.d("Test", "not enough words");
                    }
                    else {
                        Log.d("Test", "setup word pairs");
                        setupWordPairs();
                    }
                } catch (FileNotFoundException e) {
                    Log.d("Test", "catch happens");
                    e.printStackTrace();
                }
            } else {
                Log.d("Test", "uri null");
                fileToLanguage(R.raw.default_values, sSize);
            }
        }
    }

    // Gets random words from mWordPairs and puts them into language 1 and 2
    void fileToLanguage(int id, int max) {
        try {
            readWordPairs(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupWordPairs();
        /*
        for (int i = 1; i < sSize + 1; i++) {
            WordPair wordPair = getRandomWordPair(max - (i - 1));
            sLanguage1.setWord(wordPair.getWord1(), i);
            sLanguage2.setWord(wordPair.getWord2(), i);
        }
        */
    }

    void setupWordPairs(){
        Intent intent = getIntent();
        if (intent.getBooleanExtra("new_game", true)) {
            for (int i = 1; i < sSize + 1; i++) {
                WordPair wordPair = getRandomWordPair(sSize - (i - 1));
                sLanguage1.setWord(wordPair.getWord1(), i);
                sLanguage2.setWord(wordPair.getWord2(), i);
                //Log.d("Test", "Word Pair "+i+": "+wordPair.getWord1());
                //Log.d("Test", "Word Pair "+i+": "+wordPair.getWord2());
                mWordOrder = mWordOrder + wordPair.getNum();// (Integer.toString(i));
                if(i<sSize) mWordOrder = mWordOrder + " ";//.concat(" ");
            }
            Log.d("Test", "Writing Word Order: "+mWordOrder);
        } else {
            mWordOrder = mSharedPreferences.getString("WordOrder", "");
            Log.d("Test", "Loaded Word Order: "+mWordOrder);
            String[] separated = mWordOrder.split(" ");
            for (int i = 0; i < sSize; i++){
                WordPair wordPair = mWordPairs.get(Integer.parseInt(separated[i]));
                sLanguage1.setWord(wordPair.getWord1(), i+1);
                sLanguage2.setWord(wordPair.getWord2(), i+1);
                //Log.d("Test", "Word Pair "+i+": "+sLanguage1.getWord(i));
                //Log.d("Test", "Word Pair "+i+": "+sLanguage2.getWord(i));
            }
        }
        for(int i = 0; i < sSize; i++){
            Log.d("Test", "Word Pair "+i+": "+sLanguage1.getWord(i));
            Log.d("Test", "Word Pair "+i+": "+sLanguage2.getWord(i));
        }
    }

    // Hides the action bar up top
    void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    // Hides the status bar
    void hideStatusBar() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    // Sets the width and height of the menu
    void fixMenu() {
        RelativeLayout optionsMenu = findViewById(R.id.options_layout);
        android.view.ViewGroup.LayoutParams layoutParams = optionsMenu.getLayoutParams();
        layoutParams.width = sScreenWidth / 4;
        layoutParams.height = 3 * sScreenHeight / 13;
        optionsMenu.setLayoutParams(layoutParams);
    }

    // Set the title of the Action Bar
    void setActionBarName(String name) {
        if (getActionBar() != null) {
            getActionBar().setTitle(name);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }
    }

    // Toggles the language of the popup buttons
    void flipInputLanguage() {
        // Default: Language1 be the puzzle language, recognized by its name;
        // Language2 be the input language, recognized by its name
        Language holder1 = sLanguage1;
        Language holder2 = sLanguage2;
        if (mSameLanguage){
            for (int i = 0; i < sSize; i++) {
                mPopupButtons[i].setText(holder2.getWord(i+1));
            }
            mSameLanguage = false;
        }
        else {
            for (int i = 0; i < sSize; i++) {
                mPopupButtons[i].setText(holder1.getWord(i+1));
            }
            mSameLanguage = true;
        }
    }

//    void switchLanguages(){
//        Language holder = sLanguage1;
//        sLanguage1 = sLanguage2;
//        sLanguage2 = holder;
//    }


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    // Zooms in on the selected button
    void onClickZoom(View sudoku_view, Button button) {
        float zoom_scale = 3;
        // Portrait Mode
        if (mIsPortraitMode) {
            // Move Offscreen
            if (sPopupOnScreen) {
                // Pan to middle of sudoku
                mSudokuLayout.animate("translationX", 0f, 500);
                mSudokuLayout.animate("translationY", 0f, 500);
                // Move pop up view offscreen
                mPopupMenu.animate("translationY", sScreenHeight / 13 * 3f, 500);
                // Move buttons off screen
                mClearButtonUI.animate("translationX", sScreenHeight / 5f, 300);
                mToggleButtonUI.animate("translationX", sScreenHeight / 5f, 400);
                mHintButtonUI.animate("translationX", sScreenHeight / 5f, 500);
                // Zoom out
                mSudokuLayout.animate("scaleX", 1f, 500);
                mSudokuLayout.animate("scaleY", 1f, 500);

                mSudokuGrid.setSelected(-1);

                sPopupOnScreen = false;
            }
            // Move Onscreen
            else {
                // Pan to the selected button
                mSudokuLayout.animate("translationX", sudoku_view.getWidth() * 1f - button.getX() * (sudoku_view.getWidth() * 2 / (sScreenWidth * 65 / 80f)), 500);
                mSudokuLayout.animate("translationY", sudoku_view.getHeight() * 1f - button.getY() * ((sudoku_view.getHeight() + sScreenWidth * 12 / 12) / (sScreenWidth * 71 / 80f)), 500);
                // Move the pop up view on screen
                mPopupMenu.animate("translationY", 0f, 500);
                // Zoom in
                mSudokuLayout.animate("scaleX", zoom_scale, 500);
                mSudokuLayout.animate("scaleY", zoom_scale, 500);
                // Move buttons on screen

                mClearButtonUI.animate("translationX", 0f, 300);
                mToggleButtonUI.animate("translationX", 0f, 400);
                mHintButtonUI.animate("translationX", 0f, 500);

                sPopupOnScreen = true;
            }
        // Landscape Mode
        } else {
            // Move Offscreen
            if (sPopupOnScreen) {
                // Pan to middle of sudoku
                mSudokuLayout.animate("translationX", 0f, 500);
                mSudokuLayout.animate("translationY", 0f, 500);
                // Move pop up view offscreen
                mPopupMenu.animate("translationX", sScreenWidth / 10 * 3f, 500);
                // Move buttons off screen
                mClearButtonUI.animate("translationX", sScreenWidth / 5f, 300);
                mToggleButtonUI.animate("translationX", sScreenWidth / 5f, 400);
                mHintButtonUI.animate("translationX", sScreenWidth / 5f, 500);
                // Zoom out
                mSudokuLayout.animate("scaleX", 1f, 500);
                mSudokuLayout.animate("scaleY", 1f, 500);

                mSudokuGrid.setSelected(-1);

                sPopupOnScreen = false;
            }
            // Move Onscreen
            // KNOWN BUG: ZOOM IN ZOOM OUT DOESN'T WORK PROPERLY IN LANDSCAPE MODE
            else {
                // Pan to the selected button
                // Delete these Tests
//                Log.d("Test","Get Width "+sudoku_view.getWidth());
//                Log.d("Test","Get Height "+sudoku_view.getHeight());
//                Log.d("Test","button X "+button.getX());
//                Log.d("Test","button Y " + button.getY());
//                Log.d("Test", "Derived X" + (sudoku_view.getHeight() * 1f - button.getX() * (sudoku_view.getHeight() * 2 / (sScreenHeight * 71 / 80f))));
//                Log.d("Test", "Derived Y" + (sudoku_view.getWidth() * 1f - button.getY() * ((sudoku_view.getWidth() + sScreenHeight * 6 / 12) / (sScreenHeight * 71 / 80f))));

                mSudokuLayout.animate("translationX", sudoku_view.getHeight() * 1f - button.getX() * (sudoku_view.getHeight() * 2.2f / (sScreenHeight * 71 / 80f)), 500);
                mSudokuLayout.animate("translationY", sudoku_view.getWidth() * 1f - button.getY() * (1.4f*(sudoku_view.getWidth() + sScreenHeight * 6 / 12) / (sScreenHeight * 71 / 80f)), 500);
                // Move the pop up view on screen
                mPopupMenu.animate("translationX", 0f, 500);
                // Zoom in
                mSudokuLayout.animate("scaleX", zoom_scale, 500);
                mSudokuLayout.animate("scaleY", zoom_scale, 500);
                // Move buttons on screen

                mClearButtonUI.animate("translationX", 0f, 300);
                mToggleButtonUI.animate("translationX", 0f, 400);
                mHintButtonUI.animate("translationX", 0f, 500);

                sPopupOnScreen = true;
            }
        }
    }

    // Reads words from an imported csv file and stores them in mWordPairs
    void readWordPairs(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );

        String line;
        int wordCount = 0;
        try {
            while ((line = reader.readLine()) != null) {
                String [] tokens = line.split(",");

                WordPair words = new WordPair(tokens[0], tokens[1], wordCount);
                mWordPairs.add(words);
                wordCount ++;
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        }
//        fileInputStream.close();
//        parcelFileDescriptor.close();
    }

    // Reads words from our sample word list file and stores them in mWordPairs
    void readWordPairs(int id) throws IOException {
        InputStream is = getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        int wordCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");

            WordPair words = new WordPair(tokens[0], tokens[1], wordCount);
            mWordPairs.add(words);
            wordCount++;
        }
//        fileInputStream.close();
//        parcelFileDescriptor.close();
    }

    // Returns a random word pair from mWordPairs, then removes it
    WordPair getRandomWordPair(int max) {
        Random rand = new Random();
        int randInt = rand.nextInt(max);

        WordPair wordPair = mWordPairs.get(randInt);
        mWordPairs.remove(randInt);
        return wordPair;
    }

    // Informs the user that they don't have enough word pairs and sends them to the main menu
    void notEnoughWordPairs() {
        // Create a dialog box popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.notice_alert, null);
        View view = View.inflate(this, R.layout.not_enough_word_pairs, null);

        Button okay = view.findViewById(R.id.okay_button_2);
        final NoticeUI notice = new NoticeUI(builder, view, okay);

        notice.getOkay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.dismiss();
                Intent intent = new Intent(SudokuActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    void setupTextToSpeech(final Locale locale){
        //Text to Speech Initializing
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    //Log.d("Test","LANGUAGE RECOGNIZED");
                    t1.setLanguage(locale);
                    //t1.setLanguage(Locale.TRADITIONAL_CHINESE);//"zh","HK"));
                }
            }
        });
        t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    //Log.d("Test","LANGUAGE RECOGNIZED");
                    t2.setLanguage(locale);

                    //t2.setLanguage(new Locale("zh","HK"));
                }
            }
        });
    }

    void createTimer() {
        Chronometer timer = findViewById(R.id.timer);
        mTimer = new Timer(timer, mSetBase);
        mTimer.startTimer(mLastPauseTime);
    }

    public void restoreTimer(SharedPreferences preferences){
        mLastPauseTime = preferences.getLong("LastPaused", 0);
        mLastPauseTime += 800;
        mSetBase = preferences.getLong("TimerBase", SystemClock.elapsedRealtime());
    }
    public void saveTimer(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();


        editor.putLong("LastPaused", mLastPauseTime);
        editor.putLong("TimerBase", mTimer.getBase());
        editor.commit();
    }
}


//
// Code for the "input word" alert
//

//    private void createAlertDialog() {
//        // Create a dialog box popup
//        AlertDialog.Builder builder = new AlertDialog.Builder(SudokuActivity.this);
//        View view = getLayoutInflater().inflate(R.layout.word_input_dialog, null);
//        final EditText input1 = view.findViewById(R.id.et_input1);
//        final EditText input2 = view.findViewById(R.id.et_input2);
//
//        Button enter = view.findViewById(R.id.enter_button);
//        Button cancel = view.findViewById(R.id.cancel_button);
//
//        mAlert = new AlertUI(builder, view, input1, input2, enter, cancel);
//
//        // Enter button functionality
//        mAlert.getEnter().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mAlert.getInput1().getText().toString().isEmpty() && !mAlert.getInput2().getText().toString().isEmpty()) {
//                    Toast.makeText(SudokuActivity.this, "Word Pair Accepted", Toast.LENGTH_SHORT).show();
////                            String word1 = input1.getText().toString();
////                            String word2 = input2.getText().toString();
////                            addWordPair(word1, word2);
//                    mAlert.dismiss();
//                } else {
//                    Toast.makeText(SudokuActivity.this, "Please enter a word", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        mAlert.getCancel().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlert.dismiss();
//            }
//        });
//    }