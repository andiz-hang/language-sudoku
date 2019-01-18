package com.bignerdranch.android.vocabularysudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SudokuActivity extends AppCompatActivity {

    // Stefan's Contribution
    // Andi's Contribution
    // Ivan's Contributions

    private Button m00Button;
    private Button m01Button;
    private Button m02Button;
    private Button m03Button;
    private Button m04Button;
    private Button m05Button;
    private Button m06Button;
    private Button m07Button;
    private Button m08Button;
    private Button m10Button;
    private Button m11Button;
    private Button m12Button;
    private Button m13Button;
    private Button m14Button;
    private Button m15Button;
    private Button m16Button;
    private Button m17Button;
    private Button m18Button;
    private Button m20Button;
    private Button m21Button;
    private Button m22Button;
    private Button m23Button;
    private Button m24Button;
    private Button m25Button;
    private Button m26Button;
    private Button m27Button;
    private Button m28Button;
    private Button m30Button;
    private Button m31Button;
    private Button m32Button;
    private Button m33Button;
    private Button m34Button;
    private Button m35Button;
    private Button m36Button;
    private Button m37Button;
    private Button m38Button;
    private Button m40Button;
    private Button m41Button;
    private Button m42Button;
    private Button m43Button;
    private Button m44Button;
    private Button m45Button;
    private Button m46Button;
    private Button m47Button;
    private Button m48Button;
    private Button m50Button;
    private Button m51Button;
    private Button m52Button;
    private Button m53Button;
    private Button m54Button;
    private Button m55Button;
    private Button m56Button;
    private Button m57Button;
    private Button m58Button;
    private Button m60Button;
    private Button m61Button;
    private Button m62Button;
    private Button m63Button;
    private Button m64Button;
    private Button m65Button;
    private Button m66Button;
    private Button m67Button;
    private Button m68Button;
    private Button m70Button;
    private Button m71Button;
    private Button m72Button;
    private Button m73Button;
    private Button m74Button;
    private Button m75Button;
    private Button m76Button;
    private Button m77Button;
    private Button m78Button;
    private Button m80Button;
    private Button m81Button;
    private Button m82Button;
    private Button m83Button;
    private Button m84Button;
    private Button m85Button;
    private Button m86Button;
    private Button m87Button;
    private Button m88Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        m00Button = (Button) findViewById(R.id.b00);
        m00Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m00Button);} });
        m01Button = (Button) findViewById(R.id.b01);
        m01Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m01Button);} });
        m02Button = (Button) findViewById(R.id.b02);
        m02Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m02Button);} });
        m03Button = (Button) findViewById(R.id.b03);
        m03Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m03Button);} });
        m04Button = (Button) findViewById(R.id.b04);
        m04Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m04Button);} });
        m05Button = (Button) findViewById(R.id.b05);
        m05Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m05Button);} });
        m06Button = (Button) findViewById(R.id.b06);
        m06Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m06Button);} });
        m07Button = (Button) findViewById(R.id.b07);
        m07Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m07Button);} });
        m08Button = (Button) findViewById(R.id.b08);
        m08Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m08Button);} });
        m10Button = (Button) findViewById(R.id.b10);
        m10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m10Button);} });
        m11Button = (Button) findViewById(R.id.b11);
        m11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m11Button);} });
        m12Button = (Button) findViewById(R.id.b12);
        m12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m12Button);} });
        m13Button = (Button) findViewById(R.id.b13);
        m13Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m13Button);} });
        m14Button = (Button) findViewById(R.id.b14);
        m14Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m14Button);} });
        m15Button = (Button) findViewById(R.id.b15);
        m15Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m15Button);} });
        m16Button = (Button) findViewById(R.id.b16);
        m16Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m16Button);} });
        m17Button = (Button) findViewById(R.id.b17);
        m17Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m17Button);} });
        m18Button = (Button) findViewById(R.id.b18);
        m18Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m18Button);} });
        m20Button = (Button) findViewById(R.id.b20);
        m20Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m20Button);} });
        m21Button = (Button) findViewById(R.id.b21);
        m21Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m21Button);} });
        m22Button = (Button) findViewById(R.id.b22);
        m22Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m22Button);} });
        m23Button = (Button) findViewById(R.id.b23);
        m23Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m23Button);} });
        m24Button = (Button) findViewById(R.id.b24);
        m24Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m24Button);} });
        m25Button = (Button) findViewById(R.id.b25);
        m25Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m25Button);} });
        m26Button = (Button) findViewById(R.id.b26);
        m26Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m26Button);} });
        m27Button = (Button) findViewById(R.id.b27);
        m27Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m27Button);} });
        m28Button = (Button) findViewById(R.id.b28);
        m28Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m28Button);} });
        m30Button = (Button) findViewById(R.id.b30);
        m30Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m30Button);} });
        m31Button = (Button) findViewById(R.id.b31);
        m31Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m31Button);} });
        m32Button = (Button) findViewById(R.id.b32);
        m32Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m32Button);} });
        m33Button = (Button) findViewById(R.id.b33);
        m33Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m33Button);} });
        m34Button = (Button) findViewById(R.id.b34);
        m34Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m34Button);} });
        m35Button = (Button) findViewById(R.id.b35);
        m35Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m35Button);} });
        m36Button = (Button) findViewById(R.id.b36);
        m36Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m36Button);} });
        m37Button = (Button) findViewById(R.id.b37);
        m37Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m37Button);} });
        m38Button = (Button) findViewById(R.id.b38);
        m38Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m38Button);} });
        m40Button = (Button) findViewById(R.id.b40);
        m40Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m40Button);} });
        m41Button = (Button) findViewById(R.id.b41);
        m41Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m41Button);} });
        m42Button = (Button) findViewById(R.id.b42);
        m42Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m42Button);} });
        m43Button = (Button) findViewById(R.id.b43);
        m43Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m43Button);} });
        m44Button = (Button) findViewById(R.id.b44);
        m44Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m44Button);} });
        m45Button = (Button) findViewById(R.id.b45);
        m45Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m45Button);} });
        m46Button = (Button) findViewById(R.id.b46);
        m46Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m46Button);} });
        m47Button = (Button) findViewById(R.id.b47);
        m47Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m47Button);} });
        m48Button = (Button) findViewById(R.id.b48);
        m48Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m48Button);} });
        m50Button = (Button) findViewById(R.id.b50);
        m50Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m50Button);} });
        m51Button = (Button) findViewById(R.id.b51);
        m51Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m51Button);} });
        m52Button = (Button) findViewById(R.id.b52);
        m52Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m52Button);} });
        m53Button = (Button) findViewById(R.id.b53);
        m53Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m53Button);} });
        m54Button = (Button) findViewById(R.id.b54);
        m54Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m54Button);} });
        m55Button = (Button) findViewById(R.id.b55);
        m55Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m55Button);} });
        m56Button = (Button) findViewById(R.id.b56);
        m56Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m56Button);} });
        m57Button = (Button) findViewById(R.id.b57);
        m57Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m57Button);} });
        m58Button = (Button) findViewById(R.id.b58);
        m58Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m58Button);} });
        m60Button = (Button) findViewById(R.id.b60);
        m60Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m60Button);} });
        m61Button = (Button) findViewById(R.id.b61);
        m61Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m61Button);} });
        m62Button = (Button) findViewById(R.id.b62);
        m62Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m62Button);} });
        m63Button = (Button) findViewById(R.id.b63);
        m63Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m63Button);} });
        m64Button = (Button) findViewById(R.id.b64);
        m64Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m64Button);} });
        m65Button = (Button) findViewById(R.id.b65);
        m65Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m65Button);} });
        m66Button = (Button) findViewById(R.id.b66);
        m66Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m66Button);} });
        m67Button = (Button) findViewById(R.id.b67);
        m67Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m67Button);} });
        m68Button = (Button) findViewById(R.id.b68);
        m68Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m68Button);} });
        m70Button = (Button) findViewById(R.id.b70);
        m70Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m70Button);} });
        m71Button = (Button) findViewById(R.id.b71);
        m71Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m71Button);} });
        m72Button = (Button) findViewById(R.id.b72);
        m72Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m72Button);} });
        m73Button = (Button) findViewById(R.id.b73);
        m73Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m73Button);} });
        m74Button = (Button) findViewById(R.id.b74);
        m74Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m74Button);} });
        m75Button = (Button) findViewById(R.id.b75);
        m75Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m75Button);} });
        m76Button = (Button) findViewById(R.id.b76);
        m76Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m76Button);} });
        m77Button = (Button) findViewById(R.id.b77);
        m77Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m77Button);} });
        m78Button = (Button) findViewById(R.id.b78);
        m78Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m78Button);} });
        m80Button = (Button) findViewById(R.id.b80);
        m80Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m80Button);} });
        m81Button = (Button) findViewById(R.id.b81);
        m81Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m81Button);} });
        m82Button = (Button) findViewById(R.id.b82);
        m82Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m82Button);} });
        m83Button = (Button) findViewById(R.id.b83);
        m83Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m83Button);} });
        m84Button = (Button) findViewById(R.id.b84);
        m84Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m84Button);} });
        m85Button = (Button) findViewById(R.id.b85);
        m85Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m85Button);} });
        m86Button = (Button) findViewById(R.id.b86);
        m86Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m86Button);} });
        m87Button = (Button) findViewById(R.id.b87);
        m87Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m87Button);} });
        m88Button = (Button) findViewById(R.id.b88);
        m88Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ButtonClick(m88Button);} });
    }

    private void ButtonClick(Button button){
        button.setText("Clicked");
    }




}
