package com.picke.dishnow_owner;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.kakao.usermgmt.response.model.User;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResInfoActivity extends AppCompatActivity {

    private EditText Eresname;
    private EditText Eresphone;
    private EditText Eresaddress;
    private EditText Eresadd_num;
    private EditText Eresadd_detail;
    private Button Btfindaddress;
    private Button Btressignup;
    private UserInfoClass userInfoClass;
    private RequestQueue requestQueue;
    private String lat;
    private String lon;
    final String resinfo_url = "http://claor123.cafe24.com/ResSignup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_info);

        Eresname = findViewById(R.id.res_info_res_name);
        Eresphone = findViewById(R.id.res_info_resphone);
        Eresaddress = findViewById(R.id.res_info_address_edt);
        Eresadd_num = findViewById(R.id.res_info_address_num_edt);
        Eresadd_detail = findViewById(R.id.res_info_detail_address_edt);
        Btfindaddress = findViewById(R.id.res_info_search_address_btn);
        Btressignup = findViewById(R.id.res_info_register);

        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        Eresadd_num.setText(userInfoClass.getResadd_num());
        Eresaddress.setText(userInfoClass.getResaddress());
        Eresadd_detail.setText(userInfoClass.getResadd_detail());

        final Geocoder geocoder = new Geocoder(this);

        Btfindaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResInfoActivity.this,JusoActivity.class));
                finish();
            }
        });

        Btressignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(userInfoClass.getResaddress(),10);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"인터넷 상태를 확인해 주세요.",Toast.LENGTH_LONG).show();
                }
                if(list!=null){
                    if(list.size()==0){
                        Toast.makeText(getApplicationContext(),"정확한 주소를 입력해 주세요",Toast.LENGTH_LONG).show();
                    }else {
                        lat = Double.toString(list.get(0).getLatitude());
                        lon = Double.toString(list.get(0).getLongitude());
                    }
                }
                requestQueue.add(stringRequest2);
            }
        });
    }
    final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, resinfo_url, new Response.Listener<String>() {
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
            params.put("m_id",userInfoClass.getuId());
            params.put("m_ownername",userInfoClass.getOwnername());
            params.put("m_lat",String.valueOf(lat));
            params.put("m_lon",String.valueOf(lon));
            params.put("m_address",userInfoClass.getResaddress()+" "+userInfoClass.getResadd_detail());
            params.put("m_resname",userInfoClass.getResname());
            params.put("m_resnum",userInfoClass.getResid());
            params.put("m_resphone",userInfoClass.getResphone());
            return params;
        }
    };
}
