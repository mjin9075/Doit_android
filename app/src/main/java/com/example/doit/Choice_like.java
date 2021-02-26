package com.example.doit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Callback;

public class Choice_like extends AppCompatActivity  {

    String[] sports = new String[]{"복싱_킥복싱","무에타이","시스테마","유도","주짓수","카포에라","크라브마가", "태권도","택견","합기도",
            "골프","농구","당구_포켓볼","볼링","축구",
            "배드민턴","스쿼시","탁구","테니스",
            "스케이트보드_롱보드","인라인스케이트","클라이밍","트릭킹",
            "딩기요트","서핑","수영","수상스키_웨이크보드","윈드서핑","프리다이빙","패들보드",
            "발레","사격","승마","양궁","요가","저글링","폴댄스","필라테스"
    };
    //개인, 팀, 대인, 정,동,역동, 내, 외, 구기, 수상, 라켓, 맨몸, 도구, +)빠른반응, 전략, 관찰
    //외부-역동(E) 내부-정(I) /감각-관찰(S) 직관-예측(N) /사고-전략(T) 감정-(F) /판단-(J) 인식-(P)

    String[] s0 = new String[]{"개인","대인","역동","내","도구"}; //복식 킥복싱
    String[] s1 = new String[]{"개인","대인","역동","내","맨몸"}; //무에타이
    String[] s2 = new String[]{"개인","대인","역동","내","맨몸"}; //시스테마
    String[] s3 = new String[]{"개인","대인","역동","내","맨몸"}; //유도
    String[] s4 = new String[]{"개인","대인","역동","내","맨몸"}; //주짓수
    String[] s5 = new String[]{"개인","대인","역동","내","맨몸"}; //카포에라
    String[] s6 = new String[]{"개인","대인","역동","내","맨몸"}; //크리브마가
    String[] s7 = new String[]{"개인","대인","역동","내","맨몸"}; //태권도
    String[] s8 = new String[]{"개인","대인","역동","내","맨몸"}; //택견
    String[] s9 = new String[]{"개인","대인","역동","내","맨몸"}; //합기도

    String[] s10 = new String[]{"개인","동","내","외","구기","도구"}; //골프
    String[] s11 = new String[]{"팀","역동","내","외","구기","도구"}; //농구
    String[] s12 = new String[]{"개인","정","내","구기","도구"}; //당구 포켓볼
    String[] s13 = new String[]{"개인","팀","동","내","구기","도구"}; //볼링
    String[] s14 = new String[]{"팀","역동","외","구기","도구"}; //축구

    String[] s15 = new String[]{"개인","팀","대인","동","역동","내","외","라켓","도구"}; //배드민턴
    String[] s16 = new String[]{"개인","대인","동","역동","내","라켓","도구"}; //스쿼시
    String[] s17 = new String[]{"개인","팀","대인","동","역동","내","라켓","도구"}; //탁구
    String[] s18 = new String[]{"개인","팀","대인","동","역동","내","외","라켓","도구"}; //테니스

    String[] s19 = new String[]{"개인","동","외","도구"}; //스케이트보드_롱보드
    String[] s20 = new String[]{"개인","동","외","도구"}; //인라인스케이트
    String[] s21 = new String[]{"개인","동","내","외","맨몸","도구"}; //클라이밍 //인공암벽등반
    String[] s22 = new String[]{"개인","동","역동","내","외","맨몸"}; //트릭킹

    String[] s23 = new String[]{"개인","동","외","수상","도구"}; //딩기요트
    String[] s24 = new String[]{"개인","동","역동","내","외","수상","도구"}; //서핑
    String[] s25 = new String[]{"개인","동","역동","내","외","수상","맨몸"}; //수영
    String[] s26 = new String[]{"개인","동","역동","내","수상","도구"}; //수상스키_웨이크보드
    String[] s27 = new String[]{"개인","동","역동","내","수상","도구"}; //윈드서핑
    String[] s28 = new String[]{"개인","동","내","외","수상","맨몸","도구"}; //프리다이빙
    String[] s29 = new String[]{"개인","동","외","수상","도구"}; //패들보드

    String[] s30 = new String[]{"개인","정","동","내","맨몸"}; //발레
    String[] s31 = new String[]{"개인","정","내","외","도구"}; //사격
    String[] s32 = new String[]{"개인","동","역동","내","외","도구"}; //승마
    String[] s33 = new String[]{"개인","정","내","외","도구"}; //양궁
    String[] s34 = new String[]{"개인","정","동","내","맨몸"}; //요가
    String[] s35 = new String[]{"개인","동","내","도구"}; //저글링
    String[] s36 = new String[]{"개인","동","역동","내","도구"}; //폴댄스
    String[] s37 = new String[]{"개인","정","동","내","맨몸","도구"}; //필라테스

    ArrayList<String> ss0 = new ArrayList<>(Arrays.asList(s0));
    ArrayList<String> ss1 = new ArrayList<>(Arrays.asList(s1));
    ArrayList<String> ss2 = new ArrayList<>(Arrays.asList(s2));
    ArrayList<String> ss3 = new ArrayList<>(Arrays.asList(s3));
    ArrayList<String> ss4 = new ArrayList<>(Arrays.asList(s4));
    ArrayList<String> ss5 = new ArrayList<>(Arrays.asList(s5));
    ArrayList<String> ss6 = new ArrayList<>(Arrays.asList(s6));
    ArrayList<String> ss7 = new ArrayList<>(Arrays.asList(s7));
    ArrayList<String> ss8 = new ArrayList<>(Arrays.asList(s8));
    ArrayList<String> ss9 = new ArrayList<>(Arrays.asList(s9));
    ArrayList<String> ss10 = new ArrayList<>(Arrays.asList(s10));
    ArrayList<String> ss11 = new ArrayList<>(Arrays.asList(s11));
    ArrayList<String> ss12 = new ArrayList<>(Arrays.asList(s12));
    ArrayList<String> ss13 = new ArrayList<>(Arrays.asList(s13));
    ArrayList<String> ss14 = new ArrayList<>(Arrays.asList(s14));
    ArrayList<String> ss15 = new ArrayList<>(Arrays.asList(s15));
    ArrayList<String> ss16 = new ArrayList<>(Arrays.asList(s16));
    ArrayList<String> ss17 = new ArrayList<>(Arrays.asList(s17));
    ArrayList<String> ss18 = new ArrayList<>(Arrays.asList(s18));
    ArrayList<String> ss19 = new ArrayList<>(Arrays.asList(s19));
    ArrayList<String> ss20 = new ArrayList<>(Arrays.asList(s20));
    ArrayList<String> ss21 = new ArrayList<>(Arrays.asList(s21));
    ArrayList<String> ss22 = new ArrayList<>(Arrays.asList(s22));
    ArrayList<String> ss23 = new ArrayList<>(Arrays.asList(s23));
    ArrayList<String> ss24 = new ArrayList<>(Arrays.asList(s24));
    ArrayList<String> ss25 = new ArrayList<>(Arrays.asList(s25));
    ArrayList<String> ss26 = new ArrayList<>(Arrays.asList(s26));
    ArrayList<String> ss27 = new ArrayList<>(Arrays.asList(s27));
    ArrayList<String> ss28 = new ArrayList<>(Arrays.asList(s28));
    ArrayList<String> ss29 = new ArrayList<>(Arrays.asList(s29));
    ArrayList<String> ss30 = new ArrayList<>(Arrays.asList(s30));
    ArrayList<String> ss31 = new ArrayList<>(Arrays.asList(s31));
    ArrayList<String> ss32 = new ArrayList<>(Arrays.asList(s32));
    ArrayList<String> ss33 = new ArrayList<>(Arrays.asList(s33));
    ArrayList<String> ss34 = new ArrayList<>(Arrays.asList(s34));
    ArrayList<String> ss35 = new ArrayList<>(Arrays.asList(s35));
    ArrayList<String> ss36 = new ArrayList<>(Arrays.asList(s36));
    ArrayList<String> ss37 = new ArrayList<>(Arrays.asList(s37));


    ArrayList<String> sportsA = new ArrayList<>(Arrays.asList(sports)); //string[] to ArrayList
    ArrayList<Integer> sports_like = new ArrayList<>(); //0,1로만들 배열
    ArrayList<String> like = new ArrayList<>(); //사용자가 선택한 항목의 배열
    ArrayList<String> like_new = new ArrayList<>(); //사용자가 좋아한 항목의 feature 값만 저장
    ArrayList<Integer> feature_count = new ArrayList<>(); //각 feature가 포함된 횟수

    ArrayList<String> MBTI = new ArrayList<>();
    private String ID;


    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_like);

        ID = getIntent().getStringExtra("userID");
        MBTI = getIntent().getStringArrayListExtra("MBTI");


        //sportsA 배열과 크기가 같은 integer 배열 -> 0으로 채움
        int i = 0;
        while (i<sportsA.size()) {
            sports_like.add(0);
            i++;
        }
        //System.out.println("몇개일까요 : "+sports_like);


        final TextView martialArts2 = findViewById(R.id.martialArts2);
        final TextView martialArts3 = findViewById(R.id.martialArts3);
        final TextView martialArts4 = findViewById(R.id.martialArts4);
        final TextView martialArts5 = findViewById(R.id.martialArts5);
        final TextView martialArts6 = findViewById(R.id.martialArts6);
        final TextView martialArts7 = findViewById(R.id.martialArts7);
        final TextView martialArts8 = findViewById(R.id.martialArts8);
        final TextView martialArts9 = findViewById(R.id.martialArts9);
        final TextView martialArts10 = findViewById(R.id.martialArts10);
        final TextView martialArts11 = findViewById(R.id.martialArts11);

        selectSportsLike(martialArts2);
        selectSportsLike(martialArts3);
        selectSportsLike(martialArts4);
        selectSportsLike(martialArts5);
        selectSportsLike(martialArts6);
        selectSportsLike(martialArts7);
        selectSportsLike(martialArts8);
        selectSportsLike(martialArts9);
        selectSportsLike(martialArts10);
        selectSportsLike(martialArts11);


        final TextView ballGame1 = findViewById(R.id.ballGame1);
        final TextView ballGame2 = findViewById(R.id.ballGame2);
        final TextView ballGame3 = findViewById(R.id.ballGame3);
        final TextView ballGame4 = findViewById(R.id.ballGame4);
        final TextView ballGame5 = findViewById(R.id.ballGame5);

        selectSportsLike(ballGame1);
        selectSportsLike(ballGame2);
        selectSportsLike(ballGame3);
        selectSportsLike(ballGame4);
        selectSportsLike(ballGame5);


        final TextView racketSports1 = findViewById(R.id.racketSports1);
        final TextView racketSports2 = findViewById(R.id.racketSports2);
        final TextView racketSports3 = findViewById(R.id.racketSports3);
        final TextView racketSports4 = findViewById(R.id.racketSports4);

        selectSportsLike(racketSports1);
        selectSportsLike(racketSports2);
        selectSportsLike(racketSports3);
        selectSportsLike(racketSports4);


        final TextView extremeSports1 = findViewById(R.id.extremeSports1);
        final TextView extremeSports2 = findViewById(R.id.extremeSports2);
        final TextView extremeSports3 = findViewById(R.id.extremeSports3);
        final TextView extremeSports4 = findViewById(R.id.extremeSports4);

        selectSportsLike(extremeSports1);
        selectSportsLike(extremeSports2);
        selectSportsLike(extremeSports3);
        selectSportsLike(extremeSports4);


        final TextView waterSports1 = findViewById(R.id.waterSports1);
        final TextView waterSports2 = findViewById(R.id.waterSports2);
        final TextView waterSports3 = findViewById(R.id.waterSports3);
        final TextView waterSports4 = findViewById(R.id.waterSports4);
        final TextView waterSports5 = findViewById(R.id.waterSports5);
        final TextView waterSports6 = findViewById(R.id.waterSports6);
        final TextView waterSports7 = findViewById(R.id.waterSports7);

        selectSportsLike(waterSports1);
        selectSportsLike(waterSports2);
        selectSportsLike(waterSports3);
        selectSportsLike(waterSports4);
        selectSportsLike(waterSports5);
        selectSportsLike(waterSports6);
        selectSportsLike(waterSports7);


        final TextView etcSports1 = findViewById(R.id.etcSports1);
        final TextView etcSports2 = findViewById(R.id.etcSports2);
        final TextView etcSports3 = findViewById(R.id.etcSports3);
        final TextView etcSports4 = findViewById(R.id.etcSports4);
        final TextView etcSports5 = findViewById(R.id.etcSports5);
        final TextView etcSports6 = findViewById(R.id.etcSports6);
        final TextView etcSports7 = findViewById(R.id.etcSports7);
        final TextView etcSports8 = findViewById(R.id.etcSports8);

        selectSportsLike(etcSports1);
        selectSportsLike(etcSports2);
        selectSportsLike(etcSports3);
        selectSportsLike(etcSports4);
        selectSportsLike(etcSports5);
        selectSportsLike(etcSports6);
        selectSportsLike(etcSports7);
        selectSportsLike(etcSports8);

        System.out.println("like_newㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ누르기전전 : "+like);

        Button test2_next = findViewById(R.id.choiceLike_next);
        test2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String[] strData = like.toArray(new String[like.size()]);
                //String strData = stringUtils.join(like, ",");

                //sports_like(0,1로된 integer배열)을 string배열로 바꾼다.
                //string배열을 구분자를포함한 문자열로 바꾼다.
                //문자열을 서버로 보낸다.

                //사용자가 좋아하는것의배열(like)의 항목이 전체운동종목배열(sportsA)의 몇번째에 있는지 인덱스를 가져와 sportsA와 길이가 같고 0으로 채워진 배열의 해당 인덱스의 값을 1로저장
                for(int i = 0; i < like.size(); i++) {
                        if(sportsA.contains(like.get(i))) {
                            //sportsA.set(sportsA.indexOf(like.get(i)),"1");
                            //int index = sportsA.indexOf(like.get(i));
                            //System.out.println("진짜로증말로인덱스 : "+ index);
                            sports_like.set(sportsA.indexOf(like.get(i)),1);
                            //위에부분(sportsA.set(sportsA.indexOf(like.get(i)),"1");)과 같이하면 오류남 ,둘중하나만 실행해야함.
                        }
               }

                //좋아요 표시한 item의 feature를 배열로 담음
                for(int i =0; i<like.size(); i++) {
                    for(String e : like){
                        if(like.get(i).equals(e)){
                            switch (e) {
                                case "복싱_킥복싱":
                                    like_new.addAll(ss0);
                                    break;
                                case "무에타이":
                                    like_new.addAll(ss1);
                                    break;
                                case "시스테마":
                                    like_new.addAll(ss2);
                                    break;
                                case "유도":
                                    like_new.addAll(ss3);
                                    break;
                                case "주짓수":
                                    like_new.addAll(ss4);
                                    break;
                                case "카포에라":
                                    like_new.addAll(ss5);
                                    break;
                                case "크라브마가":
                                    like_new.addAll(ss6);
                                    break;
                                case  "태권도":
                                    like_new.addAll(ss7);
                                    break;
                                case "택견":
                                    like_new.addAll(ss8);
                                    break;
                                case "합기도":
                                    like_new.addAll(ss9);
                                    break;
                                case "골프":
                                    like_new.addAll(ss10);
                                    break;
                                case "농구":
                                    like_new.addAll(ss11);
                                    break;
                                case "당구_포켓볼":
                                    like_new.addAll(ss12);
                                    break;
                                case "볼링":
                                    like_new.addAll(ss13);
                                    break;
                                case "축구":
                                    like_new.addAll(ss14);
                                    break;
                                case "배드민턴":
                                    like_new.addAll(ss15);
                                    break;
                                case "스쿼시":
                                    like_new.addAll(ss16);
                                    break;
                                case "탁구":
                                    like_new.addAll(ss17);
                                    break;
                                case "테니스":
                                    like_new.addAll(ss18);
                                    break;
                                case "스케이트보드_롱보드":
                                    like_new.addAll(ss19);
                                    break;
                                case "인라인스케이트":
                                    like_new.addAll(ss20);
                                    break;
                                case "클라이밍":
                                    like_new.addAll(ss21);
                                    break;
                                case "트릭킹":
                                    like_new.addAll(ss22);
                                    break;
                                case "딩기요트":
                                    like_new.addAll(ss23);
                                    break;
                                case "서핑":
                                    like_new.addAll(ss24);
                                    break;
                                case "수영":
                                    like_new.addAll(ss25);
                                    break;
                                case "수상스키_웨이크보드":
                                    like_new.addAll(ss26);
                                    break;
                                case "윈드서핑":
                                    like_new.addAll(ss27);
                                    break;
                                case "프리다이빙":
                                    like_new.addAll(ss28);
                                    break;
                                case "패들보드":
                                    like_new.addAll(ss29);
                                    break;
                                case "발레":
                                    like_new.addAll(ss30);
                                    break;
                                case "사격":
                                    like_new.addAll(ss31);
                                    break;
                                case "승마":
                                    like_new.addAll(ss32);
                                    break;
                                case "양궁":
                                    like_new.addAll(ss33);
                                    break;
                                case "요가":
                                    like_new.addAll(ss34);
                                    break;
                                case "저글링":
                                    like_new.addAll(ss35);
                                    break;
                                case "폴댄스":
                                    like_new.addAll(ss36);
                                    break;
                                case "필라테스":
                                    like_new.addAll(ss37);
                                    break;
                            }
                        }
                    }
                }

                System.out.println("like_newㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ : "+like_new);
                //feature : 개인, 팀, 대인, 정,동,역동, 내, 외, 구기, 수상, 라켓, 맨몸, 도구,

                int feature_0 = Collections.frequency(like_new,"개인");
                int feature_1 = Collections.frequency(like_new,"팀");
                int feature_2 = Collections.frequency(like_new,"대인");
                int feature_3 = Collections.frequency(like_new,"정");
                int feature_4 = Collections.frequency(like_new,"동");
                int feature_5 = Collections.frequency(like_new,"역동");
                int feature_6 = Collections.frequency(like_new,"내");
                int feature_7 = Collections.frequency(like_new,"외");
                int feature_8 = Collections.frequency(like_new,"구기");
                int feature_9 = Collections.frequency(like_new,"수상");
                int feature_10 = Collections.frequency(like_new,"라켓");
                int feature_11 = Collections.frequency(like_new,"멘몸");
                int feature_12 = Collections.frequency(like_new,"도구");

                feature_count.add(0,feature_0);
                feature_count.add(1,feature_1);
                feature_count.add(2,feature_2);
                feature_count.add(3,feature_3);
                feature_count.add(4,feature_4);
                feature_count.add(5,feature_5);
                feature_count.add(6,feature_6);
                feature_count.add(7,feature_7);
                feature_count.add(8,feature_8);
                feature_count.add(9,feature_9);
                feature_count.add(10,feature_10);
                feature_count.add(11,feature_11);
                feature_count.add(12,feature_12);

                System.out.println("feature_count : "+feature_count);
                //System.out.println("수상 : "+feature_2);


                //ArrayList like 에 담긴 항목을 구분자를 포함한 문자열로 바꿈
                StringBuilder sb = new StringBuilder();
                //for(String item: like) {
                for(Integer item: sports_like) {
                    if(sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(item);
                }

                String strDataLike = sb.toString();

//                System.out.println("sportsA(전체종목이 있는 arrayList) :" + sportsA);
//                System.out.println("like(선택한 항목의 arrayList) :" + like);
//                System.out.println("sports_like(선택 1, 선택안함 0 으로표시한 arrayList) :" + sports_like);
//
//                System.out.println("StringB:"+ sb);
//                System.out.println("String:"+ strDataLike);

                Intent intent = new Intent(Choice_like.this, Choice_dislike.class);
                intent.putExtra("likeS",like);
                intent.putExtra("like_Int_Array",sports_like);

                intent.putExtra("MBTI",MBTI);
                System.out.println("cho_like_mbti : "+MBTI);

                intent.putExtra("userID",ID);

                Choice_like.this.startActivity(intent);
                finish();
//                System.out.println("Array:"+like);
//                System.out.println("String:"+strDataLike);
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

                    //System.out.println("되는겨????????");
                    String id = getResources().getResourceEntryName(view.getId());
                    String contents = test.getText().toString();
                    System.out.println("버튼이름: "+id);
                    //System.out.println("항목: "+contents);

                    like.add(contents);


                } else if(test.getTextColors() == ColorStateList.valueOf(Color.WHITE)){
                    test.setBackground(getResources().getDrawable(R.drawable.custom_button));
                    test.setTextColor(getResources().getColor(R.color.colorGray));
                    String contents = test.getText().toString();

                    like.remove(contents);
                }

              //  ArrayList를 JSON으로
//                try {
//                    //JSONArray jsonArray = new JSONArray(); //배열
//                   // for (int i=0; i<like.size(); i++) {
//                        JSONObject jsonObject = new JSONObject(); //배열내에들어갈 JSON
//                        jsonObject.put("like",like);
//                        jsonArray.put(jsonObject);
//                   // }
//                    Log.d("JSOM TESTㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ",jsonArray.toString());
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
            }
        });


    }

    public void selectSportsLike(View view){

        choiceLike(view.getId());
    }


//    @Override
//    public void onCheckedChanged(CompoundButton textView, boolean isChecked) {
//        if(textView.getId() == R.id.textView3) {
//            if(isChecked) {
//                textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            } else {
//                textView.setBackgroundColor(getResources().getColor(R.color.colorGray));
//            }
//
//        }
//    }
}
