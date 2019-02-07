package com.bignerdranch.android.vocabularysudoku;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
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
    
    //Variable Naming Convention:
    // m: member variable
    // c: constant
    // p: pointer
    // s: static
    // i: index

    // Layout
    static boolean  sPopUpOnScreen = false;// for pop-up-screen
    static int      sCurrentCell;

    // Holds location of incorrect cells
    int             mWrong[]= new int[81];
    int             mScreenWidth, mScreenHeight;
    // The number of correct, filled in cells
    int             mCorrectCellCount;
    DisplayMetrics  mDisplayMetrics = new DisplayMetrics();
    SudokuCell[]    mSudokuCells = new SudokuCell[81];
    Button[]        mPopUpButtons = new Button[9];

    int[]           mValues = new int[81];
    boolean         mIsLanguage1 = false; // determines whether the first language is the toggled language or not
    Language        mLanguage1 = new Language("English","one", "two","three","four","five","six","seven","eight","nine");
    Language        mLanguage2 = new Language("Mandarin","一", "二","三","四","五","六","七","八","九");
    //Language mLanguage3 = new Language("French","un", "deux","trois","quatre","cinq","six","sept","huit","neuf");
    //Point         size = new Point();
    private Button mClearButton;
    private Button mToggleButton;
    private Button mHintButton;

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
        for(int i = 0; i<9; i++) {
            final int ii = i;
            mPopUpButtons[i] = new Button(this);
            mPopUpButtons[i].setText(mLanguage2.Words[i+1]);
            mPopUpButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonClick(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mPopUpButtons[ii]);
                    if(! mSudokuCells[sCurrentCell].isLock()){
                        mSudokuCells[sCurrentCell].Button.setText(mLanguage2.Words[ii+1]);
                        mSudokuCells[sCurrentCell].Button.setTextColor(Color.BLUE);
                        if (mSudokuCells[sCurrentCell].getIndex()==0){
                            mCorrectCellCount+=1;
                        }
                        mSudokuCells[sCurrentCell].setIndex(ii+1);
                        int prev_state = mWrong[sCurrentCell];
                        mWrong[sCurrentCell] = 0;

                        // Compares sCurrentCell to all potentially conflicting cells
                        // If a conflict is found, set that cell to mWrong.


                        for (int x=0;x<9;x++) {
                            if (mWrong[sCurrentCell] == 0 && ((ii + 1 == mSudokuCells[sCurrentCell % 9 + x * 9].getIndex() && sCurrentCell % 9 + x * 9 != sCurrentCell )||(ii + 1 == mSudokuCells[sCurrentCell / 9 * 9 + x].getIndex() && sCurrentCell / 9 * 9 + x != sCurrentCell)||(ii + 1 == mSudokuCells[sCurrentCell / 9 /3*27 + sCurrentCell%9/3*3 + x%3 + x/3*9].getIndex() && sCurrentCell / 9 /3*27 + sCurrentCell%9/3*3 + x%3 + x/3*9 != sCurrentCell))) {
                                mWrong[sCurrentCell] = 1;
                            }
                        }
                        // If cell was mWrong, but is now correct
                        if ((prev_state==1)&&(mWrong[sCurrentCell]==0)){
                            mCorrectCellCount+=1;
                            // Set all cells back to default button
                            for (int x=0;x<9;x++){
                                mSudokuCells[sCurrentCell / 9 /3*27 + sCurrentCell%9/3*3 + x%3 + x/3*9].Button.setBackgroundResource(R.drawable.bg_btn);
                                mSudokuCells[sCurrentCell % 9 + x * 9].Button.setBackgroundResource(R.drawable.bg_btn);
                                mSudokuCells[sCurrentCell / 9 * 9 + x].Button.setBackgroundResource(R.drawable.bg_btn);
                            }
                        }
                        if ((prev_state==0)&&(mWrong[sCurrentCell]==1)){
                            mCorrectCellCount-=1;
                        }
                        if (mCorrectCellCount==81){
                            Toast.makeText(getApplicationContext(),"Congrats! You win!",Toast.LENGTH_SHORT).show();
                        }
                        for (int x=0;x<81;x++) {
                            if (mWrong[x]==1) {
                                for (int y = 0; y < 9; y++) {
                                    if (mWrong[x % 9 + y * 9] != 1) {
                                        mSudokuCells[x % 9 + y * 9].Button.setBackgroundResource(R.drawable.bg_btn_red);
                                    }
                                    if (mWrong[x / 9 * 9 + y] != 1) {
                                        mSudokuCells[x / 9 * 9 + y].Button.setBackgroundResource(R.drawable.bg_btn_red);
                                    }
                                    if (mWrong[x / 9 /3*27 + x%9/3*3 + y%3 + y/3*9]!=1){
                                        mSudokuCells[x / 9 /3*27 + x%9/3*3 + y%3 + y/3*9].Button.setBackgroundResource(R.drawable.bg_btn_red);
                                    }
                                }
                                mSudokuCells[x].Button.setBackgroundResource(R.drawable.bg_btn_ex_red);
                            }
                        }
                    }
                }
            });
            GridLayout.LayoutParams l_param = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
            l_param.width = mScreenWidth/4;
            l_param.height = mScreenHeight/13;
            l_param.bottomMargin = 0;
            //l_param.setGravity(Gravity.TOP);
            pop_up_grid.addView(mPopUpButtons[i], l_param);
        }



        // Loop creates buttons and adds them to grid
        Random rand = new Random();
        int rand_int = rand.nextInt(75);
        Resources res = getResources();
        String full=res.getStringArray(R.array.puzz)[rand_int];//gets puzzle 0
        //Character.getNumericValue change string to int
        for(int data = 0;data<81;data++) {mValues[data] = Character.getNumericValue(full.charAt(data));}
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                int index = i*9+j;
                final int ii = index;
                SudokuCell temp = new SudokuCell();
                temp.Button = new Button(this);
                mSudokuCells[index]= temp;
                mWrong[index]=0;
                mSudokuCells[index].Button.setBackgroundResource(R.drawable.bg_btn);
                if (mValues[index]==0){
                    mSudokuCells[index].Button.setText("");
                }
                else {
                    mCorrectCellCount+=1;
                    String word = mLanguage1.Words[mValues[index]];
                    mSudokuCells[index].Button.setText(word);
                    mSudokuCells[index].setLock(true);
                    mSudokuCells[index].setIndex(mValues[index]);
                }
                mSudokuCells[index].Button.setTextSize(8);
                // Create Listener for Button
                mSudokuCells[index].Button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ButtonClick(findViewById(R.id.pop_up_layout), findViewById(R.id.sudoku_grid), mSudokuCells[ii].Button);
                        sCurrentCell=ii;
                    }
                });

                // Put the button in the GridLayout and set its Layout Parameters
                GridLayout grid_layout = findViewById(R.id.sudoku_grid);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = mScreenWidth/13;
                lp.height = mScreenWidth/13;
                mSudokuCells[index].Button.setPadding(0,0,0,0);
                lp.setMargins(mScreenWidth / 72,mScreenHeight / 128,mScreenWidth / 72,mScreenHeight / 128);
                if (i==3 || i==6){
                    lp.setMargins(lp.leftMargin,mScreenHeight / 77,lp.rightMargin,lp.bottomMargin);
                }
                if (j==3 || j==6){
                    lp.setMargins(mScreenWidth / 43,lp.topMargin,lp.rightMargin,lp.bottomMargin);
                }
                grid_layout.addView(mSudokuCells[index].Button, lp);
            }
        }

        // Menu Button Actions
        mClearButton =(Button) findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to be filled
            }
        });
        mToggleButton = (Button) findViewById(R.id.toggle_button);
        mHintButton = (Button) findViewById(R.id.hint_button);
    }


    int[] checkCell(int[] mWrong){
        return mWrong;
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
        if (id == R.id.LanguageToggle) {
            mIsLanguage1 = !mIsLanguage1;
            for (int i = 0; i < 9; i++) {
                if (mIsLanguage1) mPopUpButtons[i].setText(mLanguage1.Words[i + 1]);
                else mPopUpButtons[i].setText(mLanguage2.Words[i + 1]);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // When a button is pressed this pulls up or pushes down the Pop Up Button
    // Zooms in on the selected button
    private void ButtonClick(View pop_up_view, View sudoku_view, Button button){

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