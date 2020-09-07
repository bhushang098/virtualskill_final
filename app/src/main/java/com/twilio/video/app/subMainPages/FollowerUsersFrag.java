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
import com.twilio.video.app.FollowerUserResponse.Datum;
import com.twilio.video.app.FollowerUserResponse.FollowerUserResponse;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.FollowerUserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerUsersFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerUsersFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView revFollowers;
    private List<Datum> followerList = new ArrayList<>();
    TextView tvFollowerEmpty;

    public FollowerUsersFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerUsersFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowerUsersFrag newInstance(String param1, String param2) {
        FollowerUsersFrag fragment = new FollowerUsersFrag();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_follower_users, null);
        revFollowers = root.findViewById(R.id.recViewFollowerUsers);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_followers);
        shimmerFrameLayout.startShimmerAnimation();
        tvFollowerEmpty = root.findViewById(R.id.tv_follower_user_item_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        loadFollowers(settings.getString("token",""));
        return root;
    }

    private void loadFollowers(String token) {
        Call<FollowerUserResponse> call = RetrifitClient.getInstance()
                .getUserApi().getFollowers(token);

        call.enqueue(new Callback<FollowerUserResponse>() {
            @Override
            public void onResponse(Call<FollowerUserResponse> call, Response<FollowerUserResponse> response) {
                Log.d("Suer Response>>", response.raw().toString());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                if(response.body()!=null)
                {

                    if (response.body().getStatus())
                    {
                        followerList = response.body().getData();
                        if(followerList.size()>0)
                        {
                            revFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
                            revFollowers.setAdapter(new FollowerUserAdapter(followerList,getContext(),token));
                        }else {
                            tvFollowerEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowerUserResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }
}