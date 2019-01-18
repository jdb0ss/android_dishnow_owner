package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    String feed_url = "http://claor123.cafe24.com/Callout.php";
    private static final String TAG = "claor123";
    private String mJsonString;
    public String start;
    boolean Start = false;
    public Intent intent;
    private Socket mSocket;
    private Button button;
    private EditText editText;
    private TextView tv;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GetData task = new GetData();
        //task.execute(feed_url,"1");
        button = findViewById(R.id.main_button);
        editText = findViewById(R.id.main_chat);
        tv = findViewById(R.id.main_tv);

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {

            }).on("recMsg", (Object... objects) -> {
                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0] + "");
                runOnUiThread(() -> {
                    tv.setText(tv.getText().toString() + jsonObject.get("message").getAsString());
                });
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
        //thread.start();

        button.setOnClickListener((view)->{
            JsonObject preJsonobject = new JsonObject();
            preJsonobject.addProperty("message",editText.getText().toString());
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(preJsonobject.toString());
            }catch(JSONException e){
                e.printStackTrace();
            }
            mSocket.emit("msg",jsonObject);
            editText.setText("");
        });
        }
    }


