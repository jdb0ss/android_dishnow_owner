package com.picke.dishnow_owner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
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
import com.picke.dishnow_owner.GPS.GpsInfo;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Button buttonsignup;
    private Button buttonauthphone;
    private EditText Eowneremail;
    private EditText Eownername;
    private EditText Eownerphone;
    private EditText Eownerid;
    private EditText Eownerpassword;
    private EditText Eownerpassword2;
    private Boolean IsPhone=false;

    private UserAuthClass userAuthClass;
    private String ownerpassword2;

    private String ownerid;
    private String ownerpassword;
    private String owneremail;
    private String ownername;
    private String ownerphone;

    private RequestQueue requestQueue;
    private final String feed_url = "http://claor123.cafe24.com/Owner_Signup.php";

    public Boolean passwordcheck(){
        ownerpassword = Eownerpassword.getText().toString();
        ownerpassword2 = Eownerpassword2.getText().toString();
        if(ownerpassword.equals(ownerpassword2)){
            userAuthClass.setOwnerpassword(ownerpassword);
            return true;
        }else {
            Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void phonecheck() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                        != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String PhoneNum = telephonyManager.getLine1Number();
        if (PhoneNum.startsWith("+82")) {
            PhoneNum = PhoneNum.replace("+82", "0");
        }
        if(ownerphone.equals(PhoneNum)){
            IsPhone = true;
            Toast.makeText(getApplicationContext(),"인증 성공!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"인증 실패.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        buttonauthphone = findViewById(R.id.signup_authphone);
        buttonsignup = findViewById(R.id.signup_signup_button);
        Eownername = findViewById(R.id.signup_ownername);
        Eownerphone = findViewById(R.id.signup_ownerphone);
        Eownerid = findViewById(R.id.signup_resid);
        Eownerpassword = findViewById(R.id.signup_respassword);
        Eownerpassword2 = findViewById(R.id.signup_respassword_repeat);

        requestQueue = Volley.newRequestQueue(this);

        final StringRequest StringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userAuthClass.setUid(response);
                Log.d("spark123", "[" + response + "]");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("spark123error", "[" + error.getMessage() + "]");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_id",ownerid);
                params.put("m_password",ownerpassword);
                params.put("m_name",ownername);
                params.put("m_phone",ownerphone);
                params.put("m_email",owneremail);
                return params;
            }
        };

        buttonauthphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonecheck();
            }
        });

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ownerid = Eownerid.getText().toString();
                ownername = Eownername.getText().toString();
                owneremail = Eowneremail.getText().toString();
                ownerphone = Eownerphone.getText().toString();
                userAuthClass.setOwnerid(ownerid);
                userAuthClass.setOwnername(ownername);
                userAuthClass.setOwnerphone(ownerphone);
                userAuthClass.setOwneremail(owneremail);

                if(passwordcheck()) {
                    if(IsPhone){
                        requestQueue.add(StringRequest);
                        Toast.makeText(getApplicationContext(),"가입 완료!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"전화번호 인증을 해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


}
