package com.blogspot.atifsoftwares.imagetotextapp.jsonclient.services;

import android.content.Context;

import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.ApiClient;
import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.ApiClientBuilder;
import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.FileUtils;
import com.blogspot.atifsoftwares.imagetotextapp.jsonclient.ParcedText;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class UserService {

    public void saveUserImage(Context context, File imageFile, Callback<ParcedText> callback) throws IOException {

        imageFile = new Compressor(context).compressToFile(imageFile);
        // create upload service client
        ApiClient service = ApiClientBuilder.getMGClient();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FileUtils.getFileExtension(imageFile.getAbsolutePath())), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        // add another part within the multipart request
        RequestBody apikey = RequestBody.create(MediaType.parse("text/plain"), "f5da5b6bed88957");
        RequestBody language = RequestBody.create(MediaType.parse("text/plain"),"rus");
        RequestBody isOverlayRequired = RequestBody.create(MediaType.parse("text/plain"),"false");;

        Call<ParcedText> result =  service.uploadImage(apikey, language,isOverlayRequired, body);

        result.enqueue(callback);

    }

}
