package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class Test_3 extends AppCompatActivity {

    ArrayList<String> MBTI = new ArrayList<>();
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_3);

        ID = getIntent().getStringExtra("userID");
        MBTI = getIntent().getStringArrayListExtra("MBTI");

        System.out.println("test_3");
        System.out.println(MBTI);

//        Button test3_back = findViewById(R.id.test3_back);
//        test3_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_back = new Intent(Test_3.this, Test_2.class);
//                Test_3.this.startActivity(intent_back);
//                finish();
//            }
//        });

        Button test3_next = findViewById(R.id.test3_next);
        test3_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)
//
//                            if(success) {
//
//                                Intent intent = new Intent(Test_3.this, Test_result.class);
//                                Test_3.this.startActivity(intent);
//                                finish();
//
//                            } else {
//                                Toast toast = Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
//                                toast.show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                };
//
//                Test_resultRequest test_resultRequest = new Test_resultRequest(MBTI,responseListener);
//                RequestQueue requestQueue = Volley.newRequestQueue(Test_3.this);
//                requestQueue.add(test_resultRequest);

                Intent intent = new Intent(Test_3.this, Choice_like.class);
                intent.putExtra("userID",ID);
                intent.putExtra("MBTI",MBTI);
                System.out.println("test3_mbti : "+MBTI);
                Test_3.this.startActivity(intent);
                finish();

            }
        });

    }
}
