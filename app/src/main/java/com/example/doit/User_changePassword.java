package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class User_changePassword extends AppCompatActivity {

    private String passwd;
    private String passwdCheck;
    private AlertDialog dialog;

    private String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);


        final EditText passwdText = findViewById(R.id.change_passwordText);
        final EditText passwdCheckText = findViewById(R.id.change_passwordCheckText);

        ID = MainActivity.ID;

        //비밀번호 일치 확인
        passwdCheckText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(passwdText.getText().toString().equals(passwdCheckText.getText().toString()))
                {
                    passwdCheckText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    passwdCheckText.setBackgroundColor(getResources().getColor(R.color.colorWarning));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Button changePassword_ok = findViewById(R.id.changePassword_ok);
        changePassword_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwd = passwdText.getText().toString();
                String passwdCheck = passwdCheckText.getText().toString();

                //빈공간 확인
                if(passwd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_changePassword.this);
                    dialog = builder.setMessage("빈칸 없이 입력 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                //비밀번호 일치
                if(!passwd.equals(passwdCheck)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_changePassword.this);
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                //중복체크 및 모든값이 입력되었다면, 정상적으로 비번변경 진행
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                        try {
                            JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                            boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)
                            if(success) { //비번update성공
                                finish(); //비번변경 완료되었다면 Activity종료
                            } else { //비번update실패
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_changePassword.this);
                                dialog = builder.setMessage("비밀번호 변경에 실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                VolleyR_User_changePasswordRequest volleyRUser_changePasswordRequest = new VolleyR_User_changePasswordRequest(ID, passwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(User_changePassword.this);
                queue.add(volleyRUser_changePasswordRequest);

            }
        });



    }
}
