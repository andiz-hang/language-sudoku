package com.bignerdranch.android.vocabularysudoku.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class LanguageTest {

    @Test
    public void getWord() {
        Language lan1=new Language("english",12);
        lan1.setWord("test",0);
        assertEquals(lan1.getWord(0),"test");
        lan1.setWord("test1",1);
        assertEquals(lan1.getWord(1),"test1");
        lan1.setWord("test2",2);
        assertEquals(lan1.getWord(2),"test2");
        lan1.setWord("test3",3);
        assertEquals(lan1.getWord(3),"test3");
        lan1.setWord("test4",4);
        assertEquals(lan1.getWord(4),"test4");
        lan1.setWord("test5",5);
        assertEquals(lan1.getWord(5),"test5");
        lan1.setWord("test6",6);
        assertEquals(lan1.getWord(6),"test6");
        lan1.setWord("test7",7);
        assertEquals(lan1.getWord(7),"test7");
        lan1.setWord("test8",8);
        assertEquals(lan1.getWord(8),"test8");
        lan1.setWord("test9",9);
        assertEquals(lan1.getWord(9),"test9");
        lan1.setWord("test10",10);
        assertEquals(lan1.getWord(10),"test10");
        lan1.setWord("test11",11);
        assertEquals(lan1.getWord(11),"test11");

        lan1.setWord("test1",0);
        assertEquals(lan1.getWord(0),"test1");
        lan1.setWord("test2",1);
        assertEquals(lan1.getWord(1),"test2");
        lan1.setWord("test3",2);
        assertEquals(lan1.getWord(2),"test3");
        lan1.setWord("test4",3);
        assertEquals(lan1.getWord(3),"test4");
        lan1.setWord("test5",4);
        assertEquals(lan1.getWord(4),"test5");
        lan1.setWord("test6",5);
        assertEquals(lan1.getWord(5),"test6");
        lan1.setWord("test7",6);
        assertEquals(lan1.getWord(6),"test7");
        lan1.setWord("test8",7);
        assertEquals(lan1.getWord(7),"test8");
        lan1.setWord("test9",8);
        assertEquals(lan1.getWord(8),"test9");
        lan1.setWord("test10",9);
        assertEquals(lan1.getWord(9),"test10");
        lan1.setWord("test11",10);
        assertEquals(lan1.getWord(10),"test11");
        lan1.setWord("test12",11);
        assertEquals(lan1.getWord(11),"test12");
    }

    @Test
    public void setWord() {
        Language lan1=new Language("english",12);
        lan1.setWord("test",0);
        assertEquals(lan1.getWord(0),"test");
        lan1.setWord("test1",1);
        assertEquals(lan1.getWord(1),"test1");
        lan1.setWord("test2",2);
        assertEquals(lan1.getWord(2),"test2");
        lan1.setWord("test3",3);
        assertEquals(lan1.getWord(3),"test3");
        lan1.setWord("test4",4);
        assertEquals(lan1.getWord(4),"test4");
        lan1.setWord("test5",5);
        assertEquals(lan1.getWord(5),"test5");
        lan1.setWord("test6",6);
        assertEquals(lan1.getWord(6),"test6");
        lan1.setWord("test7",7);
        assertEquals(lan1.getWord(7),"test7");
        lan1.setWord("test8",8);
        assertEquals(lan1.getWord(8),"test8");
        lan1.setWord("test9",9);
        assertEquals(lan1.getWord(9),"test9");
        lan1.setWord("test10",10);
        assertEquals(lan1.getWord(10),"test10");
        lan1.setWord("test11",11);
        assertEquals(lan1.getWord(11),"test11");

        lan1.setWord("test1",0);
        assertEquals(lan1.getWord(0),"test1");
        lan1.setWord("test2",1);
        assertEquals(lan1.getWord(1),"test2");
        lan1.setWord("test3",2);
        assertEquals(lan1.getWord(2),"test3");
        lan1.setWord("test4",3);
        assertEquals(lan1.getWord(3),"test4");
        lan1.setWord("test5",4);
        assertEquals(lan1.getWord(4),"test5");
        lan1.setWord("test6",5);
        assertEquals(lan1.getWord(5),"test6");
        lan1.setWord("test7",6);
        assertEquals(lan1.getWord(6),"test7");
        lan1.setWord("test8",7);
        assertEquals(lan1.getWord(7),"test8");
        lan1.setWord("test9",8);
        assertEquals(lan1.getWord(8),"test9");
        lan1.setWord("test10",9);
        assertEquals(lan1.getWord(9),"test10");
        lan1.setWord("test11",10);
        assertEquals(lan1.getWord(10),"test11");
        lan1.setWord("test12",11);
        assertEquals(lan1.getWord(11),"test12");
    }

    @Test
    public void getName() {
        Language lan1=new Language("english",12);
        assertEquals(lan1.getName(),"english");
        Language lan2=new Language("french",12);
        assertEquals(lan2.getName(),"french");
    }
}