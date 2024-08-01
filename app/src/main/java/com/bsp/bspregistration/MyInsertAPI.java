package com.bsp.bspregistration;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyInsertAPI {

    private static MyInsertAPI mInstance;
    private static RequestQueue requestQueue;
    private static Context mctx;

    private MyInsertAPI(Context context){
        mctx = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized MyInsertAPI getInstance(Context context){
        if(mInstance==null){
            mInstance=new MyInsertAPI(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T>void addTorequest(Request<T> request){
        requestQueue.add(request);
    }
}
