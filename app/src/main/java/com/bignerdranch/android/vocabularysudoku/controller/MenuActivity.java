package com.bignerdranch.android.vocabularysudoku.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.vocabularysudoku.R;

import com.bignerdranch.android.vocabularysudoku.view.NoticeUI;
import com.bignerdranch.android.vocabularysudoku.view.SampleFileNoticeUI;

import java.text.MessageFormat;

public class MenuActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private String uri = null;
    private boolean useSampleFile = false;
    private boolean mListenMode = false;
    private Spinner mSizeSpinner;
    private TextView ProgressText;
    private SeekBar seekBar;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        hideActionBar();
    }

    public void continueGame(View view){
        Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
        mSharedPreferences = getSharedPreferences("Sudoku", MODE_PRIVATE);

        if(!mSharedPreferences.contains("SaveExists")){
            newGame(view);
        }
        else {
            intent.putExtra("new_game", false);
            startActivity(intent);
        }
    }

    public void newGame(View view) {
        Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
        mSharedPreferences = getSharedPreferences("Sudoku", MODE_PRIVATE);
        intent.putExtra("new_game", true);
        intent.putExtra("uri_key", uri);
        intent.putExtra("use_sample_file", useSampleFile);
        intent.putExtra("listen_mode", mListenMode);
        intent.putExtra("size", mSharedPreferences.getInt("SUDOKU_SPINNER_GRID_VALUE", 9));
        intent.putExtra("diff_opt", mSharedPreferences.getInt("SUDOKU_BAR_DIFFICULTY_VALUE", 5));
        startActivity(intent);
    }


    // Hide the activity bar
    void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void settings(View view) {
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
