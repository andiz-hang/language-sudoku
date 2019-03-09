package com.bignerdranch.android.vocabularysudoku.model;

public class Language {
    private String      mName;
    private String[]    mWords = new String[10];

    public Language(String name, String word1, String word2, String word3, String word4, String word5, String word6, String word7, String word8, String word9) {
        mName = name;
        mWords[0] = "";
        mWords[1] = word1;
        mWords[2] = word2;
        mWords[3] = word3;
        mWords[4] = word4;
        mWords[5] = word5;
        mWords[6] = word6;
        mWords[7] = word7;
        mWords[8] = word8;
        mWords[9] = word9;
    }

    public Language(String name) {
        mName = name;
        mWords[0] = "";
        mWords[1] = "";
        mWords[2] = "";
        mWords[3] = "";
        mWords[4] = "";
        mWords[5] = "";
        mWords[6] = "";
        mWords[7] = "";
        mWords[8] = "";
        mWords[9] = "";
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