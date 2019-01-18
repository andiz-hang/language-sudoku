package com.bignerdranch.android.vocabularysudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SudokuActivity extends AppCompatActivity {

    //Stefan's contribution
    // Andi's Contribution
    // Ivan's Contributions

    private Button mTLButton;
    private Button mTMButton;
    private Button mTRButton;
    private Button mMLButton;
    private Button mMMButton;
    private Button mMRButton;
    private Button mBLButton;
    private Button mBMButton;
    private Button mBRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //Currently these only work for the first 3x3 set of buttons
        //Top Buttons
        mTLButton = (Button) findViewById(R.id.top_left_button);
        mTLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mTLButton);
            }
        });
        mTMButton = (Button) findViewById(R.id.top_middle_button);
        mTMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mTMButton);
            }
        });
        mTRButton = (Button) findViewById(R.id.top_right_button);
        mTRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mTRButton);
            }
        });

        // Middle Buttons
        mMLButton = (Button) findViewById(R.id.middle_left_button);
        mMLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mMLButton);
            }
        });
        mMMButton = (Button) findViewById(R.id.middle_middle_button);
        mMMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mMMButton);
            }
        });
        mMRButton = (Button) findViewById(R.id.middle_right_button);
        mMRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mMRButton);
            }
        });

        // Bottom Buttons
        mBLButton = (Button) findViewById(R.id.bottom_left_button);
        mBLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mBLButton);
            }
        });
        mBMButton = (Button) findViewById(R.id.bottom_middle_button);
        mBMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mBMButton);
            }
        });
        mBRButton = (Button) findViewById(R.id.bottom_right_button);
        mBRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText(mBRButton);
            }
        });
    }

    private void changeText(Button button){
        button.setText("Clicked");
    }




}
