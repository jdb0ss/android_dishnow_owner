package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import java.util.ArrayList;

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
    private EditText epeople;
    private EditText ephone;
    Button callbutton;
    Handler handler = null;
    String res_id = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.main_button);
        editText = findViewById(R.id.main_chat);
        tv = findViewById(R.id.main_tv);
        ephone = findViewById(R.id.main_phone);
        epeople = findViewById(R.id.main_people);
        callbutton = findViewById(R.id.main_call);
        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                runOnUiThread(()->{
                    JsonObject prejsonobject = new JsonObject();
                    prejsonobject.addProperty("res_id",res_id);
                    JSONObject jsonObject_id = null;
                    try{
                        jsonObject_id = new JSONObject(prejsonobject.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    JSONObject finalJsonObject_id = jsonObject_id;
                    mSocket.emit("res_id", finalJsonObject_id);
                });
            }).on("user_call", (Object... objects) -> {
                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0].toString());
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this,PopupActivity.class);
                    intent.putExtra("user_people", jsonObject.get("user_people").toString());
                    intent.putExtra("user_phone", jsonObject.get("user_phone").toString());
                    startActivity(intent);
                });
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

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


        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id","1");
                    jsonObject.put("user_people","23");
                    jsonObject.put("user_phone","123455167");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
               try {
                   jsonArray = new JSONArray();
                   jsonArray.put("1");
                   jsonArray.put("2");
                   jsonArray.put("3");
               }catch (Exception e){
                   e.printStackTrace();
               }
                try {
                    jsonObject.put("res_list",jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("user_call",jsonObject);
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
        prejsonobject.addProperty("res_id",res_id);
        JSONObject jsonObject_id = null;
        try{
            jsonObject_id = new JSONObject(prejsonobject.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        JSONObject finalJsonObject_id = jsonObject_id;
        mSocket.emit("res_id", finalJsonObject_id);
        mSocket.connect();

    }

}


