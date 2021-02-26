package com.example.doit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {


    public static Handler handler;

    public static Intent  serviceIntent;
    final Socket socket = Svc_MyService.socket;
    private OutputStream os;
    private DataOutputStream dos;

    BottomNavigationView bottomNavigationView;

    //프래그먼트 변수 선언
    RecommendFragment recommendFragment;
    FindClassFragment findClassFragment;
    UserFragment userFragment;
    GMchatListFragment gMchatListFragment;
    private List<DTO_HmFacility> fInfoLists = new ArrayList<>(); //리싸이클러뷰 아답터에 넣을 리스트
    RestAPI restAPI;


    public static String ID; //모든클래스에서 접근이 가능하도록


    //카카오 해쉬키 발행과정
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        //getHashKey(); //해쉬키 받기
        ID = getIntent().getStringExtra("userID");

        handler = new Handler();

        //서비스 시작
        if (Svc_MyService.serviceIntent==null) {
            serviceIntent = new Intent(this, Svc_MyService.class);
            //startService(serviceIntent);
            //startForegroundService(serviceIntent);

//            if (Build.VERSION.SDK_INT >= 26) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        } else {
            serviceIntent = Svc_MyService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

        //socket = Svc_MyService.socket;
        //서버로 ID 보낸다
        //sendMessage(ID);



        String strResult = getIntent().getStringExtra("strResult");
        boolean isNoti = getIntent().getBooleanExtra("isNoti",false);

        //객체를 만들어 변수에 할당
        recommendFragment = new RecommendFragment();
        findClassFragment = new FindClassFragment();
        userFragment = new UserFragment();
        gMchatListFragment = new GMchatListFragment();

        if(strResult != null){
            System.out.println("strResult : "+strResult);

            getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, findClassFragment).commit();

            //Activity->Fragment 데이터전달할땐 Bundle사용
            Bundle bundle = new Bundle();
            bundle.putString("strResult",strResult);
            findClassFragment.setArguments(bundle);
            System.out.println("bundle : "+bundle);

        } else if(isNoti) { //노티->채팅->메인으로 왔을때
            String GM_ID = getIntent().getStringExtra("userID");
            getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, gMchatListFragment).commit();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isNoti",true);
            bundle.putString("userID",GM_ID);
            gMchatListFragment.setArguments(bundle);
            System.out.println("bundle : "+bundle);
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, recommendFragment).commit();
        }


        //FragmenManager객체를 이용해 척번째 프레임에이아웃안에 추가되도록 함
        //testFragment를 R.id.container에 넣어라(replace->대체함, add->추가함)
        //트랜잭션 안에서 수행되는것이므로 마지막에 꼭 commit 해야함
//        getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, recommendFragment).commit();

        //하단탭에 OnNavigationItemSelectedListener 설정
        //OnNavigationItemSelectedListener->하단탭에 들어있는 각각의 버튼을 눌렀을때 OnNavigationItemSelectedListener()메서드가 자동으로 호출됨
        //메뉴아이템의 id값으로 버튼을 구분
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.sportsRecommend:
                        //Toast.makeText(getApplicationContext(),"1번탭",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, recommendFragment).commit();
                        return true;
                    case R.id.findClass:
                        //Toast.makeText(getApplicationContext(),"2번탭",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.gm_container, findClassFragment).commit();
                        return true;
                    case R.id.chatList:
                        //Toast.makeText(getApplicationContext(),"3번탭",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.gm_container,gMchatListFragment).commit();
                        return true;
                    case R.id.user:
                        //Toast.makeText(getApplicationContext(),"3번탭",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.gm_container,userFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    os = socket.getOutputStream();
                    dos = new DataOutputStream(os);
                    dos.writeUTF(message);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //OnTabItemSelectedListener 인터페이스는 하나의 프래그먼트에서 다른 프래그먼트로 전환하는 용도로 사용
    //OnTabItemSelectedListener 인터페이스에 onTabSelected()메서드 선언되어있음
    //setSelected()를 이용해 다른탭이 선택됨
    @Override
    public void onTabSelected(int position){
        if (position == 0) {
            bottomNavigationView.setSelectedItemId(R.id.sportsRecommend);
        } else if (position == 1) {
            bottomNavigationView.setSelectedItemId(R.id.findClass);
        } else if (position == 2) {
            bottomNavigationView.setSelectedItemId(R.id.chatList);
        } else if (position == 3) {
            bottomNavigationView.setSelectedItemId(R.id.user);
        }
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.gm_container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
//    }

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
