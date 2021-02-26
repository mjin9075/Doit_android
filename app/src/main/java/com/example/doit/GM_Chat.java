package com.example.doit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GM_Chat extends AppCompatActivity {

    final Socket socket = Svc_MyService.socket;
    //    private InputStream is;
    private OutputStream os;
    //    private DataInputStream dis;
    private DataOutputStream dos;

//    public static Handler handler;
    //private Handler mHandler = new Handler();
    //private SenderThread senderThread;
    private SendThread send;

    private String ID, GM_ID, HM_ID ,Iam;
    public static String ROOM_N;
    private boolean isList,isNoti;

    private String my_name,my_img;

    private String name;
    private TextView chatRoom_name;

    private TextView message_text;
    private EditText message_edit;

    String msg = "";
    String time, time_d;

    public static RecyclerView chat_recyclerView;
    public static Adapter_Chat adapter_Chat;
    private LinearLayoutManager linearLayoutManager;
    public static List<DTO_Chat> mChatDataList;

    List items = new ArrayList<>();

    //private GM_Chat_Client mChatClient;

    RestAPI restAPI;

    private Handler handler;
    String f_name;
    String f_img;

    DB_Chat_DBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm__chat);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //HashMap<String,List<DTO_Chat>> map = new HashMap<>();

        //Handler 를 new 하게 되면 현재 new 를 실행한 thread 에 bind 되게 된다
        //이 Handler 는 현재 thread 의 message queue 를 이용하게 되고,
        //Handler 의 method 인 post() 나 sendMessage() 를 이용하면,
        // 현재의 message queue 에 Runnable 또는 Message 가 들어가게 된다.
//        handler = new Handler();
        if(MainActivity.handler != null){
            handler = MainActivity.handler;
        } else {
            handler = Hm_MainActivity.handler;
        }

        //db헬퍼 인스턴스화
        dbHelper = new DB_Chat_DBHelper(this);

//        while (dbHelper.readChatOrderByTimeDWhereRoomN(ROOM_N).moveToNext()) {
//
//        }
//
//        System.out.println("뭔가나올까 커서서: "+dbHelper.readChatOrderByTimeDWhereRoomN(ROOM_N));


//        SQLiteDatabase db = DB_Chat_DBHelper.getInstance(this).getWritableDatabase();
//        db.insert(DB_ChatContract.ChatEntry.TABLE_NAME,null,)



        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        mChatDataList = new ArrayList<>();

        GM_ID = getIntent().getStringExtra("GM_ID");
        HM_ID = getIntent().getStringExtra("HM_ID");
        ROOM_N = getIntent().getStringExtra("ROOM_N");

        isList = getIntent().getBooleanExtra("isList",true);
        isNoti = getIntent().getBooleanExtra("isNoti",false);
        Iam = getIntent().getStringExtra("Iam");

        my_name = getIntent().getStringExtra("my_name");
        my_img = getIntent().getStringExtra("my_img");

        Log.e("GM_Chat_GM,HM,RN", GM_ID + HM_ID + ROOM_N);
        Log.e("GM_Chat_isList", isList+"");
        Log.e("GM_Chat_isNoti", isNoti+"");
        Log.e("GM_Chat_Iam", Iam);

//        Log.e("GM_Chat_my_name", my_name);
//        Log.e("GM_Chat_my_img", my_img);

        chatRoom_name = findViewById(R.id.chatRoom_name);
//        name = getIntent().getStringExtra("" +"name");
        name = getIntent().getStringExtra("name"); //gm일경우 업체명, hm일경우 gm_id
        chatRoom_name.setText(name);
//        ID = MainActivity.ID;

        if(isNoti) { //노티로 들어왔으면,
            if(Iam.equals("GM")){
                ID = GM_ID;
            } else if(Iam.equals("HM")) {
                ID = HM_ID;
            }
        } else {
            if(MainActivity.ID != null){
                ID = MainActivity.ID;
                Log.e("GM_chat_roomN:",ROOM_N+"");
                //mChatDataList = GMchatListFragment.map.get(ROOM_N);
            } else {
                ID = Hm_MainActivity.ID;
                Log.e("GM_chat_roomN:",ROOM_N+"");
                //mChatDataList = HmChatFragment.map.get(ROOM_N);
            }
        }

        Log.e("chat_ID", ID);


        //RecyclerView
        //mChatDataList = new ArrayList<>();
        //message_text = findViewById(R.id.message_text);
        message_edit = findViewById(R.id.message_edit);
        chat_recyclerView = findViewById(R.id.chat_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        chat_recyclerView.setLayoutManager(linearLayoutManager);

        //SQLite에서 데이터가져오기
        mChatDataList.clear(); //데이터초기화
        db = dbHelper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = dbHelper.readChatOrderByTimeDWhereRoomN(ROOM_N);

        try {
            cursor.moveToFirst();
            Log.e("SQLiteDB개수:" ,""+cursor.getCount());
            while (!cursor.isAfterLast()) {
                addGroupItem(cursor.getString(0),
                             cursor.getString(1),
                             cursor.getString(2),
                             cursor.getString(3),
                             cursor.getString(4),
                             cursor.getString(5),
                             cursor.getString(6),
                             cursor.getString(7));
                cursor.moveToNext();
            }
            Log.e("mChat.size",""+mChatDataList.size());
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
                db.endTransaction();
            }
        }

        adapter_Chat = new Adapter_Chat(ID,mChatDataList);
        chat_recyclerView.setAdapter(adapter_Chat);
        chat_recyclerView.scrollToPosition(adapter_Chat.getItemCount() - 1);

        //sendMessage(ROOM_N+"@"+ID);


        //일반회원만 서버에 join메세지 보내기(업체회원은 일반회원에게 먼저 대화를 신청할 수 없다, 대화방을 만들 수 없다.)
//        if(MainActivity.ID != null){
        if(Iam.equals("GM")){
            sendMessage("join@"+ROOM_N+"@"+ GM_ID +"@"+HM_ID);
            if(mChatDataList.size()==0) { //일반회원이 방에 처음 들어왔을경우(주고받은 대화가 0일경우)
                String fa_img;
                fa_img = getIntent().getStringExtra("f_img");
                if(isNoti) {
                    fa_img = getIntent().getStringExtra("my_img");
                }
                long mNow;
                Date mDate;
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);

                SimpleDateFormat mFormat = new SimpleDateFormat("a h:mm");
                //년,월,일,시,분
                SimpleDateFormat mFormatD = new SimpleDateFormat("yyyy@M@d@HH:mm:ss.SSS");

                time = mFormat.format(mDate);
                time_d = mFormatD.format(mDate);
                String firstMSG = "안녕하세요,\n\'"+name+"\' 입니다.\n궁금한 내용이 있으시면\n메세지를 남겨주세요^^";
                String HM = "HM";

                final DTO_Chat chat= new DTO_Chat();
                chat.senderID = HM_ID;
                chat.message =firstMSG;
                chat.time_c = time;
                chat.my_name = name;
                chat.my_img = fa_img;
                chat.Iam = HM;

                dbHelper.insertChat(ROOM_N,HM_ID,firstMSG,time,time_d,name,fa_img,HM);


                mChatDataList.add(chat);
                adapter_Chat.notifyDataSetChanged();
                chat_recyclerView.scrollToPosition(adapter_Chat.getItemCount() - 1);
            }
        }


        //sendMessage(ROOM_N+"@"+HM_ID);

//        //전송버튼 클릭하면 리스너에서 sendMessage()메소드 실행
        Button message_send_btn = findViewById(R.id.message_send_btn);
//        message_send_btn.setOnClickListener(this);

        //전송버튼 클릭하면 editText가 비어있는지 확인후 비어있지않다면 sendThread실행
        message_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = message_edit.getText().toString();

                if (msg == null || TextUtils.isEmpty(msg) || msg.equals("")) {
                    Toast.makeText(GM_Chat.this, "메세지를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 현재 시간 받아오기
                    long mNow;
                    Date mDate;
                    mNow = System.currentTimeMillis();
                    mDate = new Date(mNow);

                    SimpleDateFormat mFormat = new SimpleDateFormat("a h:mm");
                    //년,월,일,시,분
                    SimpleDateFormat mFormatD = new SimpleDateFormat("yyyy@M@d@HH:mm:ss.SSS");

                    time = mFormat.format(mDate);
                    time_d = mFormatD.format(mDate);

                    final DTO_Chat chat= new DTO_Chat();
                    //chat.isMe = true;
                    chat.senderID = ID;
                    chat.message = msg;
//                    chat.timeMe = time;
                    chat.time_c = time;
                    chat.my_name = my_name;
                    chat.my_img = my_img;
                    chat.Iam = Iam;


                    //obj->json 쉐어드에 저장하기 위해.
//                    Gson gson = new Gson();
//                    String chat_me_json = gson.toJson(chat);
//                    j_array1.add(chat_me_json);


                    //내가 쓴 대화는 서버로 보내기 전 리싸이클러뷰에 set
                    if(MainActivity.ID != null){
                        Log.e("GM_chat_roomN:",ROOM_N+"");
                        //mChatDataList = GMchatListFragment.map.get(ROOM_N);

                    } else {
                        Log.e("GM_chat_roomN:",ROOM_N+"");
                        //mChatDataList = HmChatFragment.map.get(ROOM_N);
                    }

                    mChatDataList.add(chat);
                    adapter_Chat.notifyDataSetChanged();
                    chat_recyclerView.scrollToPosition(adapter_Chat.getItemCount() - 1);


                    //메세지 보내기
//                    send = new SendThread(socket, msg);
//                    send.start();


                    //내부db에 내용 insert
                    dbHelper.insertChat(ROOM_N,ID,msg,time,time_d,my_name,my_img,Iam);


                    sendMessage(ROOM_N + "@" + ID + "@" + msg + "@" + time + "@" + time_d + "@" + my_name + "@" + my_img + "@" +Iam);
                    System.out.println("서버로가는 메세지:"+ROOM_N + "@" + ID + "@" + msg + "@" + time + "@" + time_d + "@" + my_name + "@" + my_img + "@" +Iam);
//                    if(Iam.equals("GM")){
//                        sendMessage(ROOM_N + "@" + ID + "@" + msg + "@" + time + "@" + time_d);
//                    } else if(Iam.equals("HM")){
//                        sendMessage(ROOM_N + "@" + ID + "@" + msg + "@" + time + "@" + time_d);
//                    }


                    //editText비우기
                    message_edit.setText(null);

                    //키보드 내리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(message_edit.getWindowToken(), 0);

                    Toast.makeText(GM_Chat.this, "전송", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void addGroupItem(String roomN, String senderID, String message, String time_c, String time_d, String my_name, String my_img, String iam) {
        DTO_Chat chatItem = new DTO_Chat();
        chatItem.setRoomN(roomN);
        chatItem.setSenderID(senderID);
        chatItem.setMessage(message);
        chatItem.setTime_c(time_c);
        chatItem.setTime_d(time_d);
        chatItem.setMy_name(my_name);
        chatItem.setMy_img(my_img);
        chatItem.setIam(iam);

        mChatDataList.add(chatItem);

    }

    void setToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onClick(View v) {
////        senderThread.sendMessage("Chatting/"+ID+"/"+message_edit.getText().toString()+ "\r\n");
//        sendMessage("Chatting/"+ROOM_N+"/"+message_edit.getText().toString()+ "\r\n");
//        message_edit.setText("");
//    }

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


    class SendThread extends Thread {
        Socket socket;
        String sendmsg;
        DataOutputStream output;


        public SendThread(Socket socket, String sendmsg) {
            this.socket = socket;
            this.sendmsg = sendmsg;
            try {
                // 채팅 서버로 메세지를 보내기 위한  스트림 생성.
//                output = new DataOutputStream(socket.getOutputStream());
                output = dos;

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }

        // 서버로 메세지 전송 ( 이클립스 서버단에서 temp 로 전달이 된다.
        public void run() {
            try {
                if (output != null) {
                    if (sendmsg != null) {

                        // 여기서 방번호와 상대방 아이디 까지 해서 보내줘야 할거같다 .
                        // 서버로 메세지 전송하는 부분
                        output.writeUTF(ROOM_N + "@" + GM_ID + "@" + HM_ID + "@" + sendmsg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed()","");

        super.onBackPressed();
//        if(isNoti) {
//            if(Iam.equals("GM")) {
//                Log.e("onBackPressed()_GM",Iam);
//
//                Intent intent_g = new Intent(GM_Chat.this,MainActivity.class);
//                intent_g.putExtra("userID",GM_ID);
//                intent_g.putExtra("isNoti",true);
//                GM_Chat.this.startActivity(intent_g);
//            }
//            else if(Iam.equals("HM")) {
//                Log.e("onBackPressed()_HM",Iam);
//
//                Intent intent_h = new Intent(GM_Chat.this,Hm_MainActivity.class);
//                intent_h.putExtra("userID",HM_ID);
//                intent_h.putExtra("isNoti",true);
//                GM_Chat.this.startActivity(intent_h);
//            }
//        }
    }

    @Override
    protected void onPause() { // 채팅 마지막 메세지 목록 갱신
        super.onPause();
        saveLastMessage();
        Log.d("saveLastMessage()", "onPause() 호출됨");

        if(isNoti) {
            if(Iam.equals("GM")) {
                Log.e("onPause()_GM",Iam);

                Intent intent_g = new Intent(GM_Chat.this,MainActivity.class);
                intent_g.putExtra("userID",GM_ID);
                intent_g.putExtra("isNoti",true);
                GM_Chat.this.startActivity(intent_g);
            }
            else if(Iam.equals("HM")) {
                Log.e("onPause()_HM",Iam);

                Intent intent_h = new Intent(GM_Chat.this,Hm_MainActivity.class);
                intent_h.putExtra("userID",HM_ID);
                intent_h.putExtra("isNoti",true);
                GM_Chat.this.startActivity(intent_h);
            }
        } else { //노티아님
            if (isList) {
                //GMchatListFragment.chatLists 이미지와 업체이름 찾기
                for (int i = 0; i < GMchatListFragment.chatLists.size(); i++) {
                    if (GMchatListFragment.chatLists.get(i).HM_ID.equals(HM_ID)) {
                        f_name = GMchatListFragment.chatLists.get(i).getFacility_name();
                        f_img = GMchatListFragment.chatLists.get(i).getFacility_image();

                        Log.e("으이구gm,f_name", f_name);
                    }
                }

                int index = mChatDataList.size() - 1;
                String lastMsg;
                if (index < 0) {
                    lastMsg = "";
                } else {
                    lastMsg = mChatDataList.get(index).message;
                }

                //채팅목록화면 리사이클러뷰 아이템 추가
                final DTO_ChatList chat_list = new DTO_ChatList();
                chat_list.ROOM_N = ROOM_N;
                chat_list.GM_ID = GM_ID;
                chat_list.HM_ID = HM_ID;
                chat_list.MSG = lastMsg;
                chat_list.TIME = time;
                chat_list.facility_image = f_img;
                chat_list.facility_name = f_name;

                if (MainActivity.ID != null) {//일반회원일경우
                    Log.e("으이구gm", "일반회원일경우");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가
                            Log.e("으이구gm", "일반회원일경우1");

                            Log.e("으이구gmGM.chatLists.size:", GMchatListFragment.chatLists.size() + "");
                            for (int j = 0; j < GMchatListFragment.chatLists.size(); j++) {
                                Log.e("으이구gm", "일반회원일경우" + j);

                                if (GMchatListFragment.chatLists.get(j).getROOM_N().equals(ROOM_N)) {
                                    Log.e("으이구hm", "" + j);
                                    Log.e("으이구hm", "" + ROOM_N);
                                    Log.e("으이구hm", "" + GMchatListFragment.chatLists.get(j).getROOM_N());

                                    Log.e("리무브_전:", GMchatListFragment.chatLists.size() + "");
                                    GMchatListFragment.chatLists.remove(GMchatListFragment.chatLists.get(j));
                                    Log.e("리무브_후:", GMchatListFragment.chatLists.size() + "");

                                    GMchatListFragment.adapter_GM_ChatList.notifyDataSetChanged();

                                    break;
                                }
                            }
                            GMchatListFragment.chatLists.add(0, chat_list);
                            Log.e("으이구gm", "일반회원일경우1_add");

                            GMchatListFragment.adapter_GM_ChatList.notifyDataSetChanged();
                        }
                    });
                } else if (Hm_MainActivity.ID != null) {//업체회원일경우
                    Log.e("으이구hm", "업체회원일경우");
                    //목록화면의 마지막 메세지 바꿔주기
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가
                            Log.e("으이구hmHM.chatLists.size:", HmChatFragment.chatLists.size() + "");
                            for (int j = 0; j < HmChatFragment.chatLists.size(); j++) {
                                if (HmChatFragment.chatLists.get(j).getROOM_N().equals(ROOM_N)) {
                                    Log.e("으이구hm", "" + j);
                                    Log.e("으이구hm", "" + ROOM_N);
                                    Log.e("으이구hm", "" + HmChatFragment.chatLists.get(j).getROOM_N());

                                    Log.e("리무브_전:", HmChatFragment.chatLists.size() + "");
                                HmChatFragment.chatLists.remove(HmChatFragment.chatLists.get(j));
                                Log.e("리무브_후:", HmChatFragment.chatLists.size() + "");
                                HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();

                                break;
                            }
                            }
                            HmChatFragment.chatLists.add(0, chat_list);
                            Log.e("으이구gm", "업체회원일경우1_add");

                            HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();
                        }
                    });
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("으이구hm","onDestroy()호출됨");


//        for(int i=0; i<mChatDataList.size(); i++) {
////            System.out.println(i + ":" + mChatDataList.get(i));
//            System.out.println(i + ":" + mChatDataList.get(i).message);
//            //마지막 메세지 = mChatDataList.get(index)
//            //index = mChatDataList.size() -1
//            int index = mChatDataList.size()-1;
//            System.out.println("마지막 메세지:" + mChatDataList.get(index).message);
//        }

//        //GMchatListFragment.chatLists 이미지와 업체이름 찾기
//        Log.e("으이구GM.chatLists.size()",GMchatListFragment.chatLists.size()+"GMchatListFragment.chatLists.size()");
//        for(int i =0; i<GMchatListFragment.chatLists.size(); i++){
//            if(GMchatListFragment.chatLists.get(i).HM_ID.equals(HM_ID)) {
//                f_name =GMchatListFragment.chatLists.get(i).getFacility_name();
//                f_img =GMchatListFragment.chatLists.get(i).getFacility_image();
//
//                Log.e("으이구gm,f_name",f_name);
//            }
//        }
//
//        //채팅목록화면 리사이클러뷰 아이템 추가
//        final DTO_ChatList chat_list = new DTO_ChatList();
//        chat_list.ROOM_N = ROOM_N;
//        chat_list.GM_ID = GM_ID;
//        chat_list.HM_ID = HM_ID;
//        chat_list.MSG = msg;
//        chat_list.TIME = time;
//        chat_list.facility_image = f_img;
//        chat_list.facility_name = f_name;
//
//        if(MainActivity.ID != null){//일반회원일경우
//            Log.e("으이구gm","일반회원일경우");
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가
//                    Log.e("으이구gm","일반회원일경우1");
//
//                    for(int j=0; j<GMchatListFragment.chatLists.size(); j++){
//                        Log.e("으이구gm","일반회원일경우"+j);
//
//                        if(GMchatListFragment.chatLists.get(j).getROOM_N().equals(ROOM_N)){
//                            GMchatListFragment.chatLists.remove(GMchatListFragment.chatLists.get(j));
//                            GMchatListFragment.adapter_GM_ChatList.notifyDataSetChanged();
//
//                            break;
//                        }
//                    }
//                    GMchatListFragment.chatLists.add(0,chat_list);
//                    Log.e("으이구gm","일반회원일경우1_add");
//
//                    GMchatListFragment.adapter_GM_ChatList.notifyDataSetChanged();
//                }
//            });
//        } else if(Hm_MainActivity.ID != null){//업체회원일경우
//            Log.e("으이구hm","업체회원일경우");
//            //목록화면의 마지막 메세지 바꿔주기
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    //리사이클러뷰에서 sender와 했던 아이템 삭제후 리사이클러뷰에 다시추가
//
//                    for(int j=0; j<HmChatFragment.chatLists.size(); j++){
//                        if(HmChatFragment.chatLists.get(j).getROOM_N().equals(ROOM_N)){
//                            Log.e("으이구hm",""+j);
//                            Log.e("으이구hm",""+ROOM_N);
//                            Log.e("으이구hm",""+HmChatFragment.chatLists.get(j).getROOM_N());
//
//                            HmChatFragment.chatLists.remove(HmChatFragment.chatLists.get(j));
//                            HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();
//
//                            break;
//                        }
//                    }
//                    HmChatFragment.chatLists.add(0,chat_list);
//                    Log.e("으이구gm","업체회원일경우1_add");
//
//                    HmChatFragment.adapter_HM_ChatList.notifyDataSetChanged();
//                }
//            });
//        }

//        saveLastMessage();
//        Log.d("saveLastMessage()", "saveLastMessage() 호출됨");

        //Log.e("onDestroy()전:", ROOM_N);
        //방번호 없애기
        ROOM_N = null;

    }

    //마지막메세지 서버에 저장
    public void saveLastMessage() {
        //룸넘버, 메세지, 시간보내기
        int index = mChatDataList.size()-1;
        String lastMsg;
        if(index<0 ) {
            lastMsg = "";

        }
//        else if(mChatDataList.get(index).message.equals("") || mChatDataList.get(index).message ==null) {
//            lastMsg = mChatDataList.get(index).message;
//
//        }
        else {
            lastMsg = mChatDataList.get(index).message;
        }
        // 현재 시간 받아오기
        long mNow;
        Date mDate;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("a h:mm");
        //년,월,일,시,분,초,밀리초
        SimpleDateFormat mFormatD = new SimpleDateFormat("yyyy@M@d@HH:mm:ss.SSS");

        time = mFormat.format(mDate);
        time_d = mFormatD.format(mDate);


        Log.d("saveLastMessage()time:", time);

        Call<DTO_ChatRoom> call = restAPI.saveLastMSG(ROOM_N,lastMsg,time,time_d);
        call.enqueue(new Callback<DTO_ChatRoom>() {
            @Override
            public void onResponse(Call<DTO_ChatRoom> call, Response<DTO_ChatRoom> response) {
                if(response.isSuccessful()) {
                    //Toast.makeText(GM_Chat.this,"저장 성공",Toast.LENGTH_SHORT);

                }
                else {
                    //Toast.makeText(GM_Chat.this,"저장 실패",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<DTO_ChatRoom> call, Throwable t) {

                Log.e("@@@@@@@@@@@@@@@xxxx", t.toString());
                Toast.makeText(GM_Chat.this, "네트워크 에러", Toast.LENGTH_SHORT);
            }
        });

    }

    //마지막 메세지 채팅목록화면에up.
    public void updateChatList() {

    }
}

