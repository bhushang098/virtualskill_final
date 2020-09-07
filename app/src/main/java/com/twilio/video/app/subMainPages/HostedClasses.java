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
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.ClassesModal.Classes;
import com.twilio.video.app.CreatedClassResponse.CreatedClassRespones;
import com.twilio.video.app.CreatedClassResponse.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SearchResponse.User_;
import com.twilio.video.app.SingleClassResponse.User;
import com.twilio.video.app.adapter.AvailableClassAdapter;
import com.twilio.video.app.adapter.CreatedClassAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostedClasses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostedClasses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView revHostedClasses;
    ShimmerFrameLayout shimmerFrameLayout;
    private List<Datum> classesDataList = new ArrayList<>();
    TextView tvEmptyStatus;
    UserObj userObj;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HostedClasses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HostedClasses.
     */
    // TODO: Rename and change types and number of parameters
    public static HostedClasses newInstance(String param1, String param2) {
        HostedClasses fragment = new HostedClasses();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_hosted_classes, null);
        revHostedClasses = root.findViewById(R.id.rec_view_hosted_classes);
       shimmerFrameLayout = root.findViewById(R.id.sh_v_hosted_classes_page);
       shimmerFrameLayout.startShimmerAnimation();
        tvEmptyStatus = root.findViewById(R.id.tv_hosted_class_item_not_available);
        loadUser();
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        loadHostedClasses(settings.getString("token",""));

        return root;

    }

    private void loadUser() {

        SharedPreferences settings =getContext().getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        userObj = gson.fromJson(settings.getString("UserObj",""),UserObj.class);

    }

    private void loadHostedClasses(String token){
        Call<CreatedClassRespones> call = RetrifitClient.getInstance()
                .getClassesApi().getCreatedClasses(token);
        call.enqueue(new Callback<CreatedClassRespones>() {
            @Override
            public void onResponse(Call<CreatedClassRespones> call, Response<CreatedClassRespones> response) {
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());
                       shimmerFrameLayout.stopShimmerAnimation();
                       shimmerFrameLayout.setVisibility(View.GONE);
                        //refreshLayout.setRefreshing(false);
                    }else {
                        classesDataList = response.body().getData();
                        if(classesDataList.size()>0)
                        {
                            revHostedClasses.setLayoutManager(new LinearLayoutManager(getContext()));
                            revHostedClasses.setAdapter(new CreatedClassAdapter(classesDataList,getContext(),userObj));

                        }else {
                            tvEmptyStatus.setVisibility(View.VISIBLE);
                        }

                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        //refreshLayout.setRefreshing(false);

                    }


                }catch (Exception e){
                    e.printStackTrace();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    //refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<CreatedClassRespones> call, Throwable t) {
                // Toast.makeText(AvailableClasses.this, "Error ", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                //refreshLayout.setRefreshing(false);
            }
        });

    }
}