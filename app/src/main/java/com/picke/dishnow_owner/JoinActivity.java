package com.picke.dishnow_owner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class JoinActivity extends AppCompatActivity {
    private Button buttonsignup;
    private AppCompatEditText Eownerid;
    private EditText Eownerpassword;
    private EditText Eownerpassword2;
    private TextView errorid;
    private TextView errorpassword;
    private TextView errorpassword2;

    private Boolean flag1=false,flag2=false,flag3=false;
    private String ownerid;
    private String uid;

    private UserAuthClass userAuthClass;
    private RequestQueue requestQueue;

    private final String feed_url = "http://claor123.cafe24.com/Owner_Signup.php";
    private final String id_url = "http://claor123.cafe24.com/Id_overlap.php";
    public static Activity _Signup_Activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        buttonsignup = findViewById(R.id.signup_signup_button);
        Eownerid = findViewById(R.id.signup_ownerid);
        Eownerpassword = findViewById(R.id.signup_ownerpassword);
        Eownerpassword2 = findViewById(R.id.signup_ownerpassword_repeat);
        errorid = findViewById(R.id.signup_iderror);
        errorpassword = findViewById(R.id.signup_passworderror);
        errorpassword2 = findViewById(R.id.signup_passwordoverlaperror);
        _Signup_Activity = JoinActivity.this;

        Eownerpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Eownerpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Eownerpassword2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Eownerpassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Eownerid.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eownerpassword.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eownerpassword2.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userAuthClass = UserAuthClass.getInstance(getApplicationContext());

        Drawable image_ok = getApplicationContext().getResources().getDrawable(R.drawable.ic_iconmonstr_check_mark_15);
        image_ok.setBounds(60,0,0,0);
        Drawable image_no = getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_x);
        image_no.setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
        image_no.setBounds(60,0,0,0);

        final StringRequest StringRequest2 = new StringRequest(Request.Method.POST, id_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success==false) {
                        errorid.setText("이미 사용중인 아이디입니다.");
                        Eownerid.setCompoundDrawablesWithIntrinsicBounds(null, null, image_no, null);
                        Eownerid.getBackground().setColorFilter(getResources().getColor(R.color.color_red), PorterDuff.Mode.SRC_ATOP);
                        flag1=false;
                    }else if(Eownerid.getText().toString().length()!=0){
                        flag1=true;
                        Eownerid.setCompoundDrawablesWithIntrinsicBounds(null,null,image_ok,null);
                        errorid.setText("");
                        Eownerid.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
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



        Eownerpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,20}$", Eownerpassword.getText().toString())){
                    errorpassword.setText("영문, 숫자 8~20자의 비밀번호를 설정하세요.");
                    Eownerpassword.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
                    Eownerpassword.setCompoundDrawablesWithIntrinsicBounds(null,null,image_no,null);
                    flag2=false;
                }else if(Eownerpassword.getText().toString().length()!=0){
                    errorpassword.setText("");
                    Eownerpassword.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
                    Eownerpassword.setCompoundDrawablesWithIntrinsicBounds(null,null,image_ok,null);
                    flag2=true;
                }
            }
        });

        Eownerpassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Eownerpassword.getText().toString().equals(Eownerpassword2.getText().toString())){
                    errorpassword2.setText("");
                    Eownerpassword2.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
                    flag3=true;
                    Eownerpassword2.setCompoundDrawablesWithIntrinsicBounds(null,null,image_ok,null);
                }else if(Eownerpassword2.getText().toString().length()!=0){
                    errorpassword2.setText("비밀번호가 일치하지 않습니다.");
                    Eownerpassword2.setCompoundDrawablesWithIntrinsicBounds(null,null,image_no,null);
                    Eownerpassword2.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
                    flag3=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==true&&flag2==true&&flag3==true) {
                    userAuthClass.setOwnerid(Eownerid.getText().toString());
                    userAuthClass.setOwnerpassword(Eownerpassword.getText().toString());
                    Intent intent = new Intent(JoinActivity.this, Additional_InfoActivity.class);
                    startActivity(intent);
                }
            }
        });
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
