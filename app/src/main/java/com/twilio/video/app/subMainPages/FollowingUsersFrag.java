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
import com.twilio.video.app.FollowingUserResponse.Datum;
import com.twilio.video.app.FollowingUserResponse.FollowingUserREsponse;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.AvailableSkillAdapter;
import com.twilio.video.app.adapter.FollworingUserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingUsersFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingUsersFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView revFolloeings;
    private List<Datum> followingsList = new ArrayList<>();
    TextView tvfolllowingsEmpty;

    public FollowingUsersFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowingUsersFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingUsersFrag newInstance(String param1, String param2) {
        FollowingUsersFrag fragment = new FollowingUsersFrag();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_following_users, null);
        revFolloeings = root.findViewById(R.id.recViewFollowingUsers);
       shimmerFrameLayout = root.findViewById(R.id.sh_v_following);
       shimmerFrameLayout.startShimmerAnimation();
        tvfolllowingsEmpty = root.findViewById(R.id.tv_following_user_item_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        loadFollowings(settings.getString("token",""));
        return root;
    }

    private void loadFollowings(String token) {
         Call<FollowingUserREsponse> call =  RetrifitClient
                .getInstance().getUserApi().getFollowingUSers(token);

         call.enqueue(new Callback<FollowingUserREsponse>() {
             @Override
             public void onResponse(Call<FollowingUserREsponse> call, Response<FollowingUserREsponse> response) {
                 Log.d("Resposne USers>>", response.raw().toString());
                 shimmerFrameLayout.stopShimmerAnimation();
                 shimmerFrameLayout.setVisibility(View.GONE);
                 if(response.body()!=null)
                 {
                     if (response.body().getStatus())
                     {
                         followingsList = response.body().getData();
                         if(followingsList.size()>0)
                         {
                             revFolloeings.setLayoutManager(new LinearLayoutManager(getContext()));
                             revFolloeings.setAdapter(new FollworingUserAdapter(followingsList,getContext(),token));

                         }else {
                             tvfolllowingsEmpty.setVisibility(View.VISIBLE);
                         }
                     }
                 }
             }

             @Override
             public void onFailure(Call<FollowingUserREsponse> call, Throwable t) {
                 shimmerFrameLayout.stopShimmerAnimation();
                 shimmerFrameLayout.setVisibility(View.GONE);
             }
         });
    }
}