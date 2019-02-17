package com.blogspot.atifsoftwares.imagetotextapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    final String SUBJECT = "Subject";
    final String NAME = "Name";
    final String COMPANY = "Company";
    final String EMAIL = "Email";
    final String TELEPHONE = "Telephone";
    final String URL = "URL";

    String _Name = "", _Company = "", _Email = "", _Telephone = "", _URL = "",_Subject = "";

    public ImageView imgV;
    public TextView txt;
    int idtext = 4000;
    int idspinn = 3000;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        String txtMain = getIntent().getStringExtra("txt");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String fName = intent.getStringExtra("fname");
        Uri myUri = Uri.parse(intent.getStringExtra("imageUri"));

        ImageView mPreviewIv;
        mPreviewIv = findViewById(R.id.imageIv);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.liner_for_edittext);
        for (String retval : fName.split("\n")) {
            LinearLayout horisont_liner = new LinearLayout(this);
            horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
            horisont_liner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            Spinner spin = new Spinner(this);
            spin.setId(idspinn++);


            final String[] types = new String[] {
                    NAME, SUBJECT, COMPANY, EMAIL, TELEPHONE, URL, "Delete"
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

        Button btnsave = (Button) findViewById(R.id.button_save);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //work with json
                JSONObject obj = new JSONObject();

                try {

                    // clean string params

                    StringBuffer stringBuffer = new StringBuffer(_Subject);
                    String sub;
                    if (_Subject.length() != 0) {
                        stringBuffer.delete(_Subject.length() - 1,_Subject.length());
                        sub = stringBuffer.toString();
                        System.out.println(sub);
                    }
                    else {
                        sub = _Subject;
                    }

                    //add json obj
                    obj.put("subject", sub);
                    obj.put("lastname", _Name);
                    obj.put("companyname", _Company);
                    obj.put("emailaddress", _Email);
                    obj.put("telephone", _Telephone);
                    obj.put("formurl", _URL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(obj);


                setResult(RESULT_OK, result);
                finish();
            }
        };

        btnsave.setOnClickListener(oclBtnOk);
    }


}
