package com.blogspot.atifsoftwares.imagetotextapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.Proxy.Type.HTTP;

public class Main2Activity extends AppCompatActivity {


    final String [] SUBJECT = new String[] {"Subject", "Должность"};
    final String [] NAME = new String[]  {"Name", "Имя"};
    final String [] COMPANY = new String[] {"Company", "Организация"};
    final String [] EMAIL = new String[] {"Email", "Эл.Почта"};
    final String [] TELEPHONE = new String[] {"Telephone", "Номер телефона"};
    final String [] URL =  new String[] {"URL", "Сайт"};

    int id_lang_eng = 1;

    int id = -1;
    String _Name = "", _Company = "", _Email = "", _Telephone = "", _URL = "",_Subject = "";
    String _Other_text= "null";
    String[] types = null;

    int type = -1;
    boolean mail = false;
    ImageView mPreviewIv;

    String fName;
    Uri myUri;



    public LinearLayout ll;
    int icon;
    String[] colors = new String[2];

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectTheme();
        types = new String[] {
                NAME[id_lang_eng], SUBJECT[id_lang_eng], COMPANY[id_lang_eng], EMAIL[id_lang_eng], TELEPHONE[id_lang_eng], URL[id_lang_eng]
        };

        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if(type == 1){
            Init1activityFrom1(intent);
        } else if(type == 3){
            Init1activityFrom3(intent);
        }

    }

    public void save_ac_btn() {

        Intent result = new Intent();

        View childView_main = null;

        for(int i = 0; i < ll.getChildCount(); i++){
            childView_main = ll.getChildAt(i);
            if (childView_main instanceof LinearLayout){

                View childView_splinn = null;
                View childView_edittext = null;

                childView_splinn = ((LinearLayout) childView_main).getChildAt(0);

                View childView = ((LinearLayout) childView_main).getChildAt(1);
                childView_edittext = ((LinearLayout) childView).getChildAt(0);

                String text = ((EditText)childView_edittext).getText().toString();
                int position;

                if (childView_splinn instanceof Spinner) {
                    position = ((Spinner) childView_splinn).getSelectedItemPosition();
                } else {
                    position = 6;
                }

                switch(position) {
                    case 0:
                        _Name += text + "\n";
                        break;
                    case 1:
                        _Subject += text + "\n";
                        break;
                    case 2:
                        _Company += text + "\n";
                        break;
                    case 3:
                        _Email += text + "\n";
                        break;
                    case 4:
                        _Telephone += text + "\n";
                        break;
                    case 5:
                        _URL += text + "\n";
                        break;
                    case 6:
                        _Other_text = text;
                    default:
                        break;
                }
            }
        }
        result.putExtra("Name", _Name);
        result.putExtra("Subject", _Subject);
        result.putExtra("Company", _Company);
        result.putExtra("Email", _Email);
        result.putExtra("Telephone", _Telephone);
        result.putExtra("URL", _URL);
        result.putExtra("Other", _Other_text);

        if(type == 1){
            result.putExtra("URI", myUri.toString());
        } else if (type == 3){
            result.putExtra("id", id);
        }

        //work with json
        Thread t = new Thread(){

            HttpClient httpClient = new DefaultHttpClient();

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                SharedPreferences mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
                String url = mSettings.getString(Setting.APP_PREFERENCES_URL, "");
                HttpPost request = new HttpPost(url);

                HttpPost request2 = new HttpPost("https://webhook.site/9145bd21-08e8-4d93-a7c9-900b8429d297");

                JSONObject obj = new JSONObject();

                try {

                    JSONArray notebookUsers = new JSONArray();
                    notebookUsers.put("email@address.com");
                    notebookUsers.put("admin@example.com");

                    obj.put("subject", set_null_json(_Subject));
                    obj.put("lastname", set_null_json(_Name));
                    obj.put("companyname", set_null_json(_Company));
                    obj.put("emailaddress", set_null_json(_Email));
                    obj.put("telephone", set_null_json(_Telephone));
                    obj.put("mobilephone", "");

                    // add new type
                    obj.put("description",cleaning_string(_Other_text));
                    obj.put("notificationreceivers",notebookUsers);
                    obj.put("formurl", set_null_json(_URL));

                    obj.put("direction","OBA");
                    obj.put("roistat","1049");
                    obj.put("ClientIDMetrika","12345");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(obj);

                StringEntity params = null;
                try {
                    params = new StringEntity(obj.toString());

                    request.setEntity(params);
                    request2.setEntity(params);

                    request.setHeader("Content-type", "application/json");

                    HttpResponse  response = httpClient.execute(request);

                    HttpResponse  response2 = httpClient.execute(request2);

                    request2.setHeader("Content-Type", "application/json");

                    String responseBody = EntityUtils.toString(response.getEntity());
                    String responseBody2 = EntityUtils.toString(response2.getEntity());

                    int code = response.getStatusLine().getStatusCode();
                    int code2 = response2.getStatusLine().getStatusCode();

                    System.out.println("Answer Server: "+ responseBody + " " + code);
                    System.out.println("Answer Server: "+ responseBody2 + " " + code2);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    httpClient.getConnectionManager().shutdown();
                }

                Looper.loop(); //Loop in the message queue
            }

        };
        t.start();

        setResult(RESULT_OK, result);
        finish();
    }

    private String set_null_json (String str){
        String b = cleaning_string(str);
        if ( b.length() != 0 ) {
            return b;
        } else return "None";
    }

    protected String cleaning_string(String inputtext){
        String clean_string = "";
        String[] c_s = inputtext.split("\n");
        for(String i:c_s){
            clean_string += i + ";";
        }

        if (clean_string != null && clean_string.length() > 0) {
            clean_string = clean_string.substring(0, clean_string.length() - 1);
        }

        return clean_string;
    }
    //actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_2activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_btn){
            save_ac_btn();
        }
        if (id == R.id.add_btn) {
            AddNewRow(0,"   ");
        }
        return super.onOptionsItemSelected(item);
    }


    protected void Init1activityFrom1(Intent intent) {
        fName = intent.getStringExtra("fname");
        myUri = Uri.parse(intent.getStringExtra("imageUri"));

        mPreviewIv = findViewById(R.id.imageIv);

        ll = (LinearLayout)findViewById(R.id.liner_for_edittext);

        for (String retval : fName.split("\n")) {

            boolean email = validateEmail(retval);
            boolean url = validateUrl(retval);
            boolean tel = validateTel(retval);

            if(!smallStr(retval)){
                continue;
            }
            if (email) {
                AddNewRow(3,retval);
            } else if (url) {
                AddNewRow(5,retval);
            } else if (tel) {
                AddNewRow(4,retval);
            } else {
                AddNewRow(0,retval);
            }
        }

        //Set Other
        AddNewRow(-9,DeleteLastSibol(fName));

        mPreviewIv.setImageURI(myUri);
    }

    protected void Init1activityFrom3(Intent intent) {

        mPreviewIv = findViewById(R.id.imageIv);

        ll = (LinearLayout)findViewById(R.id.liner_for_edittext);
        id = intent.getIntExtra("id", -1);

        //картинка
        String img_path = intent.getStringExtra("img_path");
        String img_name = intent.getStringExtra("img_name");
        Bitmap bitmap = loadImageFromStorage(img_path, img_name);
        mPreviewIv.setImageBitmap(bitmap);

        String Name = intent.getStringExtra("name");
        String Subject = intent.getStringExtra("subject");
        String Company = intent.getStringExtra("company");
        String Email = intent.getStringExtra("email");
        String Telephone = intent.getStringExtra("telephone");
        String URL = intent.getStringExtra("URL");
        String other = intent.getStringExtra("other");

        for(String retval: Name.split("\n")){
            AddNewRow(0,retval);
        }
        for(String retval: Subject.split("\n")){
            AddNewRow(1,retval);
        }
        for(String retval: Company.split("\n")){
            AddNewRow(2,retval);
        }
        for(String retval: Email.split("\n")){
            AddNewRow(3,retval);
        }
        for(String retval: Telephone.split("\n")){
            AddNewRow(4,retval);
        }
        for(String retval: URL.split("\n")){
            AddNewRow(5,retval);
        }
        AddNewRow(-9, other);

    }

    protected void AddNewRow(int index, String retval){
        Context d = new ContextThemeWrapper(getBaseContext(),R.style.dell_button);
        Context text = new ContextThemeWrapper(getBaseContext(),R.style.textview_gray);

        LinearLayout vertical_liner = new LinearLayout(this);
        vertical_liner.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(10,30,10,30);
        vertical_liner.setLayoutParams(p);

        ll.addView(vertical_liner);


        if (index == -9){
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0));

            String[] oth = {"Other","Другое"};

            textView.setText(oth[id_lang_eng]);
            textView.setTextColor(Color.parseColor(colors[1]));
            vertical_liner.addView(textView);
        } else {
            Spinner spin = new Spinner(text);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            spin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
            spin.setSelection(index);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor(colors[1])); /* if you want your item to be white */
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            vertical_liner.addView(spin);
        }

        LinearLayout horisont_liner = new LinearLayout(this);
        horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
        horisont_liner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        vertical_liner.addView(horisont_liner);

        EditText et = new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        et.setText(retval);
        et.setTextColor(Color.parseColor(colors[0]));
        horisont_liner.addView(et);

        if (index != -9) {
            ImageButton del = new ImageButton(d);
            del.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.f));
            del.setBackgroundResource(icon);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.print(1);
                    View par = (View) v.getParent();
                    View par_par = (View) par.getParent();
                    View par_par_par = (View) par_par.getParent();
                    ((LinearLayout) par_par_par).removeView(par_par);
                }
            });
            horisont_liner.addView(del);
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

    public boolean validateUrl(String adress){
        if(!((Pattern.compile("ttp|ww")).matcher(adress).find()) && mail){
            return (Pattern.compile(".com|.ru")).matcher(adress).find();
        } else {
            return (Pattern.compile("ttp|ww")).matcher(adress).find();
        }
    }
    public boolean validateEmail(String adress){
        if((Pattern.compile("@")).matcher(adress).find()){
            mail = true;
        }
        return (Pattern.compile("@")).matcher(adress).find();
    }
    public boolean validateTel(String adress){
        return (Pattern.compile("\\d{3,}")).matcher(adress).find();
    }
    public boolean smallStr ( String str ){
        return (Pattern.compile("\\w{4,}")).matcher(str).find();
    }
    protected String DeleteLastSibol(String str){
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void selectTheme() {
        SharedPreferences mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        String theme = mSettings.getString(Setting.APP_PREFERENCES_THEME, "");
        String lang = mSettings.getString(Setting.APP_PREFERENCES_LANG, "0");
        id_lang_eng = Integer.parseInt(lang);

        switch (theme){
            case "0":
                getTheme().applyStyle(R.style.BlueLightView, true);
                icon = R.drawable.delete;
                colors[0] = "#000000";
                colors[1] = "#757575";
                break;
            case "1":
                getTheme().applyStyle(R.style.GreelLightView, true);
                icon = R.drawable.delete;
                colors[0] = "#000000";
                colors[1] = "#757575";
                break;
            case "2":
                getTheme().applyStyle(R.style.DarkBlueView, true);
                icon = R.drawable.icondeletebd;
                colors[0] = "#ffffff";
                colors[1] = "#5b7a8d";
                break;
            case "3":
                getTheme().applyStyle(R.style.GreenBlueView, true);
                icon = R.drawable.icondeletegd;
                colors[0] = "#ffffff";
                colors[1] = "#64947b";
                break;
            default:
                getTheme().applyStyle(R.style.BlueLightView, true);
                icon = R.drawable.delete;
                colors[0] = "#000000";
                colors[1] = "#757575";
                break;
        }
    }
}