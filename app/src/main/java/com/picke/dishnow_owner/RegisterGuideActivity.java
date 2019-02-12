package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterGuideActivity extends AppCompatActivity {

    Button Bstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerguide);

        Bstart = findViewById(R.id.res_auth_0_startbutton);
        Bstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterGuideActivity.this,Registration_BusinessActivity.class));
                finish();
            }
        });
    }

}
