package com.picke.dishnow_owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PopupActivity extends AppCompatActivity {
    TextView res_id;
    TextView numbers;
    Button yes;
    Button nop;
   //String feed_url = "http://claor123.cafe24.com/Respermit.php";
    private static final String TAG = "claor123";
    private String mJsonString;
    public String start = "fffffffff";
    boolean Start = false;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        res_id = findViewById(R.id.resid);
        numbers = findViewById(R.id.numbers);
        final Bundle extras = getIntent().getExtras();
        res_id.setText(extras.getString("user_people"));
        numbers.setText(extras.getString("user_phone"));
        final String A = extras.getString("user_people");
        final String B = extras.getString("user_phone");
        yes = (Button) findViewById(R.id.yes_button);
        nop = (Button) findViewById(R.id.no_button);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData task = new GetData();
                //task.execute(feed_url, "1", A, B);
                intent = new Intent(PopupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        nop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PopupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                //수정
                String mres = params[1];
                String muser = params[2];
                String number = params[3];

                String data = "res_id=" + mres + "&" + "userid=" + muser + "&" + "numbers=" + number;
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
            Log.d(TAG, result);
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
                String tmp = start;
                start = userid + numbers;
                Log.d("claor1234", start);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //     Log.d(TAG, "showResult : ", e);
        }
    }
    //내가작성*******************************************************************//
}
