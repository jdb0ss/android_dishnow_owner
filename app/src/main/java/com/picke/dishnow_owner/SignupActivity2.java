package com.picke.dishnow_owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SignupActivity2 extends AppCompatActivity {

    private EditText Eonwername;
    private EditText Eownerphone;
    private EditText Ephoneauth;
    private Button phoneauthbutton;
    private Button signupbutton;
    private RequestQueue requestQueue;
    private String Authnumber="";

    final String feed_url="http://claor123.cafe24.com/smspush.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        Eonwername = findViewById(R.id.signup2_ownername);
        Eownerphone = findViewById(R.id.signup2_ownerphone);
        Ephoneauth = findViewById(R.id.signup2_phoneauth);
        phoneauthbutton = findViewById(R.id.signup2_phoneauthbutton);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("m_phone",Eownerphone.getText().toString());
                params.put("m_number",Authnumber);
                return params;
            }
        };

        phoneauthbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Authnumber = "";
                for(int i=0;i<4;i++) {
                    Authnumber += Integer.toString(random.nextInt(10));
                }
                requestQueue.add(stringRequest);
            }
        });
    }
}
