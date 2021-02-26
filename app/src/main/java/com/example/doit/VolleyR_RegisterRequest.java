package com.example.doit;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


//2020-03-19
//RegisterRequest를 이용해서 '해당 Register.php 파일에 ID,PW등 회원정보를 보내서 회원가입을 시켜라' 라는 요청을 보내는 class

public class VolleyR_RegisterRequest extends StringRequest {

    final static private String URL = "서버URL"; //회원등록 요청할 서버주소
    private Map<String, String> parameters;

    //생성자(담아보낼 정보들, 응답을 받을 리스너)
    public VolleyR_RegisterRequest(String ID, String passwd, String Email, String Mtype , Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //해당URL에 파라메터들을 POST방식으로 보내라

        //파라메터 각각의 값을 넣을 수 있도록 HashMap생성
        parameters = new HashMap<>();
        //각각의 값들을 파라메터로 매칭해줌
        parameters.put("ID",ID);
        parameters.put("passwd",passwd);
        parameters.put("Email",Email);
        parameters.put("Mtype",Mtype);
        System.out.println("type_request : "+Mtype);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
