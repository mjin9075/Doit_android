package com.example.doit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;

    private SessionCallback callback; //카카오로그인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //카카오로그인버튼
        //KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());

        //Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);


        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        //회원가입
        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });


        //로그인
        final EditText idText = findViewById(R.id.idText); //ID를 입력받는 부분
        final EditText passwdText = findViewById(R.id.passwordText); //PW를 입력받는 부분
        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String ID = idText.getText().toString();
                String passwd = passwdText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)
                            String Mtype = jsonObject.getString("Mtype");

                            if(success) { //로그인에 성공했다면
                                if(Mtype.equals("GM")){
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                                                boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                                                if(success) { //ID가 없다면(선택데이터가 없다->test 진행)
                                                    Toast.makeText(getApplicationContext(),"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, User_preference_survey.class);
                                                    intent.putExtra("userID",ID); //로그인 할때 사용자의 ID정보 같이 넘김, final로 선언
                                                    LoginActivity.this.startActivity(intent);
                                                    finish();
                                                } else { //ID가 있다(선택데이터가 있다->추천진행)

                                                    Toast.makeText(getApplicationContext(),"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                    Intent intent = new Intent(LoginActivity.this, Hm_MainActivity.class);
                                                    intent.putExtra("userID",ID); //로그인 할때 사용자의 ID정보 같이 넘김, final로 선언
                                                    LoginActivity.this.startActivity(intent);
                                                    finish();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    VolleyR_CheckDBRequest volleyRCheckDBRequest = new VolleyR_CheckDBRequest(ID, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                    queue.add(volleyRCheckDBRequest);


                                } else if(Mtype.equals("HM")){
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                                                boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                                                if(success) { //ID가 없다면(업체등록 안했다)
                                                    Toast.makeText(getApplicationContext(),"사업주님안녕하세요.",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, Hm_facility_register.class);
                                                    intent.putExtra("userID",ID); //로그인 할때 사용자의 ID정보 같이 넘김, final로 선언
                                                    LoginActivity.this.startActivity(intent);
                                                    finish();
                                                } else { //ID가 있다(업체등록을 했다)
                                                    String myfacility_name = jsonResponse.getString("facility_name");
                                                    String myfacility_image = jsonResponse.getString("facility_image");

                                                    Toast.makeText(getApplicationContext(),"사업주님안녕하세요.",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, Hm_MainActivity.class);
                                                    intent.putExtra("userID",ID); //로그인 할때 사용자의 ID정보 같이 넘김, final로 선언

                                                    intent.putExtra("facility_name",myfacility_name);
                                                    intent.putExtra("facility_image",myfacility_image);

                                                    LoginActivity.this.startActivity(intent);
                                                    finish();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    VolleyR_CheckDB_HMRequest volleyRCheckDB_HMRequest = new VolleyR_CheckDB_HMRequest(ID, responseListener);
                                    RequestQueue queueHM = Volley.newRequestQueue(LoginActivity.this);
                                    queueHM.add(volleyRCheckDB_HMRequest);


                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호를 확인해 주세요.",Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();

                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };

                VolleyR_LoginRequest volleyRLoginRequest = new VolleyR_LoginRequest(ID,passwd,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(volleyRLoginRequest); //결과로 나온 response가 위의 JSONresponse를 통해서 다뤄짐
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() { //로그인성공
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { //로그인실패
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) { //현재 다이얼로그가 켜져있을때는 함부로종료되지 않도록
            dialog.dismiss();
            dialog = null;
        }
    }
}
