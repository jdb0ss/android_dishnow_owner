package com.picke.dishnow_owner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import com.picke.dishnow_owner.GPS.GpsInfo;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private Button buttonsignup;
    private Button buttonauthphone;
    private EditText Eowneremail;
    private EditText Eownername;
    private EditText Eownerphone;
    private EditText Eownerid;
    private EditText Eownerpassword;
    private EditText Eownerpassword2;
    private TextView errorid;

    //private UserAuthClass userAuthClass;
    private String ownerpassword2;

    private String ownerid;
    private String ownerpassword;
    private String owneremail;
    private String ownername;
    private String ownerphone;
    private static String phone;
    private String uid;
    private Boolean IsPhone = false;
    private Boolean Idcheck = false;

    private RequestQueue requestQueue;

    private final String feed_url = "http://claor123.cafe24.com/Owner_Signup.php";
    private final String id_url = "http://claor123.cafe24.com/Id_overlap.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        buttonauthphone = findViewById(R.id.signup_authphone);
        buttonsignup = findViewById(R.id.signup_signup_button);
        Eownername = findViewById(R.id.signup_ownername);
        Eownerphone = findViewById(R.id.signup_ownerphone);
        Eownerid = findViewById(R.id.signup_ownerid);
        Eowneremail = findViewById(R.id.signup_owneremail);
        Eownerpassword = findViewById(R.id.signup_ownerpassword);
        Eownerpassword2 = findViewById(R.id.signup_ownerpassword_repeat);
        errorid = findViewById(R.id.signup_iderror);

        Eownerpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Eownerpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Eownerpassword2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Eownerpassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());


        Drawable image = getApplicationContext().getResources().getDrawable(R.drawable.ic_iconmonstr_arrow_25);
        image.setBounds(60,0,0,0);


        final StringRequest StringRequest2 = new StringRequest(Request.Method.POST, id_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success==false){
                        errorid.setText("중복");
                        Eownerid.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    }else{
                        Idcheck = true;
                        Eownerid.setCompoundDrawablesWithIntrinsicBounds(null,null,image,null);
                        errorid.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("claor123","errorv");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_id",ownerid);
                return params;
            }
        };
        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        Eownerid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(!isFocused)
                {
                    ownerid = Eownerid.getText().toString();
                   // Eownerid.setCompoundDrawablesWithIntrinsicBounds(null,null,image,null);
                    requestQueue.add(StringRequest2);
                }
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        final String PhoneNum = telephonyManager.getLine1Number();
        phone=PhoneNum;
        if(PhoneNum.startsWith("+82")){
            phone = "0"+phone.substring(3);
        }



        final StringRequest StringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                uid= response.substring(1,response.length()-1);
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
                ownerphone = Eownerphone.getText().toString();
                IsPhone = phone.equals(ownerphone);
                if(phone.equals(ownerphone)){
                    IsPhone=true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("인증에 성공하였습니다.");
                    builder.setNegativeButton("확인", null);
                    builder.create();
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("인증에 실패하였습니다.");
                    builder.setNegativeButton("재시도", null);
                    builder.create();
                    builder.show();
                }
            }
        });

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ownerid = Eownerid.getText().toString();
                ownername = Eownername.getText().toString();
                owneremail = Eowneremail.getText().toString();
                ownerphone = Eownerphone.getText().toString();
                ownerpassword = Eownerpassword.getText().toString();
                ownerpassword2 = Eownerpassword2.getText().toString();

                if(ownerid.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(ownerpassword.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(owneremail.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(ownername.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(ownerphone.length()==0)
                {
                        Toast.makeText(getApplicationContext(),"핸드폰 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else if(ownerid.length()<6)
                    {
                        Toast.makeText(getApplicationContext(),"아이디가 너무 짧습니다",Toast.LENGTH_SHORT).show();
                    }

                    //이메일형식체크
                    else if(!Patterns.EMAIL_ADDRESS.matcher(owneremail).matches())
                    {
                        Toast.makeText(getApplicationContext(),"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
                    }

                    //비밀번호 유효성
                    else if(!Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,20}$", ownerpassword))
                    {
                        Toast.makeText(getApplicationContext(),"비밀번호 형식이 맞지않습니다",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (Idcheck == true) {
                            if (ownerpassword2.equals(ownerpassword)) {
                            if (IsPhone == true) {
                                requestQueue.add(StringRequest);
                                Toast.makeText(getApplicationContext(), "가입 완료!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "전화번호 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디 중복 검사를 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
