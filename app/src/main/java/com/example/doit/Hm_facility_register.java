package com.example.doit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Hm_facility_register extends AppCompatActivity  {

    String HM_ID;

    private static final String TAG = "halDoItUh";
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int PICK_FROM_ALBUM = 673;
    private static final int REQUEST_IMAGE_CROP = 674;

    private String CameraImageFilePath, gin;
    private String imageFilePath;
    private Uri uri;
    private File tempFile;

    String Rpath;

    ImageView hm_facility_IMG;


    ArrayAdapter<CharSequence> adspin1, adspin2; //스피너어댑터 adspint1(큰카테고리) adspin2(하위)
    String choice_1_category="";
    String choice_2_category="";

    private GoogleMap map;
    private static final int PLACE_PICKER_REQUEST =1; // 위치검색 쓸 때
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; // 위치 권한 쓸때
    private boolean mPermissionDenied = false; // gps 권한 체크
    TextView facility_receive_add;
    ArrayList<LatLng> MarkerPoints; // 마커 저장

    private static final int REQUEST_SEARCH_ADDRESS = 676;

    private AlertDialog dialog;

//    private Uri uri;
//  String choice_2_category="";
    private String f_name;
    private String f_business_hours;
    private String f_convenience;
    private String f_etc;
    private String f_intro;
//    private String facility_receive_add;
    private String f_detailed_add;

    RestAPI restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_activity_facility_register);

        HM_ID = getIntent().getStringExtra("userID");
        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        //RestAPI restAPI = RestRequestHelper.getClient().create(RestAPI.class);


        final EditText facility_name = findViewById(R.id.facility_name);
        final EditText facility_business_hours = findViewById(R.id.facility_business_hours);
        final EditText facility_convenience = findViewById(R.id.facility_convenience);
        final EditText facility_etc = findViewById(R.id.facility_etc);
        final EditText facility_intro = findViewById(R.id.facility_intro);
        final TextView facility_receive_add = findViewById(R.id.facility_receive_add);
        final EditText facility_detailed_add = findViewById(R.id.facility_detailed_add);

        //카메라 권한설정
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")  //카메라 권한 팝업떳을때 나타나는 메세지
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


//========이미지=============================================================================================

        final ImageView hm_facility_IMG = findViewById(R.id.hm_facility_IMG);
        hm_facility_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //다이얼로그 띄우기기
                AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                builder.setTitle("").setMessage("어떻게");

                builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                    }
                });

                builder.setPositiveButton("카메라", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                        //카메라 인텐트 실행
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            //임시파일생성
                            tempFile = null;
                            try {
                                tempFile = createImageFile(); //사진찍은후 저장 할 임시폴더 (아래에 정의함)
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //임시파일생성되면 실행
                            if (tempFile != null) {
                                uri = FileProvider.getUriForFile(getApplicationContext(),"com.example.doit", tempFile);

                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }
                });

                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }




        });


//=======스피너==============================================================================================

        final Spinner spinner_1_category = findViewById(R.id.spinner_1_category);
        final Spinner spinner_2_category = findViewById(R.id.spinner_2_category);

        //첫번째 어댑터에 값을 넣음
        //R.layout.simple_.. 안드로이드에서 기본제공하는 spinner 모양
        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner_1, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1_category.setAdapter(adspin1);


        spinner_1_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adspin1.getItem(i).equals("대분류")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.category_small,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);


                } else if(adspin1.getItem(i).equals("무술")){

                    choice_1_category = adspin1.getItem(i).toString();

                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_matial,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }

                    });
                } else if(adspin1.getItem(i).equals("구기")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_ball,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }

                    });
                } else if(adspin1.getItem(i).equals("라켓")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_raket,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if(adspin1.getItem(i).equals("익스트림")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_ext,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if(adspin1.getItem(i).equals("수상")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_water,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if(adspin1.getItem(i).equals("기타종목")){
                    adspin2 = ArrayAdapter.createFromResource(Hm_facility_register.this,R.array.spinner_1_etc,R.layout.support_simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_2_category.setAdapter(adspin2);

                    spinner_2_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //두번깨 스피너에서 선택된 값
                            choice_2_category = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


//=======구글맵==============================================================================================






//        final Button search_place = findViewById(R.id.search_place);
//        search_place.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intentS = new Intent(Hm_facility_register.this,MapsActivity.class);
////                Hm_facility_register.this.startActivity(intentS);
//                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
//                try {
//                    Intent intent = intentBuilder.build(Hm_facility_register.this);
//                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });








 //========주소찾기 버튼===================================================
        final Button search_place = findViewById(R.id.search_place);
        search_place.setOnClickListener(new Button.OnClickListener()
        { @Override
        public void onClick(View view)
        {
            Intent intentR = new Intent(Hm_facility_register.this, DaumWebViewActivity.class);
//            Hm_facility_register.this.startActivity(intentR);
            Hm_facility_register.this.startActivityForResult(intentR,REQUEST_SEARCH_ADDRESS);
        }
        });



//========등록 버튼===================================================

        final Button facility_registerButton = findViewById(R.id.facility_registerButton);
        facility_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(uri==null||uri.equals("")){
                    //uri=Uri.parse("android.resource://com.example.doit/drawable/pictograms");
                    //tempFile = new File("android.resource://com.example.doit/drawable/pictograms");
                    tempFile = new File("");

                    System.out.println("이미지 없어없어"+uri);


                } else if(uri != null) {
                    System.out.println("이미지 주ㅜㅜ소"+uri);
                    System.out.println("이미지 tempFile 주ㅜㅜ소"+tempFile);

                }

                //hm_facility_IMG.setImageURI(uri);
//                //이미지 테스트
//                Intent intent = new Intent(getApplicationContext(), ImgTest.class);
//                intent.putExtra("img",uri.toString());
//                intent.putExtra("imgPath",Rpath);
//                intent.putExtra("HM_ID",HM_ID);
//                intent.putExtra("tempFile",tempFile.toString());
//                System.out.println("uri.toString() : "+uri.toString());
//                System.out.println("tempFile.toString() : "+tempFile.toString());
//                startActivity(intent);
//
//                카테고리_스포츠 종목
//                choice_2_category
                if(choice_2_category.equals("") || choice_2_category.equals("소분류")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                    dialog = builder.setMessage("업종을 선택해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
//                System.out.println(" choice_1_category : "+ choice_1_category);
//                System.out.println(" choice_2_category : "+ choice_2_category);
//
                //업체명
                String f_name = facility_name.getText().toString();
                String f_business_hours = facility_business_hours.getText().toString();
                String f_convenience = facility_convenience.getText().toString();
                String f_etc = facility_etc.getText().toString();
                String f_intro = facility_intro.getText().toString();
                String f_receive_add =facility_receive_add.getText().toString();
                String f_detailed_add =facility_detailed_add.getText().toString();

                //빈공간 확인
                if(uri==null||uri.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                    dialog = builder.setMessage("사진을 등록해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }

                //빈공간 확인
                if(f_name.equals("")  ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                    dialog = builder.setMessage("빈칸 없이 입력 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }


                //빈공간 확인
                if(f_receive_add.equals("") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                    dialog = builder.setMessage("주소를 입력해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }

//                Map<String, RequestBody> params = new HashMap<>();
//                params.put("HM_ID",RequestBody.create(MediaType.parse("text/plain"), HM_ID));
//
//                File file = new File(String.valueOf(uri));
//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Part img = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
//
//                Call<HmRegisterDTO> postCall = restAPI.hm_register(HM_ID,choice_2_category,f_name,f_business_hours,f_convenience,f_etc,f_intro,f_receive_add,f_detailed_add,img);
//
//                postCall.enqueue(new Callback<HmRegisterDTO>() {
//                    @Override
//                    public void onResponse(Call<HmRegisterDTO> call, Response<HmRegisterDTO> response) {
//                        if(response.isSuccessful()) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
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
//                            Toast.makeText(Hm_facility_register.this,"등록 실패",Toast.LENGTH_SHORT);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<HmRegisterDTO> call, Throwable t) {
//                        Log.d("@@@@@@@@@@@@@@@xxxx", t.toString());
//                        Toast.makeText(Hm_facility_register.this, "네트워크 에러", Toast.LENGTH_SHORT);
//
//                    }
//                });


                Log.e("파일을좀 볼까요",tempFile.toString());

                File file = new File(tempFile.toString());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part img = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

                Call<DTO_HmFacility> postCall = restAPI.hm_register(HM_ID,choice_2_category,f_name,f_business_hours,f_convenience,f_etc,f_intro,f_receive_add,f_detailed_add,img);
                postCall.enqueue(new Callback<DTO_HmFacility>() {
                    @Override
                    public void onResponse(Call<DTO_HmFacility> call, Response<DTO_HmFacility> response) {

                        DTO_HmFacility DTO_HmFacility = response.body();
                        final String myfacility_name = DTO_HmFacility.getFacility_name();
                        final String myfacility_image = DTO_HmFacility.getFacility_image();

                        if(response.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Hm_facility_register.this);
                            Dialog dialog = builder.setMessage("등록성공")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(),Hm_MainActivity.class);
                                            intent.putExtra("userID",HM_ID);
                                            //
                                            intent.putExtra("facility_name",myfacility_name);
                                            intent.putExtra("facility_image",myfacility_image);

                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                        else {
                            Toast.makeText(Hm_facility_register.this,"등록 실패",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<DTO_HmFacility> call, Throwable t) {
                        Log.e("@@@@@@@@@@@@@@@xxxx", t.toString());
                        Toast.makeText(Hm_facility_register.this, "네트워크 에러", Toast.LENGTH_SHORT);

                    }
                });
            }
        });
    } //onCreate 끝


    //카메라 권한 설정 리스너
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //권한 허용 했을때 나오는 토스트메세지
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //권한 허용안했을때 나오는 토스트 메세지
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };

    //임시파일 경로 생성
    private File createImageFile() throws IOException {
        //이미지 파일 이름
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //이미지가 저장될 폴더 이름 //카메라 사진의 기본위치
        //수정전
         File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //수정후->생성안됨
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        //빈 파일 생성
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //수정전
        imageFilePath = imageFile.getAbsolutePath();
        //수정후
        CameraImageFilePath = "file://" + imageFile.getAbsolutePath();

        Log.d(this.getClass().getName()+"createImageFile", CameraImageFilePath + "");
        System.out.println("@@@@@@@@@@@@@CameraImageFilePath!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+CameraImageFilePath);
        System.out.println("@@@@@@@@@@@@@@imageFilePath!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+imageFilePath);

        return imageFile;
    }

    // 카메라 전용 크롭
    private void cropCameraImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(Hm_facility_register.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            grantUriPermission(res.activityInfo.packageName, uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, REQUEST_IMAGE_CROP);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            hm_facility_IMG = findViewById(R.id.hm_facility_IMG);

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            //Bitmap bitmap = BitmapFactory.decodeFile(CameraImageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
                //exif = new ExifInterface(CameraImageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);

            } else {
                exifDegree = 0;
            }

            //setImage();
            bitmap = rotate(bitmap, exifDegree);
            //hm_facility_IMG.setImageBitmap(bitmap);

            //회전값 반영한 비트맵이미지를 uri 로 바꿈
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "uri", null);
            uri = Uri.parse(path);

            System.out.println("@@@@@@@@@@@@@@path!!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+path);
            System.out.println("@@@@@@@여기확인좀@@@@@@uri!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+uri);

            cropCameraImage();

            MediaScannerConnection.scanFile(Hm_facility_register.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{uri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            tempFile = new File(imageFilePath);
            System.out.println("@@@@@@@여기확카메라찍찍tempFile!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+tempFile);

//            Rpath = uri.getPath();
//            System.out.println("@@@@@@@@@@@@@@Rpath!!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+Rpath);
            //수정전
            //hm_facility_IMG.setImageURI(uri);
            //수정후
//             hm_facility_IMG.setImageURI(Uri.parse(CameraImageFilePath));
//             hm_facility_IMG.setImageURI(Uri.parse(imageFilePath));
            //수정 전,후 모두 같은 에러
            //Unable to decode stream: android.graphics.ImageDecoder$DecodeException: Failed to create image decoder with message 'unimplemented'Input contained an error.
            //

            //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                            tempFile = null;
                        }
                    }
                }

                return;
            }


        }
        else if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            hm_facility_IMG = findViewById(R.id.hm_facility_IMG);
            uri = data.getData();
            System.out.println("@@@@@@@@@@@@@@uri!!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+uri);

           // Cursor cursor = null;
            //try {
                //Uri스키마를 content:/// -> file:///로 변경
                String[] proj = {MediaStore.Images.Media.DATA};

                //assert uri != null;
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

                cursor.moveToFirst();

                //assert cursor != null;
                int column_index = cursor.getColumnIndex(proj[0]);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();

                String imgDecodableString = cursor.getString(column_index);
                tempFile = new File(cursor.getString(column_index));
                System.out.println("@@@@@@@@@@@@@@tempFile @@@@@@@@@@@@@@@ : "+tempFile);


            //} finally {
                //if (cursor != null) {
                    cursor.close();
                //}
            //}
            //cropImage(uri);
            cropCameraImage();

            MediaScannerConnection.scanFile(Hm_facility_register.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{uri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            Rpath = uri.getPath();
            System.out.println("@@@@@@@@@@@@@@Rpath!!!!!!!!!! @@@@@@!!!!!!!@@@@@@@@@ : "+Rpath);

            //hm_facility_IMG.setImageURI(uri);
            hm_facility_IMG.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            System.out.println("@@@@@@@@@@@@@@uri @@@@@@@@@@@@@@@ : "+uri);


            //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

//                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
//                if (tempFile != null) {
//                    if (tempFile.exists()) {
//                        if (tempFile.delete()) {
//                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
//                            tempFile = null;
//                        }
//                    }
//                }

                return;
            }


        }
        else if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {

            //=============데스트
            try { //bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 80, 80);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.
                hm_facility_IMG.setImageBitmap(bitmap);
                //hm_facility_IMG.setImageURI(uri);



            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }

            //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                            tempFile = null;
                        }
                    }
                }

                return;
            }

        }
        else if(requestCode == REQUEST_SEARCH_ADDRESS ) {
            facility_receive_add = findViewById(R.id.facility_receive_add);

            if(resultCode == RESULT_OK){
//                assert data != null;
                String address = data.getExtras().getString("address");
                System.out.println(address);
                if(address!=null)
                  facility_receive_add.setText(address);
            } else {
                Toast.makeText(Hm_facility_register.this,"주소찾기 실패",Toast.LENGTH_SHORT).show();
            }
        }

        //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

            //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

    }


    //EXIF 정보를 회전각도로 변환하는 메소드, @param exifOrientation EXIF 회전각 / @return 실제 각도
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else
            return 0;
    }

    //이미지 정방향 회전시킴 @param bitmap 비트맵 이미지,degrees 회전 각도 / @return 회전된 이미지
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        //회전각도셋팅
        matrix.postRotate(degree);
        //이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
