package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.*;

public class SudokuGridTest {
    @Test
    public void testGetSudokuCell() throws IOException {


        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            grid.getSudokuCell(i).setValue(i);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getSudokuCell(i).getValue(), i);
        }
        for(int i = 0; i < 81; i++) {
            grid.getSudokuCell(i).setValue(i + 1);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getSudokuCell(i).getValue(),i+1);
        }
    }

    @Test
    public void testCellConflictInColumn() throws IOException {
        boolean test=false;
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            if (i%9==0){
                grid.getSudokuCell(i).setValue(i/9+1);
            }
        }
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.cellConflictInColumn(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.cellConflictInColumn(9, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void testCellConflictInRow() throws IOException {
        boolean test=false;
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            if (i/9==0){
                grid.getSudokuCell(i).setValue(i%9+1);
            }
        }
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.cellConflictInRow(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.cellConflictInRow(1, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void cellConflictInBox() throws IOException {
        boolean test=false;
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            if (i/27*3 + i%9/3==0){
                grid.getSudokuCell(i).setValue(i/9*3+i%3+1);
            }
        }
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.cellConflictInBox(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.cellConflictInBox(1, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void TestSetWrongRows() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongRows(0,true);
        assertTrue(grid.getWrongRow(0));
        grid.setWrongRows(0,false);
        assertFalse(grid.getWrongRow(0));
    }

    @Test
    public void TestSetWrongCols() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongCols(0,true);
        assertTrue(grid.getWrongCol(0));
        grid.setWrongCols(0,false);
        assertFalse(grid.getWrongCol(0));
    }

    @Test
    public void TestSetWrongBoxes() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongBoxes(0,true);
        assertTrue(grid.getWrongBox(0));
        grid.setWrongBoxes(0,false);
        assertFalse(grid.getWrongBox(0));
    }

    @Test
    public void TestGetWrongRow() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongRows(0,true);
        assertTrue(grid.getWrongRow(0));
        grid.setWrongRows(0,false);
        assertFalse(grid.getWrongRow(0));
    }

    @Test
    public void TestGetWrongCol() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongCols(0,true);
        assertTrue(grid.getWrongCol(0));
        grid.setWrongCols(0,false);
        assertFalse(grid.getWrongCol(0));
    }

    @Test
    public void TestGetWrongBox() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        grid.setWrongBoxes(0,true);
        assertTrue(grid.getWrongBox(0));
        grid.setWrongBoxes(0,false);
        assertFalse(grid.getWrongBox(0));
    }

    @Test
    public void TestGetAnswers() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            grid.setAnswers(i,i);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), i);
        }
        for(int i = 0; i < 81; i++) {
            grid.setAnswers(i,80-i);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), 80-i);
        }
    }

    @Test
    public void TestSetAnswers() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            grid.setAnswers(i,i);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), i);
        }
        for(int i = 0; i < 81; i++) {
            grid.setAnswers(i,80-i);
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), 80-i);
        }
    }

    @Test
    public void testUpdateSudokuModel() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for(int i = 0; i < 81; i++) {
            if (i/27*3 + i%9/3==0){
                grid.getSudokuCell(i).setValue(i/9*3+i%3+1);
            }
            else if (i/9==3){
                grid.getSudokuCell(i).setValue(i%9+1);
            }
            else if (i%9==3){
                grid.getSudokuCell(i).setValue(i/9+1);
            }
        }
        grid.getSudokuCell(27).setLock(false);
        grid.updateSudokuModel(1,27);
        assertFalse(grid.getWrongRow(3));

        grid.updateSudokuModel(2,27);
        assertTrue(grid.getWrongRow(3));

        grid.getSudokuCell(3).setLock(false);
        grid.updateSudokuModel(1,3);
        assertFalse(grid.getWrongCol(3));

        grid.updateSudokuModel(2,3);
        assertTrue(grid.getWrongCol(3));

        grid.getSudokuCell(0).setLock(false);
        grid.updateSudokuModel(1,0);
        assertFalse(grid.getWrongBox(0));

        grid.updateSudokuModel(2,0);
        assertTrue(grid.getWrongBox(0));
    }

    @Test
    public void findConflictAtIndex() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        SudokuGrid grid = new SudokuGrid(9, 0, 9, 10, in);
        for (int i = 0; i < 81; i++) {
            if (i / 27 * 3 + i % 9 / 3 == 0) {
                grid.getSudokuCell(i).setValue(i / 9 * 3 + i % 3 + 1);
            } else if (i / 9 == 3) {
                grid.getSudokuCell(i).setValue(i % 9 + 1);
            } else if (i % 9 == 3) {
                grid.getSudokuCell(i).setValue(i / 9 + 1);
            }
        }
        grid.findConflictAtIndex(27);
        assertFalse(grid.getWrongRow(3));

        grid.getSudokuCell(27).setValue(2);
        grid.findConflictAtIndex(27);
        assertTrue(grid.getWrongRow(3));

        grid.findConflictAtIndex(3);
        assertFalse(grid.getWrongCol(3));

        grid.getSudokuCell(3).setValue(2);
        grid.findConflictAtIndex(3);
        assertTrue(grid.getWrongCol(3));

        grid.findConflictAtIndex(0);
        assertFalse(grid.getWrongBox(0));

        grid.getSudokuCell(0).setValue(2);
        grid.findConflictAtIndex(0);
        assertTrue(grid.getWrongBox(0));
    }

    @Test
    public void setSelected() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for (int i=-1;i<144;i++){
            grid.setSelected(i);
            assertEquals(i,grid.getSelected());
        }
        for (int i=-1;i<144;i++){
            grid.setSelected(i+1);
            assertEquals(i+1,grid.getSelected());
        }
    }

    @Test
    public void getSelected() throws IOException {
        String testString = "0,0,0,0,9,0,0,1,6,0,0,9,7,0,5,0,0,0,3,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,7,0,0,8,1,0,5,0,3,9,0,0,2,5,0,0,0,0,0,0,0,0,0,5,0,0,0,0,3,0,0,0,1,0,6,7,0,0,5,7,0,0,3,0,0,0,0,8,4,7,3,9,2,5,1,6,2,1,9,7,6,5,8,3,4,3,5,6,4,1,8,9,2,7,6,9,3,2,8,1,4,7,5,4,8,1,6,5,7,3,9,2,7,2,5,9,4,3,6,8,1,1,6,8,5,7,9,2,4,3,9,3,4,1,2,6,7,5,8,5,7,2,8,3,4,1,6,9\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        SudokuGrid grid = new SudokuGrid(9,0,9,10,in);
        for (int i=-1;i<144;i++){
            grid.setSelected(i);
            assertEquals(i,grid.getSelected());
        }
        for (int i=-1;i<144;i++){
            grid.setSelected(i+1);
            assertEquals(i+1,grid.getSelected());
        }
    }
}