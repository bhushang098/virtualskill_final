package com.twilio.video.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.twilio.video.app.ChatScreen;
import com.twilio.video.app.ChatUserResponse.Message;
import com.twilio.video.app.DetailedChatResponse.Datum;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;
import com.twilio.video.app.DetailedChatResponse.Messages;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ChatUSerAdapter extends RecyclerView.Adapter<ChatUSerAdapter.ChatUSerAdapterViewHolder>{

    List<Message> userList;
    Context context;
    String token;
    String userId;
    List<Datum> detailedChatList = new ArrayList<>();
    ChatItemAdapter chatItemAdapter;
    RecyclerView recyclerView;

    public ChatUSerAdapter(List<Message> userList, Context context,String token,String userId) {
        this.userList = userList;
        this.context = context;
        this.token = token;
        this.userId = userId;
        chatItemAdapter = new ChatItemAdapter(detailedChatList,context,userId);
    }

    @NonNull
    @Override
    public ChatUSerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.user_profile_chat_item,parent,false);
        return new ChatUSerAdapter.ChatUSerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUSerAdapterViewHolder holder, int position) {


        if(userList.get(position).getSender().getId().toString().equalsIgnoreCase(userId))
        {

            holder.userName.setText(userList.get(position).getReceiver().getName());
            if(userList.get(position).getReceiver().getProfile_path()!=null)
            {
                Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/"
                        +userList.get(position).getReceiver().getProfile_path()).
                        listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // holder.progressBar.setVisibility(View.INVISIBLE);

                                return false;
                            }
                        }).into(holder.userdp);
            }else {
                holder.userdp.setImageResource(R.drawable.profile_picture);
            }
        }else {
            holder.userName.setText(userList.get(position).getSender().getName());
            if(userList.get(position).getSender().getProfile_path()!=null)
            {
                Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/"
                        +userList.get(position).getSender().getProfile_path())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // holder.progressBar.setVisibility(View.INVISIBLE);

                                return false;
                            }
                        }).into(holder.userdp);
            }else {
                holder.userdp.setImageResource(R.drawable.profile_picture);
            }

        }
        holder.message.setText(userList.get(position).getContent());
        holder.timeStamp.setText(userList.get(position).getUpdatedAt().split(" ")[1]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<DetailedChatResponse> call;

                if(userList.get(position).getSender().getId().toString().equalsIgnoreCase(userId))
                {
                    call  = RetrifitClient.getInstance()

                            .getChatApi().getDetailedChatList(token,userList.get(position).getReceiver().getId()
                                            .toString()
                                    ,userList.get(position).getBelongsType());
                    inflateDetailedMessage(detailedChatList, userList.get(position).getReceiver().getId()
                            ,userList.get(position).getReceiver().getName());
                }else {
                    call  = RetrifitClient.getInstance()

                            .getChatApi().getDetailedChatList(token,userList.get(position).getSender().getId()
                                            .toString()
                                    ,userList.get(position).getBelongsType());

                    inflateDetailedMessage(detailedChatList, userList.get(position).getSender().getId()
                            ,userList.get(position).getSender().getName());

                }
                call.enqueue(new Callback<DetailedChatResponse>() {
                    @Override
                    public void onResponse(Call<DetailedChatResponse> call, Response<DetailedChatResponse> response) {
                        detailedChatList = response.body().getMessages().getData();
                        Collections.reverse(detailedChatList);
                        chatItemAdapter.setMessageList(detailedChatList);
                        recyclerView.smoothScrollToPosition(response.body().getMessages().getData().size());
                        chatItemAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<DetailedChatResponse> call, Throwable t) {
                    }
                });
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void inflateDetailedMessage(List<Datum> data, Integer id, String name) {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.chat_details_layout,
                null); // inflating popup layout
        PopupWindow mopoup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        EditText etMessage = popUpView.findViewById(R.id.et_message);
        Collections.reverse(data);
        recyclerView = popUpView.findViewById(R.id.rec_view_detailed_chat_with_user);
        ImageView ivSend = popUpView.findViewById(R.id.iv_send_chat_message);
        ImageView ivback = popUpView.findViewById(R.id.iv_toggle_chat_on_popup);
        TextView tvOtherUserName = popUpView.findViewById(R.id.tv_chat_with_user_name);
        tvOtherUserName.setText(name);
        mopoup.setAnimationStyle(android.R.style.Animation_Dialog);
        mopoup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(chatItemAdapter);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mopoup.dismiss();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeybaord(v);
                recyclerView.smoothScrollToPosition(detailedChatList.size());
                // Tost.makeText(context, etMessage.getText().toString(), Toast.LENGTH_SHORT).show();
                sendmessage(etMessage.getText().toString(),String.valueOf(id),mopoup);
            }
        });


        // Creation of popup

    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void sendmessage(String message, String otheruserId,PopupWindow popuseWindow) {

        Call <Map> call = RetrifitClient.getInstance().getChatApi()
                .sendChatMess(token,"u_2_u",message,otheruserId,"1");

        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.d("Responnse>>Chat ",response.raw().toString());
                if(response.body()!=null)
                {
                    Datum messageObj = new Datum();
                    messageObj.setContent(message);
                    messageObj.setUserId(userId);
                    messageObj.setBelongsTo(otheruserId);
                    messageObj.setCreatedAt("Now Now");
                    int index  = detailedChatList.size();
                    detailedChatList.add(index,messageObj);
                    chatItemAdapter.notifyItemInserted(index);
                    recyclerView.smoothScrollToPosition(index);
                    //recyclerView.smoothScrollToPosition(newchatist.size());
                    //popuseWindow.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.d("Exceptipn>>",t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ChatUSerAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userdp;
        TextView userName,message,timeStamp;

        public ChatUSerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userdp = itemView.findViewById(R.id.civ_dp_chat_user);
            userName = itemView.findViewById(R.id.tv_user_name_on_chat_user_list);
            timeStamp = itemView.findViewById(R.id.tv_mess_time_stamp_on_chat_user_list);
            message = itemView.findViewById(R.id.tv_last_message_on_chat_list_user);
        }
    }
}
