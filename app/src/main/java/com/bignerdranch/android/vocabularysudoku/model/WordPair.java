package com.bignerdranch.android.vocabularysudoku.model;


public class WordPair {
    private String mWord1;
    private String mWord2;
    private int mNum;

    public String getWord1() {
        return mWord1;
    }

    public String getWord2() {
        return  mWord2;
    }

    public int getNum() { return mNum; }

    public void setNum(int number) {mNum = number; }

    public WordPair(String word1, String word2, int index) {
        mWord1 = word1;
        mWord2 = word2;
        mNum = index;
    }


}
