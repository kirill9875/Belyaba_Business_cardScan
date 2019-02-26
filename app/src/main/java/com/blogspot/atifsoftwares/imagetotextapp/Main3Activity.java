package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main3Activity extends AppCompatActivity {

    static final private int ACTIVE3 = 23;
    Intent intent = null;

    static int ID_lang = 1;

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
    Bitmap[] icons = new Bitmap[7];
    String[] colors = new String[4];

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
        LinearLayout background_image = new LinearLayout(this);
        background_image.setOrientation(LinearLayout.HORIZONTAL);
        background_image.setBackgroundColor(Color.parseColor(colors[3]));
        background_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        main.addView(background_image);

        ImageView img_view = new ImageView(this);
        img_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        img_view.setImageBitmap(bitmap);
        background_image.addView(img_view);

        main.addView(vertical_main);

        String [] name_icons = {"Name","Имя"};
        String [] subject_icons = {"Subject","Должность"};
        String [] comp_icons = {"Company","Организация"};
        String [] tel_icons = {"Telephone","Номер телефона"};
        String [] email_icons = {"E-mail","Эл.почта"};
        String [] url_icons = {"URL","Сайт"};
        String [] other_icons = {"Other","Другое"};

        //Имя
        addRow(icons[0],name_icons[ID_lang],name);
        //Саб
        addRow(icons[1],subject_icons[ID_lang],subject);
        //Компа
        addRow(icons[2],comp_icons[ID_lang],company);
        //Телеф
        addRow(icons[3],tel_icons[ID_lang],telephone);
        //Почта
        addRow(icons[4],email_icons[ID_lang],email);
        //URL
        addRow(icons[5],url_icons[ID_lang],URL);
        //Друго
        addRow(icons[6],other_icons[ID_lang],other);

        String [] add = {"Added ","Добавлено "};

        TextView TextView_name = new TextView(this);
        TextView_name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView_name.setText(add[ID_lang] + dat);
        TextView_name.setTextColor(Color.parseColor(colors[1]));
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
        p2.setMargins(50,20,50,0);
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
        title_TextView_name.setTextColor(Color.parseColor(colors[0]));
        vertical_text.addView(title_TextView_name);

        TextView TextView_name = new TextView(this);
        TextView_name.setLayoutParams(p3);
        TextView_name.setTextColor(Color.parseColor(colors[1]));
        TextView_name.setText(title);
        vertical_text.addView(TextView_name);

        TextView v = new TextView(border);
        v.setBackgroundColor(Color.parseColor(colors[2]));
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
        String lang = mSettings.getString(Setting.APP_PREFERENCES_LANG, "0");
        ID_lang = Integer.parseInt(lang);

        switch (theme){
            case "0":
                getTheme().applyStyle(R.style.BlueLightView, true);
                icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconprofile);
                icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconbriefcase);
                icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconmonument);
                icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.i_ring);
                icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.i_envelope);
                icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconsphere);
                icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconpuzzle);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                colors[3] = "#ffffff";
                break;
            case "1":
                getTheme().applyStyle(R.style.GreelLightView, true);
                icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconprofile);
                icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconbriefcase);
                icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconmonument);
                icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.i_ring);
                icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.i_envelope);
                icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconsphere);
                icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconpuzzle);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                colors[3] = "#ffffff";
                break;
            case "2":
                getTheme().applyStyle(R.style.DarkBlueView, true);
                icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconnamedb);
                icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconjobdb);
                icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconcompanydb);
                icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconphonedb);
                icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconmaildb);
                icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconurldb);
                icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.b_iconpuzzledb);
                colors[0] = "#ffffff";
                colors[1] = "#5b7a8d";
                colors[2] = "#707070";
                colors[3] = "#0e1621";
                break;
            case "3":
                getTheme().applyStyle(R.style.GreenBlueView, true);
                icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconname);
                icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconbriefcase);
                icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconcompany);
                icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconphone);
                icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconmail);
                icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconurl);
                icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.g_iconpuzzledb);
                colors[0] = "#ffffff";
                colors[1] = "#64947b";
                colors[2] = "#707070";
                colors[3] = "#2a2a2a";
                break;
            default:
                getTheme().applyStyle(R.style.BlueLightView, true);
                icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconprofile);
                icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconbriefcase);
                icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconmonument);
                icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.i_ring);
                icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.i_envelope);
                icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconsphere);
                icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.i_iconpuzzle);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                colors[3] = "#ffffff";
                break;
        }
    }
}
