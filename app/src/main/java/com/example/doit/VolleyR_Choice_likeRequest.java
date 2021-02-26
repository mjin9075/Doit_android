package com.example.doit;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class VolleyR_Choice_likeRequest extends StringRequest {

    final static private String URL = "서버URL";
    //private Map<String, Object> parameters;
    private Map<String, String> parameters;



    //생성자(담아보낼 정보들, 응답을 받을 리스너)
   // public Choice_likeRequest(String ID , ArrayList<String> like, Response.Listener<String> listener) {
    public VolleyR_Choice_likeRequest(String ID , String likeS, String strLikeS, String strDislikeS, String strMBTI , Response.Listener<String> listener) {
        //StringRequest parameter를 총 4개 입력
        //방식(GET이냐, POST냐), url, 서버 응답시 처리할 내용, 에러 시 처리할 내용


        super(Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", "[" + response + "]"); // 서버와의 통신 결과 확인 목적
                        //showJSONList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse",error.getMessage());
                    }
                }); //해당URL에 파라메터들을 POST방식으로 보내라

        //파라메터 각각의 값을 넣을 수 있도록 HashMap생성
        parameters = new HashMap<>();
        //각각의 값들을 파라메터로 매칭해줌
        parameters.put("ID",ID);

//        for(int i=0; i<like.size(); i++) {
//            parameters.put("like",like.get(i));
//            System.out.println("해쉬맵에서으ddddddddd아오:"+like.get(i));
//        }

        parameters.put("likeS",likeS);
        parameters.put("strLikeS",strLikeS);
        parameters.put("strDislikeS",strDislikeS);
        parameters.put("strMBTI",strMBTI);
        System.out.println("해쉬맵에서으아오 : "+likeS);
        //System.out.println("해쉬맵에서으ddddddddd아오"+like.get(0));
        //System.out.println("해쉬맵에서으ddddtoString()"+like.get(0).toString());

    }


    //만약 POST 방식에서 전달할 요청 파라미터가 있다면 getParams 메소드에서 반환하는 HashMap 객체에 넣어줍니다.
    //이렇게 만든 요청 객체는 요청 큐에 넣어주는 것만 해주면 됩니다.
    //POST방식으로 안할거면 없어도 되는거같다.
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
