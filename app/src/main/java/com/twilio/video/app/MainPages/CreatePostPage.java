package com.twilio.video.app.MainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;

import com.twilio.video.app.HomePage;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostPage extends AppCompatActivity {

    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage,ivSendButton;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    String content, ytLinkfinal;
    File imageFile, videoFile;

    String ytVidId = "";
    boolean ytPlayerInitialized = false;
    PopupWindow progressPopup;
    String token;
    Dialog postSucessDialog;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_page);
        loadPreferences();
        setUi();
        postSucessDialog = new Dialog(this);

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


                new ImagePicker.Builder(CreatePostPage.this)
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
                new VideoPicker.Builder(CreatePostPage.this)
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

        ivSendButton.setOnClickListener(new View.OnClickListener() {
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


    private  void  startProgressPopup(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.progres_popup,
                null); // inflating popup layout
        progressPopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        progressPopup.setAnimationStyle(android.R.style.Animation_Dialog);
        progressPopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
    }

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
                Toast.makeText(CreatePostPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreatePostPage.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressPopup.dismiss();
            }
        });
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
                Toast.makeText(CreatePostPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreatePostPage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });
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
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences("login_preferences",
                Context.MODE_PRIVATE);
        token  = settings.getString("token","");

    }

    private void setUi() {
        linLayPickImage = findViewById(R.id.lin_lay_pick_image_in_new_post);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video_on_new_post);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink_on_new_post);
        ivSelectedImage = findViewById(R.id.iv_selected_image_new_post);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_new_post);
        etCaption = findViewById(R.id.et_caption_new_post);
        etYtLink = findViewById(R.id.et_yt_link_new_post);
        vvSelectedVideo = findViewById(R.id.vv_selected_video_new_post);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv_new_post);
        ivSendButton = findViewById(R.id.iv_make_post);
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
                imageFile = new File(path);
                ivSelectedImage.setImageURI(Uri.fromFile(imageFile));
                ivUnSelectImage.setVisibility(View.VISIBLE);
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