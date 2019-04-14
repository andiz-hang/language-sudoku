package com.bignerdranch.android.vocabularysudoku.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bignerdranch.android.vocabularysudoku.R;

import java.util.Objects;

public class HighscoreActivity  extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        SharedPreferences mSharedPreferences = getSharedPreferences("Sudoku", MODE_PRIVATE);
        int difficulty_num= mSharedPreferences.getInt("SUDOKU_BAR_DIFFICULTY_VALUE", 5);
        String difficulty_text=DifficultySetText(difficulty_num);
        String tmp;
        TextView difficulty = findViewById(R.id.difficulty);
        difficulty.setText(String.format("%s%s", "9x9 highscore for difficulty: ", difficulty_text));
        TextView highscore_1 = findViewById(R.id.highscore_1);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_1_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_1.setText(String.format("%s%s.%s", "1: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_1.setText(String.format("1: %s", tmp));
        TextView highscore_2 = findViewById(R.id.highscore_2);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_2_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_2.setText(String.format("%s%s.%s", "2: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_2.setText(String.format("2: %s", tmp));
        TextView highscore_3 = findViewById(R.id.highscore_3);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_3_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_3.setText(String.format("%s%s.%s", "3: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_3.setText(String.format("3: %s", tmp));
        TextView highscore_4 = findViewById(R.id.highscore_4);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_4_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_4.setText(String.format("%s%s.%s", "4: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_4.setText(String.format("4: %s", tmp));
        TextView highscore_5 = findViewById(R.id.highscore_5);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_5_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_5.setText(String.format("%s%s.%s", "5: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_5.setText(String.format("5: %s", tmp));
        TextView highscore_6 = findViewById(R.id.highscore_6);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_6_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_6.setText(String.format("%s%s.%s", "6: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_6.setText(String.format("6: %s", tmp));
        TextView highscore_7 = findViewById(R.id.highscore_7);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_7_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_7.setText(String.format("%s%s.%s", "7: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_7.setText(String.format("7: %s", tmp));
        TextView highscore_8 = findViewById(R.id.highscore_8);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_8_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_8.setText(String.format("%s%s.%s", "8: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_8.setText(String.format("8: %s", tmp));
        TextView highscore_9 = findViewById(R.id.highscore_9);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_9_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_9.setText(String.format("%s%s.%s", "9: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_9.setText(String.format("9: %s", tmp));
        TextView highscore_10 = findViewById(R.id.highscore_10);
        tmp=mSharedPreferences.getString("SUDOKU_HIGHSCORE_10_" + Integer.toString(difficulty_num), "Unset");
        if (!Objects.requireNonNull(tmp).equals("Unset"))
            highscore_10.setText(String.format("%s%s.%s", "10: ", tmp.substring(0, Objects.requireNonNull(tmp).length()-3),tmp.substring(Objects.requireNonNull(tmp).length()-3,Objects.requireNonNull(tmp).length())));
        else
            highscore_10.setText(String.format("10: %s", tmp));
        hideActionBar();
    }
    void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
    private String DifficultySetText(int progress){
        String diff;
        if (progress==0){
            diff="Beginner";
        }
        else if (progress==5){
            diff="Intermediate";
        }
        else if (progress==10){
            diff="Expert";
        }
        else {
            diff=Integer.toString(progress);
        }
        return diff;
    }
}
