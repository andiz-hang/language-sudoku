package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordPairTest {

    @Test
    public void getWord1() {
        WordPair pair;
        for (int i = 0; i < 81; i++){
            pair= new WordPair("test", "test2", i);
            assertEquals(pair.getWord1(), "test");
        }
        for (int i = 0; i < 81; i++){
            pair= new WordPair("test2", "test3", i);
            assertEquals(pair.getWord1(), "test2");
        }
    }

    @Test
    public void getWord2() {
        WordPair pair;
        for (int i = 0; i < 81; i++){
            pair= new WordPair("test", "test2", i);
            assertEquals(pair.getWord2(), "test2");
        }
        for (int i = 0; i < 81; i++){
            pair= new WordPair("test2", "test3", i);
            assertEquals(pair.getWord2(), "test3");
        }
    }
}