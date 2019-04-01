package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuCellTest {

    @Test
    public void testSetValue() {
        SudokuCell cell = new SudokuCell();
        cell.setValue(0);
        assertEquals(0,cell.getValue());
        cell.setValue(1);
        assertEquals(1,cell.getValue());
    }

    @Test
    public void testGetValue() {
        SudokuCell cell = new SudokuCell();
        cell.setValue(0);
        assertEquals(0,cell.getValue());
        cell.setValue(1);
        assertEquals(1,cell.getValue());
    }

    @Test
    public void testSetLock() {
        SudokuCell cell = new SudokuCell();
        cell.setLock(true);
        assertTrue(cell.isLock());
        cell.setLock(false);
        assertFalse(cell.isLock());
    }

    @Test
    public void testIsLock() {
        SudokuCell cell = new SudokuCell();
        cell.setLock(true);
        assertTrue(cell.isLock());
        cell.setLock(false);
        assertFalse(cell.isLock());
    }

    @Test
    public void testSetConflicting() {
        SudokuCell cell = new SudokuCell();
        cell.setConflicting(true);
        assertTrue(cell.isConflicting());
        cell.setConflicting(false);
        assertFalse(cell.isConflicting());
    }

    @Test
    public void testIsConflicting() {
        SudokuCell cell = new SudokuCell();
        cell.setConflicting(true);
        assertTrue(cell.isConflicting());
        cell.setConflicting(false);
        assertFalse(cell.isConflicting());
    }
}