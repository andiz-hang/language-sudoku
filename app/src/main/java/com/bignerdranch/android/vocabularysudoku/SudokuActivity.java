package com.bignerdranch.android.vocabularysudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SudokuActivity extends AppCompatActivity {

    // Stefan's Contribution
    // Andi's Contribution
    // Ivan's Contributions

    Button[] myButtons = new Button[81];

    // On clicking a square we show a screen with buttons with word choices
    // Pressing one of those buttons hides that screen and fills in that square


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);


        //
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                final Button myButton = new Button(this);
                myButton.setText((i*9+j) + " Button");

                myButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ButtonClick(myButton);
                    }
                });

                GridLayout grid_layout = (GridLayout) findViewById(R.id.testing_grid);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();//(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
                lp.width = 100;
                lp.height = 100;
                grid_layout.addView(myButton, lp);
                myButtons[i*9+j] = myButton;
            }
        }

        for(int i = 0; i < 81; i++){
            Log.d("Test", "Button name in myButtons["+i+"] = " + myButtons[i].getText());
        }


    }


    private void ButtonClick(Button button){
        button.setText("Clicked");
    }




}
