package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String feed_url = "http://claor123.cafe24.com/Callout.php";
    private TextView hello;
    boolean Start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("o_id");
        final String name = intent.getStringExtra("o_name");
        int resauth = intent.getIntExtra("o_resauth",0);

        hello=findViewById(R.id.main_hello);
        hello.setText("환영합니다, "+ name +"님!");





    }
}
