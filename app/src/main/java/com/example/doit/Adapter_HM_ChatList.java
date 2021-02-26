package com.example.doit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adapter_HM_ChatList extends RecyclerView.Adapter<Adapter_HM_ChatList.ViewHolder>{

    List<DTO_ChatList> items = new ArrayList<>();
    //해당 리사이클러뷰를 사용하는 맥티비티에서 어댑터를 초기화할 때  데이터(나의경우는 업체목록)가 담긴 리스트를 받아

    Context context;

    //생성자
    public Adapter_HM_ChatList(List<DTO_ChatList> items, Context context){
        this.items = items;
        this.context = context;
    }


    //onClick listener interface -> 리사이클러뷰 외부(액티비티, 프래그먼트,,등)에서 아이템 클릭이벤트를 처리하기 위해
    // interface onItemListener를 선언
    // -> 어댑터 내부에서 mListener선언, 외부에서 접근 가능하도록 setOnClickListener작성
    // -> onBindViewHolder or 뷰홀더에서 처리
    public interface onItemListener {
        void onItemClicked(int position);
    }

    private onItemListener mListener;

    public void setOnClickListener(onItemListener listener) {
        mListener = listener;
    }


   @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_hm_chatlist, parent, false);
        return new ViewHolder(itemView);
//        //다른표현
//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_class, parent, false);
//        ViewHolder viewHolder = new ViewHolder(rootView);
//        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        DTO_ChatList item = items.get(position);
        viewHolder.setItem(item, viewHolder);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView GM_ID;
        TextView MSG;
        TextView TIME;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            GM_ID = itemView.findViewById(R.id.chatlist_hm_gID);
            MSG = itemView.findViewById(R.id.chatlist_hm_lastmsg);
            TIME = itemView.findViewById(R.id.chatlist_hm_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //리스너 객체의 메소드 호출
                        if(mListener != null) {
                            mListener.onItemClicked(pos);
                        }
                    }

                }
            });

        }

        public void setItem(DTO_ChatList item, ViewHolder viewHolder){
            GM_ID.setText(item.getGM_ID());
            MSG.setText(item.getMSG());
            //TIME.setText(item.getTIME());
            if (item.getTIME_D()!=null) {
                long mNow;
                Date mDate;
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);
                SimpleDateFormat y = new SimpleDateFormat("yyyy");
                SimpleDateFormat  m = new SimpleDateFormat("M");
                SimpleDateFormat  d = new SimpleDateFormat("d");

                String nowY, nowM, nowD;
                nowY = y.format(mDate);
                nowM = m.format(mDate);
                nowD = d.format(mDate);

                String[] filter;
                filter = item.getTIME_D().split("@");
                //item.getTIME_D()=>년@월@일@시
                String ymd = filter[0]+"."+filter[1]+"."+filter[2];
                String md = filter[1]+"."+filter[2];

                if(!filter[0].equals(nowY)) { //년도 다르면
                TIME.setText(ymd);
            }
            else if (filter[1].equals(nowM) && filter[2].equals(nowD)) { //년도같고 다른월
                TIME.setText(item.getTIME());
            } else {
                TIME.setText(md);
            }
            } else {
                TIME.setText(item.getTIME());
            }


        }
    }

}
