package com.picke.dishnow_owner.Utility;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getmInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return this.requestQueue;
    }
}
