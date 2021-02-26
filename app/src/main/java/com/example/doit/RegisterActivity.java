package com.example.doit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    private String ID;
    private String passwd;
    private String passwdCheck;
    private String Email;
    private String Mtype;


    private AlertDialog dialog;
    private boolean validate = false; //ID 사용 가능 여부확인

    RestAPI restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        //이용약관
        TextView accessTermsText = findViewById(R.id.accessTermsTextView);

        String accessText = "회원가입시 개인정보처리방침, 위치기반서비스 이용약관에 동의합니다.";
        accessTermsText.setText(accessText);

        Linkify.TransformFilter accessTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String s) {
                return null;
            }
        };

        Pattern pattern1 = Pattern.compile("개인정보처리방침");
        Pattern pattern2 = Pattern.compile("위치기반서비스 이용약관");


//
//        Linkify.addLinks(accessTermsText, pattern1, "doit://access_terms",null,accessTransform);
//        Linkify.addLinks(accessTermsText, pattern2, "doit://access_terms",null,accessTransform);


        //초기화진행
        final EditText idText = findViewById(R.id.idText);
        final EditText passwdText = findViewById(R.id.passwordText);
        final EditText passwdCheckText = findViewById(R.id.passwordCheckText);
        final EditText emailText = findViewById(R.id.emailText);
        final CheckBox checkBoxAccess = findViewById(R.id.accessTermsCheckbox);

        //회원 중복 체크 버튼
        final Button validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {  //버튼 클릭했을 때 중복체크 진행
            @Override
            public void onClick(View view) {
                String ID = idText.getText().toString();
                if(validate) { //validate 체크가 되어있다면, 함수종료(회원가입진행)
                    return;
                }
                if(ID.equals("")) { //validate체크되어있지않지만, ID값이 비어있다면 예외(얼럴트다이얼로그)발생
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("ID는 빈칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                //정상적으로 ID값을 입력했다면, 중복체크 진행
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                        try {
                            JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                            boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                            if(success) { //사용할 수 있는 ID라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용 가능한 ID 입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();

                                idText.setEnabled(false); //ID값을 더이상 바꿀수 없도록 고정
                                validate = true; //중복체크 완료를 의미
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            } else { //사용 할 수 없는 ID
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 ID 입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                };
                //실제로 접속 할 수 있도록 ValidateRequest 생성자를 통해 객체를 만들어 준다.
                VolleyR_ValidateRequest volleyRValidateRequest = new VolleyR_ValidateRequest(ID, responseListener);

                //요청을 보낼 수 있도록 큐를 만든다
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                //만들어진 큐에, ValidateRequest 객체를 넣어준다.
                queue.add(volleyRValidateRequest);


            }
        });

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

        RadioGroup memberGroup = findViewById(R.id.memberGroup);
        int testGroup_1_1ID = memberGroup.getCheckedRadioButtonId(); //라디오그룹에서 어떤게 선택되어있는지 확인할 수 있게 ID값

        memberGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton EorIButton = findViewById(i); //현재 선택되어있는버튼을 찾음

                if(EorIButton.getText().toString().equals("일반회원")) {
                    Mtype="GM";
                } else if(EorIButton.getText().toString().equals("사업자")) {
                    Mtype="HM";
                }
            }
        });


        //회원가입
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = idText.getText().toString();
                String passwd = passwdText.getText().toString();
                String passwdCheck = passwdCheckText.getText().toString();
                String Email = emailText.getText().toString();

                System.out.println("type : "+Mtype);

                if(!validate) { //중복체크를 하지 않았다면
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복체크를 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }
                //빈공간 확인
                if(ID.equals("") || passwd.equals("") || Email.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸 없이 입력 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }

                //비밀번호 일치
                if(!passwd.equals(passwdCheck)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                //체크박스 확인
                if(!checkBoxAccess.isChecked()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("약관에 동의해 주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                if(Mtype == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("회원구분을 선택해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

//                //중복체크 및 모든값이 입력되었다면, 정상적으로 회원가입 진행
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
//                            boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)
//
//                            if(success) { //사용할 수 있는 ID라면
//                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
//                                        .setPositiveButton("확인",
//                                                new DialogInterface.OnClickListener() { //확인누르면 회원가입 activity종료
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        finish();
//                                                    }
//                                                })
//                                        .create();
//                                dialog.show();
//                                //finish(); //회원가입 완료되었으니 회원가입Activity종료
//
//
//                            } else { //사용 할 수 없는 ID
//                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
//                                        .setNegativeButton("확인",null)
//                                        .create();
//                                dialog.show();
//
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//
//                    }
//                };
//
//                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
//                //실제로 접속 할 수 있도록 RegisterRequest 생성자를 통해 객체를 만들어 준다.
//                RegisterRequest registerRequest = new RegisterRequest(ID, passwd, Email, type, responseListener);
//
//                //요청을 보낼 수 있도록 큐를 만든다
//                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
//
//                //만들어진 큐에, RegisterRequest 객체를 넣어준다.
//                queue.add(registerRequest);


                //객체에 담아서 서버로 보내면 json현대토 보내지기때문에 서버에서 다시 풀어야함
                // 그냥 @Filed에 하나씩 담아보냈다
//                UserDTO user = new UserDTO(); // 입력된 회원가입 데이터
//                user.setID(idText.getText().toString());
//                user.setPasswd(passwdText.getText().toString());
//                user.setEmail(emailText.getText().toString());
//                user.setMtype(Mtype);


                //RestAPI restAPI = new RestRequestHelper().getRetrofit();
                //Call<Map<String, String>> response = restAPI.signUp(ID, passwd, Email, Mtype);
                restAPI = RestRequestHelper.getClient().create(RestAPI.class);
                Call<DTO_User> postCall = restAPI.register(idText.getText().toString(),passwdText.getText().toString(),emailText.getText().toString(),Mtype);

                postCall.enqueue(new Callback<DTO_User>() {  // enqueue로 비동기 통신 실행 ,통신완료후 이벤트처리할 callback리스너 등록
                    @Override
                    public void onResponse(Call<DTO_User> call, retrofit2.Response<DTO_User> response) {
                       // Map<String, String> map = response.body();
//                        boolean success = response.getBoolean("success");
                        Log.e("가입성공",  response.body().toString());

                        DTO_User DTOUser = response.body();
                        String getID = DTOUser.getID();
                        Log.e("가입성공ID", getID);

                        if (response.isSuccessful()) { //check for Response status //onResponse 통신 성공시 Callback //response.isSuccessful()로 정상(2xx)인지 확인

                            //회원가입이 성공했다면 다이얼로그를 띄워 확인시켜주고, 다이얼로그창이 사라질때 액티비티 finish()함
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() { //확인누르면 회원가입 activity종료
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                        .create();
                                dialog.show();
                                //finish();


                        } else {
                            //통신이 실패한 경우 (응답토그 3xx, 4xx등)
                            //onResponse가 무조건 성공응답이 아니기에 확인필요
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<DTO_User> call, Throwable t) { //통신실패시 Callback //예외발생, 인터넷끊김 등 시스템적인 이유로 실패한경우
                        Log.d("@@@@@@@@@@@@@@@xxxx", t.toString());
                        Toast.makeText(RegisterActivity.this, "네트워크 에러", Toast.LENGTH_SHORT);
                    }
                });

            }
        });

    }

    //회원등록 이루어진 후, 회원등록창이 꺼진 후 실행되는 onStop
    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }



}
