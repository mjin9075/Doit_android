package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindClassDetail extends AppCompatActivity implements OnMapReadyCallback {

    private String GM_ID,HM_ID,f_img;
    private String IMG_BASE_URL ;

    private TextView FCD_f_name;
    private ImageView FCD_f_img;
    private TextView FCD_f_intro;
    private TextView FCD_f_bh;
    private TextView FCD_f_con;
    private TextView FCD_f_etc;
    private TextView FCD_f_add;

    private String f_name;


    private GoogleMap gMap;
    private Geocoder geocoder;

    private Button FCD_f_goChat;
    private String f_add_full;

    RestAPI restAPI;
    private String ROOM_N;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_class_detail);

        GM_ID = MainActivity.ID;

        IMG_BASE_URL = "서버URL";

        FCD_f_name = findViewById(R.id.FCD_f_name);
        FCD_f_img = findViewById(R.id.FCD_f_img);
        FCD_f_intro = findViewById(R.id.FCD_f_intro);
        FCD_f_bh = findViewById(R.id.FCD_f_bh);
        FCD_f_con = findViewById(R.id.FCD_f_con);
        FCD_f_etc = findViewById(R.id.FCD_f_etc);
        FCD_f_add = findViewById(R.id.FCD_f_add);

        FCD_f_goChat = findViewById(R.id.FCD_f_goChat);


        //Bundle extras = getIntent().getExtras();

        HM_ID = getIntent().getStringExtra("HM_ID");
        f_name = getIntent().getStringExtra("f_name");
        f_img = getIntent().getStringExtra("f_img");
        String f_intro = getIntent().getStringExtra("f_intro");
        String f_bh = getIntent().getStringExtra("f_bh");

        String f_con = getIntent().getStringExtra("f_con");
        String f_etc = getIntent().getStringExtra("f_etc");
        String f_add = getIntent().getStringExtra("f_add");
        String f_add_d = getIntent().getStringExtra("f_add_d");


        FCD_f_name.setText(f_name);
        FCD_f_intro.setText(f_intro);
        FCD_f_bh.setText(f_bh);
        FCD_f_con.setText(f_con);
        FCD_f_etc.setText(f_etc);

        String imgURL = IMG_BASE_URL+f_img;
        Glide.with(this).load(imgURL).into(FCD_f_img);

        f_add_full = f_add+f_add_d;

        FCD_f_add.setText(f_add_full);

        //지도에 표시
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.FCD_f_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //상담하기(채팅)
        //상담하기 누르면 채팅방 생성 및 입장 (채팅방은 고유하게-DB시퀀스넘버를 이용해 해쉬맵으로 )
        FCD_f_goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("GM : "+ GM_ID);
                System.out.println("HM : "+ HM_ID);

                restAPI = RestRequestHelper.getClient().create(RestAPI.class);
                Call<DTO_ChatRoom> postCall = restAPI.checkChatRoom(GM_ID,HM_ID);
                postCall.enqueue(new Callback<DTO_ChatRoom>() {
                    @Override
                    public void onResponse(Call<DTO_ChatRoom> call, Response<DTO_ChatRoom> response) {
                        DTO_ChatRoom DTO_ChatRoom = response.body();
                        ROOM_N = DTO_ChatRoom.getROOM_N();
                        System.out.println("ROOM_N : "+ ROOM_N);

                        if(response.isSuccessful()) { //응답답

                            Intent intent = new Intent(FindClassDetail.this,GM_Chat.class);
                            intent.putExtra("name",f_name);
                            intent.putExtra("GM_ID", GM_ID);
                            intent.putExtra("HM_ID",HM_ID);
                            intent.putExtra("ROOM_N",ROOM_N);
                            intent.putExtra("isList",false);
                            intent.putExtra("isNoti",false);
                            intent.putExtra("Iam","GM");

                            intent.putExtra("my_name",GM_ID);
                            intent.putExtra("my_img","");

                            intent.putExtra("f_img",f_img);

                            System.out.println("GoChat : "+ GM_ID +"///"+HM_ID+"///"+ROOM_N);
                            startActivity(intent);

                        }
                        else {
                            //통신이 실패한 경우 (응답토그 3xx, 4xx등)
                            //onResponse가 무조건 성공응답이 아니기에 확인필요
                            Toast.makeText(FindClassDetail.this,"ROOM_N 확인실패",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<DTO_ChatRoom> call, Throwable t) {
                        Log.e("@@@@@@@@@@@@@@@xxxx", t.toString());
                        Toast.makeText(FindClassDetail.this, "네트워크 에러", Toast.LENGTH_SHORT);

                    }
                });

//                Intent intent = new Intent(FindClassDetail.this,GM_Chat.class);
//                intent.putExtra("f_name",f_name);
//                intent.putExtra("userID",ID);
//                Log.e("GoChat_ID",ID);
//                startActivity(intent);

            }
        });

    } //onCreate 끝

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        geocoder = new Geocoder(this);

        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocationName(f_add_full,10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("지도변환","주소변환시 에러발생");
        }

        if(addressList != null) {
            if(addressList.size()==0) {
                Toast.makeText(this,"해당되는 주소정보가 없습니다.",Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this,addressList.get(0).toString(),Toast.LENGTH_LONG).show();

                String []splitStr = addressList.get(0).toString().split(",");
                //주소값 분리하기
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") +1 , splitStr[0].length()-2);
                //위도 분리하기
                String latitude = splitStr[10].substring(splitStr[10].indexOf("=")+1);
                //경도 분리하기
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=")+1);

                //(위도,경도)좌표 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                //마커 생성
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(f_name);
                markerOptions.snippet(address);
                markerOptions.position(point);

                //마커추가
                gMap.addMarker(markerOptions);

                //해당좌표로 줌
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

            }
        }

    }
}
