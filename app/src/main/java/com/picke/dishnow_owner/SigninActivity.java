package com.picke.dishnow_owner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class SigninActivity extends AppCompatActivity {

    Button signupbutton;
    Button signinbutton;
    EditText Eidinput;
    EditText Epasswordinput;
    TextView wronginput;

    private String idinput;
    private String passwordinput;

    final int PERMISSION = 1;

    private RequestQueue requestQueue;
    private UserAuthClass userAuthClass;

    private final String login_url = "http://claor123.cafe24.com/Login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        userAuthClass = UserAuthClass.getInstance(getApplicationContext());

        //permission
        if(Build.VERSION.SDK_INT>=23&&ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_NUMBERS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED
                )
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, PERMISSION);
        }

        //auto login
        SharedPreferences auto =  getSharedPreferences("auto",Activity.MODE_PRIVATE);
        String loginid,loginpassword,id,name,resauth;
        loginid = auto.getString("o_id",null);
        loginpassword = auto.getString("o_password",null);

        final StringRequest StringRequest2 = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String name = jsonObject.getString("owner_name");
                    String id = jsonObject.getString("id");
                    String resauth = jsonObject.getString("owner_resauth");

                    if(success==true){
                        Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                        Intent intent_resauth = new Intent(SigninActivity.this,ResAuthActivity.class);
                        intent.putExtra("o_id",id);
                        intent.putExtra("o_name",name);
                        intent.putExtra("o_resauth",resauth);
                        intent_resauth.putExtra("o_id",id);
                        intent_resauth.putExtra("o_name",name);
                        intent_resauth.putExtra("o_resauth",resauth);
                        if(resauth.equals("1")){
                            SharedPreferences.Editor autologin = auto.edit();
                            autologin.putString("o_resauth",resauth);
                            autologin.commit();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            startActivity(intent_resauth);
                            finish();
                        }
                    }else{
                        wronginput.setText("아이디 또는 비밀번호가 일치하지 않습니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("spark123","errorj");
                }
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
                params.put("m_id",loginid);
                params.put("m_password",loginpassword);
                return params;
            }
        };

        if(loginid!=null&&loginpassword!=null){
            //requestQueue.add(StringRequest2);  // TODO AUTO LOGIN
        }

        signupbutton = findViewById(R.id.signin_signupButton);
        signinbutton = findViewById(R.id.signin_loginButton);
        Eidinput = findViewById(R.id.signin_idinput);
        Epasswordinput = findViewById(R.id.signin_passwordinput);
        wronginput = findViewById(R.id.signin_wronginput);
        Epasswordinput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Epasswordinput.setTransformationMethod(PasswordTransformationMethod.getInstance());

        final StringRequest StringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String name = jsonObject.getString("owner_name");
                    String id = jsonObject.getString("id");
                    String resauth = jsonObject.getString("owner_resauth");

                    if(success==true){
                        Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                        Intent intent_resauth = new Intent(SigninActivity.this,ResAuthActivity.class);
                        intent.putExtra("o_id",id);
                        intent.putExtra("o_name",name);
                        intent.putExtra("o_resauth",resauth);
                        intent_resauth.putExtra("o_id",id);
                        intent_resauth.putExtra("o_name",name);
                        intent_resauth.putExtra("o_resauth",resauth);
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.putString("id",id);
                        autologin.putString("o_name",name);
                        autologin.putString("o_resauth", resauth);
                        autologin.putString("o_id",idinput);
                        autologin.putString("o_password",passwordinput);
                        autologin.commit();
                        if(resauth.equals("1")){
                            startActivity(intent);
                        }
                        else{
                            startActivity(intent_resauth);
                        }
                        finish();
                    }else{
                        wronginput.setText("아이디 또는 비밀번호가 일치하지 않습니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("spark123","errorj");
                }
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
                params.put("m_id",idinput);
                params.put("m_password",passwordinput);
                return params;
            }
        };


        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idinput = Eidinput.getText().toString();
                passwordinput = Epasswordinput.getText().toString();
                requestQueue.add(StringRequest);
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
