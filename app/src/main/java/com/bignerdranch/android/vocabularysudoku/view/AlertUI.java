package com.bignerdranch.android.vocabularysudoku.view;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.android.vocabularysudoku.R;
import com.bignerdranch.android.vocabularysudoku.controller.SudokuActivity;

public class AlertUI {

    AlertDialog.Builder mBuilder;
    View mView;
    EditText mInput;
    Button mEnter;
    Button mCancel;
    AlertDialog mDialog;

    public AlertUI(AlertDialog.Builder builder,View view, EditText input, Button enter, Button cancel) {
        mBuilder = builder;
        mView = view;
        mInput = input;
        mEnter = enter;
        mCancel = cancel;

        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    public EditText getInput() {
        return mInput;
    }

    public Button getEnter() {
        return mEnter;
    }

    public Button getCancel() {
        return mCancel;
    }
    public void dismiss() {
        mDialog.dismiss();
    }
}
