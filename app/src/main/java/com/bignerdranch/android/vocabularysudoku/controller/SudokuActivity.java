package com.bignerdranch.android.vocabularysudoku.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.EditText;
import static java.lang.Math.sqrt;

import com.bignerdranch.android.vocabularysudoku.model.Language;
import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.model.SudokuGrid;
import com.bignerdranch.android.vocabularysudoku.model.WordPair;
import com.bignerdranch.android.vocabularysudoku.view.ButtonUI;
import com.bignerdranch.android.vocabularysudoku.view.GridLayoutUI;
import com.bignerdranch.android.vocabularysudoku.view.AlertUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    static boolean sPopupOnScreen = false;// for pop-up-screen
    public static int sCurrentCell;
    public static int sScreenWidth, sScreenHeight;
    public static float sScreenXDPI, sScreenYDPI;
    public static boolean mIsPortraitMode;
    public boolean mListenMode;
    public int mSavedPuzzleNumber;
    public static Mode sGameMode = Mode.PLAY;

    public static boolean sIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    public static Language sLanguage1;
    public static Language sLanguage2;
    SudokuGrid mSudokuGrid;
    boolean mIsLanguage1 = false; // determines whether the first language is the toggled language or not
    boolean mWordListImported = false;
    public boolean mIsSquare;

    GridLayoutUI mSudokuLayout;
    GridLayoutUI mPopupMenu;
    ButtonUI mClearButtonUI;
    ButtonUI mToggleButtonUI;
    ButtonUI mHintButtonUI;

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

        // Get the size of the grid from main menu
        getSizeFromSpinner();

        //Text to Speech Initializing
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    Log.d("Test","LANGUAGE RECOGNIZED");
                    t1.setLanguage(Locale.TRADITIONAL_CHINESE);//"zh","HK"));
                }
            }
        });
        t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    Log.d("Test","LANGUAGE RECOGNIZED");
                    t2.setLanguage(new Locale("zh","HK"));
                }
            }
        });

        mRes = getResources();

        // Generating SudokuGrid
        Log.d("Test", "SudokuLayoutUI");
        if (savedInstanceState == null) { // First time opening the app
            Random rand = new Random();
            int randInt = rand.nextInt(75);
            mSudokuGrid = new SudokuGrid(this, sSize, randInt);

            GridLayout gridLayout = findViewById(R.id.sudoku_grid);
            mSudokuLayout = new GridLayoutUI(gridLayout, sSize);
            mSudokuGrid.setSudokuLayout(mSudokuLayout);
//            mSudokuGrid.sendModelToView();
        } else // Restore all saved values
            restoreGridState(savedInstanceState);

        Log.d("Test", "Sudoku initialized successful");

        // Get Screen dimensions and pixel density
        getScreenInfo();

        // Initialize language1 and language2
        initializeLanguages("English", "Mandarin");

        // Get the words from the imported file, if there is a file
        importWordsFromFile();

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
                    if(sGameMode==Mode.LISTEN){
                        String toSpeak = sLanguage2.getWord(ii+1);
                        Log.d("Test","Word: "+toSpeak);
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    // Zoom out once a word is selected from popup menu
                    onClickZoom(findViewById(R.id.sudoku_grid), mPopupButtons[ii].getButton());
                    // Change Cell text and check if puzzle is finished.
                    mSudokuGrid.updateSudokuModel(ii + 1,sCurrentCell);
                    if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
                    else mSudokuGrid.sendModelToView();
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
                        if(sGameMode==Mode.LISTEN){
                            String toSpeak = sLanguage2.getWord(mSudokuGrid.getSudokuCell(ii).getValue()); //sLanguage2.getWord(ii+1);
                            Log.d("Test","Word: "+toSpeak);
                            t2.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        onClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(ii).getButton());
                        sCurrentCell = ii;
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
                    if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
                    else mSudokuGrid.sendModelToView();
                    onClickZoom(findViewById(R.id.sudoku_grid), mSudokuLayout.getButtonUI(0).getButton());
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
                flipLanguage();
            }
        });

        // Setup hintButton
        Button hintButton = findViewById(R.id.hint_button);// highlight right answer of pop up buttons

        mHintButtonUI = new ButtonUI(hintButton);
        mHintButtonUI.setupMenuButton();
        mHintButtonUI.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), sLanguage1.getWord(mSudokuGrid.getAnswers(sCurrentCell)),Toast.LENGTH_SHORT).show();
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
        if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
        else mSudokuGrid.sendModelToView();
    } // END OF ONCREATE()

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
        for (int i = 0; i < sSize; i++) {
            if (mSudokuGrid.getWrongRow(i)) mSavedWrongRows.add(1);
            else mSavedWrongRows.add(0);
            if (mSudokuGrid.getWrongCol(i)) mSavedWrongCols.add(1);
            else mSavedWrongCols.add(0);
            if (mSudokuGrid.getWrongBox(i)) mSavedWrongBoxes.add(1);
            else mSavedWrongBoxes.add(0);
        }

        outState.putIntegerArrayList("SUDOKU_GRID_VALUES", mSavedCellValues);
        outState.putIntegerArrayList("SUDOKU_GRID_LOCKS", mSavedCellLocks);
        outState.putIntegerArrayList("SUDOKU_GRID_CONFLICTS", mSavedCellConflicts);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_ROWS", mSavedWrongRows);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_COLS", mSavedWrongCols);
        outState.putIntegerArrayList("SUDOKU_GRID_WRONG_BOXES", mSavedWrongBoxes);
        outState.putInt("SUDOKU_PUZZLE_NUMBER", mSudokuGrid.getPuzzleNum());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    // Create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Called when a Menu Button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When "Listen Mode" is clicked, allow the user to input a word for the word pair thing
        if (id == R.id.listen_mode_button) {
            if(sGameMode==Mode.PLAY) {
                sGameMode = Mode.LISTEN;
                mSudokuLayout.toNumbers();
                Toast.makeText(getApplicationContext(), "Listen Mode",Toast.LENGTH_SHORT).show();
            }
            else if(sGameMode==Mode.LISTEN) {
                sGameMode = Mode.PLAY;
                mSudokuLayout.toWords(mSudokuGrid);
                Toast.makeText(getApplicationContext(), "Play Mode",Toast.LENGTH_SHORT).show();
            }

            //String toSpeak = "Test";//.toString();
            //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
            //t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            //createAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // Called when the app is paused
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
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
        super.onResume();
    }

    // METHODS

    // Restore the attributes of the grid on rotation
    void restoreGridState(Bundle savedInstanceState) {
        int puzzleNum = savedInstanceState.getInt("SUDOKU_PUZZLE_NUMBER");
        mSudokuGrid = new SudokuGrid(this, sSize, puzzleNum);

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
            if (savedInstanceState.getIntegerArrayList("SUDOKU_GRID_CONFLICTS").get(i) == 1) {
                mSudokuGrid.getSudokuCell(i).setConflicting(true);
            } else {
                mSudokuGrid.getSudokuCell(i).setConflicting(false);
            }
            if (i < sSize) {
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
                }
            }
        }
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

    // Gets the user inputted value of the grid size.
    // Determines if the grid is square or not
    // Default value is 9
    void getSizeFromSpinner() {
        Intent intent = getIntent();
        sSize = intent.getIntExtra("size", 9);

        // Grid is a square if the size is 4 or 9
        mIsSquare = (sSize == 4 || sSize == 9);
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

    // Gets the word pairs from the imported file, or the sample file,
    // and stores them into the language classes
    void importWordsFromFile() {
        Intent intent = getIntent();
        String tmp = intent.getStringExtra("uri_key");
        boolean useSampleFile = intent.getBooleanExtra("use_sample_file", false);
        mListenMode = intent.getBooleanExtra("listen_mode", false); // MOVE ME
        if (useSampleFile) {
            mWordListImported = true;
            fileToLanguage(R.raw.word_pairs);
        } else  {
            if (tmp != null) {
                mWordListImported = true;
                try {
                    csvUri = Uri.parse(tmp);
                    readWordPairs(csvUri);
                    for (int i = 1; i < sSize + 1; i++) {
                        WordPair wordPair = getRandomWordPair(sSize - (i - 1));
                        sLanguage1.setWord(wordPair.getWord1(), i);
                        sLanguage2.setWord(wordPair.getWord2(), i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                fileToLanguage(R.raw.default_values);
            }
        }
    }

    // Gets random words from mWordPairs and puts them into language 1 and 2
    void fileToLanguage(int id) {
        try {
            readWordPairs(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < sSize + 1; i++) {
            WordPair wordPair = getRandomWordPair(sSize - (i - 1));
            sLanguage1.setWord(wordPair.getWord1(), i);
            sLanguage2.setWord(wordPair.getWord2(), i);
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

    // Toggles the language of the popup buttons and
    // Flips the boolean mIsLanguage1!
    void flipLanguage() {
        for (int i = 0; i < sSize; i++) {
            if (mIsLanguage1)
                mPopupButtons[i].setText(sLanguage2.getWord(i + 1));
            else
                mPopupButtons[i].setText(sLanguage1.getWord(i + 1));
        }
        mIsLanguage1 = !mIsLanguage1;
    }

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

                sPopupOnScreen = false;
            }
            // Move Onscreen
            else {
                // Pan to the selected button
                mSudokuLayout.animate("translationX", sudoku_view.getWidth() * 1f - button.getX() * (sudoku_view.getWidth() * 2 / (sScreenWidth * 71 / 80f)), 500);
                mSudokuLayout.animate("translationY", sudoku_view.getHeight() * 1f - button.getY() * ((sudoku_view.getHeight() + sScreenWidth * 6 / 12) / (sScreenWidth * 71 / 80f)), 500);
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
                sPopupOnScreen = false;
            }
            // Move Onscreen
            // KNOWN BUG: ZOOM IN ZOOM OUT DOESN'T WORK PROPERLY IN LANDSCAPE MODE
            else {
                // Pan to the selected button
                Log.d("Test","Get Width "+sudoku_view.getWidth());
                Log.d("Test","Get Height "+sudoku_view.getHeight());
                Log.d("Test","button X "+button.getX());
                Log.d("Test","button Y " + button.getY());
                Log.d("Test", "Derived X" + (sudoku_view.getHeight() * 1f - button.getX() * (sudoku_view.getHeight() * 2 / (sScreenHeight * 71 / 80f))));
                Log.d("Test", "Derived Y" + (sudoku_view.getWidth() * 1f - button.getY() * ((sudoku_view.getWidth() + sScreenHeight * 6 / 12) / (sScreenHeight * 71 / 80f))));

                mSudokuLayout.animate("translationX", sudoku_view.getHeight() * 1f - button.getX() * (sudoku_view.getHeight() * 2.7f / (sScreenHeight * 71 / 80f)), 500);
                mSudokuLayout.animate("translationY", sudoku_view.getWidth() * 1f - button.getY() * (1.5f*(sudoku_view.getWidth() + sScreenHeight * 6 / 12) / (sScreenHeight * 71 / 80f)), 500);
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
        try {
            while ((line = reader.readLine()) != null) {
                String [] tokens = line.split(",");

                WordPair words = new WordPair(tokens[0], tokens[1]);
                mWordPairs.add(words);
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

        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");

            WordPair words = new WordPair(tokens[0], tokens[1]);
            mWordPairs.add(words);
        }
//        fileInputStream.close();
//        parcelFileDescriptor.close();
    }

    // Returns a random word pair from mWordPairs
    WordPair getRandomWordPair(int max) {
        Random rand = new Random();
        int randInt = rand.nextInt(max);

        WordPair wordPair = mWordPairs.get(randInt);
        mWordPairs.remove(randInt);

        return wordPair;
    }
}

enum Mode {
    PLAY,
    LISTEN
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