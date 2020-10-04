package com.twilio.video.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.DetailedChatResponse.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.util.TimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatItemAdapterViewHolder> {

    List<Datum> messageList;
    Context context;
    String userId;

    public ChatItemAdapter(List<Datum> messageList, Context context,String userid) {
        this.messageList = messageList;
        this.context = context;
        this.userId = userid;
    }

    public List<Datum> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Datum> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ChatItemAdapter.ChatItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.chat_item,parent,false);
        return new ChatItemAdapter.ChatItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemAdapter.ChatItemAdapterViewHolder holder, int position) {

        if(messageList.get(position).getUserId().equalsIgnoreCase(userId))
        {
            holder.linearLayoutOthers1.setVisibility(View.GONE);
            holder.linearLayoutOthers2.setVisibility(View.GONE);
            holder.tvMyMess.setText(messageList.get(position).getContent());
            holder.tvmyName.setText(" (You)");


            try {
                long time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(messageList.get(position).getCreatedAt()).getTime();
                holder.tv_myTimeStamp.setText(new TimeService().getTimeAgo(time, System.currentTimeMillis()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // holder.tv_myTimeStamp.setText(messageList.get(position).getCreatedAt().split(" ")[0]);
        }else {

            holder.linearLayoutMine1.setVisibility(View.GONE);
            holder.linearLayoutMine2.setVisibility(View.GONE);
            holder.tvFrontUserMess.setText(messageList.get(position).getContent());
            holder.tvfrontUserName.setText(messageList.get(position).getAuthor());

            try {
                long time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(messageList.get(position).getCreatedAt()).getTime();
                holder.tvfrontUserTimeStamp.setText(new TimeService().getTimeAgo(time, System.currentTimeMillis()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //holder.tvfrontUserTimeStamp.setText(messageList.get(position).getCreatedAt().split(" ")[0]);

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatItemAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvfrontUserName,tvfrontUserTimeStamp,tvFrontUserMess,
                tvmyName,tvMyMess,tv_myTimeStamp;
        LinearLayout linearLayoutMine1,linearLayoutMine2,linearLayoutOthers1,linearLayoutOthers2;

        public ChatItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvfrontUserName = itemView.findViewById(R.id.tv_user_name_on_chat_item);
            tvfrontUserTimeStamp = itemView.findViewById(R.id.tv_mess_time_stamp_on_chat_item);
            tvmyName = itemView.findViewById(R.id.tv_user_name_on_chat_item_self);
            tvMyMess = itemView.findViewById(R.id.tv_my_message);
            tvFrontUserMess = itemView.findViewById(R.id.tv_front_user_mess);
            tv_myTimeStamp = itemView.findViewById(R.id.tv_mess_time_stamp_on_chat_item_self);

            linearLayoutMine1 = itemView.findViewById(R.id.lin_lay_my_mess_top);
            linearLayoutMine2 = itemView.findViewById(R.id.lin_lay_my_mess_actual);

            linearLayoutOthers1 = itemView.findViewById(R.id.lin_lay_front_user);
            linearLayoutOthers2 = itemView.findViewById(R.id.lin_lay_front_user_mess);
        }
    }
}
