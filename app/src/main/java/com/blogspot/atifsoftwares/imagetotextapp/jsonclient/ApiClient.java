//ApiClient.java
package com.blogspot.atifsoftwares.imagetotextapp.jsonclient;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created on 10/15/17.
 */

public interface ApiClient {

    @Multipart
    @POST("/parse/image")
    Call<ParcedText> uploadImage(@Part("apikey") RequestBody apikey,
                                 @Part("language") RequestBody language,
                                 @Part("isOverlayRequired") RequestBody isOverlayRequired,
                                 @Part MultipartBody.Part file);
}
