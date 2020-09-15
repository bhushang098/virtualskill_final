package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.ChatScreen;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.NotificationAlertResponse.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.subMainPages.ClassDetails;
import com.twilio.video.app.subMainPages.SkillDetailsPage;
import com.twilio.video.app.subMainPages.TeamDetailsPage;
import com.twilio.video.app.util.TimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationAlertAdapter extends RecyclerView.Adapter<NotificationAlertAdapter.NotificationAlertAdapterViewHolder> {

    List<Datum> notiList;
    Context context;
    String hisId;
    String token;

    public NotificationAlertAdapter(List<Datum> notiList, Context context, String hisId, String token) {
        this.notiList = notiList;
        this.context = context;
        this.hisId = hisId;
        this.token = token;
    }


    @NonNull
    @Override
    public NotificationAlertAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationAlertAdapter.NotificationAlertAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAlertAdapterViewHolder holder, int position) {

        try {
            long time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(notiList.get(position).getCreatedAt()).getTime();
            holder.time.setText(new TimeService().getTimeAgo(time, System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.message.setText(notiList.get(position).getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (notiList.get(position).getType())
                {
                    case "following" :
                        Intent i = new Intent(context, OtherUserProfile.class);
                        i.putExtra("other_user_id",notiList.get(position).getFromUser().toString());
                        i.putExtra("token",token);
                        context.startActivity(i);
                        break;

                    case "skill_query" :
                    Intent i1 = new Intent(context, ChatScreen.class);
                    context.startActivity(i1);
                    break;

                    case "team_query" :
                        Intent i2 = new Intent(context, ChatScreen.class);
                        context.startActivity(i2);
                        break;

                    case "new_team_follower" :
                        Intent i3 = new Intent(context, TeamDetailsPage.class);
                        i3.putExtra("teamId",notiList.get(position).getAdditional_id().toString());
                        i3.putExtra("status","Created");
                        context.startActivity(i3);
                        break;

                    case "new_class_follower" :
                        Intent i4 = new Intent(context, ClassDetails.class);
                        i4.putExtra("classId",notiList.get(position).getAdditional_id().toString());
                        i4.putExtra("status","Created");
                        context.startActivity(i4);
                        break;

                    case "new_skill_follower" :
                        Intent i5 = new Intent(context, SkillDetailsPage.class);
                        i5.putExtra("skillId",notiList.get(position).getAdditional_id().toString());
                        i5.putExtra("status","Created");
                        context.startActivity(i5);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    public class NotificationAlertAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView message,time;
        public NotificationAlertAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_noti_message);
            time = itemView.findViewById(R.id.tv_noti_time);
        }
    }
}