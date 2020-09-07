package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SearchStudentResponse.Datum;
import com.twilio.video.app.SearchStudentResponse.SearchStudentResponse;
import com.twilio.video.app.adapter.StudentUserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProUserPage extends AppCompatActivity {

    RecyclerView recyclerView;
    String token;
    List<Datum> proUserList = new ArrayList<>();
    int totalPages,currentPage;
    TextView tvCurrentPag,tvTotalPages,tvJumpToPage;
    EditText etPageNo;
    FloatingActionButton facNext,fabPrev;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_user_page);
        token = getIntent().getStringExtra("token");
        setUi();
        shimmerFrameLayout.startShimmerAnimation();
        getproUSers(token,"1");

        facNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage<totalPages)
                {
                    recyclerView.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                    getproUSers(token,String.valueOf(currentPage+1));

                }else {
                    Toast.makeText(ProUserPage.this, "InValid Request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage>1)
                {
                    recyclerView.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                    getproUSers(token,String.valueOf(currentPage-1));

                }else {
                    Toast.makeText(ProUserPage.this, "InValid Request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvJumpToPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPageNo.getText().toString().length()>0)
                {
                    if(Integer.parseInt(etPageNo.getText().toString())>0&&
                            Integer.parseInt(etPageNo.getText().toString())<=totalPages)
                    {
                        recyclerView.setVisibility(View.GONE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.startShimmerAnimation();
                        getproUSers(token,etPageNo.getText().toString().trim());

                    }else {
                        Toast.makeText(ProUserPage.this, "InValid Page", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void getproUSers(String token, String pgNo) {

        Call<SearchStudentResponse> call = RetrifitClient.getInstance()
                .getUserApi().getSearchPros(token,pgNo);

        call.enqueue(new Callback<SearchStudentResponse>() {
            @Override
            public void onResponse(Call<SearchStudentResponse> call, Response<SearchStudentResponse> response) {
                if(response.body()!=null)
                {

                    totalPages = response.body().getData().getLastPage();
                    currentPage = Integer.parseInt(pgNo);
//                    if(totalPages>=2)
//                    {
//                        navMenu.setVisibility(View.VISIBLE);
//                    }

                    tvCurrentPag.setText("Current Page: "+currentPage);
                    tvTotalPages.setText("Total Pages : "+totalPages);
                    proUserList = response.body().getData().getData();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ProUserPage.this));
                    //recyclerView.setAdapter(new StudentUserAdapter(ProUserPage.this,proUserList,token));
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchStudentResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setUi() {
        recyclerView = findViewById(R.id.rec_v_pro_user);
        tvCurrentPag = findViewById(R.id.tv_current_page_pro);
        tvTotalPages = findViewById(R.id.tv_total_page_pro);
        fabPrev = findViewById(R.id.fab_prev_pro);
        facNext = findViewById(R.id.fab_next_pro);
        shimmerFrameLayout = findViewById(R.id.sh_v_pro_page);
        tvJumpToPage = findViewById(R.id.tv_jump_page_pro);
        etPageNo = findViewById(R.id.et_jump_page_pro);
        navMenu = findViewById(R.id.lin_lay_nav_pro_page);
    }
}