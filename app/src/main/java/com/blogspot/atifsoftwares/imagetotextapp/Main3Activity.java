package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String company = intent.getStringExtra("company");
        String telephone = intent.getStringExtra("telephone");
        String URL = intent.getStringExtra("URL");
        String email = intent.getStringExtra("email");

        LinearLayout main = (LinearLayout)findViewById(R.id.MainView);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Имя
        TextView TextView_name = new TextView(this);
        TextView_name.setLayoutParams(p);
        TextView_name.setText(name);
        main.addView(TextView_name);
        //Компания
        TextView TextView_company = new TextView(this);
        TextView_company.setLayoutParams(p);
        TextView_company.setText(company);
        main.addView(TextView_company);
        //Телефон
        TextView TextView_telephone = new TextView(this);
        TextView_telephone.setLayoutParams(p);
        TextView_telephone.setText(telephone);
        main.addView(TextView_telephone);
        //Почта
        TextView TextView_email = new TextView(this);
        TextView_email.setLayoutParams(p);
        TextView_email.setText(email);
        main.addView(TextView_email);
        //URL
        TextView TextView_url = new TextView(this);
        TextView_url.setLayoutParams(p);
        TextView_url.setText(URL);
        main.addView(TextView_url);
    }
}
