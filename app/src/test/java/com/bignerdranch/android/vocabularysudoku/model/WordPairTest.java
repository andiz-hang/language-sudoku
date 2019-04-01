package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordPairTest {

    @Test
    public void getWord1() {
        WordPair pair=new WordPair("test","test2");
        assertEquals(pair.getWord1(),"test");
        WordPair pair2=new WordPair("test2","test3");
        assertEquals(pair2.getWord1(),"test2");
    }

    @Test
    public void getWord2() {
        WordPair pair=new WordPair("test","test2");
        assertEquals(pair.getWord2(),"test2");
        WordPair pair2=new WordPair("test2","test3");
        assertEquals(pair2.getWord2(),"test3");
    }
}