package com.bignerdranch.android.vocabularysudoku.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bignerdranch.android.vocabularysudoku.view.NoticeUI;

public class MenuActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private String uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    protected void StartGame(View view) {
        Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
        intent.putExtra("uri_key", uri);
        startActivity(intent);
    }


    protected void WordList(View view) {
//        Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
//        startActivity(intent);
    }
    protected void UploadYourList(View mView) {
//        Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
//        startActivity(intent);
        // Create a dialog box popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.notice_alert, null);

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

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/comma-separated-values");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri tmp = resultData.getData();
                uri = tmp.toString();
//                Log.i(TAG, "Uri: " + uri.toString());
            }
        }
    }

}