package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.UserAuthClass;

public class JoinedActivity extends AppCompatActivity {

        private TextView ownername;
        private Button ressignup;
        private UserAuthClass userAuthClass;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_joined);

            ownername = findViewById(R.id.res_auth_name);
            ressignup = findViewById(R.id.res_auth_signup);
            userAuthClass = UserAuthClass.getInstance(getApplicationContext());

            ownername.setText(userAuthClass.getOwnername());

            ressignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JoinedActivity.this,SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
}
