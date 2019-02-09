package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toolbar;

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

public class FindidpassActivity extends AppCompatActivity {

    private EditText Eownername;
    private EditText Eownerphonenum;
    private EditText Echecknum;
    private Button Bsendchecknum;
    private Button Bfindemail;
    private RequestQueue requestQueue;
    private String Authnumber="";
    private UserAuthClass userAuthClass;
    private UserInfoClass userInfoClass;
    private String uid;

    final String feed_url="http://claor123.cafe24.com/smspush.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findidpass);
        Eownername = findViewById(R.id.ownername);
        Eownerphonenum = findViewById(R.id.ownerphonenum);
        Echecknum = findViewById(R.id.checknum);
        Bsendchecknum = findViewById(R.id.sendchecknumbutton);
        Bfindemail = findViewById(R.id.findemailbutton);

        // 뒤로가기 버튼
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.findemail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("이메일 찾기") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("비밀번호 재설정") ;
        tabHost1.addTab(ts2) ;

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
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
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("m_phone",Eownerphonenum.getText().toString());
                params.put("m_number",Authnumber);
                return params;
            }
        };

        Bsendchecknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Authnumber = "";
                for(int i=0;i<4;i++) {
                    Authnumber += Integer.toString(random.nextInt(10));
                }
                requestQueue.add(stringRequest);
            }
        });

        Bfindemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FindidpassActivity.this, FindemailActivity.class);
                startActivity(intent1);
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