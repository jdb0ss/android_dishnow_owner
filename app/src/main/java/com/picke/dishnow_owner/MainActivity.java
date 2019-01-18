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
    private String str;
    private TextView tv;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetData task = new GetData();
        //task.execute(feed_url,"1");
        button = findViewById(R.id.main_button);
        editText = findViewById(R.id.main_chat);
        tv = findViewById(R.id.main_tv);

        try{
            mSocket= IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.connect();
           // mSocket.on("chat message",onMessageReceived);
        }catch (URISyntaxException e){
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
                        mSocket.on("chat message",onMessageReceived);
                    }
                });
            }
        });
        //thread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("chat message",editText.getText().toString());
            }
        });
    }

    // 서버로부터 전달받은 'chat-message' Event 처리.
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            String receivedData = (String) args[0];
            tv.setText(receivedData);
            //Log.d("asd",receivedData.getString("chat message"));
            //tv.setText(receivedData.getString("chat message"));

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        GetData task = new GetData();
        //task.execute(feed_url,"1");

    }

    public class GetData extends AsyncTask<String, Void, String> {
        //ProgressDialog progressDialog;
        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(ShowObectActivity.this,
            //      "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    Thread.sleep(1000);
                    URL url = new URL(params[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(10000);

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    //수정
                    String mres = params[1];

                    String data = "res_id=" + mres;

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    //int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d(TAG, data);

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "DOIN", e);
                }
                return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
//            Log.d(TAG, result);
            mJsonString = result;
            if (mJsonString == null) Log.d(TAG, "null!!!");
            showResult();
        }
    }

    public void showResult() {
        String TAG_JSON = "response";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String userid = item.getString("userid");
                String numbers = item.getString("numbers");
                start="start";
                String tmp = start;
                start = userid + numbers;
                if (start != tmp) {
                    intent = new Intent(MainActivity.this, PopupActivity.class);
                    intent.putExtra("user_id", userid);
                    intent.putExtra("numbers", numbers);
                    startActivity(intent);
                    Log.d("claor328",start);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //     Log.d(TAG, "showResult : ", e);
        }
    }
}
