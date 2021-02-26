package com.example.doit;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Svc_MyService extends Service {

    MainActivity ma = new MainActivity();

    private static final String TAG = "MyService";

    private Thread mainThread;
    public static Intent serviceIntent = null;
    // 앱이 다시 실행되었을때 충돌을 방지하기 위해  static변수로 두고고비스 실행시 intent를 가지고 있도록 한다.

    public static Socket socket;
    private InputStream is;
    private DataInputStream dis;
    private OutputStream os;
    private DataOutputStream dos;
    private String ID;

    private Handler handler;
    private List<DTO_Chat> mChatDataList;
    private Adapter_Chat adapter_Chat;

    String inRoom_n,senderID,msg,time,time_d;
    String my_name,my_img,Iam;

    String f_name;
    String f_img;


    RestAPI restAPI;

    private DB_Chat_DBHelper dbHelper;


    StringTokenizer st;

    private String My_Room;//내가 있는 방 이름

    private final String ip = "IP";
    private int port = 9999; // PORT번호


    public Svc_MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand() 호출됨");


        //handler = MainActivity.handler;
        if(MainActivity.handler != null){
            handler = MainActivity.handler;
        } else {
            handler = Hm_MainActivity.handler;
        }

        //db헬퍼
        dbHelper = new DB_Chat_DBHelper(this);

        //mChatDataList = GM_Chat.mChatDataList;
        //adapter_Chat = GM_Chat.adapter_Chat;
        serviceIntent = intent;

        if(MainActivity.ID != null){
            ID = MainActivity.ID;
            initializeNotification_GM();
        } else {
            ID = Hm_MainActivity.ID;
            initializeNotification_HM();
        }

        showToast(getApplication(),"Start Service");

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");

                //socket = new Socket();
                Log.e("service_thread","start");
                try {
                    Log.e("소켓연결시도","시도중");
                    socket = new Socket(ip, port);

                    if(socket != null) //서버에 정상적으로 접속했다면
                    {
                        try {
                            is = socket.getInputStream();
                            dis = new DataInputStream(is);
                            os = socket.getOutputStream();
                            dos = new DataOutputStream(os);

                            if(MainActivity.ID != null){
                                ID = MainActivity.ID;
                            } else {
                                ID = Hm_MainActivity.ID;
                            }
                            dos.writeUTF(ID);

                            Log.e("스트림설정","스트림설정");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } //스트림설정 끝

                        Thread th = new Thread(new Runnable() {
                            //Handler handler;

                            @Override
                            public void run() {
                                while(true)
                                {
                                    try {
                                        String msgIn = dis.readUTF(); //메세지 수신
                                        //방번호(0)@보낸사람(1)@내용(2)@TIME(3)@년(4)@월(5)@일(6)@시(7)
                                        String[] filter;
                                        filter = msgIn.split("@");
                                        inRoom_n = filter[0];
                                        senderID = filter[1];
                                        msg = filter[2];
                                        time = filter[3];
                                        time_d = filter[4]+"@"+filter[5]+"@"+filter[6]+"@"+filter[7];

                                        my_name = filter[8];
                                        my_img = filter[9];
                                        Iam = filter[10];

                                        //내부db에 내용 insert
                                        dbHelper.insertChat(inRoom_n,senderID,msg,time,time_d,my_name,my_img,Iam);

                                        // 현재 시간 받아오기
//                                        long mNow;
//                                        Date mDate;
//                                        mNow = System.currentTimeMillis();
//                                        mDate = new Date(mNow);
//                                        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm a");
//                                        String time = mFormat.format(mDate);

                                        //GMchatListFragment.chatLists 이미지와 업체이름 찾기
                                        for(int i =0; i<GMchatListFragment.chatLists.size(); i++){
                                            if(GMchatListFragment.chatLists.get(i).HM_ID.equals(filter[1])) {
                                                f_name =GMchatListFragment.chatLists.get(i).getFacility_name();
                                                f_img =GMchatListFragment.chatLists.get(i).getFacility_image();
                                            }
                                        }

                                        //채팅목록화면 리사이클러뷰 아이템 추가
                                        final DTO_ChatList chat_list = new DTO_ChatList();
                                        chat_list.ROOM_N = inRoom_n;
                                        chat_list.GM_ID = senderID;
                                        chat_list.HM_ID = senderID;
                                        chat_list.MSG = msg;
                                        chat_list.TIME = time;
                                        chat_list.facility_image=f_img;
                                        chat_list.facility_name=f_name;


                                        //chat_list.facility_image = GMchatListFragment.chatLists.get()

                                        //채팅화면 리사이클러뷰 아이템 추가
                                        final DTO_Chat chat = new DTO_Chat();
                                        //chat.isMe=false;
                                        chat.senderID = senderID;
                                        chat.message = msg;
//                                        chat.timeYou = time;
                                        chat.time_c = time;
                                        chat.my_name = my_name;
                                        chat.my_img = my_img;
                                        chat.Iam = Iam;

                                        //sendNotification(msg);
                                        System.out.println("서버로부터 들어온 메세지" + msgIn);
                                        System.out.println(msgIn);

//                                        mChatDataList.add(chat);
//                                        adapter_Chat.notifyDataSetChanged();
//
//                                        Gson gson = new Gson();
//                                        String chat_you_json = gson.toJson(chat);
//                                        GM_Chat.j_array1.add(chat_you_json);


                                        if(GM_Chat.ROOM_N != null) { //채팅방에 들어간 경우 있을경우
                                            if (GM_Chat.ROOM_N.equals(inRoom_n)) { //메세지가 들어어온 방에 있을경우
                                                Log.e("리시브스레드", "1");
                                                // 해당 채팅방의 리사이클러뷰에 뿌리기
                                               handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        GM_Chat.mChatDataList.add(chat);
                                                        //해쉬맵으로 만든 리스트에 넣으려고 했던것->실패
                                                        //GMchatListFragment.map.get(inRoom_n).add(chat);
                                                        GM_Chat.adapter_Chat.notifyDataSetChanged();
                                                        GM_Chat.chat_recyclerView.scrollToPosition(GM_Chat.adapter_Chat.getItemCount() - 1);
                                                    }
                                                });
                                            } else {//채팅 방번호는 있지만, 해당방에 보낼 메세지가 아닌경우
                                                Log.e("리시브스레드", "2");

                                                //쉐어드에 저장 (해당 방에 맞는 쉐어드)

                                                //서버에에 마지막 메세지 저장
                                                saveLastMessage(inRoom_n,msg,time,time_d);

                                                //노티알림
                                                if(MainActivity.ID != null){//일반회원일경우
                                                    //sendNotification_GM(String roomN,String gm, String hm, String f_name, String msg)
                                                    sendNotification_GM(inRoom_n,ID,senderID,f_name,msg,my_img);
                                                } else if(Hm_MainActivity.ID != null){
                                                    //sendNotification_HM(String roomN,String hm, String gm, String msg)
                                                    sendNotification_HM(inRoom_n,ID,senderID,msg,my_name,my_img);
                                                }
                                            }
                                        } else if(GM_Chat.ROOM_N == null) { //채팅화면이 아닐경우

                                            if(MainActivity.ID != null){//일반회원일경우
                                                Log.e("리시브스레드", "3");

                                                System.out.println("GM아 메세지왔다"+msg+"::"+msgIn);
                                                //sendNotification_GM(senderID,msg);

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가

                                                        for(int i=0; i<GMchatListFragment.chatLists.size(); i++){
                                                            if(GMchatListFragment.chatLists.get(i).getROOM_N().equals(inRoom_n)){
                                                                GMchatListFragment.chatLists.remove(GMchatListFragment.chatLists.get(i));
                                                                break;
                                                            }
                                                        }
                                                        GMchatListFragment.chatLists.add(0,chat_list);
                                                        GMchatListFragment.adapter_GM_ChatList.notifyDataSetChanged();
                                                    }
                                                });

                                                //GMchatListFragment.map.get(inRoom_n).add(chat); //대화내용리스트에 내용저장

                                                //서버에에 마지막 메세지 저장
                                                saveLastMessage(inRoom_n,msg,time,time_d);
                                                //노티알림
                                                sendNotification_GM(inRoom_n,ID,senderID,f_name,msg,my_img);



                                            } else if(Hm_MainActivity.ID != null){//업체회원일경우
                                                if(!HmChatFragment.room_list.contains(inRoom_n)) {
                                                    Log.e("리시브스레드", "4");
                                                    //서버에서 보낸 방번호가 룸리스트에 없으면,
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //방번호 추가
                                                            HmChatFragment.room_list.add(inRoom_n);
                                                            //리사이클러뷰에 추가
                                                            HmChatFragment.chatLists.add(0,chat_list);
                                                            HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();

                                                        }
                                                    });

                                                    //HmChatFragment.map.get(inRoom_n).add(chat);
                                                    //쉐어드에 저장 (해당 방에 맞는 쉐어드)


                                                    //서버에에 마지막 메세지 저장
                                                    saveLastMessage(inRoom_n,msg,time,time_d);

                                                    //노티알림
                                                    sendNotification_HM(inRoom_n,ID,senderID,msg,my_name,my_img);

                                                } else {//서버에서 보낸 방번호가 룸리스트에 있으면,
                                                    Log.e("리시브스레드", "5");
                                                    //목록화면의 마지막 메세지 바꿔주기
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가

                                                            for(int i=0; i<HmChatFragment.chatLists.size(); i++){
                                                                if(HmChatFragment.chatLists.get(i).getROOM_N().equals(inRoom_n)){
                                                                    HmChatFragment.chatLists.remove(HmChatFragment.chatLists.get(i));
                                                                    break;
                                                                }
                                                            }
                                                            HmChatFragment.chatLists.add(0,chat_list);
                                                            HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();
                                                        }
                                                    });

                                                    //HmChatFragment.map.get(inRoom_n).add(chat);
                                                    //서버에에 마지막 메세지 저장
                                                    saveLastMessage(inRoom_n,msg,time,time_d);
                                                    //노티알림
                                                    sendNotification_HM(inRoom_n,ID,senderID,msg,my_name,my_img);
                                                    Log.e("리시브스레드5", inRoom_n+","+ID+","+senderID+","+msg);


                                                }

                                            }
                                        }
                                        //inmessage(msgIn);
                                    } catch (IOException e) {
                                        try {
                                            is.close();
                                            dis.close();
                                            socket.close();
                                            System.out.println("서버와 연결 끊어짐");
                                        } catch (IOException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                        th.start();
                        //Connection();
                        Log.e("소켓연결됐다는데","되는건가?");
                    }
                } catch (IOException | SecurityException e1) {
                    Log.e("소켓","소켓생성이 안됐어");
                    System.out.println("I/O error: " + e1.getMessage());
                    e1.printStackTrace();
                }

        }
    });
        mainThread.start();
    //return super.onStartCommand(intent, flags, startId);
        return START_STICKY; //시스템에의해 종료되어도 다시생성
//        return START_REDELIVER_INTENT; //시스템에 의해 종료되어도 서비스,전달되었던 intent 값까지 모두유지(파일다운로드같이 중간에 값을 잃으면 안되는 것)
}

    private void inmessage(String str) { //서버로부터 들어오는 모든메세지
        String[] filter;
        filter = str.split("@"); // ROOM_N + "@" +보낸사람+ "@" msg
        if(GM_Chat.ROOM_N == filter[0]) {
        }
    }

    private void consoleLog(String log) {
        System.out.println(log);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy() 호출됨");
        super.onDestroy();

        serviceIntent = null;
        //setAlarmTimer();

        Thread.currentThread().interrupt(); //현재 스레드 중지

        if(mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }

        ID = null;

    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG,"onTaskRemoved() 호출됨");
        setAlarmTimer();

    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate() 호출됨");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind() 호출됨");
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind() 호출됨");
        return super.onUnbind(intent);
    }


    private void showToast(final Application application, final String msg) {
        Handler handler = new Handler(application.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void setAlarmTimer() {
        Log.d(TAG,"setAlarmTimer() 호출됨");
        //1초뒤에 알람보내게 설정
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);

        Intent intent = new Intent(this, Svc_receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);

    }

    public void initializeNotification_GM() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("userID",ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(null)
                .setContentTitle(null)
                .setOngoing(true)
                .setStyle(style)
                .setWhen(0)
                .setShowWhen(false)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);
    }

    public void initializeNotification_HM() {
        Intent notificationIntent = new Intent(this, Hm_MainActivity.class);
        notificationIntent.putExtra("userID",ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(null)
            .setContentTitle(null)
            .setOngoing(true)
            .setStyle(style)
            .setWhen(0)
            .setShowWhen(false)
            .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);
    }

    //노티GM // 메세지가 들어온 방번호, 내 id 보낸사람id 보낸업체 이름, 메세지
    private void sendNotification_GM(String roomN,String gm, String hm, String f_name, String msg, String img) {
        Intent intent = new Intent(this, GM_Chat.class);
        //전달할 값
        intent.putExtra("ROOM_N",roomN); //입장해야하는 방
        intent.putExtra("GM_ID", gm); //내 id
        intent.putExtra("HM_ID",hm); //보낸사람(업체호원)
        intent.putExtra("name",f_name); //보낸업체명
        intent.putExtra("isList",false);
        intent.putExtra("isNoti",true);
        intent.putExtra("Iam","GM");

        intent.putExtra("my_name",gm);
        intent.putExtra("my_img",img);

        Log.e("Noti_GM_ROOM_N", roomN);
        Log.e("Noti_GM_GM_ID", gm);
        Log.e("Noti_GM_HM_ID", hm);
        Log.e("Noti_GM_name", f_name);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //메모리에 MainActivity객체가 이미 만들어져 있을때 재사용 하도록 플래그 추가
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelID = "default";
        //getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                .setContentTitle(f_name) //상태바 드레그하면 보이는것
                .setContentText(msg) //상태바 서브타이틀
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //오레오버전 이후로 notification channel 필요함
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "기본채널",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0,notiBuilder.build());

    }

    //노티HM // 메세지가 들어온 방번호, 내 id 보낸사람id 내업체 이름,내업체이미지, 메세지
    private void sendNotification_HM(String roomN,String hm, String gm, String msg, String my_name,String my_img) {
        Intent intent = new Intent(this, GM_Chat.class);
        //전달할 값
        intent.putExtra("ROOM_N",roomN); //입장해야하는 방
        intent.putExtra("HM_ID",hm); //내 id(업체_
        intent.putExtra("GM_ID",gm); //보낸사람(일반)
        intent.putExtra("name",gm); //보낸사람(일반회원)
        intent.putExtra("isList",false);
        intent.putExtra("isNoti",true);
        intent.putExtra("Iam","HM");

        intent.putExtra("my_name",my_name);
        intent.putExtra("my_img",my_img);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //메모리에 MainActivity객체가 이미 만들어져 있을때 재사용 하도록 플래그 추가
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelID = "default";
        //getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                .setContentTitle(gm)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //오레오버전 이후로 notification channel 필요함
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "기본채널",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0,notiBuilder.build());

    }

    //마지막메세지 서버에 저장
    public void saveLastMessage(String roomN,String msg,String time,String time_d) {
        //룸넘버, 메세지, 시간보내기
        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        Call<DTO_ChatRoom> call = restAPI.saveLastMSG(roomN,msg,time,time_d);
        Log.e("서비스,서버로마지막M저장",msg);
        call.enqueue(new Callback<DTO_ChatRoom>() {
            @Override
            public void onResponse(Call<DTO_ChatRoom> call, Response<DTO_ChatRoom> response) {

                if(response.isSuccessful()) {
                    Toast.makeText(Svc_MyService.this,"저장 성공",Toast.LENGTH_SHORT);

                }
                else {
                    Toast.makeText(Svc_MyService.this,"저장 실패",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<DTO_ChatRoom> call, Throwable t) {

                Log.e("@@@@@@@@@@@@@@@xxxx", t.toString());
                Toast.makeText(Svc_MyService.this, "네트워크 에러", Toast.LENGTH_SHORT);
            }
        });

    }

    public void saveMessageShared(){

    }



}
