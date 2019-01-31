package com.picke.dishnow_owner;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
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
    public String start;
    boolean Start = false;
    public Intent intent;
    private TextView tv;
    static public Socket mSocket;
    Handler handler = null;
    String res_id;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.main_show);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        intent = getIntent();
        res_id=intent.getStringExtra("o_id");

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
                JSONObject jsonObject_id = null;
                try{
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
            }).on("user_call", (Object... objects) -> {
                vibrator.vibrate(2000);
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                ringtone.play();

                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0].toString());
                runOnUiThread(()->{
                        Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                        intent.putExtra("user_numbers", jsonObject.get("user_numbers").toString());
                        intent.putExtra("user_time", jsonObject.get("user_time").toString());
                        intent.putExtra("user_id", jsonObject.get("user_id").toString());
                        intent.putExtra("res_id", res_id);
                        //startActivity(intent);
                        //finish();
                });
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mSocket.emit("res_id", jsonObject_id);
        mSocket.connect();
    }
}
