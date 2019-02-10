package com.blogspot.atifsoftwares.imagetotextapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    Button btnSave;
    ImageView imgV;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        btnSave = (Button) findViewById(R.id.btn2);
        imgV = (ImageView) findViewById(R.id.imageIv);
        txt = (TextView) findViewById(R.id.textView1);

        String txtMain = getIntent().getStringExtra("txt");
//        txt.setText(txt.getText().toString()+ " " +txtMain); //вот из-за этойй  залупы не робит если шо 2 активити Error over999


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
