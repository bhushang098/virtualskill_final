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
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SkillItemResponse.Datum;
import com.twilio.video.app.SkillItemResponse.SkillItemResponse;
import com.twilio.video.app.adapter.AvailableSkillAdapter;
import com.twilio.video.app.adapter.JoinedSkillAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinedSkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinedSkillFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView revJoinedSkills;
   ShimmerFrameLayout shimmerFrameLayout;
    private List<Datum> skillDataList = new ArrayList<>();
    TextView tvSkillsEmpty;
    String token;
    SwipeRefreshLayout refreshLayout;

    public JoinedSkillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinedSkillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinedSkillFragment newInstance(String param1, String param2) {
        JoinedSkillFragment fragment = new JoinedSkillFragment();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_joined_skill, null);
        revJoinedSkills = root.findViewById(R.id.recViewJoinedSkill);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_joined_skill_page);
        shimmerFrameLayout.startShimmerAnimation();
        tvSkillsEmpty = root.findViewById(R.id.tv_joined_skill_item_not_available);
        refreshLayout = root.findViewById(R.id.srl_joined_skills);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token","");
        loadJoinedSkills(token);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                skillDataList.clear();
                loadJoinedSkills(token);
            }
        });
        return root;
    }

    private void loadJoinedSkills(String token) {

        Call<SkillItemResponse> call = RetrifitClient.getInstance()
                .getSkillApi().getJoinedSkills(token);

        call.enqueue(new Callback<SkillItemResponse>() {
            @Override
            public void onResponse(Call<SkillItemResponse> call, Response<SkillItemResponse> response) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Log.d("TeamResponse>>", response.raw().toString());
                if(response.body()!=null)
                {
                    skillDataList = response.body().getData();
                    if(skillDataList.size()>0)
                    {
                        tvSkillsEmpty.setVisibility(View.GONE);
                        // SetAdapter
                        revJoinedSkills.setLayoutManager(new LinearLayoutManager(getContext()));
                        revJoinedSkills.setAdapter(new JoinedSkillAdapter(skillDataList,getContext()));
                    }else {
                        tvSkillsEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SkillItemResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Log.d("Exception>>", t.toString());
            }
        });
    }
}