package com.picke.dishnow_owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResSignupActivity extends AppCompatActivity {

    private Button imageuploadbutton;
    private Button buttonressignup;
    private Button buttonfindresnum;
    private Button buttonfindlocal;
    private EditText textresnum;
    private EditText textresname;
    private EditText textownername;
    private EditText textresphonenum;
    private String sresnum;
    private String sresname;
    private String sownername;
    private String sresphonenum;

    String imagepath;
    String id;
    RequestQueue requestQueue;
    String resauth_url_url = "http://claor123.cafe24.com/ResAuthURL.php";
    String imageupload_url = "http://claor123.cafe24.com/upload/res_auth/ImageUpload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_signup);


        Intent intent = getIntent();
        id = intent.getStringExtra("o_id");
        imageuploadbutton = findViewById(R.id.res_signup_findimage);
        buttonressignup = findViewById(R.id.res_signup_ressignup);
        buttonfindresnum = findViewById(R.id.res_signup_findresnum);
        buttonfindlocal = findViewById(R.id.res_signup_findlocal);
        textresnum = findViewById(R.id.res_signup_resnum);
        textresname = findViewById(R.id.res_signup_resname);
        textownername = findViewById(R.id.res_signup_ownername);
        textresphonenum = findViewById(R.id.res_signup_resphonenum);

        imageuploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
            }
        });

        buttonressignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sresnum = textresnum.getText().toString();
                sresname = textresname.getText().toString();
                sownername = textownername.getText().toString();
                sresphonenum = textresphonenum.getText().toString();

                if(sresnum.length()==0){
                    Toast.makeText(getApplicationContext(),"사업자 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(sresname.length()==0){
                    Toast.makeText(getApplicationContext(),"음식점 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(sownername.length()==0){
                    Toast.makeText(getApplicationContext(),"사업주명을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(sresphonenum.length()==0){
                    Toast.makeText(getApplicationContext(),"음식점 전화번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(ResSignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        requestQueue = Volley.newRequestQueue(this);
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
                Log.d("claor1234","errorjsonp");
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
            new ImageUploadTask().execute(imageupload_url,imagepath);
            requestQueue.add(stringRequest);
        }
    }
}