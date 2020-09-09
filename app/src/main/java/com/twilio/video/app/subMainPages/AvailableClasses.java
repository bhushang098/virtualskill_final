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
import com.twilio.video.app.ApiModals.Creator;
import com.twilio.video.app.ClassesModal.Classes;
import com.twilio.video.app.ClassesModal.Datum;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.adapter.AvailableClassAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailableClasses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableClasses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView revAvailableClasses;
    ShimmerFrameLayout shimmerFrameLayout;
    private List<Datum> classesDataList = new ArrayList<>();
    TextView tvClasssEmply;
    String token;
    SwipeRefreshLayout refreshLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AvailableClasses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailableClasses.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableClasses newInstance(String param1, String param2) {
        AvailableClasses fragment = new AvailableClasses();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_available_classes, null);
        revAvailableClasses = root.findViewById(R.id.recViewAvailableClasses);
        refreshLayout = root.findViewById(R.id.srl_available_class);
       shimmerFrameLayout  = root.findViewById(R.id.sh_v_available_classes_page);
       shimmerFrameLayout.startShimmerAnimation();
        tvClasssEmply = root.findViewById(R.id.tv_available_class_item_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token","");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                classesDataList.clear();
                loadAvailableClasses(token);
            }
        });
        loadAvailableClasses(token);
        return root;
    }

    private void loadAvailableClasses(String token){
shimmerFrameLayout.setVisibility(View.VISIBLE);
shimmerFrameLayout.startShimmerAnimation();
        Call<Classes> call = RetrifitClient.getInstance()
                .getClassesApi().getAvailableClasses(token);
        call.enqueue(new Callback<Classes>() {
            @Override
            public void onResponse(Call<Classes> call, Response<Classes> response) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());

                        //refreshLayout.setRefreshing(false);
                    }else {
                        classesDataList = response.body().getData();
                        if(classesDataList.size()>0)
                        {
                            revAvailableClasses.setLayoutManager(new LinearLayoutManager(getContext()));
                            revAvailableClasses.setAdapter(new AvailableClassAdapter(classesDataList,getContext()));
                        }else {
                            tvClasssEmply.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<Classes> call, Throwable t) {
               // Toast.makeText(AvailableClasses.this, "Error ", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });

    }
}