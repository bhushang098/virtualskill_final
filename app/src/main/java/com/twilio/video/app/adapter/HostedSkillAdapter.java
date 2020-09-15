package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.R;
import com.twilio.video.app.SkillItemResponse.Datum;
import com.twilio.video.app.subMainPages.SkillDetailsPage;

import java.util.List;

public class HostedSkillAdapter extends RecyclerView.Adapter<HostedSkillAdapter.HostedSkillAdapterViewHolder> {

    List<Datum> skillDatList;
    Context context;
    UserObj userObj;

    public HostedSkillAdapter(List<Datum> skillDatList, Context context,UserObj userObj) {
        this.skillDatList = skillDatList;
        this.context = context;
        this.userObj = userObj;
    }


    @NonNull
    @Override
    public HostedSkillAdapter.HostedSkillAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.skill_item,parent,false);
        return new HostedSkillAdapter.HostedSkillAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostedSkillAdapter.HostedSkillAdapterViewHolder holder, int position) {

        holder.tvSkillName.setText(skillDatList.get(position).getName());
        if(skillDatList.get(position).getFee().equalsIgnoreCase("0"))
            holder.tvfee.setText("Free Skill");
        else
            holder.tvfee.setText("INR : "+skillDatList.get(position).getFee());
        holder.tvmemeber.setText(String.valueOf(skillDatList.get(position).
                getFollowers().size())+" Member");
        holder.tvhost.setText(" Hosted By : "+skillDatList.get(position).getCreator().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SkillDetailsPage.class);
                i.putExtra("status","Created");
                i.putExtra("memCount",String.valueOf(skillDatList.get(position).
                        getFollowers().size()));
                i.putExtra("skillId",skillDatList.get(position).getId().toString());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return skillDatList.size();
    }

    public class HostedSkillAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillName,tvfee,tvmemeber,tvhost;

        public HostedSkillAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkillName = itemView.findViewById(R.id.tv_skill_name_on_skill_item);
            tvfee = itemView.findViewById(R.id.tv_skill_fees_on_Skill_item);
            tvmemeber = itemView.findViewById(R.id.tv_skill_members_on_skill_item);
            tvhost = itemView.findViewById(R.id.tv_skill_host_on_skill_item);
        }
    }
}

