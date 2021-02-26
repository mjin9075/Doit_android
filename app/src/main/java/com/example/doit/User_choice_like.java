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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class User_choice_like extends AppCompatActivity {

    String[] sports = new String[]{"복싱_킥복싱","무에타이","시스테마","유도","주짓수","카포에라","크라브마가", "태권도","택견","합기도",
            "골프","농구","당구_포켓볼","볼링","축구",
            "배드민턴","스쿼시","탁구","테니스",
            "스케이트보드_롱보드","인라인스케이트","클라이밍","트릭킹",
            "딩기요트","서핑","수영","수상스키_웨이크보드","윈드서핑","프리다이빙","패들보드",
            "발레","사격","승마","양궁","요가","저글링","폴댄스","필라테스"
    };

    ArrayList<String> sportsA = new ArrayList<>(Arrays.asList(sports)); //string[] to ArrayList
    ArrayList<Integer> sports_like = new ArrayList<>(); //0,1로만들 배열
    ArrayList<String> like = new ArrayList<>(); //사용자가 선택한 항목의 배열

    ArrayList<String> ulikeSA = new ArrayList<>(); //사용자가 선택한 항목의 배열

    String[] ulike; //db에 저장된 사용자가 좋아한다고 선택한 항목의 배열, 사용자가 어떤걸 좋아한다고 선탣했었는지 보여주기 위해

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice_like);

        ///DB에 저장되어있는 데이터 확인///////////
        String ID = MainActivity.ID;
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "서버URL" + ID;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(response);

                String ulikeS = jsonElement.getAsJsonObject().get("ulikeS").getAsString(); //서버db에 문자열로저장된 사용자가 좋아하는 항목을 가져온다
                ulike = ulikeS.split(","); //가져온 문자열을 배열로 다시 저장한다 -> 사용자가 좋아한다고 선택했던 항목을 보여줄때 사용하기위해 배열로 저장

                Collections.addAll(ulikeSA,ulike);

                System.out.println("ulikeS : " + ulikeS);

                for(int i=0;i<ulike.length;i++) {
                    System.out.println("ulike"+ i + " : " + ulike[i]);
                }


                System.out.println("ulikeSA : " + ulikeSA);


                //sportsA 배열과 크기가 같은 integer 배열 -> 0으로 채움
                int j = 0;
                while (j<sportsA.size()) {
                    sports_like.add(0);
                    j++;
                }

                final TextView umartialArts2 = findViewById(R.id.umartialArts2);
                final TextView umartialArts3 = findViewById(R.id.umartialArts3);
                final TextView umartialArts4 = findViewById(R.id.umartialArts4);
                final TextView umartialArts5 = findViewById(R.id.umartialArts5);
                final TextView umartialArts6 = findViewById(R.id.umartialArts6);
                final TextView umartialArts7 = findViewById(R.id.umartialArts7);
                final TextView umartialArts8 = findViewById(R.id.umartialArts8);
                final TextView umartialArts9 = findViewById(R.id.umartialArts9);
                final TextView umartialArts10 = findViewById(R.id.umartialArts10);
                final TextView umartialArts11 = findViewById(R.id.umartialArts11);

                int i;
                int m = 12;
                TextView dm[] = new TextView[m];
                String dmC[] = new String[m];

                for(i=2; i<m; i++){
                    int ma = getResources().getIdentifier("umartialArts"+i,"id",getPackageName());
                    dm[i] = findViewById(ma);
                    dmC[i] = dm[i].getText().toString();
                    System.out.println("dmC["+i+"] :" + dmC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dmC[i].equals(e)){
                            dm[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dm[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(umartialArts2);
                selectSportsLike(umartialArts3);
                selectSportsLike(umartialArts4);
                selectSportsLike(umartialArts5);
                selectSportsLike(umartialArts6);
                selectSportsLike(umartialArts7);
                selectSportsLike(umartialArts8);
                selectSportsLike(umartialArts9);
                selectSportsLike(umartialArts10);
                selectSportsLike(umartialArts11);

                checkBG(umartialArts2);
                checkBG(umartialArts3);
                checkBG(umartialArts4);
                checkBG(umartialArts5);
                checkBG(umartialArts6);
                checkBG(umartialArts7);
                checkBG(umartialArts8);
                checkBG(umartialArts9);
                checkBG(umartialArts10);
                checkBG(umartialArts11);


                final TextView uballGame1 = findViewById(R.id.uballGame1);
                final TextView uballGame2 = findViewById(R.id.uballGame2);
                final TextView uballGame3 = findViewById(R.id.uballGame3);
                final TextView uballGame4 = findViewById(R.id.uballGame4);
                final TextView uballGame5 = findViewById(R.id.uballGame5);

                int b = 6;
                TextView db[] = new TextView[b];
                String dbC[] = new String[b];
                for(i=1; i<b; i++){ //반복문으로 레이아웃 참조하기
                    int ba = getResources().getIdentifier("uballGame"+i,"id",getPackageName());
                    db[i] = findViewById(ba);
                    dbC[i] = db[i].getText().toString();
                    System.out.println("dbC["+i+"] :" + dbC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dbC[i].equals(e)){
                            db[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            db[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(uballGame1);
                selectSportsLike(uballGame2);
                selectSportsLike(uballGame3);
                selectSportsLike(uballGame4);
                selectSportsLike(uballGame5);

                checkBG(uballGame1);
                checkBG(uballGame2);
                checkBG(uballGame3);
                checkBG(uballGame4);
                checkBG(uballGame5);


                final TextView uracketSports1 = findViewById(R.id.uracketSports1);
                final TextView uracketSports2 = findViewById(R.id.uracketSports2);
                final TextView uracketSports3 = findViewById(R.id.uracketSports3);
                final TextView uracketSports4 = findViewById(R.id.uracketSports4);

                int r = 5;
                TextView dr[] = new TextView[r];
                String drC[] = new String[r];
                for(i=1; i<r; i++){ //반복문으로 레이아웃 참조하기
                    int ra = getResources().getIdentifier("uracketSports"+i,"id",getPackageName());
                    dr[i] = findViewById(ra);
                    drC[i] = dr[i].getText().toString();
                    System.out.println("drC["+i+"] :" + drC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (drC[i].equals(e)){
                            dr[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dr[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(uracketSports1);
                selectSportsLike(uracketSports2);
                selectSportsLike(uracketSports3);
                selectSportsLike(uracketSports4);

                checkBG(uracketSports1);
                checkBG(uracketSports2);
                checkBG(uracketSports3);
                checkBG(uracketSports4);


                final TextView uextremeSports1 = findViewById(R.id.uextremeSports1);
                final TextView uextremeSports2 = findViewById(R.id.uextremeSports2);
                final TextView uextremeSports3 = findViewById(R.id.uextremeSports3);
                final TextView uextremeSports4 = findViewById(R.id.uextremeSports4);

                int ex = 5;
                TextView dex[] = new TextView[ex];
                String dexC[] = new String[ex];
                for(i=1; i<ex; i++){ //반복문으로 레이아웃 참조하기
                    int ext = getResources().getIdentifier("uextremeSports"+i,"id",getPackageName());
                    dex[i] = findViewById(ext);
                    dexC[i] = dex[i].getText().toString();
                    System.out.println("dexC["+i+"] :" + dexC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dexC[i].equals(e)){
                            dex[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dex[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(uextremeSports1);
                selectSportsLike(uextremeSports2);
                selectSportsLike(uextremeSports3);
                selectSportsLike(uextremeSports4);

                checkBG(uextremeSports1);
                checkBG(uextremeSports2);
                checkBG(uextremeSports3);
                checkBG(uextremeSports4);


                final TextView uwaterSports1 = findViewById(R.id.uwaterSports1);
                final TextView uwaterSports2 = findViewById(R.id.uwaterSports2);
                final TextView uwaterSports3 = findViewById(R.id.uwaterSports3);
                final TextView uwaterSports4 = findViewById(R.id.uwaterSports4);
                final TextView uwaterSports5 = findViewById(R.id.uwaterSports5);
                final TextView uwaterSports6 = findViewById(R.id.uwaterSports6);
                final TextView uwaterSports7 = findViewById(R.id.uwaterSports7);

                int wa = 8;
                TextView dwa[] = new TextView[wa];
                String dwaC[] = new String[wa];
                for(i=1; i<wa; i++){ //반복문으로 레이아웃 참조하기
                    int wat = getResources().getIdentifier("uwaterSports"+i,"id",getPackageName());
                    dwa[i] = findViewById(wat);
                    dwaC[i] = dwa[i].getText().toString();
                    System.out.println("dwaC["+i+"] :" + dwaC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (dwaC[i].equals(e)){
                            dwa[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            dwa[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(uwaterSports1);
                selectSportsLike(uwaterSports2);
                selectSportsLike(uwaterSports3);
                selectSportsLike(uwaterSports4);
                selectSportsLike(uwaterSports5);
                selectSportsLike(uwaterSports6);
                selectSportsLike(uwaterSports7);

                checkBG(uwaterSports1);
                checkBG(uwaterSports2);
                checkBG(uwaterSports3);
                checkBG(uwaterSports4);
                checkBG(uwaterSports5);
                checkBG(uwaterSports6);
                checkBG(uwaterSports7);


                final TextView uetcSports1 = findViewById(R.id.uetcSports1);
                final TextView uetcSports2 = findViewById(R.id.uetcSports2);
                final TextView uetcSports3 = findViewById(R.id.uetcSports3);
                final TextView uetcSports4 = findViewById(R.id.uetcSports4);
                final TextView uetcSports5 = findViewById(R.id.uetcSports5);
                final TextView uetcSports6 = findViewById(R.id.uetcSports6);
                final TextView uetcSports7 = findViewById(R.id.uetcSports7);
                final TextView uetcSports8 = findViewById(R.id.uetcSports8);

                int et = 9;
                TextView det[] = new TextView[et];
                String detC[] = new String[et];
                for(i=1; i<et; i++){ //반복문으로 레이아웃 참조하기
                    int etc = getResources().getIdentifier("uetcSports"+i,"id",getPackageName());
                    det[i] = findViewById(etc);
                    detC[i] = det[i].getText().toString();
                    System.out.println("detC["+i+"] :" + detC[i]);

                    for(String e : ulike){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                        if (detC[i].equals(e)){
                            det[i].setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                            det[i].setTextColor(Color.WHITE);
                        }
                    }
                }

                selectSportsLike(uetcSports1);
                selectSportsLike(uetcSports2);
                selectSportsLike(uetcSports3);
                selectSportsLike(uetcSports4);
                selectSportsLike(uetcSports5);
                selectSportsLike(uetcSports6);
                selectSportsLike(uetcSports7);
                selectSportsLike(uetcSports8);

                checkBG(uetcSports1);
                checkBG(uetcSports2);
                checkBG(uetcSports3);
                checkBG(uetcSports4);
                checkBG(uetcSports5);
                checkBG(uetcSports6);
                checkBG(uetcSports7);
                checkBG(uetcSports8);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



        Button uchoiceLike_next = findViewById(R.id.uchoiceLike_next);
        uchoiceLike_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("like를 알려다오 :" + like);

                for(int i = 0; i < like.size(); i++) {
                    if(sportsA.contains(like.get(i))) {  //like에 있는 항목들을 1로 바꾸어서 저장->db에 백터형식으로 저장하기 위해
                        sports_like.set(sportsA.indexOf(like.get(i)),1);
                    }
                }

                System.out.println("sportsA(전체종목이 있는 arrayList) :" + sportsA);
                System.out.println("like(선택한 항목의 arrayList) :" + like);
                System.out.println("sports_like(선택 1, 선택안함 0 으로표시한 arrayList) :" + sports_like);



                Intent intent = new Intent(User_choice_like.this, User_choice_dislike.class);
                intent.putExtra("likeS",like);
                intent.putExtra("like_Int_Array",sports_like);
                User_choice_like.this.startActivity(intent);
                finish();
            }
        });



    }


    public void choiceLike(int IDNUM){
        final TextView test = findViewById(IDNUM);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(test.getTextColors() == ColorStateList.valueOf(getResources().getColor(R.color.colorGray))) {
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                    test.setTextColor(Color.WHITE);

                    String id = getResources().getResourceEntryName(view.getId());
                    String contents = test.getText().toString();

                    like.add(contents);

                } else if(test.getTextColors() == ColorStateList.valueOf(Color.WHITE)){
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button));
                    test.setTextColor(getResources().getColor(R.color.colorGray));
                    String contents = test.getText().toString();

                    like.remove(contents);
                }

            }
        });

    }

    public void selectSportsLike(View view){

        choiceLike(view.getId());
    }

    public void checkBG(View view){

        final TextView textView = findViewById(view.getId());

        if(textView.getTextColors() == ColorStateList.valueOf(Color.WHITE)) {
            String contents = textView.getText().toString();
            like.add(contents);
        } else if(textView.getTextColors() == ColorStateList.valueOf(getResources().getColor(R.color.colorGray))) {
            String contents = textView.getText().toString();
            like.remove(contents);
        }

    }




}
