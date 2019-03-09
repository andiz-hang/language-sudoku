package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SudokuGridTest {

    @Test
    public void getSudokuCell() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues += rand.nextInt(9);
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);

    }

    @Test
    public void getSudokuCell1() {
    }

    @Test
    public void cellConflictInColumn() {
    }

    @Test
    public void cellConflictInRow() {
    }

    @Test
    public void cellConflictInBox() {
    }

    @Test
    public void setWrongRows() {
    }

    @Test
    public void setWrongCols() {
    }

    @Test
    public void setWrongBoxes() {
    }

    @Test
    public void getAnswers() {
    }

    @Test
    public void setAnswers() {
    }

    @Test
    public void getWrongRow() {
    }

    @Test
    public void getWrongCol() {
    }

    @Test
    public void getWrongBox() {
    }

    @Test
    public void isZoomed() {
    }

    @Test
    public void setSudokuLayout() {
    }

    @Test
    public void updateSudokuModel() {
    }

    @Test
    public void findConflictAtIndex() {
    }

    @Test
    public void sendModelToView() {
    }
}