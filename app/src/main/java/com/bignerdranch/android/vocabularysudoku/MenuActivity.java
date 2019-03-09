package com.bignerdranch.android.vocabularysudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    protected void StartGame(View view) {
        Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
        startActivity(intent);
    }


    protected void WordList(View view) {
//        Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
//        startActivity(intent);
    }
    protected void UploadYourList(View view) {
//        Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
//        startActivity(intent);
    }

}
