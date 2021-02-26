package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

public class DaumWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private TextView txt_address;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);

        txt_address = findViewById(R.id.txt_address);

        //WebView 초기화
        init_webView();

        //핸들러를 통한 자바스크립트 이벤트반응
        handler = new Handler();
    }

    private void init_webView() {
        webView = findViewById(R.id.daum_webView);

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        webView.loadUrl("서버URL");

    }

    private class AndroidBridge{
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txt_address.setText(String.format("(%s) %s, %s ", arg1, arg2, arg3));

                    Log.d("@@@@@@@@", arg1);
                    Log.d("@@@@@@@@", arg2);
                    Log.d("@@@@@@@@", arg3);
                    Log.d("@@@@@@@@",String.format("(%s) %s %s", arg1, arg2, arg3));

                    String address = txt_address.getText().toString();
                    System.out.println(address);
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();

//                    Intent intent = new Intent();
//                    intent.putExtra("address",address);
//                    Log.d("@@@@@@@@인테에엔", address);
//                    setResult(RESULT_OK,intent);
//                    finish();



                    Bundle extra = new Bundle();
                    Intent intent = new Intent();
                    extra.putString("address",address);
                    intent.putExtras(extra);
                    System.out.println("extra: " + extra);
                    setResult(RESULT_OK,intent);
                    finish();

                }
            });
        }

    }
}
