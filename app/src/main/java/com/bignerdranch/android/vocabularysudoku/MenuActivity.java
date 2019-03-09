package com.bignerdranch.android.vocabularysudoku;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity;

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
    protected void UploadYourList(View view) {
//        Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
//        startActivity(intent);

        performFileSearch();
    }

    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text*/*csv");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                Uri tmp = resultData.getData();
                uri = tmp.toString();
//                Log.i(TAG, "Uri: " + uri.toString());
            }
        }
    }

}
