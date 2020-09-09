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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.ApiModals.Creator;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;
import com.twilio.video.app.FormPages.CreateNewSkill;
import com.twilio.video.app.FormPages.CreateTeamPage;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.MainPages.MyProfile;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingleTeamResponse.Data;
import com.twilio.video.app.SingleTeamResponse.SingleTeamResponse;
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

public class TeamDetailsPage extends AppCompatActivity {

    TextView tvTeamNAme,tvTeamNameTb,tvJoineave,tvmembers,tvLocation,tvHost,tvAbout;
    CardView cvjoinleave,cvChat,cvEdit;
    ImageView ivCover;
    RecyclerView recyclerView;
    String token,status,teamId;
    Data teamObj = new Data();
    Creator teamHostUser = new Creator();
    UserObj user = new UserObj();
    private List<Datum> postDataList = new ArrayList<>();


    // for Post Utils
    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    TextView tvCommitPost;

    String content, ytLinkfinal;
    String ytVidId = "";
    File coverImageFile, imageFile, videoFile;
    boolean ytPlayerInitialized = false;
    private boolean is_choosing = false;
    PopupWindow progressPopup, mpopup;
    Dialog joinSuccessDialog;
    Button btnOk;


    //ForChats
    List<com.twilio.video.app.DetailedChatResponse.Datum> detailedChatList = new ArrayList<>();
    ChatItemAdapter chatItemAdapter;
    RecyclerView chatsRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details_page);
        loadPrefs();
        setUi();
        Gson json = new Gson();
        teamHostUser = json.fromJson(getIntent().
                getStringExtra("teamHost"),Creator.class);


        joinSuccessDialog = new Dialog(this);
        teamId = getIntent().getStringExtra("teamId");
        status = getIntent().getStringExtra("status");
        if (status.equalsIgnoreCase("Created")) {
            tvJoineave.setText("Change Cover");
            cvEdit.setVisibility(View.VISIBLE);
            tvHost.setText("Hosted By : "+user.getName());
        }

        if (status.equalsIgnoreCase("Joined"))
            tvJoineave.setText("Leave");

        getSingleTeam();
        loadTeamPosts(teamId);


           tvHost.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(status.equalsIgnoreCase("Created"))
                   {
                       startActivity(new Intent(TeamDetailsPage.this, MyProfile.class));
                   }else {
                       Intent i  = new Intent(TeamDetailsPage.this, OtherUserProfile.class);
                       i.putExtra("token",token);
                       i.putExtra("other_user_id",teamHostUser.getId().toString());
                       startActivity(i);
                   }
               }
           });

        cvjoinleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvJoineave.getText().equals("Change Cover"))
                    chooseImageForCover();
                else
                    joinOrLeaveTeam();
            }
        });
        cvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeamDetailsPage.this, CreateTeamPage.class);
                i.putExtra("status","Edit");
                i.putExtra("token",token);
                i.putExtra("teamId",teamId);
                Gson gson = new Gson();
                String StrSkill = gson.toJson(teamObj);
                i.putExtra("teamObj", StrSkill);
                startActivity(i);
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


                new ImagePicker.Builder(TeamDetailsPage.this)
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
                new VideoPicker.Builder(TeamDetailsPage.this)
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

    private void loadTeamPosts(String teamId) {
        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getPostByFilter(token,"team",teamId);
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(TeamDetailsPage.this));
                        recyclerView.setAdapter(new HomePostsAdapter(postDataList,TeamDetailsPage.this,user.getId(),token));
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

    private void joinOrLeaveTeam() {
             Call<MakeClassResponse> call = RetrifitClient.getInstance()
                     .getTeamsApi().joinUnJoinTeam("team/subscribe/"+teamObj.getId().toString()
                     ,token);
             call.enqueue(new Callback<MakeClassResponse>() {
                 @Override
                 public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                     Log.d(">> TRag Response>>", response.raw().toString());

                     if(response.body()!=null)
                     {
                         if (response.body().getStatus())
                         {
                             if (tvJoineave.getText().equals("Join"))
                             {
                                 tvJoineave.setText("Leave");
                                 Toast.makeText(TeamDetailsPage.this, "Team Joined ", Toast.LENGTH_SHORT).show();
                             }

                             else{
                                 Toast.makeText(TeamDetailsPage.this, "Team Left", Toast.LENGTH_SHORT).show();
                                 tvJoineave.setText("Join");
                             }
                         }
                     }
                 }

                 @Override
                 public void onFailure(Call<MakeClassResponse> call, Throwable t) {

                 }
             });

    }

    private void makePostWithText() {
        startProgressPopup(this);

        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());
        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), teamObj.getId().toString());
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
                    showSuccessJoinMess("Your Post Made Successfully");
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Toast.makeText(TeamDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressPopup.dismiss();
            }
        });
    }

    private void makePostWithYtLink(String ytLinkparam) {

        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());


        RequestBody ytLink = RequestBody.create(MediaType.parse("multipart/form-data"), ytLinkparam);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "2");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), teamObj.getId().toString());
        RequestBody ClassId = RequestBody.create(MediaType.parse("multipart/form-data"), "0" );
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
                Toast.makeText(TeamDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), teamObj.getId().toString());
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
                    showSuccessJoinMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>Fail Response>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(TeamDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makepostWithVideo() {
        startProgressPopup(this);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), etCaption.getText().toString());

        RequestBody video = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);

        MultipartBody.Part videoToSend = MultipartBody.Part.createFormData("video", videoFile.getName(), video);

        RequestBody mediaType = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody groupId = RequestBody.create(MediaType.parse("multipart/form-data"), teamId);
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
                    showSuccessJoinMess("Your Post Made Successfully");
                    //Toast.makeText(ClassDetails.this, "Made Post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeNewPostResponse> call, Throwable t) {
                Log.d(">>>FailedPostResponse>>", t.toString());
                progressPopup.dismiss();
                Toast.makeText(TeamDetailsPage.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImageForCover() {
        is_choosing = true;
        new ImagePicker.Builder(TeamDetailsPage.this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();

    }

    private void getSingleTeam() {
        Call<SingleTeamResponse> call = RetrifitClient.getInstance()
                .getTeamsApi().getSingleTeam("team/"+teamId,token);
        call.enqueue(new Callback<SingleTeamResponse>() {
            @Override
            public void onResponse(Call<SingleTeamResponse> call, Response<SingleTeamResponse> response) {
                Log.d("Response>>>", response.raw().toString());

                if(response.body()!=null)
                {
                    if(response.body().getStatus())
                    {
                        teamObj = response.body().getData();
                        setTeamData();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleTeamResponse> call, Throwable t) {
                Log.d("Error>>", t.toString());
            }
        });
    }

    private void setTeamData() {
        if (teamObj.getCoverPath() != null) {
            Glide.with(this)
                    .load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/team_covers/" + teamObj.getCoverPath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.INVISIBLE);

                    return false;
                }
            }).into(ivCover);
        }
        tvTeamNAme.setText(teamObj.getName());
        tvmembers.setText("5 Members");
        tvAbout.setText(teamObj.getAbout());
        tvTeamNameTb.setText(teamObj.getName());
        if(user.getId()== Integer.parseInt(teamObj.getCreatorId())){

        }else {
            tvHost.setText("Hosted By : "+ teamHostUser.getName());
        }


    }

    private void loadPrefs() {
        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token = settings.getString("token", "");
        Gson json = new Gson();
        user = json.fromJson(settings.getString("UserObj",""),UserObj.class);

        chatItemAdapter = new ChatItemAdapter(detailedChatList,getApplicationContext(),String.valueOf(user.getId()));
    }

    private void startChat() {
        Call<DetailedChatResponse> call = RetrifitClient.getInstance()
                .getChatApi().getDetailedChatList(token, teamId, "skill");

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
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(TeamDetailsPage.this));
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
                    sendmessage(etMessage.getText().toString(), teamId, mopoup);
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
                .sendChatMess(token, "team", message, userId,"1");

        com.twilio.video.app.DetailedChatResponse.Datum messageObj = new com.twilio.video.app.DetailedChatResponse.Datum();
        messageObj.setContent(message);
        messageObj.setUserId(userId);
        messageObj.setBelongsTo(teamId);
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
        tvTeamNAme = findViewById(R.id.tv_team_name);
        tvJoineave = findViewById(R.id.tv_join_leave_team);
        tvmembers = findViewById(R.id.tv_team_members);
        tvLocation = findViewById(R.id.tv_team_location);
        tvHost = findViewById(R.id.tv_team_host);
        tvAbout = findViewById(R.id.tv_team_info);
        tvTeamNameTb = findViewById(R.id.tv_team_name_on_toolBar);
        recyclerView = findViewById(R.id.rec_view_team_posts);

        cvjoinleave = findViewById(R.id.cv_join_leave_team);
        cvChat = findViewById(R.id.cv_chat_team);
        cvEdit =findViewById(R.id.cv_edit_team);

        ivCover = findViewById(R.id.iv_team_cover);

        // For Post Utils

        linLayPickImage = findViewById(R.id.lin_lay_pick_image_in_team);
        ivSelectedImage = findViewById(R.id.iv_selected_image_team_post);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_team_post);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video_on_team);
        vvSelectedVideo = findViewById(R.id.vv_selected_video_skill);
        etYtLink = findViewById(R.id.et_yt_link_team_post);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink_on_team);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv_team_post);
        etCaption = findViewById(R.id.et_caption_team_post);
        tvCommitPost = findViewById(R.id.tv_commit_post_on_team);


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
                Toast.makeText(TeamDetailsPage.this, "Yet To eb Added", Toast.LENGTH_SHORT).show();
            }
        });


        // Creation of popup
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
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