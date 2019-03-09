package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LanguageTest {

    @Test
    public void testGetWord() {
        Language sLanguage1 = new Language("English", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        assertEquals(sLanguage1.getWord(0),"");
        assertEquals(sLanguage1.getWord(1),"one");
        assertEquals(sLanguage1.getWord(2),"two");
        assertEquals(sLanguage1.getWord(3),"three");
        assertEquals(sLanguage1.getWord(4),"four");
        assertEquals(sLanguage1.getWord(5),"five");
        assertEquals(sLanguage1.getWord(6),"six");
        assertEquals(sLanguage1.getWord(7),"seven");
        assertEquals(sLanguage1.getWord(8),"eight");
        assertEquals(sLanguage1.getWord(9),"nine");
        //Log.d("Test", "Tests have run");
    }
    @Test
    public void testGetName() {
        Language sLanguage1 = new Language("English", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        assertEquals(sLanguage1.getName(), "English");
    }
}