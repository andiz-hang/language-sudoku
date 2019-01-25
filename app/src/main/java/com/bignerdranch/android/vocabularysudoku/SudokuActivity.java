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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class SudokuActivity extends AppCompatActivity {

    // Stefan's Contribution
    // Andi's Contribution
    // Ivan's Contributions

    // Layout
    static boolean on_screen = false;

    Button[] myButtons = new Button[81];

    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        // This is the pop up screen. Currently just a button.
        final Button PopUpButton = new Button(this);
        PopUpButton.setText("Pop Up Screen");
        PopUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ButtonClick(PopUpButton);
            }
        });
        RelativeLayout l_layout = findViewById(R.id.pop_up_layout);
        RelativeLayout.LayoutParams l_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        l_param.width = 1000;
        l_param.height = 600;
        l_param.bottomMargin = -1800;
        l_layout.addView(PopUpButton, l_param);

        // Loop creates buttons and adds them to grid
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                final Button myButton = new Button(this);
                myButton.setText((i*9+j) + " Button");

                // Create Listener for Button
                myButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ButtonClick(PopUpButton);
                    }
                });

                // Put the button in the GridLayout and set its Layout Parameters
                GridLayout grid_layout = findViewById(R.id.testing_grid);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = 100;
                lp.height = 100;
                grid_layout.addView(myButton, lp);
                // SudokuCell new_cell = new SudokuCell();
                // new_cell.button = myButton;
                // SudokuPuzzle[i*9+j] = new_cell;
                myButtons[i*9+j] = myButton;
            }
        }
    }


    // When a button is pressed this pulls up or pushes down the Pop Up Button
    private void ButtonClick(Button button){
        if (on_screen) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(button, "translationY", 0f);
            animation.setDuration(1500);
            animation.start();
            on_screen = false;
        }
        else {
            ObjectAnimator animation = ObjectAnimator.ofFloat(button, "translationY", -800f);
            animation.setDuration(1500);
            animation.start();
            on_screen = true;
        }
    }
}


// How to make random numbers
// Random rand = new Random();
// int rand_int = rand.nextInt(10);

// Log.d("Test", "Log Message Reference")