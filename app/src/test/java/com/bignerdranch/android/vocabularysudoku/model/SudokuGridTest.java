package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SudokuGridTest {

    @Test
    public void testGetSudokuCell() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getSudokuCell(i).getValue(),Integer.parseInt(initialValues.substring(i,i+1)));
        }
    }

    @Test
    public void testCellConflictInColumn() {
        boolean test=false;
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            if (i%9==0){
                initialValues=initialValues.concat(String.valueOf(i/9+1));
                //Log.d("Test","test");
            }
            else {
                Random rand = new Random();
                initialValues = initialValues.concat(String.valueOf(rand.nextInt(9)));
            }
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.CellConflictInColumn(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.CellConflictInColumn(9, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void testCellConflictInRow() {
        boolean test=false;
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            if (i/9==0){
                initialValues=initialValues.concat(String.valueOf(i%9+1));
                //Log.d("Test","test");
            }
            else {
                Random rand = new Random();
                initialValues = initialValues.concat(String.valueOf(rand.nextInt(9)));
            }
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.CellConflictInRow(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.CellConflictInRow(1, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void cellConflictInBox() {
        boolean test=false;
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            if (i/27*3 + i%9/3==0){
                initialValues=initialValues.concat(String.valueOf(i/9*3+i%3+1));
                //Log.d("Test","test");
            }
            else {
                Random rand = new Random();
                initialValues = initialValues.concat(String.valueOf(rand.nextInt(9)));
            }
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int j = 0; j < 9; j++) {
            assertFalse(grid.CellConflictInBox(0, 0, j));
        }
        for(int j = 0; j < 9; j++) {
            if(grid.CellConflictInBox(1, 0, j))test=true;
        }
        assertTrue(test);
    }

    @Test
    public void TestSetWrongRows() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongRows(0,true);
        assertTrue(grid.getWrongRow(0));
        grid.setWrongRows(0,false);
        assertFalse(grid.getWrongRow(0));
    }

    @Test
    public void TestSetWrongCols() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongCols(0,true);
        assertTrue(grid.getWrongCol(0));
        grid.setWrongCols(0,false);
        assertFalse(grid.getWrongCol(0));
    }

    @Test
    public void TestSetWrongBoxes() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongBoxes(0,true);
        assertTrue(grid.getWrongBox(0));
        grid.setWrongBoxes(0,false);
        assertFalse(grid.getWrongBox(0));
    }

    @Test
    public void TestGetWrongRow() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongRows(0,true);
        assertTrue(grid.getWrongRow(0));
        grid.setWrongRows(0,false);
        assertFalse(grid.getWrongRow(0));
    }

    @Test
    public void TestGetWrongCol() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongCols(0,true);
        assertTrue(grid.getWrongCol(0));
        grid.setWrongCols(0,false);
        assertFalse(grid.getWrongCol(0));
    }

    @Test
    public void TestGetWrongBox() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.setWrongBoxes(0,true);
        assertTrue(grid.getWrongBox(0));
        grid.setWrongBoxes(0,false);
        assertFalse(grid.getWrongBox(0));
    }

    @Test
    public void TestGetAnswers() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), Integer.parseInt(initialValues.substring(i, i + 1)));
        }
    }

    @Test
    public void TestSetAnswers() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        for(int i = 0; i < 81; i++) {
            grid.setAnswers(i,Integer.parseInt(initialValues.substring(80-i, 80-i + 1)));
        }
        for(int i = 0; i < 81; i++) {
            assertEquals(grid.getAnswers(i), Integer.parseInt(initialValues.substring(80-i, 80-i + 1)));
        }
    }

    @Test
    public void testIsZoomed() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.SetZoomed(true);
        assertTrue(grid.IsZoomed());
        grid.SetZoomed(false);
        assertFalse(grid.IsZoomed());
    }

    @Test
    public void testSetZoomed() {
        String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.SetZoomed(true);
        assertTrue(grid.IsZoomed());
        grid.SetZoomed(false);
        assertFalse(grid.IsZoomed());
    }

    @Test
    public void setSudokuLayout() {
        /*String initialValues = "";
        for(int i = 0; i < 81; i++) {
            Random rand = new Random();
            initialValues=initialValues.concat(String.valueOf(rand.nextInt(9)));
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        GridLayout testLayout = mock(GridLayout.class);
        GridLayoutUI testLayoutUI = new GridLayoutUI(testLayout);
        grid.setSudokuLayout(testLayoutUI);*/

    }

    @Test
    public void testUpdateSudokuModel() {
        String initialValues="";
        for(int i = 0; i < 81; i++) {
            if (i/27*3 + i%9/3==0){
                initialValues=initialValues.concat(String.valueOf(i/9*3+i%3+1));
            }
            else if (i/9==3){
                initialValues=initialValues.concat(String.valueOf(i%9+1));
            }
            else if (i%9==3){
                initialValues=initialValues.concat(String.valueOf(i/9+1));
            }
            else {
                Random rand = new Random();
                initialValues = initialValues.concat(String.valueOf(rand.nextInt(9)));
            }
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
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
    public void findConflictAtIndex() {
        String initialValues="";
        for(int i = 0; i < 81; i++) {
            if (i/27*3 + i%9/3==0){
                initialValues=initialValues.concat(String.valueOf(i/9*3+i%3+1));
            }
            else if (i/9==3){
                initialValues=initialValues.concat(String.valueOf(i%9+1));
            }
            else if (i%9==3){
                initialValues=initialValues.concat(String.valueOf(i/9+1));
            }
            else {
                Random rand = new Random();
                initialValues = initialValues.concat(String.valueOf(rand.nextInt(9)));
            }
        }
        SudokuGrid grid = new SudokuGrid(9,initialValues, initialValues);
        grid.FindConflictAtIndex(3);
        assertFalse(grid.getWrongRow(3));

        initialValues=initialValues.substring(0,27)+"2"+initialValues.substring(28,81);
        grid = new SudokuGrid(9,initialValues, initialValues);
        grid.FindConflictAtIndex(27);
        assertTrue(grid.getWrongRow(3));

        grid.FindConflictAtIndex(27);
        assertFalse(grid.getWrongCol(3));

        initialValues=initialValues.substring(0,3)+"2"+initialValues.substring(4,81);
        grid = new SudokuGrid(9,initialValues, initialValues);
        grid.FindConflictAtIndex(3);
        assertTrue(grid.getWrongCol(3));

        grid.FindConflictAtIndex(0);
        assertFalse(grid.getWrongBox(0));

        initialValues="2"+initialValues.substring(1,81);
        grid = new SudokuGrid(9,initialValues, initialValues);
        grid.FindConflictAtIndex(0);
        assertTrue(grid.getWrongBox(0));
    }

    @Test
    public void sendModelToView() {
    }
}