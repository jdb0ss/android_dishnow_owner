package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;

public class PopupActivity extends AppCompatActivity {
    TextView tuser_numbers;
    TextView tuser_time;
    Button byes;
    Button bnop;
    public Intent intent;
    static public Socket mSocket;
    private String res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        tuser_numbers = findViewById(R.id.popup_user_numbers);
        tuser_time = findViewById(R.id.popup_user_time);
        final Bundle extras = getIntent().getExtras();
        tuser_numbers.setText(extras.getString("user_numbers"));
        tuser_time.setText(extras.getString("user_time"));
        final String user_people = extras.getString("user_numbers");
        final String user_time = extras.getString("user_time");
        final String user_id = extras.getString("user_id");
        res_id = extras.getString("res_id");
        byes = findViewById(R.id.popup_yes_button);
        bnop = findViewById(R.id.popup_no_button);

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            JsonObject prejsonobject = new JsonObject();
            prejsonobject.addProperty("res_id", res_id);
            JSONObject jsonObject_id = null;
            try {
                jsonObject_id = new JSONObject(prejsonobject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject finalJsonObject_id = jsonObject_id;
            mSocket.emit("res_id", finalJsonObject_id);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        byes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
                prejsonobject.addProperty("user_id",user_id);
                try{
                    jsonObject = new JSONObject(prejsonobject.toString());
                    mSocket.emit("res_yes",jsonObject);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //task.execute(feed_url, "1", A, B);

                intent = new Intent(PopupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bnop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PopupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        mSocket.disconnect();
    }
    @Override
    public void onResume(){
        super.onResume();
        JsonObject prejsonobject = new JsonObject();
        prejsonobject.addProperty("res_id", res_id);
        JSONObject jsonObject_id = null;
        try {
            jsonObject_id = new JSONObject(prejsonobject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject finalJsonObject_id = jsonObject_id;
        mSocket.emit("res_id", finalJsonObject_id);
        mSocket.connect();
    }
}
