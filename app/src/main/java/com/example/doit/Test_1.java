package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class Test_1 extends AppCompatActivity {

    private String EorI,SorN,TorF,JorP;

    ArrayList<String> MBTI = new ArrayList<>(); //사용자가 좋아한 항목의 feature 값만 저장

//    private String MBTI;
    private AlertDialog dialog;
    private String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_1);

        ID = getIntent().getStringExtra("userID");


        RadioGroup testGroup_1_1 = findViewById(R.id.testGroup_1_1);
        int testGroup_1_1ID = testGroup_1_1.getCheckedRadioButtonId(); //라디오그룹에서 어떤게 선택되어있는지 확인할 수 있게 ID값
        //EorI = ((RadioButton)findViewById(testGroup_1_1ID)).getText().toString();

        testGroup_1_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton EorIButton = findViewById(i); //현재 선택되어있는버튼을 찾음
                //EorI = EorIButton.getText().toString();
                    if(EorIButton.getText().toString().equals("외부로,활동적")) {
                        EorI="E";
                    } else if(EorIButton.getText().toString().equals("내부로,반추적")) {
                        EorI="I";
                    }
            }
        });



        RadioGroup testGroup_1_2 = findViewById(R.id.testGroup_1_2);
        int testGroup_1_2ID = testGroup_1_2.getCheckedRadioButtonId(); //라디오그룹에서 어떤게 선택되어있는지 확인할 수 있게 ID값
//        SorN = ((RadioButton)findViewById(testGroup_1_2ID)).getText().toString();

        testGroup_1_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton SorNButton = findViewById(i); //현재 선택되어있는버튼을 찾음
                //SorN = SorNButton.getText().toString();
                if(SorNButton.getText().toString().equals("현재,노력")) {
                    SorN = "S";
                } else if(SorNButton.getText().toString().equals("미래,상상")) {
                    SorN = "N";
                }
            }
        });


        RadioGroup testGroup_1_3 = findViewById(R.id.testGroup_1_3);
        int testGroup_1_3ID = testGroup_1_3.getCheckedRadioButtonId(); //라디오그룹에서 어떤게 선택되어있는지 확인할 수 있게 ID값
 //       TorF = ((RadioButton)findViewById(testGroup_1_3ID)).getText().toString();

        testGroup_1_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton TorFButton = findViewById(i); //현재 선택되어있는버튼을 찾음
                //TorF = TorFButton.getText().toString();
                if(TorFButton.getText().toString().equals("분석,공평,정확")) {
                    TorF = "T";
                } else if(TorFButton.getText().toString().equals("조화,감사,설득")) {
                    TorF = "F";
                }
            }
        });


        RadioGroup testGroup_1_4 = findViewById(R.id.testGroup_1_4);
        int testGroup_1_4ID = testGroup_1_4.getCheckedRadioButtonId(); //라디오그룹에서 어떤게 선택되어있는지 확인할 수 있게 ID값
  //      JorP = ((RadioButton)findViewById(testGroup_1_4ID)).getText().toString();

        testGroup_1_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton JorPButton = findViewById(i); //현재 선택되어있는버튼을 찾음
                //JorP = JorPButton.getText().toString();

                if(JorPButton.getText().toString().equals("마무리,계획성")) {
                    JorP = "J";
                } else if(JorPButton.getText().toString().equals("기다림,융통성")) {
                    JorP = "P";
                }
            }
        });



       Button test1_next = findViewById(R.id.test1_next);
        test1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                System.out.println("EorI :"+EorI);
                System.out.println("SorN :"+SorN);
                System.out.println("TorF :"+TorF);
                System.out.println("JorP :"+JorP);

                MBTI.add(0,EorI);
                MBTI.add(1,SorN);
                MBTI.add(2,TorF);
                MBTI.add(3,JorP);

                System.out.println("MBTI : "+MBTI);
                //빈공간 확인
//                if(EorI.equals("") || SorN.equals("") || TorF.equals("") || JorP.equals("") ) {
//                if(EorI == null || EorI.equals("") ) {
                if(EorI == null || SorN == null ||TorF == null || JorP == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Test_1.this);
                    dialog = builder.setMessage("빈칸 없이 선택해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }


//                Response.Listener<String> responseListener = new Response.Listener<String>(){
//
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//                            if(success){//DB에 저장됐다면
//
//                                finish();//액티비티를 종료시킴
//                            }else{
//                                AlertDialog.Builder builder = new AlertDialog.Builder(Test_1.this);
//                                dialog = builder.setMessage("test_1 fail")
//                                        .setNegativeButton("OK", null)
//                                        .create();
//                                dialog.show();
//                            }
//
//                        }
//                        catch(Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                };//Response.Listener 완료
//
//                MBTI = EorI + SorN + TorF + JorP;
//                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
//                //실제로 접속 할 수 있도록 RegisterRequest 생성자를 통해 객체를 만들어 준다.
//                Test_1Request test_1Request = new Test_1Request(ID, EorI, SorN, TorF, JorP, MBTI, responseListener);
//                //Test_1Request test_1Request = new Test_1Request(ID, MBTI, responseListener);
//
//                //요청을 보낼 수 있도록 큐를 만든다
//                RequestQueue queue = Volley.newRequestQueue(Test_1.this);
//
//                //만들어진 큐에, RegisterRequest 객체를 넣어준다.
//                queue.add(test_1Request);

                Intent intent = new Intent(Test_1.this, Test_2.class);
                intent.putExtra("userID",ID);
                intent.putExtra("MBTI",MBTI);
                System.out.println("test1_mbti : "+MBTI);
                Test_1.this.startActivity(intent);
                finish();
            }
        });

    }
}
