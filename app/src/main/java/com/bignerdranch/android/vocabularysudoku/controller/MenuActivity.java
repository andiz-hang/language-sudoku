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

        mSizeSpinner = findViewById(R.id.size_choice_spinner);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        ProgressText = findViewById(R.id.textView);
        ProgressSetText(progress);

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
        intent.putExtra("new_game", true);
        intent.putExtra("uri_key", uri);
        intent.putExtra("use_sample_file", useSampleFile);
        intent.putExtra("listen_mode", mListenMode);
        intent.putExtra("size", getSizeSpinnerValue());
        intent.putExtra("diff_opt", getDiffBarValue());
        startActivity(intent);
    }

    private int getSizeSpinnerValue() {
        int spinner_pos = mSizeSpinner.getSelectedItemPosition();
        String[] size_values = getResources().getStringArray(R.array.size_choices);
        return Integer.valueOf(size_values[spinner_pos]);
    }
    private int getDiffBarValue() {
        return seekBar.getProgress();
    }

    // User wishes to use sample file
    public void wordList(View mView) {

        // Create a dialog box popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.notice_alert, null);
        View view = View.inflate(this, R.layout.use_sample_file_alert, null);

        Button yes = view.findViewById(R.id.yes_button);
        Button no = view.findViewById(R.id.no_button);
        final SampleFileNoticeUI notice = new SampleFileNoticeUI(builder, view, yes, no);

        notice.getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useSampleFile = true;
                notice.dismiss();
                Toast.makeText(getApplicationContext(), "Sample File Toggled On",Toast.LENGTH_SHORT).show();
            }
        });
        notice.getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useSampleFile = false;
                notice.dismiss();
                Toast.makeText(getApplicationContext(), "Sample File Toggled Off",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadYourList(View mView) {

        // Create a dialog box popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.notice_alert, null);
        View view = View.inflate(this, R.layout.notice_alert, null);

        Button okay = view.findViewById(R.id.okay_button);
        final NoticeUI notice = new NoticeUI(builder, view, okay);

        notice.getOkay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.dismiss();
                performFileSearch();
            }
        });
    }

    public void setListenMode(View mView) {
        mListenMode = !mListenMode;
        Button button = findViewById(R.id.listen_mode_button);
        if (mListenMode){
            button.setText("Listen Mode");
        } else {
            button.setText("Reading Mode");
        }
    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri tmp = resultData.getData();
                if (tmp != null) uri = tmp.toString();
//                Log.i(TAG, "Uri: " + uri.toString());
                useSampleFile = false;
            }

            Toast.makeText(getApplicationContext(),"Import Successful!",Toast.LENGTH_SHORT).show();
        }
    }

    // Hide the activity bar
    void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
    private void ProgressSetText(int progress){
        String diff="Difficulty: ";
        if (progress==0){
            diff=diff.concat("Beginner");
        }
        else if (progress==5){
            diff=diff.concat("Intermediate");
        }
        else if (progress==10){
            diff=diff.concat("Expert");
        }
        else {
            diff=diff.concat(Integer.toString(progress));
        }
        ProgressText.setText(diff);
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            ProgressSetText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
}
