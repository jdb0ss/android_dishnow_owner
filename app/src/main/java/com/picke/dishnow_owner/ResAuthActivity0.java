package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResAuthActivity0 extends AppCompatActivity {

    Button Bstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_auth0);

        Bstart = findViewById(R.id.res_auth_0_startbutton);
        Bstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResAuthActivity0.this,ResSignupActivity.class));
                finish();
            }
        });
    }

}
