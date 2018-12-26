package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String feed_url = "http://claor123.cafe24.com/Callout.php";
    private String mJsonString;
    private Intent intent;
    private String res_id;
    boolean Start = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetData task = new GetData();
        task.execute(feed_url,res_id);
        if(Start==true)startActivity(intent);
    }

    public class GetData extends AsyncTask<String, Void, String>{
        String target;

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
