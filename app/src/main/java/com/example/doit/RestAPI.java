package com.example.doit;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestAPI {

    @FormUrlEncoded
    @POST("testRegister.php") //회원가입
//    Call<UserDTO> register (@Body UserDTO userDTO);
    Call<DTO_User> register (@Field("ID") String ID, @Field("passwd") String passwd, @Field("Email")  String Email, @Field("Mtype") String Mtype);

//    @POST("testRegister.php")
//    Call<Map<String, String>> signUp(@Body UserDTO userDTO);

//    @Headers({"User-Agent: android"})
//    @POST("testLogin.php")
//    Call<Map<String, UserDTO>> authentication(@Body LoginDTO loginDTO);

//    @FormUrlEncoded
//    @POST("testRegister.php")
//    Call<HmRegisterDTO> hm_register (@Field("ID") String ID, @Field("passwd") String passwd,@Field("Email")  String Email, @Field("Mtype") String Mtype);

    @Multipart
    @POST("hm_register.php") //hm회원 시설등록
    Call<DTO_HmFacility> hm_register(@Part("HM_ID") String HM_ID,
                                     @Part("facility_category") String facility_category,
                                     @Part("facility_name") String facility_name,
                                     @Part("facility_business_hours") String facility_business_hours,
                                     @Part("facility_convenience") String facility_convenience,
                                     @Part("facility_etc") String facility_etc,
                                     @Part("facility_intro") String facility_intro,
                                     @Part("facility_address") String facility_address,
                                     @Part("facility_address_detail") String facility_address_detail,
                                     @Part MultipartBody.Part img);


    @FormUrlEncoded
    @POST("facilityCheck_HMID.php") //hm회원 아이디 보내서 자신이 등록한 시설정보 받아오기
    Call<DTO_HmFacility> hm_facility (@Field("HM_ID") String ID);

    @FormUrlEncoded
    @POST("checkChatRoom.php") //gm,hm 아이디 보내서 채팅방 확인하기
    Call<DTO_ChatRoom> checkChatRoom (@Field("GM_ID") String GM_ID, @Field("HM_ID") String HM_ID);

    @FormUrlEncoded
    @POST("chatList_hm.php") //아이디 보내서 채팅목록 받아오기
    Call<DTO_chatList_list> chatList_hm (@Field("ID") String ID);

    @FormUrlEncoded
    @POST("chatList_gm.php") //아이디 보내서 채팅목록 받아오기
    Call<DTO_chatList_list> chatList_gm (@Field("ID") String ID);

    @FormUrlEncoded
    @POST("saveLastMSG.php") //룸넘버, 마지막메세지, 시간 보내서 서버에 저장
    Call<DTO_ChatRoom> saveLastMSG (@Field("ROOM_N") String ROOM_N, @Field("MSG") String MSG , @Field("TIME") String TIME, @Field("TIME_D") String TIME_D);

    @POST("find_facility.php")
    Call<DTO_facilityList> find_facility ();




}
