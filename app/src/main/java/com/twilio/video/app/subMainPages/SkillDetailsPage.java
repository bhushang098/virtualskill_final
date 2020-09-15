package com.twilio.video.app.subMainPages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.ApiModals.Creator;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.PpUploadResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;
import com.twilio.video.app.FormPages.CreateNewSkill;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.MainPages.MyProfile;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SkillSingleResponse.Data;
import com.twilio.video.app.SkillSingleResponse.SkillSingleResponse;
import com.twilio.video.app.WebViewPage;
import com.twilio.video.app.adapter.ChatItemAdapter;
import com.twilio.video.app.adapter.HomePostsAdapter;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SkillDetailsPage extends AppCompatActivity {

    String skillId, status, token;
    Data skillObj;

    //ForUi
    TextView tvSkillName, tvSkillfees, tvMembers, tvTimestamp, tvHost, tvInfo, tvJoinLeave,tvSkillNameOnTb;
    CardView cvQuery, cvJoinLeave, cvEditSkill;
    ImageView ivSkillCover;
    RecyclerView recyclerView;
    private List<Datum> postDataList = new ArrayList<>();

    //For Post Utils
    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    TextView tvCommitPost;


    //ForChats
    List<com.twilio.video.app.DetailedChatResponse.Datum> detailedChatList = new ArrayList<>();
    ChatItemAdapter chatItemAdapter;
    RecyclerView chatsRecyclerView;



    String content, ytLinkfinal;
    String ytVidId = "";
    File coverImageFile, imageFile, videoFile;
    boolean ytPlayerInitialized = false;
    private boolean is_choosing = false;
    PopupWindow progressPopup, mpopup;
    Dialog joinSuccessDialog;
    Button btnOk;
    UserObj thisUSerObj = new UserObj();
    Creator skillHostUser = new Creator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_details_page);
        loadPrefs();
        setUi();
        joinSuccessDialog = new Dialog(this);
        skillId = getIntent().getStringExtra("skillId");
        status = getIntent().getStringExtra("status");
        Gson json = new Gson();
        if(getIntent().getStringExtra("skillHost")!=null)
        skillHostUser = json.fromJson(getIntent().getStringExtra("skillHost"),Creator.class);
        else
            skillHostUser = new Creator();
        if (status.equalsIgnoreCase("Created")) {
            tvJoinLeave.setText("Change Cover");
            cvEditSkill.setVisibility(View.VISIBLE);
            tvHost.setText("Hosted By : "+thisUSerObj.getName());
        }

        if (status.equalsIgnoreCase("Joined"))
            tvJoinLeave.setText("Leave");

        getSingleSkill();
        loadSkillPost(skillId);

        cvJoinLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJoinLeave.getText().equals("Change Cover"))
                    chooseImageForCover();
                else{
                    if(skillObj.getFee().equalsIgnoreCase("0"))
                    joinorLeaveSkill();
                    else
                        paySkill();
                }

            }
        });
        cvQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat();
            }
        });
        cvEditSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SkillDetailsPage.this, CreateNewSkill.class);
                i.putExtra("status","Edit");
                i.putExtra("token",token);
                i.putExtra("skillId",skillId);
                Gson gson = new Gson();
                String StrSkill = gson.toJson(skillObj);
                i.putExtra("skillObj", StrSkill);
                startActivity(i);
            }
        });

        tvHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equalsIgnoreCase("Created"))
                {
                    startActivity(new Intent(SkillDetailsPage.this, MyProfile.class));
                }else {
                    Intent i  = new Intent(SkillDetailsPage.this, OtherUserProfile.class);
                   i.putExtra("token",token);
                   i.putExtra("other_user_id",skillHostUser.getId().toString());
                    startActivity(i);
                }
            }
        });

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


                new ImagePicker.Builder(SkillDetailsPage.this)
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
                new VideoPicker.Builder(SkillDetailsPage.this)
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


    }

    private void loadSkillPost(String skillId) {

        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"skill",skillId);
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(SkillDetailsPage.this));
                        recyclerView.setAdapter(new HomePostsAdapter(postDataList,SkillDetailsPage.this,thisUSerObj.getId(),token));
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

    private void paySkill() {
        Intent i = new Intent(SkillDetailsPage.this, WebViewPage.class);
        String url = "https://virtualskill.in/api/pay?type=skill&id=";
       url = url+skillId+"&user_name="+thisUSerObj.getName()+"&phone="+thisUSerObj.getPhone()
                +"&email="+thisUSerObj.getEmail();
       i.putExtra("url_pay",url);

       startActivity(i);


    }

    private void chooseImageForCover() {
        is_choosing = true;
        new ImagePicker.Builder(SkillDetailsPage.this)
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
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillObj.getId().toString());

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
                Toast.makeText(SkillDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0" );
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillObj.getId().toString());

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
                Toast.makeText(SkillDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillObj.getId().toString());

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
                Toast.makeText(SkillDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody SkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillObj.getId().toString());

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
                Toast.makeText(SkillDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinorLeaveSkill() {

        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getSkillApi().joinUnJoinSkill("skill/subscribe/" + skillId, token);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d(">Response>>", response.raw().toString());

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        //Toast.makeText(SkillDetailsPage.this, response.message(), Toast.LENGTH_SHORT).show();
                        if (tvJoinLeave.getText().equals("Join"))
                        {
                            tvJoinLeave.setText("Leave");
                            Toast.makeText(SkillDetailsPage.this, "Skill Joined ", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(SkillDetailsPage.this, "Skill Left", Toast.LENGTH_SHORT).show();
                            tvJoinLeave.setText("Join");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {
                Log.d("Error>>", t.toString());

            }
        });
    }

    private void loadPrefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");
        Gson json = new Gson();
        thisUSerObj = json.fromJson(settings.getString("UserObj",""),UserObj.class);
        chatItemAdapter = new ChatItemAdapter(detailedChatList,getApplicationContext(),String.valueOf(thisUSerObj.getId()));

    }


    private void startChat() {
        Call<DetailedChatResponse> call = RetrifitClient.getInstance()
                .getChatApi().getDetailedChatList(token, skillId, "skill");

        inflateDetailedMessage(detailedChatList);

        call.enqueue(new Callback<DetailedChatResponse>() {
            @Override
            public void onResponse(Call<DetailedChatResponse> call, Response<DetailedChatResponse> response) {
                Log.d("Chat Response>>", response.raw().toString());
                if (response.body() != null) {
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

    private void inflateDetailedMessage(List<com.twilio.video.app.DetailedChatResponse.Datum> data) {
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
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(SkillDetailsPage.this));
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
                    sendmessage(etMessage.getText().toString(), skillId, mopoup);
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

    private void sendmessage(String message, String skillId, PopupWindow popuseWindow) {

        Call<Map> call = RetrifitClient.getInstance().getChatApi()
                .sendChatMess(token, "skill", message, skillId,"1");

        com.twilio.video.app.DetailedChatResponse.Datum messageObj = new com.twilio.video.app.DetailedChatResponse.Datum();
        messageObj.setContent(message);
        messageObj.setUserId(thisUSerObj.getId().toString());
        messageObj.setBelongsTo(skillId);
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

    private void getSingleSkill() {
        Call<SkillSingleResponse> call = RetrifitClient.getInstance()
                .getSkillApi().getSingleSkill("skill/" + skillId, token);

        call.enqueue(new Callback<SkillSingleResponse>() {
            @Override
            public void onResponse(Call<SkillSingleResponse> call, Response<SkillSingleResponse> response) {
                Log.d(">>Response Skill", response.raw().toString());
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        skillObj = response.body().getData();
                        setData();
                    }
                }
            }

            @Override
            public void onFailure(Call<SkillSingleResponse> call, Throwable t) {
                Log.d("Error>>", t.toString());
            }
        });


    }

    private void setData() {


        if (skillObj.getCoverPath() != null) {
            Glide.with(this)
                    .load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/skill_covers/" + skillObj.getCoverPath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.INVISIBLE);

                    return false;
                }
            }).into(ivSkillCover);
        }
       if (skillObj.getFee().equalsIgnoreCase("0"))
       {
           tvSkillfees.setText("Free Skill");
       }else {
           tvSkillfees.setText("INR : "+ skillObj.getFee());
       }
        tvSkillName.setText(skillObj.getName());
        tvMembers.setText(getIntent().getStringExtra("memCount")+" Members");
        tvTimestamp.setText(skillObj.getCreatedAt().split(" ")[0]);
        if(skillHostUser!=null)
        tvHost.setText("Hosted By : " + skillHostUser.getName());
        tvInfo.setText(skillObj.getAbout());
        tvSkillNameOnTb.setText(skillObj.getName());

    }

    private void setUi() {
        //For Ui
        tvSkillName = findViewById(R.id.tv_skill_name);
        tvJoinLeave = findViewById(R.id.tv_join_leave_skill);
        tvSkillfees = findViewById(R.id.tv_skill_fees);
        tvMembers = findViewById(R.id.tv_skill_members);
        tvTimestamp = findViewById(R.id.tv_skill_timeStamp);
        tvHost = findViewById(R.id.tv_skill_host);
        tvInfo = findViewById(R.id.tv_skill_info);
        tvCommitPost = findViewById(R.id.tv_commit_post_skill);
        tvSkillNameOnTb = findViewById(R.id.tv_skill_name_on_toolBar);
        recyclerView = findViewById(R.id.rec_view_skill_posts);

        cvJoinLeave = findViewById(R.id.cv_join_leave_skill);
        cvEditSkill = findViewById(R.id.cv_skill_edit);
        cvQuery = findViewById(R.id.cv_skill_query);
        ivSkillCover = findViewById(R.id.iv_skill_cover);


        //For PostUtils
        linLayPickImage = findViewById(R.id.lin_lay_pick_image);
        ivSelectedImage = findViewById(R.id.iv_selected_image_skills);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_skills);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video);
        vvSelectedVideo = findViewById(R.id.vv_selected_video_skill);
        etYtLink = findViewById(R.id.et_yt_link_skill);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv_skill);
        etCaption = findViewById(R.id.et_caption_skill);

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
                //ToDo Change Skill Cover
                changESkillCover();
                //Toast.makeText(SkillDetailsPage.this, "Yet To eb Added", Toast.LENGTH_SHORT).show();
            }
        });


        // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

    private void changESkillCover() {
        startProgressPopup(this);
        mpopup.dismiss();

        RequestBody skillId = RequestBody.create(MediaType.parse("multipart/form-data"),getIntent().getStringExtra("skillId"));
        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"),coverImageFile);

        MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image",coverImageFile.getName(), image);


        Call<PpUploadResponse> call = RetrifitClient.getInstance()
                .getUploadPicApi().uploadSkillCover(token,skillId,imageToSend);

        call.enqueue(new Callback<PpUploadResponse>() {
            @Override
            public void onResponse(Call<PpUploadResponse> call, Response<PpUploadResponse> response) {
                Log.d("SkillCover>>",response.raw().toString());

                progressPopup.dismiss();

                if(response.body()!=null)
                {
                    //Log.d("REsponseSkill>>",response.body().getMessage());
                    if(response.body().getCode()==200)
                        Toast.makeText(SkillDetailsPage.this, "Cover Uploaded", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SkillDetailsPage.this, "Unable To Change Cover Image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PpUploadResponse> call, Throwable t) {
                progressPopup.dismiss();
                Log.d("Exception>>",t.toString());
            }
        });
    }

    public void showSuccessJoinMess(String message) {
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



}