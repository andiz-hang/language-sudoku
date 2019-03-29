package com.bignerdranch.android.vocabularysudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Language {
    private String      mName;
    private String[]    mWords;

    public Language(String name, int size) {
        mName = name;
        mWords = new String[size + 1];
    }

    public String getWord(int index) { return mWords[index]; }

    public void setWord(String word, int index) {
        mWords[index] = word;
    }

    public String getName() { return mName; }

    // Replace the last n words with words from the list of word_pairs
    public void getNRandomWords(int n, int seed) {

    }
}