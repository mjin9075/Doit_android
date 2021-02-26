package com.example.doit;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HmMyClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HmMyClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FindClassFragment.OnFragmentInteractionListener mListener;

    public HmMyClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HmMyClassFragment newInstance(String param1, String param2) {
        HmMyClassFragment fragment = new HmMyClassFragment();
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
        // 동작가능한 view객체로 생성
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.hm_fragment_my_class,container,false);

        //Hm_MainActivity에서 ID가져오기기
       final String HM_ID = Hm_MainActivity.ID;
       final String IMG_BASE_URL = "서버URL";

        RestAPI restAPI = RestRequestHelper.getClient().create(RestAPI.class);

        //이미지 Glide 테스트
        final ImageView iv_image = rootView.findViewById(R.id.hm_myClass_img);

        final TextView hm_myClass_category2 = rootView.findViewById(R.id.hm_myClass_category2);
        final TextView hm_myClass_f_name = rootView.findViewById(R.id.hm_myClass_f_name);
        final TextView hm_myClass_f_bh = rootView.findViewById(R.id.hm_myClass_f_business_hours);
        final TextView hm_myClass_f_con = rootView.findViewById(R.id.hm_myClass_f_convenience);
        final TextView hm_myClass_f_etc = rootView.findViewById(R.id.hm_myClass_f_etc);
        final TextView hm_myClass_f_intro = rootView.findViewById(R.id.hm_myClass_f_intro);
        final TextView hm_myClass_f_add = rootView.findViewById(R.id.hm_myClass_f_add);
        final TextView hm_myClass_f_add_d = rootView.findViewById(R.id.hm_myClass_f_add_detailed);


        Call<DTO_HmFacility> call = restAPI.hm_facility(HM_ID);
        call.enqueue(new Callback<DTO_HmFacility>() {
            @Override
            public void onResponse(Call<DTO_HmFacility> call, Response<DTO_HmFacility> response) {
                DTO_HmFacility DTOHmFacility = response.body();

                String getID = DTOHmFacility.getHM_ID();
                String f_category = DTOHmFacility.getFacility_category();
                String f_img = DTOHmFacility.getFacility_image();

                String f_name = DTOHmFacility.getFacility_name();
                String f_hours = DTOHmFacility.getFacility_business_hours();
                String f_conve = DTOHmFacility.getFacility_convenience();
                String f_etc = DTOHmFacility.getFacility_etc();
                String f_intro = DTOHmFacility.getFacility_intro();
                String f_address = DTOHmFacility.getFacility_address();
                String f_address_d = DTOHmFacility.getFacility_address_detail();
                if (response.isSuccessful()) { //check for Response status //onResponse 통신 성공시 Callback //response.isSuccessful()로 정상(2xx)인지 확인

                    String imgURL = IMG_BASE_URL+f_img;
                    Glide.with(getContext()).load(imgURL).into(iv_image);
                    Log.e("imgURL결과는", imgURL);

                    hm_myClass_category2.setText(f_category);
                    hm_myClass_f_name.setText(f_name);
                    hm_myClass_f_bh.setText(f_hours);
                    hm_myClass_f_con.setText(f_conve);
                    hm_myClass_f_etc.setText(f_etc);
                    hm_myClass_f_intro.setText(f_intro);
                    hm_myClass_f_add.setText(f_address);
                    hm_myClass_f_add_d.setText(f_address_d);

                } else {
                    //통신이 실패한 경우 (응답토그 3xx, 4xx등)
                    //onResponse가 무조건 성공응답이 아니기에 확인필요
                    Toast.makeText(getContext(), "가져오기기 실패", Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onFailure(Call<DTO_HmFacility> call, Throwable t) {

            }
        });


        //return inflater.inflate(R.layout.hm_fragment_my_class, container, false);
        return rootView;
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
