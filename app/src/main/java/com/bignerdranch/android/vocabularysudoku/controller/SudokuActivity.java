package com.bignerdranch.android.vocabularysudoku.controller;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.widget.EditText;

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

    Resources res;// = getResources();

    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    ButtonUI[] mPopUpButtons = new ButtonUI[9];

    Button mClearButton;
    Button mToggleButton;
    Button mHintButton;   // unimplemented

    public static int sSize = 9;
    static boolean sPopUpOnScreen = false;// for pop-up-screen
    public static int sCurrentCell;
    public static int sScreenWidth, sScreenHeight;
    public int mSavedPuzzleNumber;
    public static Mode sGameMode = Mode.PLAY;

    public static boolean sIsMode1 = true;//mode1 is Language1 puzzle with Language2 filled in, determines whether the first mode is the toggled mode not
    public static Language sLanguage1 = new Language( "English", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    public static Language sLanguage2 = new Language( "Mandarin", "一", "二", "三", "四", "五", "六", "七", "八", "九");
    public static Language sPinyin = new Language("Pinyin","yī", "Èr", "san", "si", "wǔ", "liu", "qi", "ba", "jiu");
    SudokuGrid mSudokuGrid;
    boolean mIsLanguage1 = false; // determines whether the first language is the toggled language or not
    boolean mWordListImported = false;

    GridLayoutUI mSudokuLayout;
    GridLayoutUI mPopupMenu;
    ButtonUI mClearButtonUI;
    ButtonUI mToggleButtonUI;
    ButtonUI mHintButtonUI;   // unimplemented
    AlertUI mAlert;

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

        res = getResources();
        Log.d("Test", "SudokuLayoutUI");
        if (savedInstanceState == null) { // First time opening the app
            Random rand = new Random();
            int randInt = rand.nextInt(75);
            mSavedPuzzleNumber = randInt;
            String answerKey = res.getStringArray(R.array.answ)[randInt];
            String initialValues = res.getStringArray(R.array.puzz)[randInt];
            mSudokuGrid = new SudokuGrid(sSize, answerKey, initialValues);

            GridLayout gridLayout = findViewById(R.id.sudoku_grid);
            mSudokuLayout = new GridLayoutUI(gridLayout);
            mSudokuGrid.setSudokuLayout(mSudokuLayout);
//            mSudokuGrid.sendModelToView();
        } else { // Restore all saved values
            restoreGridState(savedInstanceState);
        }
        // Get width and height of screen
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        sScreenHeight = mDisplayMetrics.heightPixels;
        sScreenWidth = mDisplayMetrics.widthPixels;

        Intent intent = getIntent();
        String tmp = intent.getStringExtra("uri_key");
        if (tmp != null) {
            mWordListImported = true;
            try {
                csvUri = Uri.parse(tmp);
                readWordPairs(csvUri);
                for (int i = 1; i < 10; i++) {
                    WordPair wordPair = getRandomWordPair();
                    sLanguage1.setWord(wordPair.getWord1(), i);
                    sLanguage2.setWord(wordPair.getWord2(), i);
                    sPinyin.setWord(wordPair.getPinyin(), i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Get popup layout
        Log.d("Test", "popUpGrid");
        GridLayout popUpGrid = findViewById(R.id.pop_up_layout);
        mPopupMenu = new GridLayoutUI(popUpGrid);
        if (sScreenHeight > sScreenWidth) {
            mPopupMenu.getLayout().setTranslationY(sScreenHeight);
        } else {
            mPopupMenu.getLayout().setTranslationX(sScreenWidth);
        }

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
                    if(sGameMode==Mode.LISTEN){
                        String toSpeak = sLanguage2.getWord(ii+1);
                        Log.d("Test","Word: "+toSpeak);
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    // Zoom out once a word is selected from popup menu
                    onClickZoom(findViewById(R.id.sudoku_grid), mPopUpButtons[ii].getButton());
                    // Change Cell text and check if puzzle is finished.
                    mSudokuGrid.updateSudokuModel(ii + 1,sCurrentCell);
                    if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
                    else mSudokuGrid.sendModelToView();
                }
            });
            // Create and set parameters for button, then add button with parameters to Popup Grid
            if (sScreenHeight > sScreenWidth) {
                GridLayout.LayoutParams layoutParams = mPopUpButtons[i].CreatePopUpButtonParameters();
                mPopupMenu.getLayout().addView(mPopUpButtons[i].getButton(), layoutParams);
            } else {
                mPopupMenu.getLayout().addView(mPopUpButtons[i].getButton(), i);
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
                Toast.makeText(getApplicationContext(), sLanguage1.getWord(mSudokuGrid.getAnswers(sCurrentCell)),Toast.LENGTH_SHORT).show();
                sHint+=1;
                //need CHANGE !
//                int valueForPopUpHint = Character.getNumericValue((fullAnsw.charAt(sCurrentCell)));
//                mPopUpButtons[valueForPopUpHint].setBackgroundResource(R.drawable.bg_btn_yellow);
            }
        });
        //initially hide the buttons if in Portrait mode
        if (sScreenHeight > sScreenWidth) {
            mClearButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
            mToggleButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
            mHintButtonUI.getButton().setTranslationX(sScreenHeight / 5f);
        }

        //Log.d("Test","");
        if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
        else mSudokuGrid.sendModelToView();
    } //end of onCreate

    private void restoreGridState(Bundle savedInstanceState) {
        int randInt = savedInstanceState.getInt("SUDOKU_PUZZLE_NUMBER");
        String answerKey = res.getStringArray(R.array.answ)[randInt];
        String initialValues = res.getStringArray(R.array.puzz)[randInt];
        mSudokuGrid = new SudokuGrid(sSize, answerKey, initialValues);

        GridLayout gridLayout = findViewById(R.id.sudoku_grid);
        mSudokuLayout = new GridLayoutUI(gridLayout);
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
            if (i < 9) {

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
//            KNOWN BUG: NOT ALL THE WRONG CELLS ARE HIGHLIGHTED RED
//
//            mSudokuGrid.updateSudokuModel(0);
//            if (mWordListImported) mSudokuGrid.sendModelToView(sLanguage2);
//            else mSudokuGrid.sendModelToView();
    } // end of restoreGridState

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
        outState.putInt("SUDOKU_PUZZLE_NUMBER", mSavedPuzzleNumber);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

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
                mSudokuLayout.ToNumbers();
                Toast.makeText(getApplicationContext(), "Listen Mode",Toast.LENGTH_SHORT).show();
            }
            else if(sGameMode==Mode.LISTEN) {
                sGameMode = Mode.PLAY;
                mSudokuLayout.ToWords(mSudokuGrid);
                Toast.makeText(getApplicationContext(), "Play Mode",Toast.LENGTH_SHORT).show();
            }

            //String toSpeak = "Test";//.toString();
            //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
            //t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            //createAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

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


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    // Zooms in on the selected button
    private void onClickZoom(View sudoku_view, Button button) {
        float zoom_scale = 3;
        // Portrait Mode
        if (sScreenHeight > sScreenWidth) {
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
        // Landscape Mode
        } else {
            // Move Offscreen
            if (sPopUpOnScreen) {
                // Pan to middle of sudoku
                mSudokuLayout.Animate("translationX", 0f, 500);
                mSudokuLayout.Animate("translationY", 0f, 500);
                // Move pop up view offscreen
                mPopupMenu.Animate("translationX", sScreenWidth / 10 * 3f, 500);
                // Move buttons off screen
                mClearButtonUI.Animate("translationX", sScreenWidth / 5f, 300);
                mToggleButtonUI.Animate("translationX", sScreenWidth / 5f, 400);
                mHintButtonUI.Animate("translationX", sScreenWidth / 5f, 500);
                // Zoom out
                mSudokuLayout.Animate("scaleX", 1f, 500);
                mSudokuLayout.Animate("scaleY", 1f, 500);
                sPopUpOnScreen = false;
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

                mSudokuLayout.Animate("translationX", sudoku_view.getHeight() * 1f - button.getX() * (sudoku_view.getHeight() * 2.7f / (sScreenHeight * 71 / 80f)), 500);
                mSudokuLayout.Animate("translationY", sudoku_view.getWidth() * 1f - button.getY() * (1.5f*(sudoku_view.getWidth() + sScreenHeight * 6 / 12) / (sScreenHeight * 71 / 80f)), 500);
                // Move the pop up view on screen
                mPopupMenu.Animate("translationX", 0f, 500);
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

//    private void readWordPairs() {
//        InputStream is = getResources().openRawResource(R.raw.word_pairs);
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(is, Charset.forName("UTF-8"))
//        );
//
//        String line = "";
//        try {
//            while ((line = reader.readLine()) != null) {
//
//                String [] tokens = line.split(",");
//
//                WordPair words = new WordPair(tokens[0], tokens[1]);
//                mWordPairs.add(words);
//            }
//        } catch (IOException e) {
//            Log.wtf("MyActivity", "Error reading csv file on line" + line, e);
//            e.printStackTrace();
//        }
//    }

    private void readWordPairs(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String [] tokens = line.split(",");

                WordPair words = new WordPair(tokens[0], tokens[1], tokens[2]);
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

    private WordPair getRandomWordPair() {
        Random rand = new Random();
        int randInt = rand.nextInt(mWordPairs.size());

        WordPair wordPair = mWordPairs.get(randInt);
        mWordPairs.remove(randInt);

        return wordPair;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

//    private void addWordPair(String word1, String word2) {
//        String FILENAME = "word_pairs.csv";
//        FileOutputStream outputStream;
//
//        String line = word1 + "," + word2 + ", false\n";
//
//        try {
//            outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            outputStream.write(line.getBytes());
//            outputStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

enum Mode {
    PLAY,
    LISTEN
}



