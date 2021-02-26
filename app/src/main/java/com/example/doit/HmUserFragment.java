package com.example.doit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HmUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HmUserFragment extends Fragment {


    public static Intent serviceIntent;
    String ID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FindClassFragment.OnFragmentInteractionListener mListener;

    public HmUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HmUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HmUserFragment newInstance(String param1, String param2) {
        HmUserFragment fragment = new HmUserFragment();
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
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.hm_fragment_user, container, false);

        ID = Hm_MainActivity.ID;
        serviceIntent = Hm_MainActivity.serviceIntent;

        //비밀번호변경 change_PW
        final TextView hm_change_PW = rootView.findViewById(R.id.hm_change_PW);
        hm_change_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentC = new Intent(getActivity(), User_changePassword.class);
                intentC.putExtra("userID",ID);
                getActivity().startActivity(intentC);
            }
        });

        //로그아웃기능
        final TextView logout = rootView.findViewById(R.id.hm_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceIntent = new Intent(getActivity(), Svc_MyService.class);
                getActivity().stopService(serviceIntent);
                if (serviceIntent!=null) {
                    //stopService(serviceIntent);
                    serviceIntent = null;
                }
                ID = null;
//                ((Hm_MainActivity)getActivity()).stopService(serviceIntent); //로그아웃하면 서비스 중지.
//                //채팅목록 초기화
//                HmChatFragment.chatLists.clear();
//                //방정보 초기화
//                GM_Chat.ROOM_N = null;

                //
//                try {
//                    Svc_MyService.socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



//        return inflater.inflate(R.layout.hm_fragment_user, container, false);
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
