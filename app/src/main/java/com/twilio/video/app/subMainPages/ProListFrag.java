package com.twilio.video.app.subMainPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SearchStudentResponse.Datum;
import com.twilio.video.app.SearchStudentResponse.SearchStudentResponse;
import com.twilio.video.app.adapter.CommentsAdapter;
import com.twilio.video.app.adapter.StudentUserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProListFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProListFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private List<Datum> proList = new ArrayList<>();
    TextView tvEmpty;
    UserObj userObj;
    int totalPages;
    int  currentPage;
    private String token;


    public ProListFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProListFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProListFrag newInstance(String param1, String param2) {
        ProListFrag fragment = new ProListFrag();
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
        loadprefs();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_pro_list, null);
        recyclerView = root.findViewById(R.id.rec_v_pro_user_frag);
        shimmerFrameLayout = root.findViewById(R.id.sh_v_pro_frag);
        tvEmpty = root.findViewById(R.id.tv_pro_user_not_available_frag);
        shimmerFrameLayout.startShimmerAnimation();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(currentPage<totalPages)
                {
                    loadMoreItems();
                }

            }
        });

        // load the first page
        loadMoreItems();

        return root;
    }

    private void loadprefs() {

        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);

        token = settings.getString("token","");
        Gson json = new Gson();

        userObj = json.fromJson(settings.getString("UserObj",""),UserObj.class);


    }

    private void loadMoreItems() {
        currentPage++;
        // update recycler adapter with next page
        RetrifitClient.getInstance().getUserApi().getSearchPros(token,String.valueOf(currentPage))
        .enqueue(new Callback<SearchStudentResponse>() {
            @Override
            public void onResponse(Call<SearchStudentResponse> call, Response<SearchStudentResponse> response) {
                Log.d("Resposne>>", response.raw().toString());

                if(response.body()!=null)
                {
                    totalPages = response.body().getData().getLastPage();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    proList.addAll(response.body().getData().getData());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new StudentUserAdapter(getContext(), proList,token,userObj.getId()));
                }
            }

            @Override
            public void onFailure(Call<SearchStudentResponse> call, Throwable t) {
                Log.d("Exception>>",t.toString());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

}
