package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.twilio.video.app.FollowingUserResponse.Datum;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;

import java.util.List;

public class FollworingUserAdapter extends RecyclerView.Adapter<FollworingUserAdapter.FollworingUserAdapterViewHolder> {

    List<Datum> userList;
    Context context;
    String token;

    public FollworingUserAdapter(List<Datum> userList, Context context, String token) {
        this.userList = userList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public FollworingUserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.main_user_item,parent,false);
        return new FollworingUserAdapter.FollworingUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollworingUserAdapterViewHolder holder, int position) {

        holder.name.setText(userList.get(position).getFollowing().getName());

        if(userList.get(position).getFollowing().getProfilePath()!=null)
        {
            Glide.with(context).load("http://nexgeno1.s3.us-east-2.amazonaws.com/public/uploads/profile_photos/" +
                    userList.get(position).getFollowing().getProfilePath()).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);

                            return false;
                        }
                    }).into(holder.profile);
        }else {

            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png").
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);

                            return false;
                        }
                    }).into(holder.profile);
        }

        if(userList.get(position).getFollowing().getCoverPath()!=null)
        {
            Glide.with(context).load("http://nexgeno1.s3.us-east-2.amazonaws.com/public/uploads/covers/"
                    + userList.get(position).getFollowing().getCoverPath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.INVISIBLE);

                    return false;
                }
            }).into(holder.cover);
        }else {
            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png").
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);

                            return false;
                        }
                    }).into(holder.cover);
        }

        holder.type.setVisibility(View.VISIBLE);
        if(userList.get(position).getFollowing().getUserType()==1)
        {
            holder.type.setText("Professor");
        }else
        {
            holder.type.setText("Student");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OtherUserProfile.class);
                i.putExtra("token",token);
                i.putExtra("other_user_id",userList.get(position).getFollowing().getId().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class FollworingUserAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView cover,profile;
        TextView name,status,date,type;
        RatingBar ratingBar;
        public FollworingUserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.iv_main_user_item_cover);
            profile = itemView.findViewById(R.id.civ_main_user_item_profile);
            name = itemView.findViewById(R.id.tv_user_name_on_main_user_item);
            status = itemView.findViewById(R.id.tv_main_user_item_status);
            date = itemView.findViewById(R.id.tv_main_user_item_date);
            ratingBar = itemView.findViewById(R.id.rtb_main_user_item);
            type = itemView.findViewById(R.id.tv_user_type_on_main_user_item);

        }
    }
}
