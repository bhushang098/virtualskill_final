package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.SearchResponse.User;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserAdapterViewHolder> {

    List<User> userList;
    Context context;
    String token;

    public SearchUserAdapter(List<User> userList, Context context, String token) {
        this.userList = userList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public SearchUserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.user_item,parent,false);
        return new SearchUserAdapter.SearchUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapterViewHolder holder, int position) {

        holder.name.setText(userList.get(position).getName());
        if(userList.get(position).getUserType()==1)
        {
            holder.type.setText("Professor");
        }else {
            holder.type.setText("Student");
        }
        if(userList.get(position).getUsername()!=null)
        {
         holder.userName.setText("@"+userList.get(position).getUsername());
        }else {
            holder.userName.setText("@UserName");
        }

        if(userList.get(position).getProfilePath()!=null)
        {
            Glide.with(context).load("http://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/" +
                    userList.get(position).getProfilePath()).
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

        if(userList.get(position).getCoverPath()!=null)
        {
            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/covers/" + userList.get(position).getCoverPath()).listener(new RequestListener<Drawable>() {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OtherUserProfile.class);
                i.putExtra("token",token);
                i.putExtra("other_user_id",userList.get(position).getId().toString());
               context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class SearchUserAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView cover,profile;
        TextView name,type,userName;
        public SearchUserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.imageView);
            profile = itemView.findViewById(R.id.circleImageView);
            name = itemView.findViewById(R.id.tv_user_name_on_user_item);
            type = itemView.findViewById(R.id.tv_user_type);
            userName = itemView.findViewById(R.id.tv_user_user_name);
        }
    }
}
