package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

    public class ResAuthActivity extends AppCompatActivity {

        private TextView ownername;
        private TextView resauthplz;
        private Button ressignup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_res_auth);

            ownername = findViewById(R.id.res_auth_name);
            resauthplz = findViewById(R.id.res_auth_plz);
            ressignup = findViewById(R.id.res_auth_signup);

            Intent intent = getIntent();
            final String name = intent.getStringExtra("o_name");
            final String id = intent.getStringExtra("o_id");

            ownername.setText("환영합니다 " + name + " 님!");

            Intent intent1 = new Intent(ResAuthActivity.this,ResSignupActivity.class);
            intent1.putExtra("o_id",id);
            startActivity(intent1);
        }
}
