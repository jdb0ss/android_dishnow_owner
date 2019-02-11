package com.picke.dishnow_owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.JSONParser;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResSignupActivity extends AppCompatActivity {

    private Button imageuploadbutton;
    private Button nextbutton;
    private EditText textresnum;
    private EditText textownername;
    private EditText Eimagelocal;
    private String sresnum;
    private String sownername;

    String imagepath="";
    String id;
    RequestQueue requestQueue;
    String resauth_url_url = "http://claor123.cafe24.com/ResAuthURL.php";
    String imageupload_url = "http://claor123.cafe24.com/upload/res_auth/ImageUpload.php";
    private UserInfoClass userInfoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_signup);
        nextbutton = findViewById(R.id.ressignup_signupbutton);
        imageuploadbutton = findViewById(R.id.ressignup_imagebutton);
        textresnum = findViewById(R.id.ressignup_resnum);
        textownername = findViewById(R.id.ressignup_name);
        Eimagelocal = findViewById(R.id.ressignup_imagefile);

        Toolbar toolbar = findViewById(R.id.ressignup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textresnum.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        textownername.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eimagelocal.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        final Geocoder geocoder = new Geocoder(this);

        id = userInfoClass.getuId();

        imageuploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
            }
        });

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sresnum = textresnum.getText().toString();
                sownername = textownername.getText().toString();

                if(sresnum.length()==0){
                    Toast.makeText(getApplicationContext(),"사업자 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(sownername.length()==0){
                    Toast.makeText(getApplicationContext(),"사업주명을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    requestQueue.add(stringRequest);
                    userInfoClass.setOwnername(textownername.getText().toString());
                    userInfoClass.setResid(textresnum.getText().toString());
                    Intent intent = new Intent(ResSignupActivity.this, ResInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    final StringRequest stringRequest = new StringRequest(Request.Method.POST, resauth_url_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("spark123","errorv");
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("m_url","http://claor123.cafe24.com/upload/res_auth/"+JSONParser.get_url());
            params.put("m_id",id);
            return params;
        }
    };


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
                Log.d("spark123","jsonparsererror");
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean){
            super.onPostExecute(aBoolean);
            if(progressDialog!=null)progressDialog.dismiss();
            Eimagelocal.setText(JSONParser.get_url());

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
            new ImageUploadTask().execute(imageupload_url,imagepath);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}