package com.twilio.video.app.MainPages;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.PpUploadResponse;
import com.twilio.video.app.FollowerUserResponse.FollowerUserResponse;
import com.twilio.video.app.FollowingUserResponse.FollowingUserREsponse;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingelUSerByIDResponse.Data;
import com.twilio.video.app.SingelUSerByIDResponse.Follower;
import com.twilio.video.app.SingelUSerByIDResponse.Following;
import com.twilio.video.app.SingelUSerByIDResponse.SingleUserbyIDResponse;
import com.twilio.video.app.adapter.HomePostsAdapter;
import com.twilio.video.app.adapter.UsersTabAdapter;
import com.twilio.video.app.subMainPages.FollowerUsersFrag;
import com.twilio.video.app.subMainPages.FollowersPage;
import com.twilio.video.app.subMainPages.FollowingPage;
import com.twilio.video.app.subMainPages.FollowingUsersFrag;
import com.twilio.video.app.subMainPages.PostsPage;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity {

    ImageView coverPhoto;
    CircleImageView profilPic;
    CardView changeCoverView;
    TextView username,gender,phone,skill;
    private static  String PREFS_NAME = "login_preferences";
    Data userObj = new Data();
    File coverImage;

    String token,thisUserID;

    private  static  String TOKEN = "token";
    private  String DefaultTokenValue = "";

    LinearLayout latoutProRating,layoutInterests;


    //For Post Utils
    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    TextView tvCommitPost;
    File imageFile, videoFile;
    String content, ytLinkfinal;
    String ytVidId = "";
    boolean ytPlayerInitialized = false;
    TextView tvPostNo,tvFollowerNo,tvFollowingNo,tvNoPostToShow;
    // For Dialogsa And Progress
    Button btnOk;
    Dialog postSucessDialog;
    PopupWindow progressPopup,mpopup;
    boolean is_choosing = false;
    private boolean is_profile_pic = false;

    ShimmerFrameLayout shimmerFrameLayout;
    //Ratings
    RatingBar ratingBar;
    TextView tvRating;


    //Data
    private List<Follower> followerList = new ArrayList<>();
    private List<Following> followingsList = new ArrayList<>();
    private List<com.twilio.video.app.HomePostModal.Datum> postDataList = new ArrayList<>();
    RecyclerView recyclerView;




    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        token = settings.getString(TOKEN,DefaultTokenValue);
        Gson json = new Gson();
        userObj = json.fromJson(settings.getString("UserObj",""), Data.class);
        thisUserID = String.valueOf(userObj.getId());

        getUserByApi(thisUserID);
        loadUserPosts(thisUserID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setUi();
        postSucessDialog = new Dialog(this);
        shimmerFrameLayout.startShimmerAnimation();
        tvFollowerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, FollowersPage.class));
            }
        });

        tvFollowingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MyProfile.this, FollowingPage.class));
            }
        });
        tvPostNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfile.this, PostsPage.class);
                i.putExtra("otherUserId",userObj.getId().toString());
                i.putExtra("user_name",userObj.getName());
                startActivity(i);
            }
        });

        changeCoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCoverImage();
            }
        });
        profilPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });

        // For Post Utils

        linLayPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vvSelectedVideo.getVisibility() == View.VISIBLE) {
                    vvSelectedVideo.setVisibility(View.GONE);
                }
                if (vvSelectedYtVideo.getVisibility() == View.VISIBLE) {
                    vvSelectedYtVideo.setVisibility(View.GONE);
                }
                if (etYtLink.getVisibility() == View.VISIBLE) {
                    etYtLink.setVisibility(View.GONE);
                }

                videoFile = null;
                etYtLink.setText("");


                new ImagePicker.Builder(MyProfile.this)
                        .mode(ImagePicker.Mode.GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();

            }
        });
        ivUnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelecetMedia();
            }
        });
        linLayPickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etYtLink.getVisibility() == View.VISIBLE) {
                    etYtLink.setVisibility(View.GONE);
                }
                if (vvSelectedYtVideo.getVisibility() == View.VISIBLE) {
                    vvSelectedYtVideo.setVisibility(View.GONE);
                }
                if (ivSelectedImage.getVisibility() == View.VISIBLE) {
                    ivSelectedImage.setVisibility(View.GONE);
                }

                imageFile = null;
                etYtLink.setText("");
                new VideoPicker.Builder(MyProfile.this)
                        .mode(VideoPicker.Mode.GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();
            }
        });
        linLayPickYtLInk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vvSelectedVideo.getVisibility() == View.VISIBLE) {
                    vvSelectedVideo.setVisibility(View.GONE);
                }
                if (ivSelectedImage.getVisibility() == View.VISIBLE) {
                    ivSelectedImage.setVisibility(View.GONE);
                }

                imageFile = null;
                videoFile = null;
                etYtLink.setVisibility(View.VISIBLE);
                ivUnSelectImage.setVisibility(View.VISIBLE);

            }
        });
        etYtLink.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                ytVidId = getYouTubeId(s.toString());

                if (ytVidId.length() > 4)
                {
                    vvSelectedYtVideo.setVisibility(View.VISIBLE);

                    if(ytPlayerInitialized)
                    {

                    }else {
                        vvSelectedYtVideo.initializeWithWebUi(new YouTubePlayerListener() {
                            @Override
                            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(ytVidId,0);
                                ytLinkfinal = ytVidId;
                            }

                            @Override
                            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlayerState playerState) {

                            }

                            @Override
                            public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlaybackQuality playbackQuality) {

                            }

                            @Override
                            public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlaybackRate playbackRate) {

                            }

                            @Override
                            public void onError(@NotNull YouTubePlayer youTubePlayer, PlayerConstants.@NotNull PlayerError playerError) {

                            }

                            @Override
                            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

                            }

                            @Override
                            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

                            }

                            @Override
                            public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

                            }

                            @Override
                            public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

                            }

                            @Override
                            public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

                            }
                        },true);
                        ytPlayerInitialized = true;
                    }

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        tvCommitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ytLinkfinal = etYtLink.getText().toString();
                content = etCaption.getText().toString();

                if (videoFile == null && imageFile == null && ytLinkfinal.isEmpty()) {
                    makePostWithText();
                } else {
                    if (videoFile == null && imageFile == null) {
                        makePostWithYtLink(ytVidId);
                    } else {
                        if (videoFile == null) {
                            makePostWithIamge();
                        } else {
                            makepostWithVideo();
                        }
                    }
                }
            }
        });

        //Till

    }

    private void clearSelecetMedia() {
        ivSelectedImage.setVisibility(View.GONE);
        ivUnSelectImage.setVisibility(View.GONE);
        vvSelectedVideo.setVisibility(View.GONE);
        etYtLink.setVisibility(View.GONE);
        vvSelectedYtVideo.setVisibility(View.GONE);
        imageFile = null;
        videoFile = null;
        etYtLink.setText("");
        vvSelectedYtVideo.release();
    }

    private void setUserData(String rating)
    {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        if(userObj.getProfilePath()!=null)
        {
            Glide.with(this).
                    load("http://nexgeno1.s3.us-east-2.amazonaws.com/public/uploads/profile_photos/"
                            +userObj.getProfilePath())
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
                    }).
                    into(profilPic);
        }else {
            Glide.with(this).
                    load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png")
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
                    }).
                    into(profilPic);
        }

        if (userObj.getCoverPath()!=null)
        {
            Glide.with(this).load("http://nexgeno1.s3.us-east-2.amazonaws.com/public/uploads/covers/"
                    +userObj.getCoverPath()).
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
                    }).
                    into(coverPhoto);
        }else {
            Glide.with(this).
                    load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png")
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
                    }).
                    into(coverPhoto);

        }


        username.setText(userObj.getName());
        phone.setText(userObj.getPhone());
        tvFollowerNo.setText(String.valueOf(followerList.size()));
        tvFollowingNo.setText(String.valueOf(followingsList.size()));

        if(userObj.getSex()==0)
        {
            gender.setText("Male");
        }else {
            gender.setText("Female");
        }

        if(userObj.getSkill()!=null)
            skill.setText(userObj.getSkill());

        if(userObj.getInterests().isEmpty())
        {

        }else {
            String[] interestsAry = userObj.getInterests().split("\"");
            layoutInterests.removeAllViews();

            for(int i = 1;i<interestsAry.length;i++)
            {
                addCardInalyout(interestsAry[i]);
            }
        }
        if(userObj.getUserType()==1)
        {
            latoutProRating.setVisibility(View.VISIBLE);
            if(userObj.getRating()==null)
            {
                tvRating.setText("Not Rated");
            }else {
                ratingBar.setRating(Float.parseFloat(rating));
                ratingBar.setIsIndicator(true);
                tvRating.setText(rating);
            }
        }
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

        // cardview.setPadding(15, 2, 15, 2);
        cardview.setForegroundGravity(Gravity.CENTER);

        cardview.setCardElevation(5);

        TextView textview = new TextView(this);

        textview.setLayoutParams(layoutparams);

        textview.setText(s);
        textview.setTextSize(14);

        textview.setTextColor(Color.WHITE);
        textview.setBackgroundColor(R.color.cardDarkBackground);

        textview.setPadding(25, 5, 25, 5);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);
        if (s.length() > 1) {
            layoutInterests.addView(cardview);
            layoutInterests.addView(new TextView(this));
        }
    }

    private void getUserByApi(String thisUserID) {

        Call<SingleUserbyIDResponse> call = RetrifitClient.getInstance()
                .getUserApi().getUSerById("user/get/"+thisUserID,token);
        call.enqueue(new Callback<SingleUserbyIDResponse>() {
            @Override
            public void onResponse(Call<SingleUserbyIDResponse> call, Response<SingleUserbyIDResponse> response) {
                Log.d("User>obJResponse>",response.raw().toString());
                try{
                    if (response.body()==null)
                    {
                        //progressPopup.dismiss();
                        if(response.errorBody()==null){
                            //progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            // showAuthError();
                        }

                    }else {
                        //progressPopup.dismiss();
                        userObj = response.body().getData();
                        followerList = response.body().getData().getFollower();
                        followingsList = response.body().getData().getFollowing();
                        if(response.body().getRating()==null)
                        {
                            setUserData("Not Rated");
                        }else {
                            int temp =  new Double(response.body().getRating()).intValue();
                            setUserData(String.valueOf(temp));
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Exception>>",e.toString());
                    // progressPopup.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SingleUserbyIDResponse> call, Throwable t) {
                Toast.makeText(MyProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseProfileImage() {
        is_profile_pic = true;
        is_choosing = false;
        new ImagePicker.Builder(MyProfile.this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    private void chooseCoverImage() {
        is_choosing = true;
        is_profile_pic = false;
        new ImagePicker.Builder(MyProfile.this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    private void setUi() {
        coverPhoto = findViewById(R.id.iv_profile_background);
        profilPic = findViewById(R.id.civ_profile_pic);
        username = findViewById(R.id.tv_user_name_on_profile);
        gender = findViewById(R.id.tv_user_gender_on_profile);
        phone = findViewById(R.id.tv_user_phone_on_profile);
        skill = findViewById(R.id.tv_user_skill);
        ratingBar  = findViewById(R.id.rtv_self);
        tvRating = findViewById(R.id.tv_rating_self);

        changeCoverView = findViewById(R.id.cv_change_cover_pic);
        tvPostNo = findViewById(R.id.tv_post_no);
        tvFollowerNo = findViewById(R.id.tv_followers_no);
        tvFollowingNo = findViewById(R.id.tv_following_no);
        tvNoPostToShow = findViewById(R.id.tv_no_post_self);
        shimmerFrameLayout = findViewById(R.id.sh_v_my_profile);
        latoutProRating = findViewById(R.id.lin_lay_rating_view_my_profile);
        layoutInterests = findViewById(R.id.lin_lay_interests);

        //For Post util Layout
        linLayPickImage = findViewById(R.id.lin_lay_pick_image_on_profile);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video_on_profile);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink_on_profile);
        ivSelectedImage = findViewById(R.id.iv_selected_image__on_profile);
        vvSelectedVideo = findViewById(R.id.vv_selected_video__on_profile);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv__on_profile);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_on_profile);
        etYtLink = findViewById(R.id.et_yt_link_on_profile);
        etCaption = findViewById(R.id.et_caption_on_profile);
        tvCommitPost = findViewById(R.id.tv_commit_post_on_profile);
        //recView
        recyclerView = findViewById(R.id.rec_v_my_profile);

    }

    //PostMethods
    private void makePostWithText() {

        startProgressPopup(this);

        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());
        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, null, null, null, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                    progressPopup.dismiss();
                    showSuccessPostMess("Your Post Made Successfully");
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Toast.makeText(MyProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressPopup.dismiss();
            }
        });
    }
    private void makePostWithYtLink(String ytLinkparam) {

        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());


        RequestBody ytLink = RequestBody.create(MediaType.parse("multipart/form-data"), ytLinkparam);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "2");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, null, ytLink, null, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessPostMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Toast.makeText(MyProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressPopup.dismiss();
            }
        });
    }
    private String getYouTubeId (String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }
    private void makePostWithIamge() {

        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image",imageFile.getName(), image);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, imageToSend, null, null, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessPostMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>Fail Response>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(MyProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void makepostWithVideo() {
        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());

        RequestBody video = RequestBody.create(MediaType.parse("multipart/form-data"),videoFile);

        MultipartBody.Part videoToSend = MultipartBody.Part.createFormData("video",videoFile.getName(),video);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, null, null, videoToSend, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessPostMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>FailedPostResponse>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(MyProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSuccessPostMess(String message) {

        postSucessDialog.setContentView(R.layout.class_join_success);
        TextView tvMessage = postSucessDialog.findViewById(R.id.tv_class_subscribe_message_on_dialog);
        btnOk = postSucessDialog.findViewById(R.id.btn_ok_class_join);
        postSucessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        tvMessage.setText(message);
        postSucessDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSucessDialog.dismiss();
                clearSelecetMedia();
                etCaption.setText("");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            for (String path : mPaths
            ) {
                Log.d("Image Got>>>>>>>>>", path);
                ivSelectedImage.setVisibility(View.VISIBLE);
                coverImage = new File(path);
                if(is_choosing)
                {
                    showPopup();
                }else {
                    if(is_profile_pic){
                        showPopup(is_profile_pic);
                    }else {
                        imageFile = new File(path);
                        ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
                        ivUnSelectImage.setVisibility(View.VISIBLE);
                    }

                }


            }
        }

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            for (String path : mPaths
            ) {
                Log.d("Video Got>>>>>>>>>", path);
                //ivSelectedImage.setVisibility(View.GONE);
                vvSelectedVideo.setVisibility(View.VISIBLE);
                videoFile = new File(path);
                vvSelectedVideo.setVideoURI(Uri.fromFile(videoFile));
                vvSelectedVideo.setMediaController(new MediaController(this));
                //vvSelectedVideo.start();
                ivUnSelectImage.setVisibility(View.VISIBLE);
            }
        }

    }

    private void showPopup(boolean is_profile_pic) {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.choosed_pic_layout,
                null); // inflating popup layout
        mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ImageView image = popUpView.findViewById(R.id.iv_choosed__image);
        image.setImageURI(Uri.fromFile(coverImage));
        ImageView ivCross = popUpView.findViewById(R.id.iv_cancle_poup_in_profile);
        Button uploadPic = popUpView.findViewById(R.id.btn_upload_cover_pic);
        uploadPic.setText("Set Profile");
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpopup.dismiss();
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCoverPic();
            }
        });


        // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

    private void showPopup() {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.choosed_pic_layout,
                null); // inflating popup layout
        mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ImageView image = popUpView.findViewById(R.id.iv_choosed__image);
        image.setImageURI(Uri.fromFile(coverImage));
        ImageView ivCross = popUpView.findViewById(R.id.iv_cancle_poup_in_profile);
        Button uploadPic = popUpView.findViewById(R.id.btn_upload_cover_pic);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpopup.dismiss();
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCoverPic();
            }
        });


        // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }
    private void uploadCoverPic() {

        startProgressPopup(this);
        mpopup.dismiss();

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"),coverImage);

        MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image",coverImage.getName(), image);

        // Same Method For Cover And Profile Depending On Booleean Value
        Call<PpUploadResponse> call;
        if(is_profile_pic)
        {
            call = RetrifitClient.getInstance().getUploadPicApi().uploadProfilePic(token,imageToSend);
        }else {
            call = RetrifitClient.getInstance()
                    .getUploadPicApi().uploadCoverPic(token,imageToSend);
        }


        call.enqueue(new Callback<PpUploadResponse>() {
            @Override
            public void onResponse(Call<PpUploadResponse> call, Response<PpUploadResponse> response) {
                progressPopup.dismiss();
                if(response.body().getCode()==200)
                {
                    if(is_choosing){

                        Glide.with(MyProfile.this).load(response.body().getImage()).listener(new RequestListener<Drawable>() {
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
                    }else {
                        Glide.with(MyProfile.this).load(response.body().getImageThumb()).listener(new RequestListener<Drawable>() {
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

                    Toast.makeText(MyProfile.this, "Picture Changed Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Response>>>", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<PpUploadResponse> call, Throwable t) {
                progressPopup.dismiss();
            }
        });
    }


    private  void  startProgressPopup(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.progres_popup,
                null); // inflating popup layout
        progressPopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        progressPopup.setAnimationStyle(android.R.style.Animation_Dialog);
        progressPopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

    private void loadUserPosts(String thisUid) {

        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"self",thisUid);
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
                        {
                            tvNoPostToShow.setVisibility(View.GONE);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                        recyclerView.setAdapter(new HomePostsAdapter(postDataList,MyProfile.this,Integer.parseInt(thisUid),token));
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