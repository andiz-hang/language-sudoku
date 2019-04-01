package com.bignerdranch.android.vocabularysudoku.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bignerdranch.android.vocabularysudoku.R;

import com.bignerdranch.android.vocabularysudoku.view.NoticeUI;
import com.bignerdranch.android.vocabularysudoku.view.SampleFileNoticeUI;

public class MenuActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private String uri = null;
    private boolean useSampleFile = false;
    private boolean mListenMode = false;
    private Spinner mSizeSpinner;
    private Spinner mDiffSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mSizeSpinner = findViewById(R.id.size_choice_spinner);
        mDiffSpinner = findViewById(R.id.difficulty_spinner);

        hideActionBar();
    }

    public void startGame(View view) {
        Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
        intent.putExtra("uri_key", uri);
        intent.putExtra("use_sample_file", useSampleFile);
        intent.putExtra("listen_mode", mListenMode);
        intent.putExtra("size", getSizeSpinnerValue());
        intent.putExtra("diff_opt", getDiffSpinnerValue());
        startActivity(intent);
    }

    private int getSizeSpinnerValue() {
        int spinner_pos = mSizeSpinner.getSelectedItemPosition();
        String[] size_values = getResources().getStringArray(R.array.size_choices);
        return Integer.valueOf(size_values[spinner_pos]);
    }
    private String getDiffSpinnerValue() {
        return mDiffSpinner.getSelectedItem().toString();
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
}
