package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    protected Intent result;

    protected String name;
    protected String company;
    protected String telephone;
    protected String URL;
    protected String email;
    protected byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        final Intent intent = getIntent();
        result = new Intent();

        Button btn_del = (Button)findViewById(R.id.button_delete);
        Button btn_edit = (Button)findViewById(R.id.button_edit);

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dell(intent.getIntExtra("id",-1));
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(intent.getIntExtra("id",-1));
            }
        });

        name = intent.getStringExtra("name");
        company = intent.getStringExtra("company");
        telephone = intent.getStringExtra("telephone");
        URL = intent.getStringExtra("URL");
        email = intent.getStringExtra("email");
        img = intent.getByteArrayExtra("img");

        Bitmap bitmap = getImage(img);

        LinearLayout main = (LinearLayout)findViewById(R.id.MainView);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //картинка
        ImageView img_view = new ImageView(this);
        img_view.setLayoutParams(p);
        img_view.setImageBitmap(bitmap);
        main.addView(img_view);

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

    protected void dell(int id){
        result.putExtra("id", id);
        setResult(RESULT_OK, result);
        finish();
    }

    protected void edit(int id){

    }

    protected static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
