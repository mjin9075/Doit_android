package com.example.doit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {


    public static Intent serviceIntent;
    String ID;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);

        ID = MainActivity.ID;
        serviceIntent = Svc_MyService.serviceIntent;


        //관심사 변경 (관심사데이터가 있을경우와 없을경우를 생각)
        final TextView interest = rootView.findViewById(R.id.interest);
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                        try {
                            JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                            boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                            if(success) { //ID가 없다면(관심사 데이터가 없다->test 진행)

                                Intent intentR = new Intent(getActivity(), Test_1.class);
                                intentR.putExtra("userID",ID);
                                getActivity().startActivity(intentR);


                            } else { //ID가 있다(관심사 데이터가 있다->관심사 변경)
                                Intent intentR = new Intent(getActivity(), User_choice_like.class);
                                intentR.putExtra("userID",ID);
                                getActivity().startActivity(intentR);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                //실제로 접속 할 수 있도록 ValidateRequest 생성자를 통해 객체를 만들어 준다.
                VolleyR_CheckDBRequest volleyRCheckDBRequest = new VolleyR_CheckDBRequest(ID, responseListener);

                //요청을 보낼 수 있도록 큐를 만든다
                RequestQueue queue = Volley.newRequestQueue(getActivity());

                //만들어진 큐에, ValidateRequest 객체를 넣어준다.
                queue.add(volleyRCheckDBRequest);

                //onDestroy();
            }
        });


        //비밀번호변경 change_PW
        final TextView change_PW = rootView.findViewById(R.id.change_PW);
        change_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentC = new Intent(getActivity(), User_changePassword.class);
                intentC.putExtra("userID",ID);
                getActivity().startActivity(intentC);
            }
        });


        //로그아웃기능
        final TextView logout = rootView.findViewById(R.id.log_out);
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

//                ((MainActivity)getActivity()).stopService() //로그아웃하면 서비스 중지.
                //((MainActivity)getActivity()).stopService(serviceIntent); //로그아웃하면 서비스 중지.


                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                //onDestroy();
            }
        });

        //return inflater.inflate(R.layout.fragment_user, container, false);
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
