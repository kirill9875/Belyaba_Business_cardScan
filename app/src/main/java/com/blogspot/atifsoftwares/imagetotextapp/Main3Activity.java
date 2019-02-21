package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main3Activity extends AppCompatActivity {

    static final private int ACTIVE3 = 23;
    Intent intent = null;

    protected Intent result;

    protected String name;
    protected String subject;
    protected String company;
    protected String telephone;
    protected String URL;
    protected String email;
    protected String img_path;
    protected String img_name;
    protected String other;
    protected String dat;

    LinearLayout vertical_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectTheme();
        setContentView(R.layout.activity_main3);

        intent = getIntent();
        result = new Intent();

        name = intent.getStringExtra("name");
        subject = intent.getStringExtra("subject");
        company = intent.getStringExtra("company");
        telephone = intent.getStringExtra("telephone");
        URL = intent.getStringExtra("URL");
        email = intent.getStringExtra("email");
        img_path = intent.getStringExtra("img_path");
        img_name = intent.getStringExtra("img_name");
        other = intent.getStringExtra("other");
        dat = intent.getStringExtra("date");

        Bitmap bitmap = loadImageFromStorage(img_path, img_name);

        LinearLayout main = (LinearLayout)findViewById(R.id.MainView);

        vertical_main = new LinearLayout(this);
        vertical_main.setOrientation(LinearLayout.VERTICAL);
        vertical_main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        //картинка
        ImageView img_view = new ImageView(this);
        img_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        img_view.setImageBitmap(bitmap);
        main.addView(img_view);
        main.addView(vertical_main);
        //Имя
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.name),"Name",name);
        //Саб
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.job),"Subject",subject);
        //Компания
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.company),"Company",company);
        //Телефон
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.telephone),"Telephone",telephone);
        //Почта
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.mail),"E-mail",email);
        //URL
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.mail),"URL",URL);
        //Другое
        addRow(BitmapFactory.decodeResource(getResources(), R.drawable.mail),"Other",other);

        TextView TextView_name = new TextView(this);
        TextView_name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView_name.setText("Added " + dat);
        main.addView(TextView_name);
    }

    protected void dell(int id){
        result.putExtra("id", id);
        result.putExtra("do", "dell");
        setResult(RESULT_OK, result);
        finish();
    }

    protected void edit(int id){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", 3);
        intent.putExtra("name", name);
        intent.putExtra("subject", subject);
        intent.putExtra("company", company);
        intent.putExtra("telephone", telephone);
        intent.putExtra("URL", URL);
        intent.putExtra("email", email);
        intent.putExtra("img_path", img_path);
        intent.putExtra("img_name", img_name);
        intent.putExtra("other", other);

        startActivityForResult(intent, ACTIVE3);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ACTIVE3) {
            if (resultCode == RESULT_OK) {
                data.putExtra("do", "edit");
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    private Bitmap loadImageFromStorage(String path, String name)
    {
        Bitmap b = null;
        try {
            File f=new File(path, name + ".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    private void addRow(Bitmap b, String title, String text){
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Context theme = new ContextThemeWrapper(getBaseContext(),R.style.textviewtitle2);
        Context border = new ContextThemeWrapper(getBaseContext(),R.style.border_bottom);

        p.setMargins(40,0,0,0);
        p2.setMargins(30,20,30,0);
        p3.setMargins(40,5,0,20);

        LinearLayout horisontal_main = new LinearLayout(this);
        horisontal_main.setOrientation(LinearLayout.HORIZONTAL);
        horisontal_main.setLayoutParams(p2);
        vertical_main.addView(horisontal_main);


        ImageView img_view = new ImageView(this);
        img_view.setLayoutParams(new LinearLayout.LayoutParams(90,90));
        img_view.setImageBitmap(b);
        horisontal_main.addView(img_view);

        LinearLayout vertical_text = new LinearLayout(this);
        vertical_text.setOrientation(LinearLayout.VERTICAL);
        vertical_text.setLayoutParams(p);
        horisontal_main.addView(vertical_text);

        TextView title_TextView_name = new TextView(theme);
        title_TextView_name.setLayoutParams(p);
        title_TextView_name.setText(DeleteLastSibol(text));
        vertical_text.addView(title_TextView_name);

        TextView TextView_name = new TextView(this);
        TextView_name.setLayoutParams(p3);
        TextView_name.setText(title);
        vertical_text.addView(TextView_name);

        TextView v = new TextView(border);
        vertical_text.addView(v);
    }

    protected String DeleteLastSibol(String str){
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_3act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.del){
            dell(intent.getIntExtra("id",-1));
        }
        if (id == R.id.edit) {
            edit(intent.getIntExtra("id",-1));
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectTheme() {
        SharedPreferences mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        String theme = mSettings.getString(Setting.APP_PREFERENCES_THEME, "");
        switch (theme){
            case "0":
                getTheme().applyStyle(R.style.BlueLightView, true);
                break;
            case "1":
                getTheme().applyStyle(R.style.GreelLightView, true);
                break;
            case "2":
                getTheme().applyStyle(R.style.DarkBlueView, true);
                break;
            case "3":
                getTheme().applyStyle(R.style.GreenBlueView, true);
                break;
            default:
                getTheme().applyStyle(R.style.BlueLightView, true);
                break;
        }
    }
}
