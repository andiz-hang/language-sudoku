package com.bignerdranch.android.vocabularysudoku.view;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity;

public class NoticeUI {

    private AlertDialog.Builder mBuilder;
    private View mView;
    private Button mOkay;
    private AlertDialog mDialog;

    public NoticeUI(AlertDialog.Builder builder, View view, Button okay) {
        mBuilder = builder;
        mView = view;
        mOkay = okay;

        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    public View getOkay() {
        return mOkay;
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
