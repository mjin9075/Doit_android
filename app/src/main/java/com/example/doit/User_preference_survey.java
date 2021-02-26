package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User_preference_survey extends AppCompatActivity {

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference_survey);

        ID = getIntent().getStringExtra("userID");

        Button survey_NO = findViewById(R.id.survey_NO);
        survey_NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(User_preference_survey.this, MainActivity.class);
                intent.putExtra("userID",ID);
                User_preference_survey.this.startActivity(intent);
                finish();
            }
        });

        Button survey_YES = findViewById(R.id.survey_YES);
        survey_YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(User_preference_survey.this, Test_1.class);
                intent.putExtra("userID",ID);
                User_preference_survey.this.startActivity(intent);
                //finish();
            }
        });
    }
}
