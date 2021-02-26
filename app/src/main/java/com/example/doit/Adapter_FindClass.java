package com.example.doit;

import android.content.Context;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

public class Adapter_FindClass extends RecyclerView.Adapter<Adapter_FindClass.ViewHolder> implements Filterable {

    List<DTO_HmFacility> items = new ArrayList<>(); //필터링 되지 않은 리스트
    List<DTO_HmFacility> itemsFilteredList = new ArrayList<>(); //필터링 된 리스트
    List<DTO_HmFacility> itemsFilteringList = new ArrayList<>(); //필터링 중인 리스트

    //해당 리사이클러뷰를 사용하는 맥티비티에서 어댑터를 초기화할 때  데이터(나의경우는 업체목록)가 담긴 리스트를 받아
    //필터링되지않은 리스트와, 필터링 된 리스트에 할당(getFiler와 리사이클러뷰의 사이즈를 결정하는 getItenCount에서 사용)

    Context context;

    //생성자
    public  Adapter_FindClass(List<DTO_HmFacility> items, Context context){
        this.items = items;
        this.itemsFilteredList = items;
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
        View itemView = inflater.inflate(R.layout.item_find_class, parent, false);
        return new ViewHolder(itemView);
//        //다른표현
//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_class, parent, false);
//        ViewHolder viewHolder = new ViewHolder(rootView);
//        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        DTO_HmFacility item = itemsFilteredList.get(position);
        viewHolder.setItem(item, viewHolder);

//                    //리스너 정의
//            if(mListener != null) {
//                final int pos = position;
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListener.onItemClicked(position);
//
//
//                    }
//                });
//            }

    }

    @Override
    public int getItemCount() {
        return itemsFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                                    //CharSequence는 입력된 스트링 ,사용될 액티비티에서 OnTextChanged()오버라이팅 해서 사용

                String charString = charSequence.toString();
                //if(charString.isEmpty()){
                if(charString==null || charString.length() == 0){
                    //입력된 스트링이 비어있다면, 필터링 되지 않은 리스트를 필터링된 리스트로 사용
                    itemsFilteredList = items;
                    //itemsFilteredList.addAll(items);
                } else {
                    //필터링 되지않은 리스트를 하나하나 검색해서, 일치하는 케이스에대해 필터링 중인 리스트에 추가
                    String filterPattern = charSequence.toString().toLowerCase().trim(); //toLowerCase() : 모두 소문자로 , trim():공백제거
                    itemsFilteringList.clear();
                    for(DTO_HmFacility item : items){
//                        if(item.getFacility_category().equals(charString) || item.getFacility_name().equals(charString)){
                        if(item.getFacility_category().contains(charString.toLowerCase()) || item.getFacility_name().contains(charString.toLowerCase()) || item.getFacility_address().contains(charString.toLowerCase())){
                            itemsFilteringList.add(item);
                            Log.e("items", item.toString());
                            Log.e("itemsFilteringList", itemsFilteringList.toString());
                        }
                    }
//                    for(DTO_HmFacility facility_category : items) {
//                        //필터대상 셋팅
//                        if(facility_category.getFacility_category().toLowerCase().contains(charString.toLowerCase()));{
//                            itemsFilteringList.add(facility_category);
//                        }
//                    }
                    itemsFilteredList = itemsFilteringList;
                }

                FilterResults results = new FilterResults();
                results.values = itemsFilteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFilteredList = (List<DTO_HmFacility>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView f_img;
        TextView f_name;
        TextView f_address;
        TextView f_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            f_img = itemView.findViewById(R.id.f_img);
            f_name = itemView.findViewById(R.id.f_name);
            f_address = itemView.findViewById(R.id.f_address);
            f_category = itemView.findViewById(R.id.f_category);

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

        public void setItem(DTO_HmFacility item, ViewHolder viewHolder){
            f_name.setText(item.getFacility_name());
            f_address.setText(item.getFacility_address());
            f_category.setText(item.getFacility_category());

            String IMG_BASE_URL = "서버URL";

            String imgURL = IMG_BASE_URL+item.getFacility_image();
            Glide.with(viewHolder.f_img.getContext()).load(imgURL).into(f_img);
        }
    }

}
