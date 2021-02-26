package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HM_Chat extends AppCompatActivity {

    private String html = "";
    private Handler mHandler;

    final Socket socket = Svc_MyService.socket;
    private OutputStream os;
    private DataOutputStream dos;

    //private SendThread send;

    //private String ID;
    //private String ROOM_N = "11";

    public TextView Toptext;
    String TAG = "HM_CHAT_Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm_chat);

//        ID = Hm_MainActivity.ID;
//        Log.e("HM_ID : ",ID);
//
//        sendMessage(ROOM_N+"@"+ID);

//        final EditText et = (EditText) findViewById(R.id.EditText01);
//        Button btn = (Button) findViewById(R.id.Button01);
//        Toptext = (TextView) findViewById(R.id.Toptext);
//
//        mHandler = new Handler();



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
}
