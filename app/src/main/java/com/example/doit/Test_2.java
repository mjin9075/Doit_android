package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Test_2 extends AppCompatActivity {


    ArrayList<String> MBTI = new ArrayList<>();
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);

        ID = getIntent().getStringExtra("userID");
        MBTI = getIntent().getStringArrayListExtra("MBTI");


//        Button test2_back = findViewById(R.id.test2_back);
//        test2_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_back = new Intent(Test_2.this, Test_1.class);
//                Test_2.this.startActivity(intent_back);
//                finish();
//            }
//        });

        Button test2_next = findViewById(R.id.test2_next);
        test2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Test_2.this, Test_3.class);
                Intent intent = new Intent(Test_2.this, Choice_like.class);
                intent.putExtra("userID",ID);
                intent.putExtra("MBTI",MBTI);
                System.out.println("test2_mbti : "+MBTI);
                Test_2.this.startActivity(intent);
                finish();
            }
        });


    }
}
