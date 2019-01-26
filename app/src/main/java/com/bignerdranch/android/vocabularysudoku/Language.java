package com.bignerdranch.android.vocabularysudoku;

public class Language {
    String name;
    String[] Words = new String[9];

    public Language(String name, String word1, String word2, String word3, String word4, String word5, String word6, String word7, String word8, String word9) {
        this.name = name;
        Words[0] = word1;
        Words[1] = word2;
        Words[2] = word3;
        Words[3] = word4;
        Words[4] = word5;
        Words[5] = word6;
        Words[6] = word7;
        Words[7] = word8;
        Words[8] = word9;
    }
}
