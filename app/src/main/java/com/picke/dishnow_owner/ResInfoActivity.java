package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.kakao.usermgmt.response.model.User;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.VolleySingleton;

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
            }
        });
    }
}
