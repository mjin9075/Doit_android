package com.example.doit;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VolleyR_LoginRequest extends StringRequest {
    final static private String URL = "서버URL"; //회원등록 요청할 서버주소
    private Map<String, String> parameters;

    //생성자(담아보낼 정보들, 응답을 받을 리스너)
    public VolleyR_LoginRequest(String ID, String passwd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); //해당URL에 파라메터들을 POST방식으로 보내라

        //파라메터 각각의 값을 넣을 수 있도록 HashMap생성
        parameters = new HashMap<>();
        //각각의 값들을 파라메터로 매칭해줌
        parameters.put("ID",ID);
        parameters.put("passwd",passwd);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
