package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.twilio.video.app.R;
import com.twilio.video.app.SkillItemResponse.Datum;
import com.twilio.video.app.subMainPages.SkillDetailsPage;

import java.util.List;

public class AvailableSkillAdapter  extends RecyclerView.Adapter<AvailableSkillAdapter.SkillItemAdapterViewHolder> {

    List<Datum> skillDatList;
    Context context;

    public AvailableSkillAdapter(List<Datum> skillDatList, Context context) {
        this.skillDatList = skillDatList;
        this.context = context;
    }

    @NonNull
    @Override
    public SkillItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.skill_item,parent,false);
        return new AvailableSkillAdapter.SkillItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillItemAdapterViewHolder holder, int position) {

        holder.tvSkillName.setText(skillDatList.get(position).getName());
        if(skillDatList.get(position).getFee().equalsIgnoreCase("0"))
            holder.tvfee.setText("Free Skill");
        else
            holder.tvfee.setText("INR : "+skillDatList.get(position).getFee());
        holder.tvmemeber.setText(String.valueOf(skillDatList.get(position).
                getFollowers_count()+" Member"));
        holder.tvhost.setText(" Hosted By : "+skillDatList.get(position).getCreator().getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SkillDetailsPage.class);
                i.putExtra("status","Available");
                i.putExtra("skillId",skillDatList.get(position).getId().toString());
                Gson json = new Gson();
                String strHost = json.toJson(skillDatList.get(position).getCreator());
                i.putExtra("memCount",String.valueOf(skillDatList.get(position).
                       getFollowers_count()));
                i.putExtra("skillHost",strHost);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return skillDatList.size();
    }

    public class SkillItemAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillName,tvfee,tvmemeber,tvhost;
        public SkillItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkillName = itemView.findViewById(R.id.tv_skill_name_on_skill_item);
            tvfee = itemView.findViewById(R.id.tv_skill_fees_on_Skill_item);
            tvmemeber = itemView.findViewById(R.id.tv_skill_members_on_skill_item);
            tvhost = itemView.findViewById(R.id.tv_skill_host_on_skill_item);
        }
    }
}
