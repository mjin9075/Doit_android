package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class User_choice_dislike extends AppCompatActivity {


    String[] sports = new String[]{"복싱_킥복싱","무에타이","시스테마","유도","주짓수","카포에라","크라브마가", "태권도","택견","합기도",
            "골프","농구","당구_포켓볼","볼링","축구",
            "배드민턴","스쿼시","탁구","테니스",
            "스케이트보드_롱보드","인라인스케이트","클라이밍","트릭킹",
            "딩기요트","서핑","수영","수상스키_웨이크보드","윈드서핑","프리다이빙","패들보드",
            "발레","사격","승마","양궁","요가","저글링","폴댄스","필라테스"
    };

    ArrayList<String> sportsA = new ArrayList<>(Arrays.asList(sports)); //string[] to ArrayList
    ArrayList<String> dislikeS = new ArrayList<>();
    ArrayList<String> likeS = new ArrayList<>();
    ArrayList<Integer> sports_dislike = new ArrayList<>();

    String[] udislike; //db에 저장된 사용자가 싫어한다고 선택한 항목의 배열, 사용자가 어떤걸 싫어한다고 선택했는지 보여주기 위해

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice_dislike);

        Intent intent = getIntent();
        likeS = intent.getStringArrayListExtra("likeS");
        sports_dislike = intent.getIntegerArrayListExtra("like_Int_Array");

        ///DB에 저장되어있는 데이터 확인///////////
        String ID = MainActivity.ID;
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "서버URL" + ID;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(response);

                String udislikeS = jsonElement.getAsJsonObject().get("udislikeS").getAsString(); //서버db에 문자열로저장된 사용자가 좋아하는 항목을 가져온다
                udislike = udislikeS.split(","); //가져온 문자열을 배열로 다시 저장한다 -> 사용자가 좋아한다고 선택했던 항목을 보여줄때 사용하기위해 배열로 저장


                System.out.println("udislike : " + udislike);


                final TextView dis_umartialArts2 = findViewById(R.id.dis_umartialArts2);
                final TextView dis_umartialArts3 = findViewById(R.id.dis_umartialArts3);
                final TextView dis_umartialArts4 = findViewById(R.id.dis_umartialArts4);
                final TextView dis_umartialArts5 = findViewById(R.id.dis_umartialArts5);
                final TextView dis_umartialArts6 = findViewById(R.id.dis_umartialArts6);
                final TextView dis_umartialArts7 = findViewById(R.id.dis_umartialArts7);
                final TextView dis_umartialArts8 = findViewById(R.id.dis_umartialArts8);
                final TextView dis_umartialArts9 = findViewById(R.id.dis_umartialArts9);
                final TextView dis_umartialArts10 = findViewById(R.id.dis_umartialArts10);
                final TextView dis_umartialArts11 = findViewById(R.id.dis_umartialArts11);

                int i;
                int m = 12;
                TextView dm[] = new TextView[m];
                String dmC[] = new String[m];

                for(i=2; i<m; i++){
                    int ma = getResources().getIdentifier("dis_umartialArts"+i,"id",getPackageName());
                    dm[i] = findViewById(ma);
                    dmC[i] = dm[i].getText().toString();
                    System.out.println("dmC["+i+"] :" + dmC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dmC[i].equals(e)){
                            dm[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dm[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dmC[i].equals(e)){
                            dm[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            dm[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_umartialArts2);
                selectSportsDisLike(dis_umartialArts3);
                selectSportsDisLike(dis_umartialArts4);
                selectSportsDisLike(dis_umartialArts5);
                selectSportsDisLike(dis_umartialArts6);
                selectSportsDisLike(dis_umartialArts7);
                selectSportsDisLike(dis_umartialArts8);
                selectSportsDisLike(dis_umartialArts9);
                selectSportsDisLike(dis_umartialArts10);
                selectSportsDisLike(dis_umartialArts11);

                checkBG(dis_umartialArts2);
                checkBG(dis_umartialArts3);
                checkBG(dis_umartialArts4);
                checkBG(dis_umartialArts5);
                checkBG(dis_umartialArts6);
                checkBG(dis_umartialArts7);
                checkBG(dis_umartialArts8);
                checkBG(dis_umartialArts9);
                checkBG(dis_umartialArts10);
                checkBG(dis_umartialArts11);


                final TextView dis_uballGame1 = findViewById(R.id.dis_uballGame1);
                final TextView dis_uballGame2 = findViewById(R.id.dis_uballGame2);
                final TextView dis_uballGame3 = findViewById(R.id.dis_uballGame3);
                final TextView dis_uballGame4 = findViewById(R.id.dis_uballGame4);
                final TextView dis_uballGame5 = findViewById(R.id.dis_uballGame5);

                int b = 6;
                TextView db[] = new TextView[b];
                String dbC[] = new String[b];
                for(i=1; i<b; i++){ //반복문으로 레이아웃 참조하기
                    int ba = getResources().getIdentifier("dis_uballGame"+i,"id",getPackageName());
                    db[i] = findViewById(ba);
                    dbC[i] = db[i].getText().toString();
                    System.out.println("dbC["+i+"] :" + dbC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dbC[i].equals(e)){
                            db[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            db[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dbC[i].equals(e)){
                            db[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            db[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_uballGame1);
                selectSportsDisLike(dis_uballGame2);
                selectSportsDisLike(dis_uballGame3);
                selectSportsDisLike(dis_uballGame4);
                selectSportsDisLike(dis_uballGame5);

                checkBG(dis_uballGame1);
                checkBG(dis_uballGame2);
                checkBG(dis_uballGame3);
                checkBG(dis_uballGame4);
                checkBG(dis_uballGame5);


                final TextView dis_uracketSports1 = findViewById(R.id.dis_uracketSports1);
                final TextView dis_uracketSports2 = findViewById(R.id.dis_uracketSports2);
                final TextView dis_uracketSports3 = findViewById(R.id.dis_uracketSports3);
                final TextView dis_uracketSports4 = findViewById(R.id.dis_uracketSports4);

                int r = 5;
                TextView dr[] = new TextView[r];
                String drC[] = new String[r];
                for(i=1; i<r; i++){ //반복문으로 레이아웃 참조하기
                    int ra = getResources().getIdentifier("dis_uracketSports"+i,"id",getPackageName());
                    dr[i] = findViewById(ra);
                    drC[i] = dr[i].getText().toString();
                    System.out.println("drC["+i+"] :" + drC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (drC[i].equals(e)){
                            dr[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dr[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (drC[i].equals(e)){
                            dr[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            dr[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_uracketSports1);
                selectSportsDisLike(dis_uracketSports2);
                selectSportsDisLike(dis_uracketSports3);
                selectSportsDisLike(dis_uracketSports4);

                checkBG(dis_uracketSports1);
                checkBG(dis_uracketSports2);
                checkBG(dis_uracketSports3);
                checkBG(dis_uracketSports4);


                final TextView dis_uextremeSports1 = findViewById(R.id.dis_uextremeSports1);
                final TextView dis_uextremeSports2 = findViewById(R.id.dis_uextremeSports2);
                final TextView dis_uextremeSports3 = findViewById(R.id.dis_uextremeSports3);
                final TextView dis_uextremeSports4 = findViewById(R.id.dis_uextremeSports4);

                int ex = 5;
                TextView dex[] = new TextView[ex];
                String dexC[] = new String[ex];
                for(i=1; i<ex; i++){ //반복문으로 레이아웃 참조하기
                    int ext = getResources().getIdentifier("dis_uextremeSports"+i,"id",getPackageName());
                    dex[i] = findViewById(ext);
                    dexC[i] = dex[i].getText().toString();
                    System.out.println("dexC["+i+"] :" + dexC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dexC[i].equals(e)){
                            dex[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dex[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dexC[i].equals(e)){
                            dex[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            dex[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_uextremeSports1);
                selectSportsDisLike(dis_uextremeSports2);
                selectSportsDisLike(dis_uextremeSports3);
                selectSportsDisLike(dis_uextremeSports4);

                checkBG(dis_uextremeSports1);
                checkBG(dis_uextremeSports2);
                checkBG(dis_uextremeSports3);
                checkBG(dis_uextremeSports4);


                final TextView dis_uwaterSports1 = findViewById(R.id.dis_uwaterSports1);
                final TextView dis_uwaterSports2 = findViewById(R.id.dis_uwaterSports2);
                final TextView dis_uwaterSports3 = findViewById(R.id.dis_uwaterSports3);
                final TextView dis_uwaterSports4 = findViewById(R.id.dis_uwaterSports4);
                final TextView dis_uwaterSports5 = findViewById(R.id.dis_uwaterSports5);
                final TextView dis_uwaterSports6 = findViewById(R.id.dis_uwaterSports6);
                final TextView dis_uwaterSports7 = findViewById(R.id.dis_uwaterSports7);

                int wa = 8;
                TextView dwa[] = new TextView[wa];
                String dwaC[] = new String[wa];
                for(i=1; i<wa; i++){ //반복문으로 레이아웃 참조하기
                    int wat = getResources().getIdentifier("dis_uwaterSports"+i,"id",getPackageName());
                    dwa[i] = findViewById(wat);
                    dwaC[i] = dwa[i].getText().toString();
                    System.out.println("dwaC["+i+"] :" + dwaC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dwaC[i].equals(e)){
                            dwa[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dwa[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dwaC[i].equals(e)){
                            dwa[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            dwa[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_uwaterSports1);
                selectSportsDisLike(dis_uwaterSports2);
                selectSportsDisLike(dis_uwaterSports3);
                selectSportsDisLike(dis_uwaterSports4);
                selectSportsDisLike(dis_uwaterSports5);
                selectSportsDisLike(dis_uwaterSports6);
                selectSportsDisLike(dis_uwaterSports7);

                checkBG(dis_uwaterSports1);
                checkBG(dis_uwaterSports2);
                checkBG(dis_uwaterSports3);
                checkBG(dis_uwaterSports4);
                checkBG(dis_uwaterSports5);
                checkBG(dis_uwaterSports6);
                checkBG(dis_uwaterSports7);


                final TextView dis_uetcSports1 = findViewById(R.id.dis_uetcSports1);
                final TextView dis_uetcSports2 = findViewById(R.id.dis_uetcSports2);
                final TextView dis_uetcSports3 = findViewById(R.id.dis_uetcSports3);
                final TextView dis_uetcSports4 = findViewById(R.id.dis_uetcSports4);
                final TextView dis_uetcSports5 = findViewById(R.id.dis_uetcSports5);
                final TextView dis_uetcSports6 = findViewById(R.id.dis_uetcSports6);
                final TextView dis_uetcSports7 = findViewById(R.id.dis_uetcSports7);
                final TextView dis_uetcSports8 = findViewById(R.id.dis_uetcSports8);

                int et = 9;
                TextView det[] = new TextView[et];
                String detC[] = new String[et];
                for(i=1; i<et; i++){ //반복문으로 레이아웃 참조하기
                    int etc = getResources().getIdentifier("dis_uetcSports"+i,"id",getPackageName());
                    det[i] = findViewById(etc);
                    detC[i] = det[i].getText().toString();
                    System.out.println("detC["+i+"] :" + detC[i]);

                    for(String e : udislike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (detC[i].equals(e)){
                            det[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            det[i].setTextColor(Color.WHITE);
                        }
                    }

                    for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (detC[i].equals(e)){
                            det[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                            det[i].setTextColor(Color.BLACK);
                        }
                    }
                }

                selectSportsDisLike(dis_uetcSports1);
                selectSportsDisLike(dis_uetcSports2);
                selectSportsDisLike(dis_uetcSports3);
                selectSportsDisLike(dis_uetcSports4);
                selectSportsDisLike(dis_uetcSports5);
                selectSportsDisLike(dis_uetcSports6);
                selectSportsDisLike(dis_uetcSports7);
                selectSportsDisLike(dis_uetcSports8);

                checkBG(dis_uetcSports1);
                checkBG(dis_uetcSports2);
                checkBG(dis_uetcSports3);
                checkBG(dis_uetcSports4);
                checkBG(dis_uetcSports5);
                checkBG(dis_uetcSports6);
                checkBG(dis_uetcSports7);
                checkBG(dis_uetcSports8);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



        Button uchoiceDisLike_finish = findViewById(R.id.uchoiceDisLike_finish);
        uchoiceDisLike_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("dislike를 알려다오 :" + dislikeS);

                for(int i = 0; i < dislikeS.size(); i++) {
                    if(sportsA.contains(dislikeS.get(i))) {  //like에 있는 항목들을 1로 바꾸어서 저장->db에 백터형식으로 저장하기 위해
                        sports_dislike.set(sportsA.indexOf(dislikeS.get(i)),-1);
                    }
                }

                System.out.println("sportsA(전체종목이 있는 arrayList) :" + sportsA);
                System.out.println("like(선택한 항목의 arrayList) :" + dislikeS);
                System.out.println("sports_dislike(선택 1, 선택안함 0 으로표시한 arrayList) :" + sports_dislike);


                //서버로 보내기위해 각 배열을 문자열로 바꿈
                // 0,1,-1 로 된 배열(백터데이터)
                StringBuilder sb = new StringBuilder();
                for(Integer item: sports_dislike) {
                    if(sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(item);
                }
                String strDataLike = sb.toString();

                //넘겨받은 likeS(좋아하는운동이름배열
                StringBuilder sbLikeS = new StringBuilder();
                for(String itemLikeS: likeS) {
                    if(sbLikeS.length() > 0) {
                        sbLikeS.append(',');
                    }
                    sbLikeS.append(itemLikeS);
                }
                String strLikeS = sbLikeS.toString();

                //싫어하는운동이름배열
                StringBuilder sbDislikeS = new StringBuilder();
                for(String itemDislikeS: dislikeS) {
                    if(sbDislikeS.length() > 0) {
                        sbDislikeS.append(',');
                    }
                    sbDislikeS.append(itemDislikeS);
                }
                String strDislikeS = sbDislikeS.toString();


                String ID = MainActivity.ID;
                Response.Listener<String> responseListener = new Response.Listener<String>(){//응답을 문자열로 받아서 여기다 넣어라 (응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//DB에 저장됐다면
                                finish();//액티비티를 종료시킴
                            }else{
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료


                VolleyR_User_choice_likeRequest VolleyR_User_choice_likeRequest = new VolleyR_User_choice_likeRequest(ID, strDataLike, strLikeS, strDislikeS ,responseListener);

                //요청을 보낼 수 있도록 큐를 만든다
                RequestQueue queueUser = Volley.newRequestQueue(User_choice_dislike.this);

                //만들어진 큐에, Request 객체를 넣어준다.
                queueUser.add(VolleyR_User_choice_likeRequest);


//                Intent intent = new Intent(User_choice_dislike.this, );
//                intent.putExtra("userID",ID);
//                User_choice_dislike.this.startActivity(intent);
                finish();




            }
        });



    }


    public void choiceDisLike(int IDNUM){
        final TextView test = findViewById(IDNUM);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(test.getTextColors() == ColorStateList.valueOf(getResources().getColor(R.color.colorGray))) {
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                    test.setTextColor(Color.WHITE);
                    String contents = test.getText().toString();

                    dislikeS.add(contents);

                } else if(test.getTextColors() == ColorStateList.valueOf(Color.WHITE)){
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button));
                    test.setTextColor(getResources().getColor(R.color.colorGray));
                    String contents = test.getText().toString();
                    dislikeS.remove(contents);
                }
            }
        });
    }

    public void selectSportsDisLike(View view){
        choiceDisLike(view.getId());
    }

    public void checkBG(View view){

        final TextView textView = findViewById(view.getId());

        if(textView.getTextColors() == ColorStateList.valueOf(Color.WHITE)) {
            String contents = textView.getText().toString();
            dislikeS.add(contents);
        } else if(textView.getTextColors() == ColorStateList.valueOf(getResources().getColor(R.color.colorGray))) {
            String contents = textView.getText().toString();
            dislikeS.remove(contents);
        }

    }
}
