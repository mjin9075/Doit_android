package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgTest extends AppCompatActivity {

    ImageView IMG;
    private String uriS;
    private Uri uri;
    RestAPI restAPI;
    String HM_ID;
    String path;
    String tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_test);

        IMG = findViewById(R.id.IMG);
        restAPI = RestRequestHelper.getClient().create(RestAPI.class);


        Bundle extras = getIntent().getExtras();
        uriS = extras.getString("img", "");
        HM_ID = extras.getString("HM_ID");
        path = extras.getString("imgPath");
        tempFile = extras.getString("tempFile");

        System.out.println("HM_ID : "+HM_ID);
        System.out.println("ImgTest_tempFile : "+tempFile);


        IMG.setImageURI(Uri.parse(uriS));

        uri = Uri.parse(uriS);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
//                File file = new File(tempFile);
////                File file = new File(String.valueOf(path));
////                File file = new File(String.valueOf("/sdcard/Pictures/1592469352746.jpg"));
////                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//                MultipartBody.Part img = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
//
//                Call<HmRegisterDTO> postCall = restAPI.hm_register(HM_ID,img);
//
//                postCall.enqueue(new Callback<HmRegisterDTO>() {
//                    @Override
//                    public void onResponse(Call<HmRegisterDTO> call, Response<HmRegisterDTO> response) {
//                        if(response.isSuccessful()) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(ImgTest.this);
//                            Dialog dialog = builder.setMessage("등록성공")
//                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            finish();
//                                        }
//                                    })
//                                    .create();
//                            dialog.show();
//                        }
//                        else {
//                            Toast.makeText(ImgTest.this,"등록 실패",Toast.LENGTH_SHORT);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<HmRegisterDTO> call, Throwable t) {
//                        Log.d("@@@@@@@@@@@@@@@xxxx", t.toString());
//                        Toast.makeText(ImgTest.this, "네트워크 에러", Toast.LENGTH_SHORT);
//
//                    }
//                });

            }
        });

    }
}
