package com.blogspot.atifsoftwares.imagetotextapp;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.ParcedText;
import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.ParsedResult;
import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.services.UserService;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IOCRCallBack  {

    //Константы
    static final private String TAG = "MyApp";
    static final private int CHOOSE_THIEF = 21;
    static final private int ACTIVE2 = 22;
    static final private int ACTIVE_SETT = 23;
    static final private int ID_FOR_CARD = 9000;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    int ID_land = 1;

    final String cam [] = {"Camera","Камера"};
    final String gal [] = {"Gallery","Галерея"};

    final String[] it = {"Setting","Настройки"}; //относиться к мюню и item

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    DBHelper dbHelper;
    SQLiteDatabase DB;

    IOCRCallBack mIOCRCallBack;
    String mAPiKey = "f5da5b6bed88957";
    boolean isOverlayRequired = true;
    String mImageUrl = "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg";
    String mLanguage = "rus";

    int image_id;
    String[] colors = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckSetting();
        selectTheme();
        setContentView(R.layout.activity_main);
        mIOCRCallBack = this;

        //Получение разрешений и подключение к БД
        GetPermission();
        ConnectDB();

        //Инициализация первого активити
        Init1activity();

        //Добавление свойств кнопке
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageImportDialog();
            }
        });


    }

    private void CheckSetting() {
        SharedPreferences mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        if(!(mSettings.contains(Setting.APP_PREFERENCES_URL)) || !(mSettings.contains(Setting.APP_PREFERENCES_THEME)) ||!(mSettings.contains(Setting.APP_PREFERENCES_RU))) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(Setting.APP_PREFERENCES_URL, Setting.DEFAULT_URL);
            editor.putString(Setting.APP_PREFERENCES_THEME, Setting.DEFAULT_THEME);
            editor.putString(Setting.APP_PREFERENCES_RU, Setting.DEFAULT_RU);
            editor.apply();
        }
    }

    //actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mi = menu.getItem(1);
        mi.setTitle(it[ID_land]);
        return true;
    }
    //handle actionbar item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.addImage){
            showImageImportDialog();
        }
        if (id == R.id.settings){
            Intent intent = new Intent(this, Setting.class);
            startActivityForResult(intent, ACTIVE_SETT);
        }
        return super.onOptionsItemSelected(item);
    }
    private void showImageImportDialog() {
        //items to display in dialog
        String items [] = {
                cam[ID_land] , gal [ID_land]
        };
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //set title
        String [] img = {"Select Image","Выберите"};

        dialog.setTitle(img[ID_land]);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    //camera option clicked
                    if (!checkCameraPermission()){
                        //camera permission not allowed, request it
                        requestCameraPermission();
                    }
                    else {
                        //permission allowed, take picture
                        pickCamera();
                    }
                }
                if (which == 1){
                    //gallery option clicked
                    if (!checkStoragePermission()){
                        //Storage permission not allowed, request it
                        requestStoragePermission();
                    }
                    else {
                        //permission allowed, take picture
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show(); //show dialog
    }

    private void pickGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        //intent to take image from camera, it will also be save to storage to get high quality image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic"); //title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text");// description
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        /*Check camera permission and return the result
         *In order to get high quality image we have to save image to external storage first
         * before inserting to image view that's why storage permission will also be required*/
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void GetPermission() {
        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void ConnectDB() {
        dbHelper = new DBHelper(this);
        DB = dbHelper.getWritableDatabase();
    }

    private void AddListenerForCard(int id) {
        Cursor  c = DB.query(DBHelper.TABLE_NAME,null,"_id = " + Integer.toString(id),null,null,null,null);
        if (c.moveToFirst()) {
            Intent intent = new Intent(this, Main3Activity.class);
            intent.putExtra("id", c.getInt(c.getColumnIndex("_id")));
            intent.putExtra("name", c.getString(c.getColumnIndex("name")));
            intent.putExtra("subject", c.getString(c.getColumnIndex("subject")));
            intent.putExtra("company", c.getString(c.getColumnIndex("company")));
            intent.putExtra("telephone", c.getString(c.getColumnIndex("telephone")));
            intent.putExtra("URL", c.getString(c.getColumnIndex("URL")));
            intent.putExtra("email", c.getString(c.getColumnIndex("email")));
            intent.putExtra("img_path", c.getString(c.getColumnIndex("img_path")));
            intent.putExtra("img_name", c.getString(c.getColumnIndex("img_name")));
            intent.putExtra("other", c.getString(c.getColumnIndex("description")));
            intent.putExtra("date", c.getString(c.getColumnIndex("date")));

            startActivityForResult(intent, ACTIVE2);
        }
    }
    private void Init1activity(){
        LinearLayout main = (LinearLayout)findViewById(R.id.main_layout);
        main.removeAllViews();

        Cursor cursor = DB.query("[Cards]", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id")) + ID_FOR_CARD;
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String company = cursor.getString(cursor.getColumnIndex("subject"));
                String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                String dat = cursor.getString(cursor.getColumnIndex("date"));
                Bitmap bitmap = loadImageFromStorage(cursor.getString(cursor.getColumnIndex("img_path")),  cursor.getString(cursor.getColumnIndex("img_name")));

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f);
                LinearLayout.LayoutParams p5 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                p2.setMargins(20,0,0,0);
                p3.setMargins(30,40,30,30);

                Context text_title = new ContextThemeWrapper(getBaseContext(),R.style.textviewtitle);
                Context text = new ContextThemeWrapper(getBaseContext(),R.style.textview);
                Context border = new ContextThemeWrapper(getBaseContext(),R.style.border_bottom);
                Context d = new ContextThemeWrapper(getBaseContext(),R.style.data);

                LinearLayout vertical_Main = new LinearLayout(this);
                vertical_Main.setOrientation(LinearLayout.VERTICAL);
                vertical_Main.setLayoutParams(p3);
                vertical_Main.setId(id);
                main.addView(vertical_Main);

                LinearLayout horisont_liner = new LinearLayout(this);
                horisont_liner.setOrientation(LinearLayout.HORIZONTAL);
                horisont_liner.setLayoutParams(p3);
                vertical_Main.addView(horisont_liner);

                LinearLayout vertical_main = new LinearLayout(this);
                vertical_main.setOrientation(LinearLayout.VERTICAL);
                vertical_main.setLayoutParams(p);
                horisont_liner.addView(vertical_main);

                LinearLayout vertical_text = new LinearLayout(this);
                vertical_text.setOrientation(LinearLayout.VERTICAL);
                vertical_main.addView(vertical_text);
                //Дата
                TextView TextView_data = new TextView(this);
                TextView_data.setTextColor(Color.parseColor(colors[1]));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date date1 = new Date();
                try {
                    date1 = dateFormat.parse(dat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date now = new Date();

                int seconds = (int) ((now.getTime() - date1.getTime()) / (1000));

                String [] sec = {"sec","сек."};
                String [] min = {"min","мин."};
                String [] hour = {"hour","ч."};
                String [] day = {"day","дней"};

                if (seconds < 60){TextView_data.setText(seconds + sec[ID_land]); }
                else if (seconds < 60*60) {TextView_data.setText(((int)(seconds/60)) + min[ID_land]);}
                else if (seconds < 60*60*24) {TextView_data.setText(((int)(seconds/60/60)) + hour[ID_land]);}
                else {TextView_data.setText(((int)(seconds/60/60/24)) + day[ID_land]);}

                vertical_main.addView(TextView_data);

                //Имя
                TextView TextView_name = new TextView(text_title);
                TextView_name.setText(DeleteLastSibol(name));
                TextView_name.setTextColor(Color.parseColor(colors[0]));
                vertical_text.addView(TextView_name);
                //Должность
                TextView TextView_company = new TextView(text);
                TextView_company.setText(DeleteLastSibol(company));
                TextView_company.setTextColor(Color.parseColor(colors[0]));
                vertical_text.addView(TextView_company);
                //Телефон
                TextView TextView_telephon = new TextView(text);
                TextView_telephon.setText(DeleteLastSibol(telephone));
                TextView_telephon.setTextColor(Color.parseColor(colors[0]));
                vertical_text.addView(TextView_telephon);

                LinearLayout vertical_img = new LinearLayout(this);
                vertical_img.setOrientation(LinearLayout.VERTICAL);
                vertical_img.setLayoutParams(p4);
                horisont_liner.addView(vertical_img);

                ImageView img = new ImageView(this);
                img.setImageBitmap(scaleDown(bitmap,400,true));
                vertical_img.addView(img);

                TextView v = new TextView(border);
                v.setBackgroundColor(Color.parseColor(colors[2]));
                vertical_Main.addView(v);


                vertical_Main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddListenerForCard(v.getId() - ID_FOR_CARD);
                    }
                });

            } while (cursor.moveToNext());
        } else {
            LinearLayout horisontal_img = new LinearLayout(this);
            horisontal_img.setOrientation(LinearLayout.VERTICAL);
            horisontal_img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            if(image_id == R.drawable.mosner){
                View par = (View) main.getParent();
                ((ScrollView) par).setBackgroundColor(Color.parseColor("#3a7e73"));
            }
            main.addView(horisontal_img);

            ImageView img = new ImageView(this);
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Bitmap b = BitmapFactory.decodeResource(getResources(), image_id);
            img.setImageBitmap(scaleDown(b,1000,true));
            horisontal_img.addView(img);

            cursor.close();
        }
    }
    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length >0){
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        pickCamera();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if (grantResults.length >0){
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickGallery();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //handle image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_THIEF) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String Name = data.getStringExtra("Name");
                String Subject = data.getStringExtra("Subject");
                String Company = data.getStringExtra("Company");
                String Email = data.getStringExtra("Email");
                String Telephone = data.getStringExtra("Telephone");
                String URL = data.getStringExtra("URL");
                String Other_text = data.getStringExtra("Other");
                Uri URI = Uri.parse(data.getStringExtra("URI"));

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), URI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Cursor c = DB.rawQuery("SELECT * FROM Cards ORDER BY _id DESC LIMIT 1;", new String[] {});
                int lastId = -1;
                if (c != null && c.moveToFirst()) {
                    lastId = c.getInt(0);
                }
                String filename = "file"+Integer.toString(lastId+1);
                String path = saveToInternalStorage(scaleDown(bitmap,800,true), filename);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.NAME,Name);
                contentValues.put(DBHelper.SUBJECT,Subject);
                contentValues.put(DBHelper.COMPANY,Company);
                contentValues.put(DBHelper.EMAIL,Email);
                contentValues.put(DBHelper.TELEPHONE,Telephone);
                contentValues.put(DBHelper.URL,URL);
                contentValues.put(DBHelper.DESCRIPTION, Other_text);
                contentValues.put(DBHelper.IMAGE_PATH, path);
                contentValues.put(DBHelper.IMAGE_NAME, filename);
                contentValues.put(DBHelper.DATE, currentDateandTime);

                long id = DB.insert(DBHelper.TABLE_NAME, null, contentValues);
                System.out.print("Занесено в табл " + id + '\n');
            }
        } else if(requestCode == ACTIVE2){
            if (resultCode == RESULT_OK) {
                assert data != null;
                String doo = data.getStringExtra("do");
                if(doo.equals("dell")){
                    int id = data.getIntExtra("id", -1);
                    if(id != -1){
                        DB.delete(DBHelper.TABLE_NAME, "_id = " + id, null);
                    }
                } else if (doo.equals("edit")){
                    int id = data.getIntExtra("id", -1);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.NAME, data.getStringExtra("Name"));
                    contentValues.put(DBHelper.SUBJECT,data.getStringExtra("Subject"));
                    contentValues.put(DBHelper.COMPANY,data.getStringExtra("Company"));
                    contentValues.put(DBHelper.EMAIL,data.getStringExtra("Email"));
                    contentValues.put(DBHelper.TELEPHONE,data.getStringExtra("Telephone"));
                    contentValues.put(DBHelper.URL,data.getStringExtra("URL"));

                    DB.update(DBHelper.TABLE_NAME, contentValues, "_id="+id, null);
                }

            }
        } else if(requestCode == ACTIVE_SETT){
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra("type").equals("DB")) {
                    DB.execSQL("DROP TABLE IF EXISTS " + DBHelper.TABLE_NAME);
                    String zp = "CREATE TABLE " + DBHelper.TABLE_NAME + " (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                            DBHelper.NAME + " text, " +
                            DBHelper.SUBJECT + " text, " +
                            DBHelper.COMPANY + " text, " +
                            DBHelper.EMAIL + " text, " +
                            DBHelper.TELEPHONE + " text, " +
                            DBHelper.URL + " text, " +
                            DBHelper.DESCRIPTION + " text, " +
                            DBHelper.DATE + " text, " +
                            DBHelper.IMAGE_NAME + " text, " +
                            DBHelper.IMAGE_PATH + " text)";

                    DB.execSQL(zp);
                } else if (data.getStringExtra("type").equals("save")) {
                    this.finish();
                    this.startActivity(this.getIntent());
                }
            }

        } else if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //got image from gallery now crop it
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //got image from camera now crop it
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                        .start(this);
            }
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                image_uri = result.getUri(); //get image uri

                //get drawable bitmap for text recognition
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,30,stream);
                    byte[] byteArray = stream.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(MainActivity.this, mAPiKey, isOverlayRequired, bitmapToBase64(bitmap), mLanguage,mIOCRCallBack);
//                oCRAsyncTask.execute();
                UserService service = new UserService();
                try {
                    service.saveUserImage(this, new File(image_uri.getPath()), new Callback<ParcedText>() {
                        @Override
                        public void onResponse(Call<ParcedText> call, Response<ParcedText> response) {
                            List<ParsedResult> list = response.body().getParsedResults();
                            String[] items = new String[list.size()];

                            for (int i = 0; i < list.size(); i++) {
                                items[i] = list.get(i).getParsedText();
                            }
                            Log.d(TAG, Arrays.toString(items));
                        }

                        @Override
                        public void onFailure(Call<ParcedText> call, Throwable t) {
                            Log.d(TAG, "Upload Failed.!");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectTheme() {
        SharedPreferences mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        String theme = mSettings.getString(Setting.APP_PREFERENCES_THEME, "");
        String lang = mSettings.getString(Setting.APP_PREFERENCES_LANG, "0");
        ID_land = Integer.parseInt(lang);

        switch (theme){
            case "0":
                getTheme().applyStyle(R.style.BlueLightView, true);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                image_id = R.drawable.mosner;
                break;
            case "1":
                getTheme().applyStyle(R.style.GreelLightView, true);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                image_id = R.drawable.mosner;
                break;
            case "2":
                getTheme().applyStyle(R.style.DarkBlueView, true);
                colors[0] = "#ffffff";
                colors[1] = "#5b7a8d";
                colors[2] = "#707070";
                image_id = R.drawable.clickoncamerabg;
                break;
            case "3":
                getTheme().applyStyle(R.style.GreenBlueView, true);
                colors[0] = "#ffffff";
                colors[1] = "#64947b";
                colors[2] = "#707070";
                image_id = R.drawable.clickoncameragreen;
                break;
            default:
                getTheme().applyStyle(R.style.BlueLightView, true);
                colors[0] = "#000000";
                colors[1] = "#757575";
                colors[2] = "#757575";
                image_id = R.drawable.mosner;
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Init1activity();
    }

    protected String DeleteLastSibol(String str){
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    @Override
    public void getOCRCallBackResult(String response) {
        String yspex = response;
        JSONObject jsonObj = null;
        String stringTEXT;
        try {
            jsonObj = new JSONObject(yspex);
            stringTEXT = jsonObj.getJSONArray("ParsedResults").getJSONObject(0).getString("ParsedText");

            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("imageUri", image_uri.toString());
            intent.putExtra("fname",stringTEXT);
            intent.putExtra("type",1);
            startActivityForResult(intent, CHOOSE_THIEF);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String bas = "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
        return bas;
    }
}