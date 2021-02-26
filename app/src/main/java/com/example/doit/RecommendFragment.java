package com.example.doit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecommendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecommendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendFragment extends Fragment {

    private AlertDialog dialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RecommendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendFragment newInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    //    @Override
//    public void onAttach(@NonNull Context context) {  //프래그먼트가 액티비티위에 올라갈ㄸ때 호출
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    //초기화해야하는 리소스들을 여기서 초기화 해줌
    //프래그먼트를 생성하면서 넘겨준 값들이 있다면, 여기서 변수에 넣어주면 됨
    //UI 초기화는 할 수 없다
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    //Layout을 inflate 하는곳.  view 객체를 얻을 수 있음
    //여기서 View를 초기화 할 수 있음
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 인플레이트(inflate)를 한다는 것은 동작 가능한 view 객체로 생성한다는 의미
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recommend, container, false);

        final Button recommend = rootView.findViewById(R.id.recommend);
       // System.out.println("온크뷰2");


        //choice_dis액티비티에서 다시 넘어올때 볼리를 통한 서버통신이 되지않아 item테이블에 아이디 유무에따른 분기를 할수없었음
        //안된이유-> choice_dis 액티비티에서 메인액티비티로 이동 시 ID를 넘기지않아
        //메인에서 프래그먼트로 replace될때 넘어오는 ID값이 없었다.
        //해결-> intent에 ID담아보냄
        final String ID = MainActivity.ID;


        //System.out.println("온크뷰3");


//        //MBTI test
//        Button test_start_button = rootView.findViewById(R.id.test_start_button);
//        test_start_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getActivity(),Test_1.class);
//                startActivity(intent);
//                getActivity().finish();
//
//
//            }
//        });



//        //Button test = rootView.findViewById(R.id.test);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //System.out.println("클릭클릭");
//
//                Intent intent = new Intent(getActivity(), Choice_like.class);
//                startActivity(intent);
//                //getActivity().finish();
//                //onDestroy();
//            }
//        });

        //추천버튼을 눌렀을때 선호도DB에서 사용자의 ID가 있는지 check
        //id가 있다(선호도 조사 했다)-> 추천진행
        //id가 없다-> test를 진행할지 물어본다(dialog) , 선호도 없이 추천원하는경우 사용자 평가점수가 높은 항목을 추천
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                        try {
                            JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                            boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                            if(success) { //ID가 없다면(선택데이터가 없다->test 진행)
                                //다이얼로그 띄우기기
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("").setMessage("조금 더 정확한 추천을 위한 "+ID+"님의 정보가 부족해요");

                                builder.setPositiveButton("test하러가기", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id){
                                        Intent intentR = new Intent(getActivity(), Test_1.class);
                                        intentR.putExtra("userID",ID);
                                        getActivity().startActivity(intentR);
                                    }
                                });

                                builder.setNeutralButton("인기종목 보러가기", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id){
                                        Intent intentR = new Intent(getActivity(), Recommend_result.class);
                                        intentR.putExtra("userID",ID);
                                        getActivity().startActivity(intentR);
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                           } else { //ID가 있다(선택데이터가 있다->추천진행)
                                Intent intentR = new Intent(getActivity(), Recommend_result.class);
                                intentR.putExtra("userID",ID);
                                getActivity().startActivity(intentR);
                                //getActivity().finish();


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



            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        final Button button_test = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recommend,null,false).findViewById(R.id.test);
        final Button button_rec = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recommend,null,false).findViewById(R.id.recommend);
        String ID = MainActivity.ID;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //해당 웹사이트에 접속한 이후, 특정 JSON 응답을 다시 받을 수 있도록작성
                try {
                    JSONObject jsonResponse = new JSONObject(response); //JSONObject를 만들어서 response값을 넣어줌
                    boolean success = jsonResponse.getBoolean("success"); //해당과정이 정상적으로 진행 되었는지(응답 값 확인)

                    if(success) { //ID가 없다면(선택데이터가 없다->test 진행)

                    } else { //ID가 있다(선택데이터가 있다->추천진행)

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

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onDetach() { //프래그먼트가 액티비티에서 사라진 순간
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
