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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.TeamResponse.AvailAbleTeamsResponse;
import com.twilio.video.app.TeamResponse.Datum;
import com.twilio.video.app.adapter.AvailableTeamsAdapter;
import com.twilio.video.app.adapter.HostedTeamsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostedTeamsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostedTeamsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView revHostedTeams;
   ShimmerFrameLayout shimmerFrameLayout;
    private List<Datum> teamDataList = new ArrayList<>();
    TextView tvTeamsEmpty;
    UserObj userObj;
    String token;
    SwipeRefreshLayout refreshLayout;

    public HostedTeamsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HostedTeamsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static HostedTeamsFrag newInstance(String param1, String param2) {
        HostedTeamsFrag fragment = new HostedTeamsFrag();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_hosted_teams, null);
        revHostedTeams = root.findViewById(R.id.recViewHostedTeams);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_hosted_teams_page);
        refreshLayout = root.findViewById(R.id.srl_hosted_teams);
        shimmerFrameLayout.startShimmerAnimation();
        loadUser();
        tvTeamsEmpty = root.findViewById(R.id.tv_hosted_teams_item_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token","");
        loadHostedTeams(token);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                teamDataList.clear();
                loadHostedTeams(token);
            }
        });
        return root;
    }

    private void loadUser() {
        SharedPreferences settings =getContext().getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        userObj = gson.fromJson(settings.getString("UserObj",""), UserObj.class);
    }

    @Override
    public void onResume() {
        loadHostedTeams(token);
        super.onResume();
    }

    private void loadHostedTeams(String token) {

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        Call<AvailAbleTeamsResponse> call = RetrifitClient.getInstance()
                .getTeamsApi().getCreatedTeams(token);

        call.enqueue(new Callback<AvailAbleTeamsResponse>() {
            @Override
            public void onResponse(Call<AvailAbleTeamsResponse> call, Response<AvailAbleTeamsResponse> response) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Log.d("TeamResponse>>", response.raw().toString());
                if(response.body()!=null)
                {
                    teamDataList = response.body().getData();
                    if(teamDataList.size()>0)
                    {
                        // SetAdapter
                        tvTeamsEmpty.setVisibility(View.INVISIBLE);
                        revHostedTeams.setLayoutManager(new LinearLayoutManager(getContext()));
                        revHostedTeams.setAdapter(new HostedTeamsAdapter(teamDataList,getContext(),userObj));
                    }else {
                        tvTeamsEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AvailAbleTeamsResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Log.d("Exception>>", t.toString());

            }
        });
    }
}