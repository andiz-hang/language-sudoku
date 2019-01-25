package com.bignerdranch.android.vocabularysudoku;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        // This is the pop up screen. Currently just a button.
        for(int i = 0; i<9; i++) {
            final Button PopUpButton = new Button(this);
            PopUpButton.setText("Pop Up Screen");
            PopUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonClick(findViewById(R.id.pop_up_layout));
                }
            });
            GridLayout l_layout = findViewById(R.id.pop_up_layout);
            GridLayout.LayoutParams l_param = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
            l_param.width = 300;
            l_param.height = 300;
            l_param.bottomMargin = 0;
            l_layout.addView(PopUpButton, l_param);
        }



        // Loop creates buttons and adds them to grid
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                final Button myButton = new Button(this);
                myButton.setText((i*9+j) + " Button");

                // Create Listener for Button
                myButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ButtonClick(findViewById(R.id.pop_up_layout));
                    }
                });

                // Put the button in the GridLayout and set its Layout Parameters
                GridLayout grid_layout = findViewById(R.id.testing_grid);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = 100;
                lp.height = 100;
                grid_layout.addView(myButton, lp);
                Entry new_entry = new Entry();
                new_entry.mButton = myButton;
                Sudoku[i*9+j] = new_entry;
                //myButtons[i*9+j] = myButton;
            }
        }
    }


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    private void ButtonClick(View grid){
        if (on_screen) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(grid, "translationY", 1800f);
            animation.setDuration(1000);
            animation.start();
            on_screen = false;
        }
        else {
            ObjectAnimator animation = ObjectAnimator.ofFloat(grid, "translationY", 600f);
            animation.setDuration(2000);
            animation.start();
            on_screen = true;
        }
    }
}


// How to make random numbers
// Random rand = new Random();
// int rand_int = rand.nextInt(10);

// Log.d("Test", "Log Message Reference")