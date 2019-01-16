package com.picke.dishnow_owner;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {
    static String filename="";

    public static JSONObject uploadImage(String imageUploadUrl,String sourceImageFile,String id){
        try{
            File sourceFile = new File(sourceImageFile);
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);
            filename = id+"_res_auth"+filename.substring(filename.lastIndexOf("."));

            //OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file",filename,RequestBody.create(MEDIA_TYPE_PNG,sourceFile))
                    .addFormDataPart("result","photo_image")
                    .build();

            Request request = new Request.Builder()
                    .url(imageUploadUrl)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            return new JSONObject(res);
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG2", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG2", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
    public static String get_url(){
        return filename;
    }
}
