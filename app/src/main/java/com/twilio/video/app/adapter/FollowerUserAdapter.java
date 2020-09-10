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
import com.twilio.video.app.FollowerUserResponse.Datum;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;

import java.util.List;

public class
FollowerUserAdapter extends  RecyclerView.Adapter<FollowerUserAdapter.FollowerUserAdapterViewHolder> {
    List<Datum> userList;
    Context context;
    String token;

    public FollowerUserAdapter(List<Datum> userList, Context context, String token) {
        this.userList = userList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public FollowerUserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.main_user_item,parent,false);
        return new FollowerUserAdapter.FollowerUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerUserAdapterViewHolder holder, int position) {

        holder.name.setText(userList.get(position).getFollower().getName());


        if(userList.get(position).getFollower().getProfilePath()!=null)
        {
            Glide.with(context).load("http://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/" +
                    userList.get(position).getFollower().getProfilePath()).
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

        if(userList.get(position).getFollower().getCoverPath()!=null)
        {
            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/covers/"
                    + userList.get(position).getFollower().getCoverPath()).listener(new RequestListener<Drawable>() {
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
        if(userList.get(position).getFollower().getUserType()==1)
        {
            holder.type.setText("Professor");
            holder.location.setVisibility(View.GONE);
            holder.skillActual.setVisibility(View.GONE);
            holder.skill.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);

        }else
        {
            holder.type.setText("Student");
            if(userList.get(position).getFollower().getLocation()!=null)
                holder.location.setText("Location : "+userList.get(position).getFollower().getLocation());
            if(userList.get(position).getFollower().getSkill()!=null)
                holder.skillActual.setText(userList.get(position).getFollower().getSkill());


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OtherUserProfile.class);
                i.putExtra("token",token);
                i.putExtra("other_user_id",userList.get(position).getFollower().getId().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class FollowerUserAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView cover,profile;
        TextView name,status,date,type,location,skill,skillActual;
        RatingBar ratingBar;

        public FollowerUserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.iv_main_user_item_cover);
            profile = itemView.findViewById(R.id.civ_main_user_item_profile);
            name = itemView.findViewById(R.id.tv_user_name_on_main_user_item);
            status = itemView.findViewById(R.id.tv_main_user_item_status);
            date = itemView.findViewById(R.id.tv_main_user_item_date);
            ratingBar = itemView.findViewById(R.id.rtb_main_user_item);
            type = itemView.findViewById(R.id.tv_user_type_on_main_user_item);
            location = itemView.findViewById(R.id.tv_user_location_on_main_item);
            skillActual = itemView.findViewById(R.id.tv_use_skill_actual_on_main_item);
            skill = itemView.findViewById(R.id.tv_user_skill_on_main_item);

        }
    }
}
