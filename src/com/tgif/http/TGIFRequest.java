package com.tgif.http;

import android.app.Application;

public class TGIFRequest extends Application {
	/*public static final String TAG = TGIFRequest.class.getName();
	private RequestQueue mRequestQueue;
    private static TGIFRequest mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue.start();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //mRequestQueue = Volley.newRequestQueue(TGIFRequest.this);
    }
 
    public static synchronized TGIFRequest getInstance() {
        return mInstance;
    }
    
    public RequestQueue getRequestQueue() {
    	if (mRequestQueue == null) {
    		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
    	return mRequestQueue;
    }
    
    public <T> void add(Request<T> req) {
    	req.setTag(TAG);
    	getRequestQueue().add(req);
    }
    public void cancel() {
    	mRequestQueue.cancelAll(TAG);
    }*/
}
