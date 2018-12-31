package com.picke.dishnow_owner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.picke.dishnow_owner.GPS.GpsInfo;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private GpsInfo gps;
    private Button buttongps;
    private Button buttonsignup;
    private EditText Eresname;
    private EditText Eresaddress;
    private EditText Eresphone;
    private EditText Eownername;
    private EditText Estarttime;
    private EditText Eendtime;
    private EditText Eownerphone;
    private EditText Epassword;

    private String resname;
    private String resaddress;
    private String resphone;
    private String ownername;
    private String starttime;
    private String endtime;
    public String lat;
    public String lon;
    private String ownerphone;
    private String password;

    private RequestQueue requestQueue;
    private final String feed_url = "http://claor123.cafe24.com/Owner_Signup.php";


    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        buttongps = findViewById(R.id.signup_gps_button);
        buttonsignup = findViewById(R.id.signup_signup_button);
        Eresname = findViewById(R.id.signup_resname);
        Eresaddress = findViewById(R.id.signup_resaddress);
        Eresphone = findViewById(R.id.signup_resphone);
        Eownername = findViewById(R.id.signup_ownername);
        Estarttime = findViewById(R.id.signup_resstarttime);
        Eendtime = findViewById(R.id.signup_resendtime);
        Eownerphone = findViewById(R.id.signup_ownerphone);
        Epassword = findViewById(R.id.signup_password);

        requestQueue = Volley.newRequestQueue(this);


        buttongps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPermission) {
                    callPermission();
                    return;
                }
                gps = new GpsInfo(SignupActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    if (latitude != 0 && longitude != 0) {
                        lat = Double.toString(latitude);
                        lon = Double.toString(longitude);
                        Toast.makeText(getApplicationContext(), "GPS 확인 완료!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "GPS 상태를 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        final StringRequest StringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("claor123", "[" + response + "]");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("claor123error", "[" + error.getMessage() + "]");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_name", resname);
                params.put("m_address", resaddress);
                params.put("m_phone", resphone);
                params.put("m_owner", ownername);
                params.put("m_start", starttime);
                params.put("m_end", endtime);
                params.put("m_lat", lat);
                params.put("m_lon", lon);
                params.put("m_owner_phone", ownerphone);
                params.put("m_password", password);
                return params;
            }
        };

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resname = Eresname.getText().toString();
                resaddress = Eresaddress.getText().toString();
                resphone = Eresphone.getText().toString();
                ownername = Eownername.getText().toString();
                starttime = Estarttime.getText().toString();
                endtime = Eendtime.getText().toString();
                ownerphone = Eownerphone.getText().toString();
                password = Epassword.getText().toString();
                requestQueue.add(StringRequest);
            }
        });

        callPermission();  // 권한 요청을 해야 함

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String PhoneNum = telephonyManager.getLine1Number();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
}
