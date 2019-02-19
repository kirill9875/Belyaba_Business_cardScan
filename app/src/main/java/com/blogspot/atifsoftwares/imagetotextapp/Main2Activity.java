package com.blogspot.atifsoftwares.imagetotextapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.Proxy.Type.HTTP;

public class Main2Activity extends AppCompatActivity {

    final String SUBJECT = "Subject";
    final String NAME = "Name";
    final String COMPANY = "Company";
    final String EMAIL = "Email";
    final String TELEPHONE = "Telephone";
    final String URL = "URL";

    int id = -1;
    String _Name = "", _Company = "", _Email = "", _Telephone = "", _URL = "",_Subject = "";

    int type = -1;
    int idtext = 4000;
    int idspinn = 3000;
    ImageView mPreviewIv;

    String fName;
    Uri myUri;

    final String[] types = new String[] {
            NAME, SUBJECT, COMPANY, EMAIL, TELEPHONE, URL, "Delete"
    };

    public LinearLayout ll;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
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

        View childView = null;
        for(int i = 0; i < ll.getChildCount(); i++){
            childView = ll.getChildAt(i);
            if (childView instanceof LinearLayout){

                View childView_splinn = null;
                View childView_edittext = null;

                childView_splinn = ((LinearLayout) childView).getChildAt(0);
                childView_edittext = ((LinearLayout) childView).getChildAt(1);

                String text = ((EditText)childView_edittext).getText().toString();
                String position = ((Spinner)childView_splinn).getSelectedItem().toString();

                switch(position) {
                    case NAME:
                        _Name += text + "\n";
                        break;
                    case SUBJECT:
                        _Subject += text + "\n";
                        break;
                    case COMPANY:
                        _Company += text + "\n";
                        break;
                    case EMAIL:
                        _Email += text + "\n";
                        break;
                    case TELEPHONE:
                        _Telephone += text + "\n";
                        break;
                    case URL:
                        _URL += text + "\n";
                        break;
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

                HttpPost request = new HttpPost("https://salesprrest.croc.ru/api/leads");

                HttpPost request2 = new HttpPost("https://webhook.site/9145bd21-08e8-4d93-a7c9-900b8429d297");
//                HttpPost request = new HttpPost("https://salesprrest.croc.ru/api/leads");

                JSONObject obj = new JSONObject();


                try {

                    JSONArray notebookUsers = new JSONArray();
                    notebookUsers.put("email@address.com");
                    notebookUsers.put("admin@example.com");


                    obj.put("subject", cleaning_string(_Subject));
                    obj.put("lastname", cleaning_string(_Name));
                    obj.put("companyname", cleaning_string(_Company));
                    obj.put("emailaddress", cleaning_string(_Email));
                    obj.put("telephone", cleaning_string(_Telephone));
                    obj.put("mobilephone", "");

                    // add new type
                    obj.put("description", cleaning_string(_Telephone));
                    obj.put("notificationreceivers",notebookUsers);
                    obj.put("formurl", cleaning_string(_URL));

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
            add_text();
        }

        return super.onOptionsItemSelected(item);
    }

    private void add_text() {
        LinearLayout horisont_liner = new LinearLayout(this);
        horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
        horisont_liner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Spinner spin = new Spinner(this);
        spin.setId(idspinn++);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(adapter);

        horisont_liner.addView(spin);

        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setId(idtext++);

        horisont_liner.addView(et);
        ll.addView(horisont_liner);

    }

    protected void Init1activityFrom1(Intent intent) {
        fName = intent.getStringExtra("fname");
        myUri = Uri.parse(intent.getStringExtra("imageUri"));

        mPreviewIv = findViewById(R.id.imageIv);

        ll = (LinearLayout)findViewById(R.id.liner_for_edittext);

        for (String retval : fName.split("\n")) {
            AddNewRow(0,retval);
        }
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
    }

    protected void AddNewRow(int index, String retval){
        LinearLayout horisont_liner = new LinearLayout(this);
        horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
        horisont_liner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Spinner spin = new Spinner(this);
        spin.setId(idspinn++);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(adapter);
        spin.setSelection(index);

        horisont_liner.addView(spin);




        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setText(retval);
        et.setId(idtext++);


        horisont_liner.addView(et);

        Button del = new Button(this);

        //del.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        del.setText("x");

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print(1);
                View par = (View) v.getParent();
                View par_par = (View) par.getParent();
                ((LinearLayout)par_par).removeView(par);
            }
        });

        horisont_liner.addView(del);
        ll.addView(horisont_liner);
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
}
