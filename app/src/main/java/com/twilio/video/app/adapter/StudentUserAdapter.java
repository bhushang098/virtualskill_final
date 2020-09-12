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
import com.twilio.video.app.MainPages.MyProfile;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.SearchStudentResponse.Datum;

import java.util.List;

public class StudentUserAdapter extends RecyclerView.Adapter<StudentUserAdapter.StudentUserAdapterViewHolder> {

    Context context;
    List<Datum> studentList;
    String token;
    int hisUid;

    public StudentUserAdapter(Context context, List<Datum> studentList,String token,int hisUid) {
        this.context = context;
        this.studentList = studentList;
        this.token = token;
        this.hisUid = hisUid;
        ;    }



    @NonNull
    @Override
    public StudentUserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.main_user_item,parent,false);
        return new StudentUserAdapter.StudentUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentUserAdapterViewHolder holder, int position) {

        holder.name.setText(studentList.get(position).getName());


        if(studentList.get(position).getProfilePath()!=null)
        {
            Glide.with(context).load("http://nexgeno1.s3.us-east-2.amazonaws.com/public/uploads/profile_photos/" +
                    studentList.get(position).getProfilePath()).
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

        if(studentList.get(position).getCoverPath()!=null)
        {
            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/covers/"
                    + studentList.get(position).getCoverPath()).listener(new RequestListener<Drawable>() {
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
        if(studentList.get(position).getUserType()==1)
        {
            holder.type.setText("Professor");
            holder.location.setVisibility(View.GONE);
            holder.skillActual.setVisibility(View.GONE);
            holder.skill.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);

        }else
        {
            holder.type.setText("Student");
            if(studentList.get(position).getLocation()!=null)
                holder.location.setText("Location : "+studentList.get(position).getLocation());
            if(studentList.get(position).getSkill()!=null)
                holder.skillActual.setText(studentList.get(position).getSkill());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(studentList.get(position).getId()==hisUid)
                {
                    context.startActivity(new Intent(context, MyProfile.class));
                }
                else {
                    Intent i = new Intent(context, OtherUserProfile.class);
                    i.putExtra("token",token);
                    i.putExtra("other_user_id",studentList.get(position).getId().toString());
                    context.startActivity(i);
                }

            }
        });

    }

    public void setList(List<Datum> list) {
        this.studentList = list;
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class StudentUserAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView cover,profile;
        TextView name,status,date,type,location,skill,skillActual;
        RatingBar ratingBar;

        public StudentUserAdapterViewHolder(@NonNull View itemView) {
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
