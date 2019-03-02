package com.bignerdranch.android.vocabularysudoku.Model;

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

    public String getWord(int index) { return mWords[index]; }
}
