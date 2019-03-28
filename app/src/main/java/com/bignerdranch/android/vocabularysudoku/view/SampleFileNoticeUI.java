package com.bignerdranch.android.vocabularysudoku.view;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity;

public class SampleFileNoticeUI {

    private AlertDialog.Builder mBuilder;
    private View mView;
    private Button mYes;
    private Button mNo;
    private AlertDialog mDialog;

    public SampleFileNoticeUI(AlertDialog.Builder builder, View view, Button yes, Button no) {
        mBuilder = builder;
        mView = view;
        mYes = yes;
        mNo = no;

        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    public View getYes() {
        return mYes;
    }

    public View getNo() {
        return mNo;
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
