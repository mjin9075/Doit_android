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
 * Use the {@link HmChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HmChatFragment extends Fragment implements Adapter_HM_ChatList.onItemListener{
    private final String TAG = "HmChatFragment";

    private String HM_ID, myf_name, myf_img;

    private RestAPI restAPI;

    private RecyclerView hm_chatList_recyclerView;
    public static Adapter_HM_ChatList adapter_HM_ChatList;
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

    private HmChatFragment.OnFragmentInteractionListener mListener;

    public HmChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HmChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HmChatFragment newInstance(String param1, String param2) {
        HmChatFragment fragment = new HmChatFragment();
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
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출됨");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView() 호출됨");

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.hm_fragment_chat, container, false);

        if(getArguments() != null){
            isNoti = getArguments().getBoolean("isNoti",false);
            if(isNoti) {
                HM_ID = getArguments().getString("userID");
            }
        } else {
            HM_ID = Hm_MainActivity.ID;
            myf_name = Hm_MainActivity.myf_name;
            myf_img = Hm_MainActivity.myf_img;
        }


        chatData =  new ArrayList<>();

        hm_chatList_recyclerView = rootView.findViewById(R.id.hm_chatList_recyclerView);
        adapter_HM_ChatList = new Adapter_HM_ChatList(chatLists,getActivity());

        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        int fsize = chatLists.size();
        //if(fsize==0){
        Log.e("hm프래그Lists.size클리어전: ",chatLists.size()+"");
        chatLists.clear();
        Log.e("hm프래그Lists.size클리어후: ",chatLists.size()+"");

        room_list.clear();
        Call<DTO_chatList_list> call = restAPI.chatList_hm(HM_ID);
        call.enqueue(new Callback<DTO_chatList_list>() {
            @Override
            public void onResponse(Call<DTO_chatList_list> call, Response<DTO_chatList_list> response) {

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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    hm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
                    hm_chatList_recyclerView.setAdapter(adapter_HM_ChatList);
                    adapter_HM_ChatList.notifyDataSetChanged();

                    if(chatLists.size()==0){
                        TextView noChat = rootView.findViewById(R.id.noChat);
                        noChat.setVisibility(View.VISIBLE);
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

    //} else {
//        // adapter_findClass = new Adapter_FindClass(fInfoLists, getContext());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
//            hm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
//            hm_chatList_recyclerView.setAdapter(adapter_HM_ChatList);
//            adapter_HM_ChatList.notifyDataSetChanged();
//
//
//    }

        //어댑터의 클릭리스너 호출
        adapter_HM_ChatList.setOnClickListener(this);




//        final Button go_client = rootView.findViewById(R.id.go_client);
//        go_client.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), HM_Chat.class);
//                getActivity().startActivity(intent);
//            }
//        });

        //방 테스트
//        final Button go_chat_aa = rootView.findViewById(R.id.go_client_aa);
//        go_chat_aa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), GM_Chat.class);
//                intent.putExtra("name","aaa");
//                intent.putExtra("GM_ID", "aaa");
//                intent.putExtra("HM_ID",HM_ID);
//                intent.putExtra("ROOM_N","10");
//                getActivity().startActivity(intent);
//            }
//        });
//
//        final Button go_chat_bb = rootView.findViewById(R.id.go_client_bb);
//        go_chat_bb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), GM_Chat.class);
//                intent.putExtra("name","bbb");
//                intent.putExtra("GM_ID", "bbb");
//                intent.putExtra("HM_ID",HM_ID);
//                intent.putExtra("ROOM_N","11");
//                getActivity().startActivity(intent);
//            }
//        });


        return rootView;
    }

    @Override
    public void onItemClicked(int position) {
        Log.e("onItemClicked", String.valueOf(position));

        String ROOM_N = adapter_HM_ChatList.items.get(position).getROOM_N();
        String GM_ID = adapter_HM_ChatList.items.get(position).getGM_ID();
        //String MSG = adapter_HM_ChatList.items.get(position).getMSG();

       // String my_name = adapter_HM_ChatList.items.get(0).getFacility_name();
//        String my_name = chatLists.get(position).getFacility_name();
        //String my_img = adapter_HM_ChatList.items.get(0).getFacility_image();

//        Log.e("HM_my_name",my_name);
//        Log.e("HM_my_img",my_img);

        Intent intent = new Intent(getContext(),GM_Chat.class);
        intent.putExtra("name",GM_ID);
        intent.putExtra("ROOM_N",ROOM_N);
        intent.putExtra("HM_ID",HM_ID);
        intent.putExtra("GM_ID",GM_ID);
        intent.putExtra("isList",true);
        intent.putExtra("isNoti",false);
        intent.putExtra("Iam","HM");

        intent.putExtra("my_name",myf_name); //내 업체 이름
        intent.putExtra("my_img",myf_img); //내 업체 이미지

        System.out.println("GoChat_HM : "+ GM_ID +"///"+HM_ID+"///"+ROOM_N+"///"+myf_name+"///"+myf_img);
        //intent.putExtra("MSG",MSG);

        startActivity(intent);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Log.d(TAG, "ID : "+ HM_ID);
        Log.e(TAG, "HMchatLists.size():"+ chatLists.size());

        adapter_HM_ChatList = new Adapter_HM_ChatList(chatLists,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        hm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
        hm_chatList_recyclerView.setAdapter(adapter_HM_ChatList);
        adapter_HM_ChatList.notifyDataSetChanged();

        //어댑터의 클릭리스너 호출
        adapter_HM_ChatList.setOnClickListener(this);

//        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
//        int fsize = chatLists.size();
//        //if(fsize==0){
//        chatLists.clear();
//        room_list.clear();
//        Call<DTO_chatList_list> call = restAPI.chatList(HM_ID);
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
//                    hm_chatList_recyclerView.setLayoutManager(linearLayoutManager);
//                    hm_chatList_recyclerView.setAdapter(adapter_HM_ChatList);
//                    adapter_HM_ChatList.notifyDataSetChanged();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
