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
import com.twilio.video.app.TeamResponse.Datum;
import com.twilio.video.app.subMainPages.TeamDetailsPage;

import java.util.List;

public class AvailableTeamsAdapter extends RecyclerView.Adapter<AvailableTeamsAdapter.AvailableTeamsAdapterViewHolder> {

    List<Datum> teamsList;
    private Context context;

    public AvailableTeamsAdapter(List<Datum> teamsList, Context context) {
        this.teamsList = teamsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AvailableTeamsAdapter.AvailableTeamsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.team_item,parent,false);
        return new AvailableTeamsAdapter.AvailableTeamsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AvailableTeamsAdapter.AvailableTeamsAdapterViewHolder holder, int position) {

        holder.teamName.setText(teamsList.get(position).getName());
        holder.location.setText("Location : "+teamsList.get(position).getLocation());
        // No Field For members holder.members.setText(teamsList.get(position).me());
        // No Joined holder.joined.setText(teamsList.get(position).getName());
        holder.host.setText("Hosted By : "+teamsList.get(position).getCreated_by().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TeamDetailsPage.class);
                Gson json = new Gson();
                String strHost = json.toJson(teamsList.get(position).getCreated_by());
                i.putExtra("teamHost",strHost);
                i.putExtra("status","Available");
                i.putExtra("teamId", teamsList.get(position).getTeamIds().toString());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    public class AvailableTeamsAdapterViewHolder extends  RecyclerView.ViewHolder{
        TextView teamName,location,members,joined,host;

        public AvailableTeamsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.tv_team_name_on_team_item);
            location = itemView.findViewById(R.id.tv_team_location_on_team_item);
            members = itemView.findViewById(R.id.tv_num_member_on_team_item);
            joined = itemView.findViewById(R.id.tv_num_joined_on_team_item);
            host = itemView.findViewById(R.id.tv_team_host_on_team_name);

        }
    }
}
