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
import com.twilio.video.app.JobsResponse.Datum;
import com.twilio.video.app.MainPages.JobDetails;
import com.twilio.video.app.R;

import java.util.List;

public class FindJobsAdapter extends RecyclerView.Adapter<FindJobsAdapter.FindJobsAdapterViewHolder> {

    List<Datum> jobDataList;
    Context context;
    String token;
    String status;

    public FindJobsAdapter(List<Datum> jobDataList, Context context, String token,String status) {
        this.jobDataList = jobDataList;
        this.context = context;
        this.token = token;
        this.status = status;
    }

    @NonNull
    @Override
    public FindJobsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.job_item,parent,false);
        return new FindJobsAdapter.FindJobsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindJobsAdapterViewHolder holder, int position) {

        holder.jobNme.setText(jobDataList.get(position).getTitle());
        holder.location.setText("Location : "+jobDataList.get(position).getLocation());
        holder.qualification.setText("Min Qualification : "+jobDataList.get(position).getMinQualification());
        if(jobDataList.get(position).getRequiredExp()==0)
        {
            holder.experience.setText("Experience : "+" Freshers ");
        }else {
            holder.experience.setText("Experience : "+jobDataList.get(position).getRequiredExp());
        }

        holder.salary.setText("Salary : "+jobDataList.get(position).getSalary());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JobDetails.class);
                Gson json = new Gson();
                String JobobjStr = json.toJson(jobDataList.get(position));
                i.putExtra("status",status);
                i.putExtra("jobObj",JobobjStr);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobDataList.size();
    }

    public class FindJobsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView jobNme,location,qualification,experience,salary;
        public FindJobsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNme = itemView.findViewById(R.id.tv_job_name_on_item);
            location = itemView.findViewById(R.id.tv_job_location_on_item);
            qualification = itemView.findViewById(R.id.tv_minimum_qua_on_item);
            experience = itemView.findViewById(R.id.tv_experience_on_item);
            salary = itemView.findViewById(R.id.tv_salary_on_item);

        }
    }
}
