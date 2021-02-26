package com.example.doit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecoItemAdapter extends RecyclerView.Adapter<RecoItemAdapter.ViewHolder> {
    private ArrayList<String> mData = null;


    //아이템뷰를 저장하는 뷰홀더클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //뷰객체대한 참조
            textView = itemView.findViewById(R.id.text1);
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음
    RecoItemAdapter(ArrayList<String> list) {
        mData = list;
    }


    @NonNull
    @Override
    //아이템 뷰를 위한 뷰홀더객체 생성해서 리턴
    public RecoItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.reco_item, parent, false);
        RecoItemAdapter.ViewHolder viewHolder = new RecoItemAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    //position에 해당하는 델=이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull RecoItemAdapter.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);
    }

    @Override
    //전체 데이터 갯수 리턴
    public int getItemCount() {
        return mData.size();
//        return 1;
    }





}
