package com.twilio.video.app.subMainPages;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.ApiModals.ClassJoinLeaveResponse;
import com.twilio.video.app.ApiModals.Creator;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.RoomJoinResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.FormPages.CreateClassPage;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.MainPages.MyProfile;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingleClassResponse.Class;
import com.twilio.video.app.SingleClassResponse.SingleClassResponse;
import com.twilio.video.app.SingleClassResponse.User;
import com.twilio.video.app.WebViewPage;
import com.twilio.video.app.adapter.HomePostsAdapter;
import com.twilio.video.app.data.Preferences;
import com.twilio.video.app.ui.room.RoomActivity;
import com.twilio.video.app.util.DateUtil;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassDetails extends AppCompatActivity {

    Class classobj = new Class();
    User classHostUser = new User();
    TextView className, location, fees, dateFromTo, duration, host,
            about, tvHostOrJoin, tvJoinLeaveClass, tvCommitPost, tvClassNameOnToolBar, tvBookSeat;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String PREFS_NAME = "login_preferences";
    String token,classId;
    UserObj userObj;
    CardView cvHostOrJoinClass, cvBookSeat, cvJoinorLeave;
    ProgressBar progressBar;
    Dialog joinSuccessDialog;
    Button btnOk;
    String status = "";
    String ytVidId = "";
    ImageView ivClassCover;
    boolean ytPlayerInitialized = false;
    RecyclerView recyclerView;
    private List<Datum> postDataList = new ArrayList<>();


    //For Post Purpose
    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    String content, ytLinkfinal;
    File coverImageFile, imageFile, videoFile;
    private boolean is_choosing = false;
    PopupWindow progressPopup, mpopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        loadpreference();
        status = getIntent().getStringExtra("status");
        classId = getIntent().getStringExtra("classId");
        Gson json = new Gson();
        classHostUser = json.fromJson(getIntent().getStringExtra("classHost"),User.class);
        setUi();
        joinSuccessDialog = new Dialog(this);
        getsingleClass();
        loadClassPost(classId);

        cvJoinorLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvJoinLeaveClass.getText().toString().equalsIgnoreCase("Change Cover Pic")) {
                    chooseImageForCover();
                } else {

                    if(classobj.getFee().equalsIgnoreCase("0"))
                    joinOrLeaveClass(classobj.getEId());
                    else
                        payClass();
                }
            }
        });

        cvHostOrJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(classobj.getEId());
            }
        });

        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equalsIgnoreCase("Created"))
                {
                    startActivity(new Intent(ClassDetails.this, MyProfile.class));
                }else {
                    Intent i  = new Intent(ClassDetails.this, OtherUserProfile.class);
                    i.putExtra("token",token);
                    i.putExtra("other_user_id",classHostUser.getId().toString());
                    startActivity(i);
                }

            }
        });

        // For Post Utils
        linLayPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_choosing = false;

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


                new ImagePicker.Builder(ClassDetails.this)
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
                new VideoPicker.Builder(ClassDetails.this)
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
                String[] ytLink = s.toString().split("/");
                ytVidId = ytLink[ytLink.length - 1];

                if (ytVidId.length() > 4) {
                    vvSelectedYtVideo.setVisibility(View.VISIBLE);

                    if (ytPlayerInitialized) {

                    } else {
                        vvSelectedYtVideo.initializeWithWebUi(new YouTubePlayerListener() {
                            @Override
                            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(ytVidId, 0);
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
                        }, true);
                        ytPlayerInitialized = true;
                    }

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        //>>>>>

        tvCommitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDO make new Post In Class
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


        // Book set Used for Class ed it For Creater
        // And Book Seat For Normal User


        cvBookSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userObj.getId() == classobj.getCreatorId()) {
                    Intent i = new Intent(ClassDetails.this, CreateClassPage.class);
                    i.putExtra("token", token);
                    Gson gson = new Gson();
                    String userObjStr = gson.toJson(userObj);
                    String classObjString = gson.toJson(classHostUser);
                    i.putExtra("classObj", classObjString);
                    i.putExtra("userObj", userObjStr);
                    startActivity(i);
                    finish();
                } else {
                    joinOrLeaveClass(classobj.getEId());
                }
            }
        });

    }

    private void loadClassPost(String classId) {
        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"class",classId);
        call.enqueue(new Callback<HomePostModal>() {
            @Override
            public void onResponse(Call<HomePostModal> call, Response<HomePostModal> response) {
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());

//                        shimmerFrameLayout.stopShimmerAnimation();
//                        shimmerFrameLayout.setVisibility(View.GONE);

                    }else {
                        postDataList = response.body().getPosts().getData();
                        recyclerView.setLayoutManager(new LinearLayoutManager(ClassDetails.this));
                        recyclerView.setAdapter(new HomePostsAdapter(postDataList,ClassDetails.this,userObj.getId(),token));
//                        shimmerFrameLayout.stopShimmerAnimation();
//                        shimmerFrameLayout.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
//                    shimmerFrameLayout.stopShimmerAnimation();
//                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HomePostModal> call, Throwable t) {
                //   Toast.makeText(HomePage.this, "Error ", Toast.LENGTH_SHORT).show();

//                shimmerFrameLayout.stopShimmerAnimation();
//                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void payClass() {

            Intent i = new Intent(ClassDetails.this, WebViewPage.class);
            String url = "https://virtualskill.in/api/pay?type=class&id=";
            url = url+classId+"&user_name="+userObj.getName()+"&phone="+userObj.getPhone()
                    +"&email="+userObj.getEmail();
            i.putExtra("url_pay",url);

            startActivity(i);

    }

    private void getsingleClass() {
        Call<SingleClassResponse> call = RetrifitClient.getInstance()
                .getClassesApi().getSingleClass("class/"+classId.toString(),token);

        call.enqueue(new Callback<SingleClassResponse>() {
            @Override
            public void onResponse(Call<SingleClassResponse> call, Response<SingleClassResponse> response) {
                Log.d("Response<<", response.raw().toString());

                classobj = response.body().getClass_();
                if (classobj==null)
                {
                    Toast.makeText(ClassDetails.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }else {

                    classHostUser = response.body().getUser();
                    setClassdeta();
                }

            }

            @Override
            public void onFailure(Call<SingleClassResponse> call, Throwable t) {

            }
        });

    }

    private void setClassdeta() {

        if(classobj.getCoverPath()!=null)
        {
            Glide.with(this)
                    .load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/event_covers/"+classobj.getCoverPath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(ivClassCover);
        }

        className.setText(classobj.getName());
        location.setText(classobj.getLocation());
        tvClassNameOnToolBar.setText(classobj.getName());
        if (classobj.getFee().equalsIgnoreCase("0")) {
            fees.setText("Free Class");
        } else {
            fees.setText("Fee : " + classobj.getFee()+" INR");
        }

        if (userObj.getId() == classobj.getCreatorId()) {
            tvBookSeat.setText("Edit Info");
            tvHostOrJoin.setText("Host Live Class");
            host.setText("Hosted By : " + userObj.getName());
        } else {
            tvHostOrJoin.setText("Join Live Class");
            host.setText("Hosted By : " + classHostUser.getName());
        }

        if (status.equalsIgnoreCase("Joined"))
            tvJoinLeaveClass.setText("Leave");

        if (status.equalsIgnoreCase("Created"))
            tvJoinLeaveClass.setText("Change Cover Pic");

        dateFromTo.setText(DateUtil.getDate(classobj.getStartDate()) + " - " + DateUtil.getDate(classobj.getEndDate()));
        duration.setText("Duration : " + classobj.getDuration().toString() + " minutes");

        about.setText(classobj.getAbout());

    }

    private void chooseImageForCover() {
        is_choosing = true;
        new ImagePicker.Builder(ClassDetails.this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();

    }

    private void makePostWithText() {
        startProgressPopup(this);

        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());
        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(classobj.getEId()));
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
                    showSuccessJoinMess("Your Post Made Successfully");
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Toast.makeText(ClassDetails.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(classobj.getEId()));
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, null, ytLink, null, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessJoinMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Toast.makeText(ClassDetails.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressPopup.dismiss();
            }
        });
    }

    private void makePostWithIamge() {

        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image", imageFile.getName(), image);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(classobj.getEId()));
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, imageToSend, null, null, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessJoinMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>Fail Response>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(ClassDetails.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makepostWithVideo() {
        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());

        RequestBody video = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);

        MultipartBody.Part videoToSend = MultipartBody.Part.createFormData("video", videoFile.getName(), video);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(classobj.getEId()));
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");

        Call<MakeNewPostResponse> call = RetrifitClient.getInstance().getPostApi().
                makeNewPostByAPI(token, content, null, null, videoToSend, mediaType, groupId, ClassId, SkillId);

        call.enqueue(new Callback<MakeNewPostResponse>() {
            @Override
            public void onResponse(Call<MakeNewPostResponse> call, Response<MakeNewPostResponse> response) {

                Log.d(">>>PostResponse>>", response.raw().toString());

                if (response.body().getCode() == 200) {
                    progressPopup.dismiss();
                    showSuccessJoinMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>FailedPostResponse>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(ClassDetails.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinOrLeaveClass(int classId) {

        Call<ClassJoinLeaveResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        call = RetrifitClient.getInstance()
                .getClassesApi().joinUnJoinClass("class/subscribe/" + String.valueOf(classId), token);

        call.enqueue(new Callback<ClassJoinLeaveResponse>() {

            @Override
            public void onResponse(Call<ClassJoinLeaveResponse> call, Response<ClassJoinLeaveResponse> response) {
                try {
                    if (response.body() == null) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d("ResponseJoinunJoin>>>>", response.raw().toString());

                        if (response.body().getStatus() == true) {
                            Toast.makeText(ClassDetails.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            if (tvJoinLeaveClass.getText().equals("Join"))
                                tvJoinLeaveClass.setText("Leave");
                            progressBar.setVisibility(View.GONE);

                            if(response.body().getMessage().equalsIgnoreCase("Success"))
                            {
                                showSuccessJoinMess(classobj.getName() +"  Class Joined Successfully");
                            }else
                            {
                                showSuccessJoinMess("You Left "+ classobj.getName() +" Class ");
                            }


                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ClassJoinLeaveResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void requestPermissions(int classId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permissionsGranted()) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSIONS_REQUEST_CODE);
            } else {
                //setupLocalMedia();

                //Store class Id in Asared prefs Fort temp Time
                gteClassIdForprefs(token, classId);

            }
        } else {
            //setupLocalMedia();
            gteClassIdForprefs(token, classId);

        }
    }

    private boolean permissionsGranted() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int resultStorage =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return ((resultCamera == PackageManager.PERMISSION_GRANTED)
                && (resultMic == PackageManager.PERMISSION_GRANTED)
                && (resultStorage == PackageManager.PERMISSION_GRANTED));
    }

    private String loadpreference() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = settings.getString("UserObj", "");
        userObj = gson.fromJson(json, UserObj.class);
        token = settings.getString("token", "");

        return userObj.getName();

    }

    private void setUi() {
        className = findViewById(R.id.tv_class_name_on_details);
        location = findViewById(R.id.tv_class_location_on_details);
        fees = findViewById(R.id.tv_free_paid_class_on_details);
        dateFromTo = findViewById(R.id.tv_date_from_to_on_class_details);
        duration = findViewById(R.id.tv_class_duration_on_details);
        host = findViewById(R.id.tv_class_host_name);
        about = findViewById(R.id.tv_class_about);
        cvHostOrJoinClass = findViewById(R.id.cv_class_host_join);
        tvHostOrJoin = findViewById(R.id.tv_class_join_host);
        tvJoinLeaveClass = findViewById(R.id.tv_join_leave_class);
        cvBookSeat = findViewById(R.id.cv_class_book_seat);
        cvJoinorLeave = findViewById(R.id.cv_join_leave_class);
        progressBar = findViewById(R.id.pb_class_details);
        ivClassCover = findViewById(R.id.iv_class_cover);
        // Post Utils
        linLayPickImage = findViewById(R.id.lin_lay_pick_image_in_class);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video_on_class);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink_on_class);
        ivSelectedImage = findViewById(R.id.iv_selected_image_class_post);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_class_post);
        etCaption = findViewById(R.id.et_caption_class_post);
        etYtLink = findViewById(R.id.et_yt_link_class_post);
        vvSelectedVideo = findViewById(R.id.vv_selected_video_class_post);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv_class_post);
        tvCommitPost = findViewById(R.id.tv_commit_post_on_post);
        recyclerView = findViewById(R.id.rec_view_class_posts);

        // till
        tvClassNameOnToolBar = findViewById(R.id.tv_class_name_on_toolBar);
        tvBookSeat = findViewById(R.id.tv_book_seat_on_class_details);

    }


    private void gteClassIdForprefs(String token, int classId) {

        Call<RoomJoinResponse> call;

        if (userObj.getId() == classobj.getCreatorId()) {
            call = RetrifitClient.getInstance()
                    .getClassesApi().getRoomJoinToken("class/host/" + String.valueOf(classId), token);
        } else {
            call = RetrifitClient.getInstance()
                    .getClassesApi().getRoomJoinToken("class/room_join/" + String.valueOf(classId), token);
        }

        call.enqueue(new Callback<RoomJoinResponse>() {
            @Override
            public void onResponse(Call<RoomJoinResponse> call, Response<RoomJoinResponse> response) {
                try {
                    Log.d("RoomJoinResponse",response.raw().toString());

                    if (response.body() == null) {

                    } else {
                        if (response.body().getStatus() == false) {
                            Toast.makeText(ClassDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (response.body().getToken() == null) {

                        } else {
                            String roomJoinToken = response.body().getToken();
                            savePreferences(roomJoinToken);
                            Intent i = new Intent(ClassDetails.this, RoomActivity.class);
                            i.putExtra("user_name", userObj.getName());
                            i.putExtra("room_name", classobj.getName());
                            i.putExtra("token",token);
                            i.putExtra("classId",String.valueOf(classId));

                            startActivity(i);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RoomJoinResponse> call, Throwable t) {
                Log.d("Exception>in>room>token",t.toString());
            }
        });
    }


    private void savePreferences(String roomJoinToken) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // Edit and commit
        editor.putString(Preferences.ROOM_JOIN_TOKEN, roomJoinToken);
        editor.commit();
    }

    public void showSuccessJoinMess(String message) {
        progressBar.setVisibility(View.INVISIBLE);
        joinSuccessDialog.setContentView(R.layout.class_join_success);
        TextView tvMessage = joinSuccessDialog.findViewById(R.id.tv_class_subscribe_message_on_dialog);
        btnOk = joinSuccessDialog.findViewById(R.id.btn_ok_class_join);
        joinSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
        tvMessage.setText(message);
        joinSuccessDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinSuccessDialog.dismiss();
                finish();
            }
        });

    }

    private void startProgressPopup(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.progres_popup,
                null); // inflating popup layout
        progressPopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        progressPopup.setAnimationStyle(android.R.style.Animation_Dialog);
        progressPopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            for (String path : mPaths
            ) {
                Log.d("Image Got>>>>>>>>>", path);

                if (is_choosing) {
                    coverImageFile = new File(path);
                    showPopup();

                } else {
                    imageFile = new File(path);
                    ivSelectedImage.setVisibility(View.VISIBLE);
                    ivSelectedImage.setImageURI(Uri.fromFile(imageFile));
                    ivUnSelectImage.setVisibility(View.VISIBLE);
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

    private void showPopup() {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.choosed_pic_layout,
                null); // inflating popup layout
        mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ImageView image = popUpView.findViewById(R.id.iv_choosed__image);
        image.setImageURI(Uri.fromFile(coverImageFile));
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
                changeClassCover();
            }
        });


        // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

    private void changeClassCover() {

        startProgressPopup(this);
        mpopup.dismiss();

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"),coverImageFile);

        MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image",coverImageFile.getName(), image);

        Call<ResponseBody> call = RetrifitClient.getInstance().getUploadPicApi()
                .uploadClassCover("class_upload_cover/"+classobj.getEId().toString(),token,imageToSend);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("Tag>>>",response.raw().toString());
                progressPopup.dismiss();
                if(response.body()!=null)
                {
                    Toast.makeText(ClassDetails.this, "Cover Changed Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}