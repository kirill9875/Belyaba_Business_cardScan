package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_URL = "url_json"; // Json
    public static final String APP_PREFERENCES_THEME = "theme"; // тема
    public static final String APP_PREFERENCES_RU = "ru"; // рус распознание

    public static final String DEFAULT_URL = "https://salesprrest.croc.ru/api/leads"; // Json
    public static final String DEFAULT_THEME = "0"; // тема
    public static final String DEFAULT_RU = "false"; // рус распознание

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        TextView sett_url = (TextView)findViewById(R.id.sett_url);
        Spinner sett_theme = (Spinner)findViewById(R.id.sett_theme);
        Switch sett_ru = (Switch)findViewById(R.id.sett_ru);
        Button sett_DB = (Button)findViewById(R.id.sett_DB);


        sett_DB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        });

        if(!(mSettings.contains(APP_PREFERENCES_URL)) || !(mSettings.contains(APP_PREFERENCES_THEME)) ||!(mSettings.contains(APP_PREFERENCES_RU))) {
            setdefaultsetting();
            sett_url.setText(DEFAULT_URL);
        } else {
            String url = mSettings.getString(APP_PREFERENCES_URL, "");
            String theme = mSettings.getString(APP_PREFERENCES_THEME, "");
            String ru = mSettings.getString(APP_PREFERENCES_RU, "");

            sett_url.setText(url);
            sett_theme.setSelection(Integer.parseInt(theme));
            sett_ru.setChecked(Boolean.parseBoolean(ru));
        }

    }

    protected void setdefaultsetting(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_URL, DEFAULT_URL);
        editor.putString(APP_PREFERENCES_THEME, DEFAULT_THEME);
        editor.putString(APP_PREFERENCES_RU, DEFAULT_RU);
        editor.apply();
    }
}
