package com.twilio.video.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.HomePostModal.Comment;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.util.TimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsAdapterViewHolder> {

    List<Comment> commentsList;
    private Context context;
    int userId;

    public CommentsAdapter(List<Comment> commentsList, Context context,int userId) {
        this.commentsList = commentsList;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public CommentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.comment_item,parent,false);
        return new CommentsAdapter.CommentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapterViewHolder holder, int position) {
        holder.comment.setText(commentsList.get(position).getComment());
        if (userId != commentsList.get(position).getCommentUserId())
        {
            holder.deleteView.setVisibility(View.GONE);
        }

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO delete Comment
            }
        });
        try {
            long time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(commentsList.get(position).getCreatedAt()).getTime();
            holder.timesAgo.setText(new TimeService().getTimeAgo(time,System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView userName, timesAgo, comment;
        CircleImageView userProfile;
        ImageView deleteView;



        public CommentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_name_on_comments);
            timesAgo = itemView.findViewById(R.id.tv_time_to_comment);
            comment = itemView.findViewById(R.id.tv_comment);
            userProfile = itemView.findViewById(R.id.profile_pic_on_comment);
            deleteView = itemView.findViewById(R.id.iv_delete_comment);

        }
    }
}
