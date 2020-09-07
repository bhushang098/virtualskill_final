package com.twilio.video.app.SearchPages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SearchResponse.Post;
import com.twilio.video.app.SearchResponse.SearchResponse;
import com.twilio.video.app.SearchResponse.Team;
import com.twilio.video.app.adapter.SearchPostAdapter;
import com.twilio.video.app.adapter.SearchTeamAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPostFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPostFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Post> postList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView tvEmpty;

    public SearchPostFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchPostFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchPostFrag newInstance(String param1, String param2) {
        SearchPostFrag fragment = new SearchPostFrag();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search_post, null);
        recyclerView = root.findViewById(R.id.recViewSearchPost);
        progressBar = root.findViewById(R.id.pb_search_post);
        tvEmpty = root.findViewById(R.id.tv_search_post_not_available);
        SharedPreferences settings = getContext().getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        searchData(settings.getString("token",""),getArguments().getString("search_key"));
        return root;
    }

    private void searchData(String token,String key) {

        progressBar.setVisibility(View.VISIBLE);
        Call<SearchResponse> call = RetrifitClient.getInstance()
                .getSearchApi().searchByKey(token,key);

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("Search Response>>",response.raw().toString());
                if (response.body()!=null)
                {
                    postList = response.body().getPosts();
                    if(postList.size()>0)
                    {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new SearchPostAdapter(postList,getContext(),token));
                    }else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }

                }else {
                    Toast.makeText(getContext(), "Can Not Make Search", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}