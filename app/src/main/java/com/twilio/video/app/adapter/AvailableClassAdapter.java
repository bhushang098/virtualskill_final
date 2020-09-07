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
import com.twilio.video.app.ClassesModal.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.subMainPages.ClassDetails;
import com.twilio.video.app.util.DateUtil;
import com.twilio.video.app.util.TimeService;

import java.util.List;


public class AvailableClassAdapter extends RecyclerView.Adapter<AvailableClassAdapter.AvailableClassAdapterViewHolder> {

    List<Datum> classList;
    private Context context;

    public AvailableClassAdapter(List<Datum> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @NonNull
    @Override
    public AvailableClassAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.class_item,parent,false);
        return new AvailableClassAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableClassAdapterViewHolder holder, int position) {

        holder.className.setText(classList.get(position).getName());
        holder.className.setAllCaps(true);
        holder.location.setText("Location : "+classList.get(position).getLocation());
        String fees = classList.get(position).getFee();
        if(fees.equalsIgnoreCase("0"))
        {
            holder.fees.setText("Free Class");
        }else {
            holder.fees.setText("INR:  "+classList.get(position).getFee());
        }
        holder.classHost.setText("Hosted By  "+classList.get(position).getCreator().getName());
        holder.date.setText(" " + DateUtil.getDate(classList.get(position).getStartDate())+" - "+DateUtil.getDate(classList.get(position).getEndDate()));
        if(classList.get(position).getRecurringClass()==1)
        {
            holder.timing.setText(" Daily "+ TimeService.getTimeInAmPm(classList.get(position).getStartTime()));
        }else {
            holder.timing.setText(" At "+TimeService.getTimeInAmPm(classList.get(position).getStartTime()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ClassDetails.class);
                Gson json = new Gson();
                String strHost = json.toJson(classList.get(position).getCreator());
                i.putExtra("classHost",strHost);
                i.putExtra("classId",classList.get(position).getEId().toString());
                i.putExtra("status","Available");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    class AvailableClassAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView className, classHost, location, fees,date,timing;


        public AvailableClassAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.tv_class_name);

            classHost = itemView.findViewById(R.id.tv_host_name);
            location = itemView.findViewById(R.id.tv_class_location);
            fees = itemView.findViewById(R.id.tv_free_paid_class);
            date = itemView.findViewById(R.id.tv_class_since);
            timing = itemView.findViewById(R.id.tv_class_timing);

        }
    }
}
