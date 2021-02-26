package com.example.doit;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Choice_dislike extends AppCompatActivity  {

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


    private AlertDialog dialog;

    ArrayList<String> MBTI = new ArrayList<>();

    private String ID = MainActivity.ID;
    private String strDataLike;
    private String strLikeS;
    private String strDislikeS;
    private String strMBTI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_dislike);

        Intent intent = getIntent();
        //likeS = (ArrayList<String>) intent.getSerializableExtra("likeS");
        likeS = intent.getStringArrayListExtra("likeS");

        //sports_dislike = (ArrayList<Integer>) intent.getSerializableExtra("like_Int_Array");
        sports_dislike = intent.getIntegerArrayListExtra("like_Int_Array");

        MBTI = intent.getStringArrayListExtra("MBTI");
        ID = getIntent().getStringExtra("userID");

        System.out.println("넘겨받은 like_Int_Array : " + sports_dislike);  //선택 1, 선택안함 0 으로표시한 arrayList를 sports_dislike로받음
        System.out.println("넘겨받은 likeS : " + likeS);  //
        //String sports_dislike = intent.getExtras().getString("like_Int_Array");
        //System.out.println("넘겨받음:"+likeS);


//        for(int i = 0; i < likeS.size(); i++) {
//            if(test.equals(likeS.get(i))) {
//                test.setBackgroundColor(Color.GRAY);
//                System.out.println("넘겨받은 likeS중  (choiceDisL내부): " + likeS.get(i));  //
//            } else{
//
//            }
//        }


//        final TextView dis_martialArts1 = findViewById(R.id.dis_martialArts1);
        final TextView dis_martialArts2 = findViewById(R.id.dis_martialArts2);
        final TextView dis_martialArts3 = findViewById(R.id.dis_martialArts3);
        final TextView dis_martialArts4 = findViewById(R.id.dis_martialArts4);
        final TextView dis_martialArts5 = findViewById(R.id.dis_martialArts5);
        final TextView dis_martialArts6 = findViewById(R.id.dis_martialArts6);
        final TextView dis_martialArts7 = findViewById(R.id.dis_martialArts7);
        final TextView dis_martialArts8 = findViewById(R.id.dis_martialArts8);
        final TextView dis_martialArts9 = findViewById(R.id.dis_martialArts9);
        final TextView dis_martialArts10 = findViewById(R.id.dis_martialArts10);
        final TextView dis_martialArts11 = findViewById(R.id.dis_martialArts11);

        int i;
        int m = 12;
        TextView dm[] = new TextView[m];
        String dmC[] = new String[m];

        for(i=2; i<m; i++){
            int ma = getResources().getIdentifier("dis_martialArts"+i,"id",getPackageName());
            dm[i] = findViewById(ma);
            dmC[i] = dm[i].getText().toString();
            System.out.println("dmC["+i+"] :" + dmC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (dmC[i].equals(e)){
                    dm[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    dm[i].setTextColor(Color.BLACK);
                }
            }
        }

//        String dm2 = dis_martialArts2.getText().toString();
////        if(dm2.equals(likeS.get(0))){
////
////            dis_martialArts2.setBackground(getResources().getDrawable(R.drawable.custom_button_3));
////            dis_martialArts2.setTextColor(Color.BLACK);
////
////        }
//        for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
//            if (dm2.equals(e)){
//                dis_martialArts2.setBackground(getResources().getDrawable(R.drawable.custom_button_3));
//                dis_martialArts2.setTextColor(Color.BLACK);
//            }
//        }

        selectSportsDisLike(dis_martialArts2);
        selectSportsDisLike(dis_martialArts3);
        selectSportsDisLike(dis_martialArts4);
        selectSportsDisLike(dis_martialArts5);
        selectSportsDisLike(dis_martialArts6);
        selectSportsDisLike(dis_martialArts7);
        selectSportsDisLike(dis_martialArts8);
        selectSportsDisLike(dis_martialArts9);
        selectSportsDisLike(dis_martialArts10);
        selectSportsDisLike(dis_martialArts11);

        final TextView dis_ballGame1 = findViewById(R.id.dis_ballGame1);
        final TextView dis_ballGame2 = findViewById(R.id.dis_ballGame2);
        final TextView dis_ballGame3 = findViewById(R.id.dis_ballGame3);
        final TextView dis_ballGame4 = findViewById(R.id.dis_ballGame4);
        final TextView dis_ballGame5 = findViewById(R.id.dis_ballGame5);

        int b = 6;
        TextView db[] = new TextView[b];
        String dbC[] = new String[b];
        for(i=1; i<b; i++){ //반복문으로 레이아웃 참조하기
            int ba = getResources().getIdentifier("dis_ballGame"+i,"id",getPackageName());
            db[i] = findViewById(ba);
            dbC[i] = db[i].getText().toString();
            System.out.println("dbC["+i+"] :" + dbC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (dbC[i].equals(e)){
                    db[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    db[i].setTextColor(Color.BLACK);
                }
            }
        }

        selectSportsDisLike(dis_ballGame1);
        selectSportsDisLike(dis_ballGame2);
        selectSportsDisLike(dis_ballGame3);
        selectSportsDisLike(dis_ballGame4);
        selectSportsDisLike(dis_ballGame5);

        final TextView dis_racketSports1 = findViewById(R.id.dis_racketSports1);
        final TextView dis_racketSports2 = findViewById(R.id.dis_racketSports2);
        final TextView dis_racketSports3 = findViewById(R.id.dis_racketSports3);
        final TextView dis_racketSports4 = findViewById(R.id.dis_racketSports4);

        int r = 5;
        TextView dr[] = new TextView[r];
        String drC[] = new String[r];
        for(i=1; i<r; i++){ //반복문으로 레이아웃 참조하기
            int ra = getResources().getIdentifier("dis_racketSports"+i,"id",getPackageName());
            dr[i] = findViewById(ra);
            drC[i] = dr[i].getText().toString();
            System.out.println("drC["+i+"] :" + drC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (drC[i].equals(e)){
                    dr[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    dr[i].setTextColor(Color.BLACK);
                }
            }
        }

        selectSportsDisLike(dis_racketSports1);
        selectSportsDisLike(dis_racketSports2);
        selectSportsDisLike(dis_racketSports3);
        selectSportsDisLike(dis_racketSports4);

        final TextView dis_extremeSports1 = findViewById(R.id.dis_extremeSports1);
        final TextView dis_extremeSports2 = findViewById(R.id.dis_extremeSports2);
        final TextView dis_extremeSports3 = findViewById(R.id.dis_extremeSports3);
        final TextView dis_extremeSports4 = findViewById(R.id.dis_extremeSports4);

        int ex = 5;
        TextView dex[] = new TextView[ex];
        String dexC[] = new String[ex];
        for(i=1; i<ex; i++){ //반복문으로 레이아웃 참조하기
            int ext = getResources().getIdentifier("dis_extremeSports"+i,"id",getPackageName());
            dex[i] = findViewById(ext);
            dexC[i] = dex[i].getText().toString();
            System.out.println("dexC["+i+"] :" + dexC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (dexC[i].equals(e)){
                    dex[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    dex[i].setTextColor(Color.BLACK);
                }
            }
        }

        selectSportsDisLike(dis_extremeSports1);
        selectSportsDisLike(dis_extremeSports2);
        selectSportsDisLike(dis_extremeSports3);
        selectSportsDisLike(dis_extremeSports4);

        final TextView dis_waterSports1 = findViewById(R.id.dis_waterSports1);
        final TextView dis_waterSports2 = findViewById(R.id.dis_waterSports2);
        final TextView dis_waterSports3 = findViewById(R.id.dis_waterSports3);
        final TextView dis_waterSports4 = findViewById(R.id.dis_waterSports4);
        final TextView dis_waterSports5 = findViewById(R.id.dis_waterSports5);
        final TextView dis_waterSports6 = findViewById(R.id.dis_waterSports6);
        final TextView dis_waterSports7 = findViewById(R.id.dis_waterSports7);

        int wa = 8;
        TextView dwa[] = new TextView[wa];
        String dwaC[] = new String[wa];
        for(i=1; i<wa; i++){ //반복문으로 레이아웃 참조하기
            int wat = getResources().getIdentifier("dis_waterSports"+i,"id",getPackageName());
            dwa[i] = findViewById(wat);
            dwaC[i] = dwa[i].getText().toString();
            System.out.println("dwaC["+i+"] :" + dwaC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (dwaC[i].equals(e)){
                    dwa[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    dwa[i].setTextColor(Color.BLACK);
                }
            }
        }

        selectSportsDisLike(dis_waterSports1);
        selectSportsDisLike(dis_waterSports2);
        selectSportsDisLike(dis_waterSports3);
        selectSportsDisLike(dis_waterSports4);
        selectSportsDisLike(dis_waterSports5);
        selectSportsDisLike(dis_waterSports6);
        selectSportsDisLike(dis_waterSports7);


        final TextView dis_etcSports1 = findViewById(R.id.dis_etcSports1);
        final TextView dis_etcSports2 = findViewById(R.id.dis_etcSports2);
        final TextView dis_etcSports3 = findViewById(R.id.dis_etcSports3);
        final TextView dis_etcSports4 = findViewById(R.id.dis_etcSports4);
        final TextView dis_etcSports5 = findViewById(R.id.dis_etcSports5);
        final TextView dis_etcSports6 = findViewById(R.id.dis_etcSports6);
        final TextView dis_etcSports7 = findViewById(R.id.dis_etcSports7);
        final TextView dis_etcSports8 = findViewById(R.id.dis_etcSports8);

        int et = 9;
        TextView det[] = new TextView[et];
        String detC[] = new String[et];
        for(i=1; i<et; i++){ //반복문으로 레이아웃 참조하기
            int etc = getResources().getIdentifier("dis_etcSports"+i,"id",getPackageName());
            det[i] = findViewById(etc);
            detC[i] = det[i].getText().toString();
            System.out.println("detC["+i+"] :" + detC[i]);

            for(String e : likeS){ //for each문 : for(변수 : 반복 가능한 객체){ // 실행문 }
                if (detC[i].equals(e)){
                    det[i].setBackground(getResources().getDrawable(R.drawable.custom_button_3));
                    det[i].setTextColor(Color.BLACK);
                }
            }
        }

        selectSportsDisLike(dis_etcSports1);
        selectSportsDisLike(dis_etcSports2);
        selectSportsDisLike(dis_etcSports3);
        selectSportsDisLike(dis_etcSports4);
        selectSportsDisLike(dis_etcSports5);
        selectSportsDisLike(dis_etcSports6);
        selectSportsDisLike(dis_etcSports7);
        selectSportsDisLike(dis_etcSports8);


        Button test2_next = findViewById(R.id.choiceDisLike_finish);
        test2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String[] strData = like.toArray(new String[like.size()]);
                //String strData = stringUtils.join(like, ",");

                //sports_like(0,1로된 integer배열)을 string배열로 바꾼다.
                //string배열을 구분자를포함한 문자열로 바꾼다.
                //문자열을 서버로 보낸다.

                for(int i = 0; i < dislikeS.size(); i++) {
                    if(sportsA.contains(dislikeS.get(i))) {
                        //sportsA.set(sportsA.indexOf(like.get(i)),"1");
                        //int index = sportsA.indexOf(like.get(i));
                        //System.out.println("진짜로증말로인덱스 : "+ index);
                        sports_dislike.set(sportsA.indexOf(dislikeS.get(i)),-1);
                        //위에부분(sportsA.set(sportsA.indexOf(like.get(i)),"1");)과 같이하면 오류남 ,둘중하나만 실행해야함.
                    }
                }

//                System.out.println("sportsA 배ㅐㅐㅐㅐㅐ열ㄹㄹㄹㄹㄹ:" + sportsA.get(0));
//                System.out.println("like 배ㅐㅐㅐㅐㅐ열ㄹㄹㄹㄹㄹ:" + like.get(1));

                //String str = sports_like.toString(); //배열 자체를 문자로바꿈[ ] 포함

                //ArrayList like 에 담긴 항목을 구분자를 포함한 문자열로 바꿈
                StringBuilder sb = new StringBuilder();
                //for(String item: like) {
                for(Integer item: sports_dislike) {
                    if(sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(item);
                }
                strDataLike = sb.toString();

                /////////넘겨받은 likeS(좋아하는운동이름배열)을 문자열로바꿔 서버로 보낸다
                StringBuilder sbLikeS = new StringBuilder();
                for(String itemLikeS: likeS) {
                    if(sbLikeS.length() > 0) {
                        sbLikeS.append(',');
                    }
                    sbLikeS.append(itemLikeS);
                }
                strLikeS = sbLikeS.toString();

                /////////dislike(싫어하는운동이름배열)을 문자열로바꿔 서버로 보낸다
                StringBuilder sbDislikeS = new StringBuilder();
                for(String itemDislikeS: dislikeS) {
                    if(sbDislikeS.length() > 0) {
                        sbDislikeS.append(',');
                    }
                    sbDislikeS.append(itemDislikeS);
                }
                strDislikeS = sbDislikeS.toString();

                /////////MBTI(MBTI배열)을 문자열로바꿔 서버로 보낸다
                StringBuilder sbMBTI = new StringBuilder();
                for(String itemMBTI: MBTI) {
                    if(sbMBTI.length() > 0) {
                        sbMBTI.append(',');
                    }
                    sbMBTI.append(itemMBTI);
                }
                strMBTI = sbMBTI.toString();




                System.out.println("sportsA(전체종목이 있는 arrayList) :" + sportsA);
                System.out.println("dislike(선택한 항목의 arrayList) :" + dislikeS);
                System.out.println("sports_dislike(선택 1, 선택안함 0 으로표시한 arrayList) :" + sports_dislike);

                System.out.println("StringBuilder sb:"+ sb);
                System.out.println("String strDataLike = sb.toString():"+ strDataLike);


                 //모든클래스에서 접근이 가능하도록 MainActivity에서 public static String ID; 선언
                //String likeS = like.toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){//응답을 문자열로 받아서 여기다 넣어라 (응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            System.out.println("response"+ response);

                            if(success){//DB에 저장됐다면
                                finish();//액티비티를 종료시킴
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Choice_dislike.this);
                                dialog = builder.setMessage("Choice_like fail")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                //실제로 접속 할 수 있도록 Request 생성자를 통해 객체를 만들어 준다.
                VolleyR_Choice_likeRequest VolleyR_Choice_likeRequest = new VolleyR_Choice_likeRequest(ID, strDataLike, strLikeS, strDislikeS , strMBTI,responseListener);

                //요청을 보낼 수 있도록 큐를 만든다
                RequestQueue queue = Volley.newRequestQueue(Choice_dislike.this);

                //만들어진 큐에, Request 객체를 넣어준다.
                queue.add(VolleyR_Choice_likeRequest);
//
//
                Intent intent = new Intent(Choice_dislike.this, MainActivity.class);
                intent.putExtra("userID",ID);
//                //intent.putExtra("dislike_Int_Array",sports_dislike);
                Choice_dislike.this.startActivity(intent);
                finish();


//                System.out.println("ㅊㅊㅊㅊㅊㅊㅊㅊ");
//                System.out.println("Array:"+like);
//                System.out.println("String:"+strDataLike);
            }
        });

    }

    public void choiceDisLike(int IDNUM){
        final TextView test = findViewById(IDNUM);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                for(int i = 0; i < likeS.size(); i++) {
//                    if(test.equals(likeS.get(i))) {
//                        test.setBackgroundColor(Color.GRAY);
//                        System.out.println("넘겨받은 likeS중  (choiceDisL내부): " + likeS.get(i));  //
//                    } else{
//
//                    }
//                }

                if(test.getTextColors() == ColorStateList.valueOf(getResources().getColor(R.color.colorGray))) {
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button_2));
                    test.setTextColor(Color.WHITE);
                    //System.out.println("되는겨????????");
                    String id = getResources().getResourceEntryName(view.getId());
                    String contents = test.getText().toString();
                    System.out.println("버튼이름: "+id);
                    System.out.println("항목: "+contents);

                    dislikeS.add(contents);

                } else if(test.getTextColors() == ColorStateList.valueOf(Color.WHITE)){
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button));
                    test.setTextColor(getResources().getColor(R.color.colorGray));
                    String contents = test.getText().toString();
                    dislikeS.remove(contents);
                }
//                else if(test.getTextColors() == ColorStateList.valueOf(Color.BLACK)){
//                    test.setClickable(false);
//                }
            }
        });
    }

    public void selectSportsDisLike(View view){
        choiceDisLike(view.getId());
    }


}
