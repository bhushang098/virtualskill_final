package com.twilio.video.app.MainPages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.DetailedChatResponse.Datum;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingelUSerByIDResponse.Data;
import com.twilio.video.app.SingelUSerByIDResponse.Follower;
import com.twilio.video.app.SingelUSerByIDResponse.Following;
import com.twilio.video.app.SingelUSerByIDResponse.SingleUserbyIDResponse;
import com.twilio.video.app.adapter.ChatItemAdapter;
import com.twilio.video.app.adapter.HomePostsAdapter;
import com.twilio.video.app.subMainPages.FollowersPage;
import com.twilio.video.app.subMainPages.FollowingPage;
import com.twilio.video.app.subMainPages.PostsPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserProfile extends AppCompatActivity {

    ImageView coverPhoto, iveditRating, ivChat, ivGender;
    CircleImageView profilPic;
    CardView cvFollowunFollow, cvCompany;
    LinearLayout latoutProRating, layoutInterests;
    TextView username, gender, location, tvUserNameOnTb, tvSkill,tvFollowToggle;
    TextView tvPosts, tvFollowings, tvFollowers,tvNoPost;
    private static String PREFS_NAME = "login_preferences";
    Data otherUserObj = new Data();
    UserObj thisUserObj = new UserObj();
    private List<Follower> followerList = new ArrayList<>();
    private List<Following> followingsList = new ArrayList<>();
    private List<com.twilio.video.app.HomePostModal.Datum> postDataList = new ArrayList<>();
    int hisUserId;
    String token;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    //ForChats
    List<Datum> detailedChatList = new ArrayList<>();
    ChatItemAdapter chatItemAdapter;
    RecyclerView chatsRecyclerView;

    RatingBar ratingBar;
    TextView tvRating,tvSkillOthers;

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        Gson json = new Gson();
        thisUserObj = json.fromJson(settings.getString("UserObj", ""), UserObj.class);
        hisUserId = thisUserObj.getId();
        chatItemAdapter = new ChatItemAdapter(detailedChatList,getApplicationContext(),String.valueOf(hisUserId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        loadPreferences();
        setUi();
        shimmerFrameLayout.startShimmerAnimation();
        // Get The Id from Intent and Get User From Api
        token = getIntent().getStringExtra("token");
        getuserById(getIntent().getStringExtra("other_user_id"));
        loadUserPosts(getIntent().getStringExtra("other_user_id"));


        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat();
            }
        });
        iveditRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateRatingBox();
            }
        });

        cvFollowunFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<MakeClassResponse> call = RetrifitClient.getInstance()
                        .getUserApi().
                                followOrUnFollow("users/follow_switch/"+String.valueOf(otherUserObj.getId()),token);

                call.enqueue(new Callback<MakeClassResponse>() {
                    @Override
                    public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                        Log.d("response>>",response.raw().toString());
                        if(response.body()!=null)
                        {
                            if(response.body().getStatus())
                            {
                                if(tvFollowToggle.getText().toString()
                                        .equalsIgnoreCase("Follow")){
                                    tvFollowToggle.setText("Unfollow");
                                }else
                                {
                                    tvFollowToggle.setText("Follow");
                                }

                                Toast.makeText(OtherUserProfile.this, " "+
                                        response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<MakeClassResponse> call, Throwable t) {

                        Log.d("exception>>",t.toString());
                    }
                });
            }
        });

        tvFollowings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Followings
                Intent i = new Intent(OtherUserProfile.this, FollowingPage.class);
                Gson json = new Gson();
                String followingListString = json.toJson(followingsList);
                followingListString = "{"+'"'+"following_list"+'"'+":"+followingListString+"}";
                Log.d("Strings >>", followingListString);
                i.putExtra("following_list", followingListString);
                startActivity(i);
            }
        });

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Followers
                Intent i = new Intent(OtherUserProfile.this, FollowersPage.class);
                Gson json = new Gson();
                String followerListString = json.toJson(followerList);
                followerListString = "{"+'"'+"followers_list"+'"'+":"+followerListString+"}";
                Log.d("Strings >>", followerListString);
                i.putExtra("followers_list", followerListString);
                startActivity(i);
            }
        });
        tvPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OtherUserProfile.this, PostsPage.class);
                i.putExtra("otherUserId", getIntent().getStringExtra("other_user_id"));
                i.putExtra("user_name", otherUserObj.getName());
                startActivity(i);
            }
        });

    }

    private void getuserById(String other_user_id) {

        Call<SingleUserbyIDResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSerById("user/get/" + other_user_id, token);

        call.enqueue(new Callback<SingleUserbyIDResponse>() {
            @Override
            public void onResponse(Call<SingleUserbyIDResponse> call, Response<SingleUserbyIDResponse> response) {
                Log.d("Response>>", response.raw().toString());

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        otherUserObj = response.body().getData();
                        followingsList = response.body().getData().getFollowing();
                        followerList = response.body().getData().getFollower();
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if(response.body().getIs_following()==1)
                        {
                            tvFollowToggle.setText("Unfollow");
                        }
                        if(response.body().getRating()==null)
                        {
                            setOtherUserData(null);
                        }else {
                            int temp =  new Double(response.body().getRating()).intValue();
                            setOtherUserData(String.valueOf(temp));
                        }

                    }
                }

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SingleUserbyIDResponse> call, Throwable t) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                Log.d("Exception>>>>", t.toString());
            }
        });
    }


    private void inflateRatingBox() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.rating_popup_layout,
                null); // inflating popup layout
        ImageView ivClose = popUpView.findViewById(R.id.iv_close_rating_popup);
        Button btnClose = popUpView.findViewById(R.id.btn_close_rating_popup);
        Button btnSave = popUpView.findViewById(R.id.btn_save_rating);
        AppCompatRatingBar ratingBar = popUpView.findViewById(R.id.rtb_popup);
        EditText etReview = popUpView.findViewById(R.id.et_review);
        PopupWindow mopoup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        mopoup.setAnimationStyle(android.R.style.Animation_Dialog);
        mopoup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mopoup.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mopoup.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() < 1.0f) {
                    Toast.makeText(OtherUserProfile.this, "Please Give Rating First", Toast.LENGTH_SHORT).show();
                } else {
                    if (etReview.getText().toString().length() > 0) {

                        setproRatingByAPi(otherUserObj.getId().toString(),
                                ratingBar.getRating(),etReview.getText().toString().trim(),mopoup);



                    } else {
                        Toast.makeText(OtherUserProfile.this, "Please Say something About " + otherUserObj.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setproRatingByAPi(String teacherId, float rating, String review, PopupWindow mopoup) {


        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSettingsApi().setProRating(token,teacherId,String.valueOf(rating),review);

        call.enqueue(new Callback<MakeClassResponse>() {

            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("Response>>",response.raw().toString());
                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {

                        Toast.makeText(OtherUserProfile.this, "Rating Added", Toast.LENGTH_SHORT).show();
                        mopoup.dismiss();
                    }else {
                        Toast.makeText(OtherUserProfile.this, "Can Not Add Rating", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d("Exception>>",t.toString());

            }

        });

    }

    private void startChat() {
        Call<DetailedChatResponse> call = RetrifitClient.getInstance()
                .getChatApi().getDetailedChatList(token, otherUserObj.getId().toString(), "u_2_u");

        inflateDetailedMessage(detailedChatList);

        call.enqueue(new Callback<DetailedChatResponse>() {
            @Override
            public void onResponse(Call<DetailedChatResponse> call, Response<DetailedChatResponse> response) {
                Log.d("Chat Response>>", response.raw().toString());
                if (response.body() != null) {
                    detailedChatList.clear();
                    detailedChatList = response.body().getMessages().getData();
                    Collections.reverse(detailedChatList);
                    chatItemAdapter.setMessageList(detailedChatList);
                    chatsRecyclerView.smoothScrollToPosition(response.body().getMessages().getData().size());
                    chatItemAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<DetailedChatResponse> call, Throwable t) {
                Log.d("Exception>>>",t.toString());

            }
        });
    }

    private void inflateDetailedMessage(List<Datum> data) {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.chat_details_layout,
                null); // inflating popup layout
        PopupWindow mopoup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        EditText etMessage = popUpView.findViewById(R.id.et_message);
        Collections.reverse(data);

        ProgressBar progressBar = popUpView.findViewById(R.id.pb_chat_details);
        chatsRecyclerView = popUpView.findViewById(R.id.rec_view_detailed_chat_with_user);
        ImageView ivSend = popUpView.findViewById(R.id.iv_send_chat_message);
        ImageView ivback = popUpView.findViewById(R.id.iv_toggle_chat_on_popup);
        mopoup.setAnimationStyle(android.R.style.Animation_Dialog);
        mopoup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(OtherUserProfile.this));
        chatsRecyclerView.setAdapter(chatItemAdapter);
        chatsRecyclerView.smoothScrollToPosition(data.size());

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mopoup.dismiss();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, etMessage.getText().toString(), Toast.LENGTH_SHORT).show();
                if (etMessage.getText().toString().length() > 0)
                {
                    hideKeybaord(v);
                    recyclerView.smoothScrollToPosition(detailedChatList.size());
                    sendmessage(etMessage.getText().toString(), otherUserObj.getId().toString(), mopoup);
                    etMessage.setText("");
                }

            }
        });

        // Creation of popup

    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void sendmessage(String message, String userId, PopupWindow popuseWindow) {

        Call<Map> call = RetrifitClient.getInstance().getChatApi()
                .sendChatMess(token, "u_2_u", message, userId,"1");

        Datum messageObj = new Datum();
        messageObj.setContent(message);
        messageObj.setUserId(String.valueOf(hisUserId));
        messageObj.setBelongsTo(userId);
        messageObj.setCreatedAt("Now Now");
        int index  = detailedChatList.size();
        detailedChatList.add(index,messageObj);
        chatItemAdapter.notifyItemInserted(index);
        chatsRecyclerView.smoothScrollToPosition(index);


        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.d("Responnse>>Chat ", response.raw().toString());
                if (response.body() != null) {

                }else {
                    detailedChatList.remove(index);
                    chatItemAdapter.notifyItemRemoved(index);
                    Toast.makeText(getApplicationContext(), "Can,t Send Message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.d("Failuer>>", t.toString());

            }
        });
    }

    private void setUi() {
        coverPhoto = findViewById(R.id.iv_profile_background_other);
        profilPic = findViewById(R.id.civ_profile_pic_other);
        username = findViewById(R.id.tv_user_name_on_profile_other);
        gender = findViewById(R.id.tv_user_gender_on_profile_other);
        location = findViewById(R.id.tv_user_location_on_profile_other);
        cvFollowunFollow = findViewById(R.id.cv_change_cover_pic_other);
        tvUserNameOnTb = findViewById(R.id.tv_other_user_name);
        toolbar = findViewById(R.id.tb_other_user);
        latoutProRating = findViewById(R.id.lin_lay_other_user_pro_rating);
        iveditRating = findViewById(R.id.iv_edit_pro_rating);
        cvCompany = findViewById(R.id.cv_profile_company_other);
        layoutInterests = findViewById(R.id.lin_lay_other_user_interests);
        ivChat = findViewById(R.id.iv_chat_other_user);
        tvSkill = findViewById(R.id.tv_other_user_skill);
        ivGender = findViewById(R.id.iv_other_user_gender);
        tvPosts = findViewById(R.id.tv_post_no_other);
        tvFollowings = findViewById(R.id.tv_following_no_other);
        tvFollowers = findViewById(R.id.tv_followers_no_other);
        shimmerFrameLayout = findViewById(R.id.sh_v_other_profile);
        tvFollowToggle = findViewById(R.id.tv_follow_toggle);
        recyclerView = findViewById(R.id.rec_v_other_user_profile);
        tvNoPost = findViewById(R.id.tv_no_post_otheris);
        ratingBar = findViewById(R.id.rtv_other);
        tvRating =findViewById(R.id.tv_rating_others);
        tvSkillOthers = findViewById(R.id.tv_skill_others);

    }

    private void setOtherUserData(String rating) {

        if (otherUserObj.getProfilePath() != null) {
            Glide.with(this).
                    load("http://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/" + otherUserObj.getProfilePath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);

                            return false;
                        }
                    }).into(profilPic);
        }

        if (otherUserObj.getCoverPath() != null) {
            Glide.with(this).load("https://virtualskill.in/storage/uploads/covers/" +
                    otherUserObj.getCoverPath()).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // holder.progressBar.setVisibility(View.INVISIBLE);

                            return false;
                        }
                    }).into(coverPhoto);
        }

        username.setText(otherUserObj.getName());
        location.setText(" " + otherUserObj.getLocation());
        if (otherUserObj.getSkill() != null) {
            tvSkill.setText("" + otherUserObj.getSkill());
        }

        if (otherUserObj.getSex() == 0) {
            gender.setText(" Male");
        } else {
            gender.setText(" Female");
            ivGender.setImageResource(R.drawable.female);
        }
        tvUserNameOnTb.setText(otherUserObj.getName());
        if(otherUserObj.getUserType()==1)
        {
            latoutProRating.setVisibility(View.VISIBLE);
            if(rating==null)
            {
                tvRating.setText("Not Rated");
                ratingBar.setIsIndicator(true);
                ratingBar.setClickable(false);
            }else {
                ratingBar.setRating(Float.parseFloat(rating));
                ratingBar.setIsIndicator(true);
                ratingBar.setClickable(false);
                tvRating.setText(rating);
            }
        }

        if(otherUserObj.getSkill()==null)
            tvSkillOthers.setVisibility(View.GONE);
        else
            tvSkillOthers.setText("Skill : "+otherUserObj.getSkill());

        if (otherUserObj.getInterests().isEmpty()) {

        } else {
            String[] interestsAry = otherUserObj.getInterests().split("\"");

            for (int i = 1; i < interestsAry.length; i++) {
                addCardInalyout(interestsAry[i]);
            }
        }
        tvFollowers.setText(String.valueOf(followerList.size()));
        tvFollowings.setText(String.valueOf(followingsList.size()));

    }

    @SuppressLint("ResourceAsColor")
    private void addCardInalyout(String s) {
        CardView cardview = new CardView(this);

        ViewGroup.LayoutParams layoutparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(5);

        cardview.setPadding(15, 2, 15, 2);
        cardview.setForegroundGravity(Gravity.CENTER);

        cardview.setCardBackgroundColor(R.color.cardDarkBackground);
        cardview.setCardElevation(5);

        TextView textview = new TextView(this);

        textview.setLayoutParams(layoutparams);


        textview.setText(s);

        textview.setTextSize(14);

        textview.setTextColor(Color.WHITE);

        textview.setPadding(25, 2, 25, 2);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);
        if (s.length() > 1) {
            layoutInterests.addView(cardview);
            layoutInterests.addView(new TextView(this));
        }


    }

    private void loadUserPosts(String otherUserId) {

        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"self",otherUserId);
        call.enqueue(new Callback<HomePostModal>() {
            @Override
            public void onResponse(Call<HomePostModal> call, Response<HomePostModal> response) {
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }else {
                        postDataList = response.body().getPosts().getData();
                        if(postDataList.size()>0)
                            tvNoPost.setVisibility(View.INVISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(OtherUserProfile.this));
                        recyclerView.setAdapter(new HomePostsAdapter(postDataList,OtherUserProfile.this,hisUserId,token));
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HomePostModal> call, Throwable t) {
                //   Toast.makeText(HomePage.this, "Error ", Toast.LENGTH_SHORT).show();

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

}
