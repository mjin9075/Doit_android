package com.example.doit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.ViewHolder> {
//    private List<DTO_Chat> mDataset;
//    private String myNickName;
    //생성자
//    public Adapter_Chat(List<DTO_Chat> myDataset, Context context, String myNickName){
//        mDataset = myDataset;
//        this.myNickName = myNickName;
//    }

    private final List<DTO_Chat> mData;
    private final String mID;

//    public Adapter_Chat(List<DTO_Chat> list) {
    public Adapter_Chat(String ID, List<DTO_Chat> list) {
        mData = list;
        mID = ID;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView TextView_nickname;
//        public TextView TextView_msg;
//        public View rootView;

        LinearLayout layoutMe;
        LinearLayout layoutYou;
        de.hdodenhof.circleimageview.CircleImageView img_you;
        TextView nickname;
        TextView bubbleYou;
        TextView bubbleMe;
        TextView timeMe;
        TextView timeYou;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            TextView_nickname = itemView.findViewById(R.id.TextView_nickname);
//            TextView_msg = itemView.findViewById(R.id.TextView_msg);

            layoutMe = itemView.findViewById(R.id.layout_me);
            layoutYou = itemView.findViewById(R.id.layout_you);

            img_you = itemView.findViewById(R.id.img_you);

            nickname =  itemView.findViewById(R.id.nickname_text);
            bubbleYou =  itemView.findViewById(R.id.bubble_you_text);
            bubbleMe =  itemView.findViewById(R.id. bubble_me_text);
            timeMe =  itemView.findViewById(R.id. time_me);
            timeYou =  itemView.findViewById(R.id. time_you);


        }
    }

   @NonNull
    @Override
    public Adapter_Chat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_row_chat, parent, false);

       ViewHolder vh = new ViewHolder(v);
       return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
//        DTO_Chat chat = mDataset.get(position);
//
//        holder.TextView_nickname.setText(chat.getNickname());
//        holder.TextView_msg.setText(chat.getMsg());
//
//        if(chat.getNickname().equals(this.myNickName)) {
//            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        }
//        else {
//            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//        }

        DTO_Chat chat = mData.get(position);
        //if (chat.isMe) {
        if (mID.equals(chat.senderID)) {


            viewHolder.bubbleMe.setText(chat.message);
            viewHolder.timeMe.setText(chat.time_c);

            viewHolder.layoutMe.setVisibility(View.VISIBLE);
            viewHolder.layoutYou.setVisibility(View.GONE);

        } else {
            viewHolder.bubbleYou.setText(chat.message);
//            viewHolder.nickname.setText(chat.senderID);
            viewHolder.nickname.setText(chat.my_name); //보낸사람의 name(gm경우 id, hm의경우 시설이름)
            viewHolder.timeYou.setText(chat.time_c);

            //이미지
            //보낸사람이 gm일 경우, drawable의 이미지, hm일 경우 글라이드로 \

            if(chat.Iam.equals("HM")) {
                String IMG_BASE_URL = "서버URL";

                String imgURL = IMG_BASE_URL+chat.my_img;
                Glide.with(viewHolder.img_you.getContext()).load(imgURL).into(viewHolder.img_you);

            } else if(chat.Iam.equals("GM")) {
                viewHolder.img_you.setImageResource(R.drawable.ic_person_black_24dp);
            }



            viewHolder.layoutMe.setVisibility(View.GONE);
            viewHolder.layoutYou.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return mData == null ? 0 :  mData.size();
//                return mData.size();
    }

    public DTO_Chat getChat(int position) {
        //return mDataset != null ? mDataset.get(position) : null;
        return mData.get(position);

    }
//
//    public void addChat(DTO_Chat chat) {
//        mDataset.add(chat);
//        notifyItemInserted(mDataset.size()-1); //갱신
//    }



    @Override
    public long getItemId(int position) {
        return position;
    }


}
