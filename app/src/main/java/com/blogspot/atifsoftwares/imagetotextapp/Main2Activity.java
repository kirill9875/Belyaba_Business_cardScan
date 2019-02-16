package com.blogspot.atifsoftwares.imagetotextapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.net.Uri;

public class Main2Activity extends AppCompatActivity {

    Button btnSave;
    public ImageView imgV;
    public TextView txt;
    int idtext = 4000;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        btnSave = (Button) findViewById(R.id.btn2);

        String txtMain = getIntent().getStringExtra("txt");
//        txt.setText(txt.getText().toString()+ " " +txtMain); //вот из-за этойй  залупы не робит если шо 2 активити Error over999


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String fName = intent.getStringExtra("fname");
        Uri myUri = Uri.parse(intent.getStringExtra("imageUri"));

        ImageView mPreviewIv;
        mPreviewIv = findViewById(R.id.imageIv);
        LinearLayout ll = (LinearLayout)findViewById(R.id.liner_for_edittext);
        for (String retval : fName.split("\n")) {
            LinearLayout horisont_liner = new LinearLayout(this);
            horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
            horisont_liner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            Spinner spin = new Spinner(this);
            spin.setId(idtext);


            final String[] types = new String[] {
                    "Name", "Company", "URL"
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin.setAdapter(adapter);

            horisont_liner.addView(spin);

            EditText et = new EditText(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            et.setLayoutParams(p);
            et.setText(retval);
            et.setId(idtext++);


            horisont_liner.addView(et);
            ll.addView(horisont_liner);
        }
        mPreviewIv.setImageURI(myUri);

    }
}
