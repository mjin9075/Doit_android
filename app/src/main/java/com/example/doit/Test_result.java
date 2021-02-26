package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Test_result extends AppCompatActivity {

    public static String MBTI;

     private TextView test_result;
     private RequestQueue queue;
     private Map<String, String> parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        MBTI = getIntent().getStringExtra("MBTI");
        System.out.println("test_result");
        System.out.println(MBTI);
        test_result = findViewById(R.id.test_result);

//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JsonParser jsonParser = new JsonParser();
//                    JsonElement jsonElement = jsonParser.parse(response);
//                    test_result.setText(jsonElement+"\n");
//                    test_result.setText(test_result.getText()+jsonElement.getClass().getName() + "\n");
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)
//
//                    if(success) {
//
//                        test_result.setText(response);
//
//                    } else {
//                        Toast toast = Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
//                        toast.show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//        Test_resultRequest test_resultRequest = new Test_resultRequest(MBTI,responseListener);
//        RequestQueue requestQueue = Volley.newRequestQueue(Test_result.this);
//        requestQueue.add(test_resultRequest);
        queue = Volley.newRequestQueue(this);
        String url = "서버URL";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(response);

                System.out.println("jsonElementttttttttttttttttt :" +jsonElement);
                test_result.setText(jsonElement+"\n");
                test_result.setText(test_result.getText()+jsonElement.getClass().getName() + "\n");
                    //result_contents = response.getString("array");
                    //test_result.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {@Override
        public Map<String, String> getParams() throws AuthFailureError {
            parameters = new HashMap<>();
            parameters.put("MBTI", MBTI);
            Log.d("MBTI","");
            return parameters;
        }}
        ;

        queue.add(stringRequest);
    }
}
