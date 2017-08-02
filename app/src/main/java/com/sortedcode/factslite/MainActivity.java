package com.sortedcode.factslite;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    // Will show the string "data" that holds the results
    TextView facts,textView;
    // URL of object to be parsed
    String url = "https://raw.githubusercontent.com/visnkmr/Factset/master/facts.json";
    String facturl = "https://raw.githubusercontent.com/visnkmr/Factset/master/factcount.json";
    // Defining the Volley request queue that handles the URL request concurrently
    GestureDetector mGestureDetector;
    int factno=0,i,count,fcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facts=findViewById(R.id.facts);
        textView=findViewById(R.id.textView);
        sendRequest();
        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {}

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() < e2.getX()) {
                    if(textView.getVisibility()==View.VISIBLE) {
                        //Left to Right swipe performed
                        i = factno - 1;
                        if (i < 0) i = count - 1;
                        factno=i % count;
                        facts.setText(list.get(factno));

                    }
                }

                if (e1.getX() > e2.getX()) {
                    //Right to Left swipe performed
                    i = factno + 1;
                    if (i >= count) i = 0;
                    factno=i % count;
                    facts.setText(list.get(factno));
                }
                return true;
            }
        });
    }
    List<String> list = new ArrayList<String>();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    private void sendRequest() {
        if (list.isEmpty()) {
            // Formulate the request and handle the response.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String resultval = response;
                            Log.e(TAG, "Response from url: " + resultval);
                            JSONObject jObject;
                            try {
                                jObject = new JSONObject(response);
                                count = Integer.parseInt(jObject.getString("factcount"));
                                Log.i("factno", factno + "");
                                JSONArray factsarr = jObject.getJSONArray("facts");
                                for (int i = 0; i < factsarr.length(); i++) {
                                    list.add(factsarr.getString(i));
                                }
                                if (factno < count && factno >= 0) {
                                    facts.setText(list.get(factno));
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            // Do something with the response

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            facts.setText("No Internet Connection!");
                            textView.setVisibility(View.GONE);// Handle error
                        }
                    });

// Add the request to the RequestQueue.
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);
            mRequestQueue.getCache().clear();
            mRequestQueue.add(stringRequest);
            mRequestQueue.getCache().clear();
        }
        else{
            if (factno < count && factno >= 0) {
                facts.setText(list.get(factno));
            }
        }
    }
    /*void checkcount(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, facturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resultval = response;
                        Log.e(TAG, "Response from url: " + resultval);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(response);
                            fcount = Integer.parseInt(jObject.getString("factcount"));
                            Log.i("factno", factno + "");
                            if (fcount>count) {
                                sendRequest();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // Do something with the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        facts.setText("No Internet Connection!");
                        textView.setVisibility(View.GONE);// Handle error
                    }
                });

// Add the request to the RequestQueue.
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.getCache().clear();
        mRequestQueue.add(stringRequest);
        mRequestQueue.getCache().clear();
    }*/

    @Override
    protected void onStart() {
        super.onStart();
     //   checkcount();
    }
}



