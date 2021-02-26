package com.example.doit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindClassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindClassFragment extends Fragment implements Adapter_FindClass.onItemListener {

    private RestAPI restAPI;

    private RecyclerView findClass_recyclerView;
    private Adapter_FindClass adapter_findClass;
    private List<DTO_HmFacility> fInfoLists = new ArrayList<>(); //리싸이클러뷰 아답터에 넣을 리스트

    private EditText findClass_edt;

    private String strResult;
    private String preText;

//    final List<DTO_HmFacility> fInfoLists = new ArrayList<>();
//        final RecyclerView findClass_recyclerView = rootView.findViewById(R.id.findClass_recyclerView);
//        final Adapter_FindClass[] adapter_findClass = new Adapter_FindClass[1];



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FindClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindClassFragment newInstance(String param1, String param2) {
        FindClassFragment fragment = new FindClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); //OptionsMenu 사용하기위해(나의경우 searchView를 쓰기위해)
        // Inflate the layout for this fragment
        // 인플레이트(inflate)를 한다는 것은 동작 가능한 view 객체로 생성한다는 의미
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_find_class, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final String ID = MainActivity.ID;
        final String IMG_BASE_URL = "서버URL";

        restAPI = RestRequestHelper.getClient().create(RestAPI.class);
        findClass_recyclerView = rootView.findViewById(R.id.findClass_recyclerView);
        //adapter_findClass = new Adapter_FindClass(fInfoLists,getActivity());

        //final TextView class_text = rootView.findViewById(R.id.class_text);
        findClass_edt = rootView.findViewById(R.id.findClass_edt);
        //findClass_edt.addTextChangedListener(textWatcher);

        int fsize = fInfoLists.size();
        System.out.println("fsize: "+fsize);


//        recommend_result에서 넘겨받은 값
        if(getArguments() != null){
            strResult = getArguments().getString("strResult");
//            System.out.println("strResult_oneClass : "+strResult);
//            findClass_edt.addTextChangedListener(textWatcher);
            findClass_edt.setText(strResult);
            Log.e("adapter","5");

            //findClass_edt.addTextChangedListener(textWatcher);
            //adapter_findClass.getFilter().filter(strResult);
            Log.e("adapter","6");
        } else {
            findClass_edt.addTextChangedListener(textWatcher);
            Log.e("adapter", "7");
        }

        adapter_findClass = new Adapter_FindClass(fInfoLists, getContext());

        if(fsize==0){
            //서버에서 전체리스트 받아오기
            Call<DTO_facilityList> call = restAPI.find_facility();
            call.enqueue(new Callback<DTO_facilityList>() {
                @Override
                public void onResponse(Call<DTO_facilityList> call, Response<DTO_facilityList> response) {

                    if(response.isSuccessful()){
                        DTO_facilityList facilityList = response.body();

                        //Log.e("아 뭘 받아옵니까", facilityList.getfList().toString());

                        List<DTO_HmFacility> fInfoList2 = facilityList.getfList();

                        for(DTO_HmFacility fInfoList : fInfoList2){ //서버에서 받은( DTO를 통해 나온 )리스트의 항목을 하나씩 빼서 리사이클러뷰에 뿌려질 리스트에 담음
                            Log.e("아 받아옵니까", fInfoList.getFacility_name() );
                            fInfoLists.add(fInfoList);
                        }

                        Log.e("adapter","1");

                        //adapter_findClass = new Adapter_FindClass(fInfoLists, getContext());
                        Log.e("adapter","1");
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        Log.e("adapter","2");
                        findClass_recyclerView.setLayoutManager(linearLayoutManager);
                        Log.e("adapter","3");
                        findClass_recyclerView.setAdapter(adapter_findClass);
                        Log.e("adapter","4");



                        if(strResult  != null) {
                            Log.e("adapter", "777");
                            adapter_findClass.getFilter().filter(strResult);
                            Log.e("adapter", "888");
                            strResult = null;
                            System.out.println("strResult : " +strResult);
                            findClass_edt.addTextChangedListener(textWatcher);
                            Log.e("adapter", "999");
                        }
                    } else {
                        Log.e("retro", 2+"Error");
                    }
                }
                @Override
                public void onFailure(Call<DTO_facilityList> call, Throwable t) {
                    Log.e("@@Frag_onFailurexx", t.toString());
                }
            });
        } else {
           // adapter_findClass = new Adapter_FindClass(fInfoLists, getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            findClass_recyclerView.setLayoutManager(linearLayoutManager);
            findClass_recyclerView.setAdapter(adapter_findClass);

        }

        //어댑터의 클릭리스너 호출
        adapter_findClass.setOnClickListener(this);

        //return inflater.inflate(R.layout.fragment_find_class, container, false);
        return rootView;
    }


    //Edit Text가 눌릴때 마다 감지하는 부분
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override //변경되기전 문자열
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.e("adapter","8");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 Log.e("adapter", "9");
                adapter_findClass.getFilter().filter(charSequence);
                Log.e("adapter", "10");
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.e("adapter","11");
            //adapter_findClass.getFilter().filter(editable);
       }
    };

    //리사이클러뷰 클릭이벤트 정의
    @Override
    public void onItemClicked(int position) {
        Log.e("onItemClicked", String.valueOf(position));

        String HM_ID = adapter_findClass.itemsFilteredList.get(position).getHM_ID();
        String f_name = adapter_findClass.itemsFilteredList.get(position).getFacility_name();
        String f_img = adapter_findClass.itemsFilteredList.get(position).getFacility_image();
        String f_intro = adapter_findClass.itemsFilteredList.get(position).getFacility_intro();
        String f_bh = adapter_findClass.itemsFilteredList.get(position).getFacility_business_hours();

        String f_con = adapter_findClass.itemsFilteredList.get(position).getFacility_convenience();
        String f_etc = adapter_findClass.itemsFilteredList.get(position).getFacility_etc();
        String f_add = adapter_findClass.itemsFilteredList.get(position).getFacility_address();
        String f_add_d = adapter_findClass.itemsFilteredList.get(position).getFacility_address_detail();


        Intent intent = new Intent(getContext(),FindClassDetail.class);

        intent.putExtra("HM_ID",HM_ID);
        intent.putExtra("f_name",f_name);
        intent.putExtra("f_img",f_img);
        intent.putExtra("f_intro",f_intro);
        intent.putExtra("f_bh",f_bh);

        intent.putExtra("f_con",f_con);
        intent.putExtra("f_etc",f_etc);
        intent.putExtra("f_add",f_add);
        intent.putExtra("f_add_d",f_add_d);


        startActivity(intent);





    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        //fInfoLists.clear();
    }

    public void setArgument(Bundle bundle) {
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
