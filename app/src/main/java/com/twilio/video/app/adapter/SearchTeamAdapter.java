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
import com.twilio.video.app.SearchResponse.Team;
import com.twilio.video.app.subMainPages.TeamDetailsPage;

import java.util.List;

public class SearchTeamAdapter extends RecyclerView.Adapter<SearchTeamAdapter.SearchTeamAdapterViewHolder> {

    List<Team> teamsList;
    private Context context;

    public SearchTeamAdapter(List<Team> teamsList, Context context) {
        this.teamsList = teamsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchTeamAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.team_item,parent,false);
        return new SearchTeamAdapter.SearchTeamAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTeamAdapterViewHolder holder, int position) {

        holder.teamName.setText(teamsList.get(position).getName());
        holder.location.setText("Location : "+teamsList.get(position).getLocation());
        // No Field For members holder.members.setText(teamsList.get(position).me());
        // No Joined holder.joined.setText(teamsList.get(position).getName());
        // No Host NAme holder.host.setText(teamsList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TeamDetailsPage.class);
                i.putExtra("status","Available");
                i.putExtra("teamId", teamsList.get(position).getId().toString());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }


    public class SearchTeamAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView teamName,location,members,joined,host;
        public SearchTeamAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.tv_team_name_on_team_item);
            location = itemView.findViewById(R.id.tv_team_location_on_team_item);
            members = itemView.findViewById(R.id.tv_num_member_on_team_item);
            joined = itemView.findViewById(R.id.tv_num_joined_on_team_item);
            host = itemView.findViewById(R.id.tv_team_host_on_team_name);
        }
    }
}
