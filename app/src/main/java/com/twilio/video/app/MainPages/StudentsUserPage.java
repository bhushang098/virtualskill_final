package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

public class StudentsUserPage extends AppCompatActivity {

    RecyclerView recyclerView;
    String token;
    List<Datum> studentsList = new ArrayList<>();
    int totalPages,currentPage;
    TextView tvCurrentPag,tvTotalPages,tvJumpToPage;
    EditText etPageNo;
    FloatingActionButton facNext,fabPrev;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadprefs();
        setContentView(R.layout.activity_students_user_page);
        setUi();
        shimmerFrameLayout.startShimmerAnimation();
        getStudents(token,"1");

        facNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage<totalPages)
                {
                    recyclerView.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                    getStudents(token,String.valueOf(currentPage+1));

                }else {
                    Toast.makeText(StudentsUserPage.this, "InValid Request", Toast.LENGTH_SHORT).show();
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
                    getStudents(token,String.valueOf(currentPage-1));

                }else {
                    Toast.makeText(StudentsUserPage.this, "InValid Request", Toast.LENGTH_SHORT).show();
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
                        getStudents(token,etPageNo.getText().toString().trim());

                    }else {
                        Toast.makeText(StudentsUserPage.this, "InValid Page", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void getStudents(String token,String pageNo) {
        Call<SearchStudentResponse> call = RetrifitClient.getInstance()
                .getUserApi().getSearchStudents(token,pageNo);

        call.enqueue(new Callback<SearchStudentResponse>() {
            @Override
            public void onResponse(Call<SearchStudentResponse> call, Response<SearchStudentResponse> response) {
                if(response.body()!=null)
                {
                    totalPages = response.body().getData().getLastPage();
                    currentPage = Integer.parseInt(pageNo);

                    tvCurrentPag.setText("Current Page: "+currentPage);
                    tvTotalPages.setText("Total Pages : "+totalPages);
                    studentsList = response.body().getData().getData();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    if(recyclerView.getVisibility()==View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);

                    recyclerView.setLayoutManager(new LinearLayoutManager(StudentsUserPage.this));
                   // recyclerView.setAdapter(new StudentUserAdapter(StudentsUserPage.this,studentsList,token));
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



    private void loadprefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);

        token = settings.getString("token","");
    }

    private void setUi() {
        recyclerView = findViewById(R.id.rec_v_students_user);
        tvCurrentPag = findViewById(R.id.tv_current_page_stud);
        tvTotalPages = findViewById(R.id.tv_total_page_stud);
        fabPrev = findViewById(R.id.fab_prev_stud);
        facNext = findViewById(R.id.fab_next_stud);
        shimmerFrameLayout = findViewById(R.id.sh_v_students_page);
        tvJumpToPage = findViewById(R.id.tv_jump_page_stud);
        etPageNo = findViewById(R.id.et_jump_page_stud);

    }
}