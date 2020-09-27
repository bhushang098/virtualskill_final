package com.twilio.video.app.subMainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.twilio.video.app.JobsResponse.Datum;
import com.twilio.video.app.JobsResponse.FindJobsRespose;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.FindJobsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppliedJobsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppliedJobsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    private List<Datum> JobDataList = new ArrayList<>();
    TextView tvJobsEmpty;
    String token;
    SwipeRefreshLayout refreshLayout;

    public AppliedJobsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppliedJobsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AppliedJobsFrag newInstance(String param1, String param2) {
        AppliedJobsFrag fragment = new AppliedJobsFrag();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_applied_jobs, null);
        recyclerView = root.findViewById(R.id.recViewAppliedJobs);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_applied_jobs_page);
        tvJobsEmpty = root.findViewById(R.id.tv_applied_jobs_not_available);
        refreshLayout = root.findViewById(R.id.srl_applied_jobs);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token","");
        loadAppliedJobs(token);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JobDataList.clear();
                loadAppliedJobs(token);
            }
        });
        return root;
    }

    private void loadAppliedJobs(String token) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        Call<FindJobsRespose> call = RetrifitClient.getInstance()
                .gteJobsApi().getJobs(token);

        call.enqueue(new Callback<FindJobsRespose>() {
            @Override
            public void onResponse(Call<FindJobsRespose> call, Response<FindJobsRespose> response) {
                Log.d("Response>>",response.raw().toString());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                if(response.body()!=null)
                {
                    JobDataList = response.body().getAppliedJobs().getData();
                    if(JobDataList.size()>0)
                    {
                        tvJobsEmpty.setVisibility(View.GONE);
                        // SetAdapter
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new FindJobsAdapter(JobDataList,getContext(),token,"applied"));
                    }else {
                        tvJobsEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<FindJobsRespose> call, Throwable t) {

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Log.d("Exception>>", t.toString());
            }
        });
    }
}