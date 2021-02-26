package com.example.doit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Hm_MainActivity extends AppCompatActivity implements OnTabItemSelectedListener{

    public static Handler handler;
    public static Intent serviceIntent;
    final Socket socket = Svc_MyService.socket;
    private OutputStream os;
    private DataOutputStream dos;

    BottomNavigationView hm_bottomNavigationView;

    //프래그먼트 변수 선언
    HmChatFragment hm_chatFragment;
    HmMyClassFragment hm_myClassFragment;
    HmUserFragment hm_userFragment;

    public static String ID; //모든클래스에서 접근이 가능하도록
    public static String myf_name; //모든클래스에서 접근이 가능하도록
    public static String myf_img; //모든클래스에서 접근이 가능하도록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_activity_main);

        Stetho.initializeWithDefaults(this);

        ID = getIntent().getExtras().getString("userID");
        myf_name = getIntent().getExtras().getString("facility_name");
        myf_img = getIntent().getExtras().getString("facility_image");
        Log.e("ID:",ID);

        handler = new Handler();

        //서비스 시작
        if (Svc_MyService.serviceIntent==null) {
            serviceIntent = new Intent(this, Svc_MyService.class);
            //startService(serviceIntent);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        } else {
            serviceIntent = Svc_MyService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

        //서버로 ID 보낸다
        //sendMessage(ID);

        boolean isNoti = getIntent().getBooleanExtra("isNoti",false);

        hm_chatFragment = new HmChatFragment();
        hm_myClassFragment = new HmMyClassFragment();
        hm_userFragment = new HmUserFragment();

        if(isNoti) { //노티->채팅->메인으로 왔을때
            String HM_ID = getIntent().getStringExtra("userID");
            getSupportFragmentManager().beginTransaction().replace(R.id.hm_container,  hm_chatFragment).commit();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isNoti",true);
            bundle.putString("userID",HM_ID);
            hm_chatFragment.setArguments(bundle);
            System.out.println("bundle : "+bundle);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.hm_container, hm_chatFragment).commit();
        }

        //프래그먼트 매니저를 통해 만들어 놓은 각각의 프래그먼트를 현 액티비티의 프레임에이아웃안에 추가되도록 함
        //replace(a,b)->a를 b로 대체하라
        //트랜젝션 안에서 replace되므로 마지막에 commt해줘야한다
        //Transaction:거래,ㅐ매매,처리(과정) , commit : 저지르다, 약속하다

        //hm_bottom_menu 아이템의 id값으로 버튼을 구분
        hm_bottomNavigationView = findViewById(R.id.hm_bottom_navigation);
        hm_bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.hm_chat :
                                getSupportFragmentManager().beginTransaction().replace(R.id.hm_container, hm_chatFragment).commit();
                                return true;

                            case R.id.hm_myClass:
                                //Toast.makeText(getApplicationContext(),"2번탭",Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.hm_container,hm_myClassFragment).commit();
                                return true;

                            case R.id.hm_user:
                                //Toast.makeText(getApplicationContext(),"3번탭",Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.hm_container,hm_userFragment).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );
    }

//    public void sendMessage(final String message) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    os = socket.getOutputStream();
//                    dos = new DataOutputStream(os);
//                    dos.writeUTF(message);
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


    @Override
    public void onTabSelected(int position) {
        if(position==0){
            hm_bottomNavigationView.setSelectedItemId(R.id.hm_chat);
        } else if(position==1){
            hm_bottomNavigationView.setSelectedItemId(R.id.hm_myClass);
        } else if(position==2){
            hm_bottomNavigationView.setSelectedItemId(R.id.hm_user);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) {
            //stopService(serviceIntent);
            serviceIntent = null;
        }
        //ID=null;
    }

}
