package com.twilio.video.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.R;
import com.twilio.video.app.SearchResponse.Class;
import com.twilio.video.app.subMainPages.ClassDetails;

import java.util.List;

public class SearchClassAdapter extends RecyclerView.Adapter<SearchClassAdapter.SearchClassAdapterViewHolder> {

    List<Class> classList;
    private Context context;

    public SearchClassAdapter(List<Class> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchClassAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.class_item,parent,false);
        return new SearchClassAdapter.SearchClassAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchClassAdapterViewHolder holder, int position) {

        holder.className.setText(classList.get(position).getName());
        holder.location.setText(classList.get(position).getLocation());
        String fees = classList.get(position).getFee();
        if(fees.equalsIgnoreCase("0"))
        {
            holder.fees.setText("Free Class");
        }else {
            holder.fees.setText("INR:  "+classList.get(position).getFee());
        }

        holder.classHost.setText("Hosted By  "+classList.get(position).getCreatorId().toString());
        holder.date.setText("   " +classList.get(position).getCreatedAt());
        holder.timing.setText(" Time   "+classList.get(position).getStartTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ClassDetails.class);
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


    public class SearchClassAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView className, classHost, location, fees,date,timing;
        public SearchClassAdapterViewHolder(@NonNull View itemView) {
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
