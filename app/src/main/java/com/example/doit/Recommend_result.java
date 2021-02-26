package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Recommend_result extends AppCompatActivity {

    private static final String TAG = "AAAAAAAAAAA";
    private String ID;

    private TextView rec_result;
    private TextView rec_result2;
    private TextView rec_result_explanation;

    private RequestQueue queue;
    private Map<String, String> parameters;


    ArrayList<String> item = new ArrayList<>();
    ArrayList<String> item_explan = new ArrayList<>();

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_result);

        ID = getIntent().getStringExtra("userID");

        rec_result = findViewById(R.id.rec_result);
        rec_result2 = findViewById(R.id.rec_result2);
        rec_result_explanation = findViewById(R.id.rec_result_explanation);
        final Button rec_next = findViewById(R.id.rec_next);
        final Button findClassButton = findViewById(R.id.findClassButton);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                try {
                    JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                    boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                    if(success) { //ID가 없다면(선택데이터가 없다)->인기종목10가지 보여주기

                        String url = "서버URL";
                        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JsonParser jsonParser = new JsonParser();
                                JsonElement jsonElement = jsonParser.parse(response);
                                JsonObject  jobject = jsonElement.getAsJsonObject();
                                JsonArray jarray = jobject.getAsJsonArray("popularList");
                                JsonArray jarrayExplan = jobject.getAsJsonArray("explanation");

                                for (int i=0; i<jarray.size(); i++){
                                    item.add(jarray.get(i).toString().replace("\"",""));
                                    item_explan.add(jarrayExplan.get(i).toString().replace("\"",""));
                                    System.out.println("popularList" + i + " :" +jarray.get(i).toString());
                                }
                                rec_result.setText("인기 운동");

                                //인기종목을 하나씩보여주기 위해(버튼을 누를때마다 다름학목이 보여짐)
                                i=0;
                                rec_result2.setText(item.get(i));
                                rec_result_explanation.setText(item_explan.get(i).replace("\\r\\n","\n"));
                                rec_next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(i<item.size()-1){
                                            i= i+1;
                                            rec_result2.setText(item.get(i));
                                            rec_result_explanation.setText(item_explan.get(i).replace("\\r\\n","\n"));
                                            System.out.println("iiiiiiiiiiiiii"+i);
                                            System.out.println("iiiiipopularListiiiiiii"+item.size());
                                        }else if(i==item.size()-1) {
                                            rec_result2.setText("끝끝 디엔드");
                                            rec_result_explanation.setText("끝끝 디엔드");
                                        }
                                    }
                                });

                                findClassButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String strResult = rec_result2.getText().toString();
                                        //System.out.println("strResult : "+strResult);
                                        FindClassFragment findClassFragment = new FindClassFragment();

                                        Intent intent = new Intent(Recommend_result.this,MainActivity.class);
                                        intent.putExtra("strResult",strResult);
                                        intent.putExtra("userID",ID);
                                        Recommend_result.this.startActivity(intent);

                                    }
                                });



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(Recommend_result.this);
                        queue.add(stringRequest);


                    } else { //ID가 있다(선택데이터가 있다)->추천결과값보여주기

                        String url = "서버URL" + ID;
                        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JsonParser jsonParser = new JsonParser();
                                JsonElement jsonElement = jsonParser.parse(response);
                                JsonObject  jobject = jsonElement.getAsJsonObject();
                                JsonArray jarrayReco = jobject.getAsJsonArray("reco");
                                JsonArray jarrayExplan = jobject.getAsJsonArray("explanation");

                                for (int i=0; i<jarrayReco.size(); i++){
                                    item.add(jarrayReco.get(i).toString().replace("\"",""));
                                    item_explan.add(jarrayExplan.get(i).toString().replace("\"",""));
//                                    item_explan.add(jarrayExplan.get(i).toString().replace("\r\n","\n"));
                                    System.out.println("reco" + i + " :" +jarrayReco.get(i).toString());
                                }

                                rec_result.setText(ID + "님 을 위한 추천 운동");

                                //사용자에게 추천되는 항목을 하나씩보여주기 위해(버튼을 누를때마다 다름학목이 보여짐)
                                ////////////////////////////피드백을 위한 작업은 아직 하지않음음
                                i=0;
                                rec_result2.setText(item.get(i));
                                rec_result_explanation.setText(item_explan.get(i).replace("\\r\\n","\n"));

                                rec_next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(i<item.size()-1){
                                            i= i+1;
                                            rec_result2.setText(item.get(i));
                                            rec_result_explanation.setText(item_explan.get(i).replace("\\r\\n","\n"));

                                        }else if(i==item.size()-1) {
                                            rec_result2.setText("끝끝 디엔드");
                                            rec_result_explanation.setText("끝끝 디엔드");
                                        }
                                    }
                                });

                                findClassButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String strResult = rec_result2.getText().toString();
                                        //System.out.println("strResult : "+strResult);
                                        FindClassFragment findClassFragment = new FindClassFragment();

                                        Intent intent = new Intent(Recommend_result.this,MainActivity.class);
                                        intent.putExtra("strResult",strResult);
                                        intent.putExtra("userID",ID);
                                        Recommend_result.this.startActivity(intent);

                                    }
                                });

//                                //리싸이클러뷰에 리니어레이아웃매니저 객체 지정
//                                RecyclerView recyclerView = findViewById(R.id.recycler1);
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(Recommend_result.this,LinearLayoutManager.HORIZONTAL,false);
//                                recyclerView.setLayoutManager(layoutManager);
//
//                                PagerSnapHelper snapHelper = new PagerSnapHelper();
//                                snapHelper.attachToRecyclerView(recyclerView);
//
//                                //리싸이클러뷰에 adapter객체 지정
//                                RecoItemAdapter recoItemAdapter = new RecoItemAdapter(item);
//                                recyclerView.setAdapter(recoItemAdapter);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(Recommend_result.this);
                        queue.add(stringRequest);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        VolleyR_CheckDBRequest volleyRCheckDBRequest = new VolleyR_CheckDBRequest(ID, responseListener);
        RequestQueue queueC = Volley.newRequestQueue(Recommend_result.this);
        queueC.add(volleyRCheckDBRequest);























    }
}
