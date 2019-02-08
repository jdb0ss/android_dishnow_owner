package com.picke.dishnow_owner;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignupActivity2 extends AppCompatActivity {

    private EditText Eonwername;
    private EditText Eownerphone;
    private EditText Ephoneauth;
    private Button phoneauthbutton;
    private Button signupbutton;
    private TextView tverrorphoneauth;
    private RequestQueue requestQueue;
    private String Authnumber="";
    private UserAuthClass userAuthClass;
    private String uid;
    private Boolean flag=false;

    final String feed_url="http://claor123.cafe24.com/smspush.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        Eonwername = findViewById(R.id.signup2_ownername);
        Eownerphone = findViewById(R.id.signup2_ownerphone);
        Ephoneauth = findViewById(R.id.signup2_phoneauth);
        phoneauthbutton = findViewById(R.id.signup2_phoneauthbutton);

        userAuthClass = UserAuthClass.getInstance(getApplicationContext());
        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        Drawable image = getApplicationContext().getResources().getDrawable(R.drawable.ic_iconmonstr_check_mark_15);
        image.setBounds(60,0,0,0);



        Toolbar toolbar = findViewById(R.id.signup2_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final StringRequest StringRequest_signup = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
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
                params.put("m_id",userAuthClass.getOwnerid());
                params.put("m_password",userAuthClass.getOwnerpassword());
                params.put("m_name",userAuthClass.getOwnername());
                params.put("m_phone",userAuthClass.getOwnerphone());
                params.put("m_email","123");
                return params;
            }
        };

        Ephoneauth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Authnumber.equals(Ephoneauth.getText().toString())){
                    flag=true;
                    Ephoneauth.setCompoundDrawablesWithIntrinsicBounds(null,null,image,null);
                    tverrorphoneauth.setText("");
                    Ephoneauth.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
                }else{
                    Ephoneauth.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
                    Ephoneauth.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    tverrorphoneauth.setText("인증번호를 확인해주세요.");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final StringRequest stringRequest_phone = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("m_phone",Eownerphone.getText().toString());
                params.put("m_number",Authnumber);
                return params;
            }
        };

        phoneauthbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Authnumber = "";
                for(int i=0;i<4;i++) {
                    Authnumber += Integer.toString(random.nextInt(10));
                }
                requestQueue.add(stringRequest_phone);
            }
        });


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuthClass.setOwnername(Eonwername.getText().toString());
                userAuthClass.setOwnerphone(Eownerphone.getText().toString());
                requestQueue.add(StringRequest_signup);
                userAuthClass.setUid(uid);
                Intent intent = new Intent(SignupActivity2.this,ResAuthActivity.class);
                startActivity(intent);
                finish();
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
