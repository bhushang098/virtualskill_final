package com.twilio.video.app.subMainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.TeamResponse.AvailAbleTeamsResponse;
import com.twilio.video.app.TeamResponse.Datum;
import com.twilio.video.app.adapter.AvailableTeamsAdapter;
import com.twilio.video.app.adapter.JoinedTeamsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinedTeamsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinedTeamsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView revJoinedTeams;
    private List<Datum> teamDataList = new ArrayList<>();
    TextView tvTeamsEmpty;
    ShimmerFrameLayout shimmerFrameLayout;

    public JoinedTeamsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinedTeamsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinedTeamsFrag newInstance(String param1, String param2) {
        JoinedTeamsFrag fragment = new JoinedTeamsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_joined_teams, null);
        revJoinedTeams = root.findViewById(R.id.recViewJoinedTeams);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_joined_teams_page);
        shimmerFrameLayout.startShimmerAnimation();
        tvTeamsEmpty = root.findViewById(R.id.tv_joined_teams_item_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        loadJoinedTeams(settings.getString("token",""));
        return root;
    }

    private void loadJoinedTeams(String token) {


        Call<AvailAbleTeamsResponse> call = RetrifitClient.getInstance()
                .getTeamsApi().getJoinedTeams(token);

        call.enqueue(new Callback<AvailAbleTeamsResponse>() {
            @Override
            public void onResponse(Call<AvailAbleTeamsResponse> call, Response<AvailAbleTeamsResponse> response) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                Log.d("TeamResponse>>", response.raw().toString());
                if(response.body()!=null)
                {
                    teamDataList = response.body().getData();
                    if(teamDataList.size()>0)
                    {
                        // SetAdapter
                        revJoinedTeams.setLayoutManager(new LinearLayoutManager(getContext()));
                        revJoinedTeams.setAdapter(new JoinedTeamsAdapter(teamDataList,getContext()));
                    }else {
                        tvTeamsEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AvailAbleTeamsResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                Log.d("Exception>>", t.toString());
            }
        });


    }
}