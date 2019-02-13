package com.picke.dishnow_owner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VerificationActivity extends AppCompatActivity {

    private EditText Eonwername;
    private EditText Eownerphone;
    private EditText Ephoneauth;
    private Button phoneauthbutton;
    private Button signupbutton;
    private TextView tverrorphoneauth;
    private RequestQueue requestQueue;
    private String Authnumber="";
    private UserAuthClass userAuthClass;
    private UserInfoClass userInfoClass;
    private String uid;
    private Boolean flag=true;
    private final Context context =this;
    private TimerTask second;
    private TextView Ttimer;
    private final Handler handler = new Handler();
    private Integer timer_minute=3;
    private Integer timer_second=0;

    final String feed_url_signup = "http://claor123.cafe24.com/Owner_Signup.php";
    final String feed_url_phone = "http://claor123.cafe24.com/smspush.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Eonwername = findViewById(R.id.signup2_ownername);
        Eownerphone = findViewById(R.id.signup2_ownerphone);
        Ephoneauth = findViewById(R.id.signup2_phoneauth);
        phoneauthbutton = findViewById(R.id.signup2_phoneauthbutton);
        tverrorphoneauth =findViewById(R.id.signup2_phoneautherror);
        signupbutton = findViewById(R.id.signup2_signup_button);
        Ttimer = findViewById(R.id.signup2_phonetimer);

        Eonwername.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Eownerphone.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);
        Ephoneauth.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);

        userAuthClass = UserAuthClass.getInstance(getApplicationContext());
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        Drawable image = getApplicationContext().getResources().getDrawable(R.drawable.ic_iconmonstr_check_mark_15);
        image.setBounds(60,0,0,0);

        Toolbar toolbar = findViewById(R.id.signup2_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        final StringRequest stringRequest_phone = new StringRequest(Request.Method.POST, feed_url_phone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setMessage("인증번호가 전송되었습니다.")
                        .setPositiveButton("확인",null)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                alert.show();

                Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout parent = (LinearLayout) positiveButton.getParent();
                parent.setGravity(Gravity.CENTER_HORIZONTAL);
                View leftSpacer = parent.getChildAt(1);
                leftSpacer.setVisibility(View.GONE);

                TextView message = (TextView)alert.findViewById(android.R.id.message);
                message.setGravity(Gravity.CENTER);

                testStart();
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

        final StringRequest StringRequest_signup = new StringRequest(Request.Method.POST, feed_url_signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                uid = response.substring(1,response.length()-1);
                userAuthClass.setUid(uid);
                userInfoClass.setuId(uid);
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
                params.put("m_email",userAuthClass.getOwnerid());
                params.put("m_sex",userAuthClass.getOwnersex());
                params.put("m_birth",userAuthClass.getOwnerbirth());
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
                if(flag) {
                    userAuthClass.setOwnername(Eonwername.getText().toString());
                    userAuthClass.setOwnerphone(Eownerphone.getText().toString());
                    requestQueue.add(StringRequest_signup);

                    Intent intent = new Intent(VerificationActivity.this, JoinedActivity.class);
                    startActivity(intent);
                    JoinActivity joinActivity = (JoinActivity)JoinActivity._Signup_Activity;
                    joinActivity.finish();
                    finish();
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

    public void testStart(){
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
                timer_second--;
                if(timer_second<0){
                    timer_second=59;
                    timer_minute--;
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                String second_show=Integer.toString(timer_second);
                if(second_show.length()==1){
                    second_show="0"+second_show;
                }
                Ttimer.setText(Integer.toString(timer_minute)+":"+second_show);
            }
        };
        handler.post(updater);
    }
}
