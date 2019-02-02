package com.bignerdranch.android.vocabularysudoku;

public class Language {
    String name;
    String[] Words = new String[10];

    public Language(String name, String word1, String word2, String word3, String word4, String word5, String word6, String word7, String word8, String word9) {
        this.name = name;
        Words[0] = "";
        Words[1] = word1;
        Words[2] = word2;
        Words[3] = word3;
        Words[4] = word4;
        Words[5] = word5;
        Words[6] = word6;
        Words[7] = word7;
        Words[8] = word8;
        Words[9] = word9;
    }
}
