package com.bignerdranch.android.vocabularysudoku;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class SudokuActivity extends AppCompatActivity {

    // Stefan's Contribution
    // Andi's Contribution
    // Ivan's Contributions

    // Layout
    static boolean on_screen = false;

    //Button[] myButtons = new Button[81];
    Entry[] Sudoku = new Entry[81];
    Button[] PopUpButtons = new Button[9];
    int[] values = new int[81];
    static boolean isLanguage1 = true; // determines whether the first language is the toggled language or not
    Language mLanguage1 = new Language("English","one", "two","three","four","five","six","seven","eight","nine");
    Language mLanguage2 = new Language("Mandarin","一", "二","三","四","五","六","七","八","九");
    Point size = new Point();
    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        // Creates the language toggle button in the top corner of the screen



        // This are the Pop Up Screen Buttons.
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        GridLayout pop_up_grid=findViewById(R.id.pop_up_layout);
        pop_up_grid.setTranslationY(size.y);
        for(int i = 0; i<9; i++) {
            final Button PopUpButton = new Button(this);
            PopUpButton.setText(mLanguage2.Words[i]);
            PopUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonClick(findViewById(R.id.pop_up_layout), findViewById(R.id.testing_grid), PopUpButton);
                }
            });
            GridLayout.LayoutParams l_param = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
            l_param.width = size.x/4;
            l_param.height = 150;
            l_param.bottomMargin = 0;
            pop_up_grid.addView(PopUpButton, l_param);
        }



        // Loop creates buttons and adds them to grid
        Resources res = getResources();
        for(int data = 0;data<81;data++){
            values[data]=Character.getNumericValue(res.getStringArray(R.array.puzz)[0].charAt(data));
        }
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                final Button myButton = new Button(this);
                if (values[i*9+j]==0){
                    myButton.setText("");
                }
                else {
                    myButton.setText(String.valueOf(values[i*9+j]));
                }

                // Create Listener for Button
                myButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ButtonClick(findViewById(R.id.pop_up_layout), findViewById(R.id.testing_grid), myButton);

                    }
                });

                // Put the button in the GridLayout and set its Layout Parameters
                GridLayout grid_layout = findViewById(R.id.testing_grid);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = size.x/10;
                lp.height = size.x/10;
                if (i==3 || i==6){
                    lp.setMargins(lp.leftMargin,20,lp.rightMargin,0);
                }
                if (j==3 || j==6){
                    lp.setMargins(20,lp.topMargin,lp.rightMargin,0);
                }
                grid_layout.addView(myButton, lp);
                Entry new_entry = new Entry();
                new_entry.mButton = myButton;
                Sudoku[i*9+j] = new_entry;
                //myButtons[i*9+j] = myButton;
            }
        }


    }

    // Toggle Language Button
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.LanguageToggle) {
            isLanguage1 = !isLanguage1;
        }
        return super.onOptionsItemSelected(item);
    }


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    private void ButtonClick(View pop_up_view, View sudoku_view, Button button){

        float zoom_scale = 3;
        // Move Offscreen
        if (on_screen) {

            Animate(sudoku_view, "translationX", 0f,500);
            Animate(sudoku_view, "translationY", 0f,500);

            Animate(pop_up_view, "translationY", size.y*1f, 500);
            Animate(sudoku_view, "scaleX", 1f, 500);
            Animate(sudoku_view, "scaleY", 1f, 500);
            on_screen = false;
        }

        // Move Onscreen
        else {

            Animate(sudoku_view, "translationX",  sudoku_view.getWidth()*1f - button.getX() * (sudoku_view.getWidth()*2/800f),500);
            Animate(sudoku_view, "translationY",  sudoku_view.getHeight()*1f - button.getY()*((sudoku_view.getHeight()+200)/800f),500);

            //Animate(sudoku_view, "translationX",  (zoom_scale*sudoku_view.getWidth()/2f)-button.getX()*zoom_scale,500);
            //Animate(sudoku_view, "translationY",  (zoom_scale*sudoku_view.getHeight()/2f)-button.getY()*zoom_scale,500);

            Log.d("Test", "Button.getX : " + button.getX());
            Log.d("Test", "Button.getY : " + button.getY());

            //Log.d("Test", "Screen.getX : " + sudoku_view.getWidth());
            //Log.d("Test", "Screen.getY : " + sudoku_view.getHeight());

            //Log.d("Test", "Calculation.X : " + Float.toString(button.getX()-sudoku_view.getWidth()));
            //Log.d("Test", "Calculation.Y : " + Float.toString(button.getY()-sudoku_view.getHeight()));

            Log.d("Test", " ");

            Animate(pop_up_view, "translationY", size.y*5/8f, 500);
            Animate(sudoku_view, "scaleX", zoom_scale, 500);
            Animate(sudoku_view, "scaleY", zoom_scale, 500);
            on_screen = true;
        }
    }

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