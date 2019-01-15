package com.picke.dishnow_owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class ResSignupActivity extends AppCompatActivity {

    private Button imageuploadbutton;
    String imagepath;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_signup);

        Intent intent = getIntent();
        id = intent.getStringExtra("o_id");
        imageuploadbutton = findViewById(R.id.res_signup_imageupload);
        imageuploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
            }
        });

    }

    private class ImageUploadTask extends AsyncTask<String, Integer, Boolean>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... params){
            try{
                JSONObject jsonObject = JSONParser.uploadImage(params[0],params[1],id);
                if(jsonObject != null)
                    return jsonObject.getString("result").equals("success");
            } catch (JSONException e){

            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean){
            super.onPostExecute(aBoolean);
            if(progressDialog!=null)progressDialog.dismiss();
           imagepath="";

        }
    }

    private void getGallery(){
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent,"이미지를 올릴 매체를 선택하세요.");
        startActivityForResult(chooserIntent,1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode == 1010){
            if(data==null){
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri,filePathColumn,null,null,null);

            if(cursor!=null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagepath = cursor.getString(columnIndex);
            }
            String ImageUploadURL = "http://claor123.cafe24.com/upload/ImageUpload.php";
            new ImageUploadTask().execute(ImageUploadURL,imagepath);
        }
    }
}
