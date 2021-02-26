package com.example.doit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GMchatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GMchatListFragment extends Fragment implements Adapter_GM_ChatList.onItemListener{
    private final String TAG = "GMchatListFragment";

    private String GM_ID;

    private RestAPI restAPI;

    private RecyclerView gm_chatList_recyclerView;
    public static Adapter_GM_ChatList adapter_GM_ChatList;
    public static List<DTO_ChatList> chatLists = new ArrayList<>();
    public static List<String> room_list = new ArrayList<>();

    public static HashMap<String, List<DTO_Chat>> map = new HashMap<>();
    public static List<DTO_Chat> chatData;

    private boolean isNoti;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GMchatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GMchatListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GMchatListFragment newInstance(String param1, String param2) {
        GMchatListFragment fragment = new GMchatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출됨");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated() 호출됨");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() 호출됨");

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출됨");

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_gm_chat_list, container, false);

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gm_chat_list, container, false);

        if(getArguments() != null){
            isNoti = getArguments().getBoolean("isNoti",false);
            if(isNoti) {
                GM_ID = getArguments().getString("userID");
            }
        } else {
            GM_ID = MainActivity.ID;
        }

        chatData =  new ArrayList<>();
        gm_chatList_recyclerView = rootView.findViewById(R.id.gm_chatList_recyclerView);
        adapter_GM_ChatList = new Adapter_GM_ChatList(chatLists,getActivity());

        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        int fsize = chatLists.size();
        //if(fsize==0){
        Log.e("gm프래그Lists.size클리어전: ",chatLists.size()+"");
        chatLists.clear();
        Log.e("gm프래그Lists.size클리어후: ",chatLists.size()+"");
        room_list.clear();
        Call<DTO_chatList_list> call = restAPI.chatList_gm(GM_ID);
        call.enqueue(new Callback<DTO_chatList_list>() {
            @Override
            public void onResponse(Call<DTO_chatList_list> call, Response<DTO_chatList_list> response) {
                Log.e("GMchatListResponse",response+"");

                DTO_chatList_list DTO_chatList_list = response.body();
                List<DTO_ChatList> fInfoList2 = DTO_chatList_list.getfList();

                if(response.isSuccessful()) {
                    for(DTO_ChatList fInfoList : fInfoList2) {
                        chatLists.add(fInfoList);
                        Log.e("내가가진방번호 : ",fInfoList.getROOM_N());
                        room_list.add(fInfoList.getROOM_N());
                        //map.put(fInfoList.getROOM_N(),chatData);
                    }
                    //Log.e("방번호 해쉬맵:",map.entrySet()+"");

                    Log.e(TAG, "chatLists.통신:"+ chatLists.size());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    gm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
                    gm_chatList_recyclerView.setAdapter(adapter_GM_ChatList);
                    adapter_GM_ChatList.notifyDataSetChanged();

                    if(chatLists.size()==0){
                        TextView noChatG = rootView.findViewById(R.id.noChatG);
                        noChatG.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.e("retro", 2+"Error");
                }
            }
            @Override
            public void onFailure(Call<DTO_chatList_list> call, Throwable t) {
                Log.e("@@Frag_onFailurexx", t.toString());
            }
        });

        Log.e(TAG, "chatLists.온크리:"+ chatLists.size());

        //어댑터의 클릭리스너 호출
        adapter_GM_ChatList.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClicked(int position) {
        Log.e("onItemClicked", String.valueOf(position));

        String ROOM_N = adapter_GM_ChatList.items.get(position).getROOM_N();
        String HM_ID = adapter_GM_ChatList.items.get(position).getHM_ID();
        //String MSG = adapter_HM_ChatList.items.get(position).getMSG();
        String f_name = adapter_GM_ChatList.items.get(position).getFacility_name();
        String f_img = adapter_GM_ChatList.items.get(position).getFacility_image();

        Log.e("onItemClicked : ", String.valueOf(position));

        Intent intent = new Intent(getContext(),GM_Chat.class);
        intent.putExtra("name",f_name);
        intent.putExtra("ROOM_N",ROOM_N);
        intent.putExtra("HM_ID",HM_ID);
        intent.putExtra("GM_ID",GM_ID);
        intent.putExtra("isList",true);
        intent.putExtra("isNoti",false);
        intent.putExtra("Iam","GM");

        intent.putExtra("my_name",GM_ID);
        intent.putExtra("my_img","");
        intent.putExtra("f_img",f_img);

        System.out.println("GoChat_GM : "+ GM_ID +"///"+HM_ID+"///"+ROOM_N);

        //intent.putExtra("MSG",MSG);

        startActivity(intent);

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");
        Log.d(TAG, "ID : "+ GM_ID);
        Log.e(TAG, "chatLists.size():"+ chatLists.size());


//        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gm_chat_list, container, false);
//
//        gm_chatList_recyclerView = rootView.findViewById(R.id.gm_chatList_recyclerView);
        adapter_GM_ChatList = new Adapter_GM_ChatList(chatLists,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        gm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
        gm_chatList_recyclerView.setAdapter(adapter_GM_ChatList);
        adapter_GM_ChatList.notifyDataSetChanged();

        //어댑터의 클릭리스너 호출
        adapter_GM_ChatList.setOnClickListener(this);


//        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
//
//        chatLists.clear();
//        room_list.clear();
//        Call<DTO_chatList_list> call = restAPI.chatList_gm(GM_ID);
//        call.enqueue(new Callback<DTO_chatList_list>() {
//            @Override
//            public void onResponse(Call<DTO_chatList_list> call, Response<DTO_chatList_list> response) {
//
//                DTO_chatList_list DTO_chatList_list = response.body();
//                List<DTO_ChatList> fInfoList2 = DTO_chatList_list.getfList();
//
//                if(response.isSuccessful()) {
//                    for(DTO_ChatList fInfoList : fInfoList2) {
//                        chatLists.add(fInfoList);
//                        Log.e("내가가진방번호 : ",fInfoList.getROOM_N());
//                        room_list.add(fInfoList.getROOM_N());
//                    }
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
//                    gm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
//                    gm_chatList_recyclerView.setAdapter(adapter_GM_ChatList);
//                    adapter_GM_ChatList.notifyDataSetChanged();
//
//                } else {
//                    Log.e("retro", 2+"Error");
//                }
//            }
//            @Override
//            public void onFailure(Call<DTO_chatList_list> call, Throwable t) {
//                Log.e("@@Frag_onFailurexx", t.toString());
//            }
//        });



    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");
        Log.e(TAG, "chatLists.onStop():"+ chatLists.size());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() 호출됨");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");

    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() 호출됨");
    }

}