package com.twilio.video.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.BuildConfig;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.UserObj;
import com.twilio.video.app.ChatUserResponse.ChatUserResponse;
import com.twilio.video.app.Dialogs.ConformationDialog;
import com.twilio.video.app.HomePostModal.Datum;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.MainPages.ClassesPage;
import com.twilio.video.app.MainPages.MyProfile;
import com.twilio.video.app.MainPages.ProUserPage;
import com.twilio.video.app.MainPages.SearchPage;
import com.twilio.video.app.MainPages.SettingsActivity;
import com.twilio.video.app.MainPages.SkillPage;
import com.twilio.video.app.MainPages.StudentsUserPage;
import com.twilio.video.app.MainPages.TeamsPage;
import com.twilio.video.app.MainPages.UsersPage;
import com.twilio.video.app.adapter.ChatUSerAdapter;
import com.twilio.video.app.adapter.HomePostsAdapter;
import com.twilio.video.app.subMainPages.ClassDetails;
import com.twilio.video.app.util.NetworkOperator;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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
import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

import static tvi.webrtc.ContextUtils.getApplicationContext;

public class HomePage extends AppCompatActivity{
    Toolbar toolbar;
    ImageButton drawerButtion;
    ImageView searchButton;
    ImageBadgeView ivGoChatScreen;
    NavigationView navView;
    private  List<Datum> postDataList = new ArrayList<>();
    private RecyclerView revPostView;
    private SwipeRefreshLayout refreshLayout;
    FloatingActionButton fabHodeNav;
    OnSwipeTouchListener onSwipeTouchListener;
    String token,name,mail;
    private static  String PREFS_NAME = "login_preferences";
    private static  String PREF_UNAME = "Username";
    private static  String PREF_PASSWORD = "Password";
    private  static  String TOKEN = "token";
    private  String DefaultTokenValue = "";

    private  String DefaultUnameValue = "";
    private String UnameValue,fcm_token;

    private  String DefaultPasswordValue = "";

    private String PasswordValue;
    private RelativeLayout errorlayout;
    private TextView erroeTitle;
    private TextView errorMessage;
    boolean callCheckUpdate = true;
    int versioncode =1;

    //For Header
    ImageView ivCoverOnheader,civprofileOnHeader;
    TextView tvUserName,tvUserMail;

    UserObj userObj = new UserObj();

    // For Post Utils
    LinearLayout linLayPickImage, linLayPickVideo, linLayPickYtLInk;
    ImageView ivSelectedImage, ivUnSelectImage;
    VideoView vvSelectedVideo;
    EditText etCaption, etYtLink;
    YouTubePlayerView vvSelectedYtVideo;
    String content, ytLinkfinal;
    File imageFile, videoFile;
    TextView tvCommitPost;
    String ytVidId = "";
    boolean ytPlayerInitialized = false;
    PopupWindow progressPopup,updatePopup;
    // For Dialogsa And Progress
    Button btnOk;
    Dialog postSucessDialog;


    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    private void startbgService() {
        startService(new Intent(HomePage.this,MyNotificationService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        loadPreferences();
        setUi();
       // startbgService();
        postSucessDialog = new Dialog(this);
        setNavHeaderdata();

        if(callCheckUpdate);
        checkforUpdate();
        loadHomePosts(token);
        loadUnreadmesscount();
        toolbar.setTitle("");
        ivGoChatScreen.setBadgeValue(22)
                .setBadgeOvalAfterFirst(true)
                .setBadgeTextSize(10)
                .setBadgeBackground(getResources().getDrawable(R.drawable.unerad_count_bg))
                .setMaxBadgeValue(99)
                .setBadgePosition(BadgePosition.TOP_RIGHT)
                .setBadgeTextStyle(Typeface.NORMAL)
                .setShowCounter(true)
                .setBadgePadding(2);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationExtenderExample.ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationExtenderExample.ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null)
                    Log.d("OneSignalId", "registrationId:" + registrationId);
            }
        });

        setSupportActionBar(toolbar);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearSelecetMedia();
                loadHomePosts(token);


            }
        });
        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.flHome));
        fabHodeNav.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                navView.setVisibility(View.INVISIBLE);
                fabHodeNav.setVisibility(View.INVISIBLE);
            }
        });

        ivGoChatScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivGoChatScreen.visibleBadge(false);
                startActivity(new Intent(HomePage.this,ChatScreen.class));
            }
        });

        drawerButtion.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"WrongConstant", "RestrictedApi"})
            @Override
            public void onClick(View v) {
                if(navView.getVisibility()==View.VISIBLE){
                    navView.setVisibility(View.INVISIBLE);
                    fabHodeNav.setVisibility(View.INVISIBLE);

                }else {
                    navView.setVisibility(View.VISIBLE);
                    fabHodeNav.setVisibility(View.VISIBLE);
                    fabHodeNav.requestFocusFromTouch();
                }
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_skill:
                        startActivity(new Intent(HomePage.this, SkillPage.class));
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.nav_teams:
                        startActivity(new Intent(HomePage.this, TeamsPage.class));
                        overridePendingTransition(0, 0);

                        break;

                    case R.id.nav_classes:
                        startActivity(new Intent(HomePage.this, ClassesPage.class));
                        overridePendingTransition(0, 0);

                        break;

                    case R.id.nav_users:
                        startActivity(new Intent(HomePage.this, UsersPage.class));
                        overridePendingTransition(0, 0);

                        break;

                }

                return false;
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_my_profile:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i6 = new Intent(HomePage.this,MyProfile.class);
                        i6.putExtra("token",token);
                        startActivity(i6);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_home:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.nav_skill:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i4 = new Intent(HomePage.this,SkillPage.class);
                        i4.putExtra("token",token);
                        startActivity(i4);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_teams:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i5 = new Intent(HomePage.this,TeamsPage.class);
                        i5.putExtra("token",token);
                        startActivity(i5);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_classes:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i3 = new Intent(HomePage.this,ClassesPage.class);
                        i3.putExtra("token",token);
                        startActivity(i3);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.nav_users:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i2 = new Intent(HomePage.this, StudentsUserPage.class);
                        i2.putExtra("token",token);
                        startActivity(i2);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_settings:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(HomePage.this,SettingsActivity.class);
                        i.putExtra("token",token);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.log_out:
                        ConformationDialog.showConformDialog(HomePage.this,"LogOut",
                                "Do You Really Want To Log Out Of VirtualSkill","log_out");
                        break;
                    case R.id.nav_users_pro:
                        fabHodeNav.setVisibility(View.GONE);
                        navView.setVisibility(View.INVISIBLE);
                        Intent ii = new Intent(HomePage.this, ProUserPage.class);
                        ii.putExtra("token",token);

                        startActivity(ii);
                        overridePendingTransition(0, 0);
                        break;
                       // Toast.makeText(HomePage.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }


                return true;

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


                new ImagePicker.Builder(HomePage.this)
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
                new VideoPicker.Builder(HomePage.this)
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, SearchPage.class);
                i.putExtra("token",token);
                startActivity(i);
            }
        });
    }

    private void loadUnreadmesscount() {
        Call<ChatUserResponse> call = RetrifitClient.getInstance().getChatApi()
                .getChatUsers(token);
        call.enqueue(new Callback<ChatUserResponse>() {
            @Override
            public void onResponse(Call<ChatUserResponse> call, Response<ChatUserResponse> response) {
                Log.d(">>>Chat Response", response.raw().toString());

                if(response.body() == null) {

                } else {
                    ivGoChatScreen.setBadgeValue(response.body().getTotalNew());

                }
            }

            @Override
            public void onFailure(Call<ChatUserResponse> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                Log.d(">>Exceptopn>>", t.toString());
            }
        });
    }

    private void checkforUpdate() {
        callCheckUpdate = false;

        Call<Map<String,Integer>> call = RetrifitClient.getInstance()
                .getSettingsApi().hasUpdate(token);

        call.enqueue(new Callback<Map<String,Integer>>() {
            @Override
            public void onResponse(Call<Map<String,Integer>> call, Response<Map<String,Integer>> response) {
                Log.d("Response>>",response.raw().toString());
              versioncode = BuildConfig.VERSION_CODE;

              if(response.body()!=null)
              {
                  if(versioncode<response.body().get("code")) {
                        showUpdatePopup();
                  }

              }

            }

            @Override
            public void onFailure(Call<Map<String,Integer>> call, Throwable t) {

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showUpdatePopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.update_popup_layout,
                null); // inflating popup layout

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        updatePopup = new PopupWindow(popUpView, width-10,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        updatePopup.setElevation(10f);

        ImageView ivCross = popUpView.findViewById(R.id.iv_close_update_popup);
        Button updateBtn = popUpView.findViewById(R.id.btn_update_app);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePopup.dismiss();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "com.virtualskill"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });



        // Creation of popup
        updatePopup.setAnimationStyle(android.R.style.Animation_Dialog);
        updatePopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

    }



    private void setNavHeaderdata() {
        tvUserName.setText(name);
        tvUserMail.setText(mail);

        if(userObj.getProfilePath()!=null)
        {
            Glide.with(this).
                    load("http://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/uploads/profile_photos/"
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
                    }).into(civprofileOnHeader);
        }

        if(userObj.getCoverPath()!= null)
        {
            Glide.with(this).load("https://virtualskill.in/storage/uploads/covers/"+
                    userObj.getCoverPath()).
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
                    }).into(ivCoverOnheader);
        }
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

    private void setUi() {
        navView = findViewById(R.id.drawer_main_navigation);
        toolbar = findViewById(R.id.tbHome);
        drawerButtion = findViewById(R.id.drawer_home_button);
        fabHodeNav = findViewById(R.id.fab_hide_nav_home);
        revPostView = findViewById(R.id.recViewHomePosts);
        refreshLayout = findViewById(R.id.refLayoutHomePosts);
        errorlayout = findViewById(R.id.errorlayout);
        erroeTitle = findViewById(R.id.errorTitle);
        ivGoChatScreen = findViewById(R.id.iv_go_chat_screen);
        errorMessage = findViewById(R.id.errormessage);
        searchButton = findViewById(R.id.iv_search_icon);
//
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.drawer_main_navigation);
        View hView =  navigationView.getHeaderView(0);

        //For Header Nav
        ivCoverOnheader = hView.findViewById(R.id.iv_user_cover_on_header);
        civprofileOnHeader = hView.findViewById(R.id.civ_user_profile_on_header);
        tvUserMail = hView.findViewById(R.id.tv_user_mail);
        tvUserName = hView.findViewById(R.id.tv_user_name);


        // post Utils
        linLayPickImage = findViewById(R.id.lin_lay_pick_image_in_home);
        linLayPickVideo = findViewById(R.id.lin_lay_pick_video_on_home);
        linLayPickYtLInk = findViewById(R.id.lin_lay_pick_ytLink_on_home);
        ivSelectedImage = findViewById(R.id.iv_selected_image_home_post);
        ivUnSelectImage = findViewById(R.id.iv_unSelect_image_home_post);
        etCaption = findViewById(R.id.et_caption_home_post);
        etYtLink = findViewById(R.id.et_yt_link_home_post);
        vvSelectedVideo = findViewById(R.id.vv_selected_video_home_post);
        vvSelectedYtVideo = findViewById(R.id.yt_selected_vv_home_post);
        tvCommitPost = findViewById(R.id.tv_commit_post_on_home);
        //>>>>>>

        mShimmerViewContainer = findViewById(R.id.sh_v_home_page);
        mShimmerViewContainer.startShimmerAnimation();
    }

    private void loadHomePosts(String token){
        errorlayout.setVisibility(View.INVISIBLE);
        Call<HomePostModal> call = RetrifitClient.getInstance()
                .getPostApi().getHomePosts(token);
        call.enqueue(new Callback<HomePostModal>() {
            @Override
            public void onResponse(Call<HomePostModal> call, Response<HomePostModal> response) {
                try{
                    if(response.body() == null){
                        Log.d("Error>>", response.errorBody().string());
                        refreshLayout.setRefreshing(false);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);


                    }else {
                        if(response.body().getStatus()==null){
                            postDataList = response.body().getPosts().getData();
                            revPostView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                            revPostView.setAdapter(new HomePostsAdapter(postDataList,HomePage.this,userObj.getId(),token));
                            refreshLayout.setRefreshing(false);
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }else {
                            // Token Is Expired Or Invalid
                                loginByApi(UnameValue,PasswordValue);
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }

                    }


                }catch (Exception e){
                    e.printStackTrace();
                    refreshLayout.setRefreshing(false);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<HomePostModal> call, Throwable t) {
                Toast.makeText(HomePage.this, "Error ", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }

    private void loginByApi(String email, String password) {
        Call<Map> call = RetrifitClient.getInstance()
                .getApi().loginUser(email,password,true);
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                try{
                    if (response.body()==null)
                    {
                        if(response.errorBody()==null){
                           // progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            //showAuthError();
                        }

                    }else {
                        if(response.body().get("error")==null)
                        {
                            String token = response.body().get("token").toString();
                            Log.d("HTML>>>>>  ", token);
                            //progressBar.setVisibility(View.INVISIBLE);
                            //Saving uName And Password For Further Uses
                            saveNewToken(token);
                            loadHomePosts(token);

                        }else {
                            //progressBar.setVisibility(View.INVISIBLE);
                           // showAuthError();
                        }
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
               // progressBar.setVisibility(View.INVISIBLE);
               // showAuthError();
            }
        });
    }

    private void saveNewToken(String token) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // Edit and commit

        editor.putString(TOKEN,token);
        editor.commit();
    }
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences fcmTokenpref = getSharedPreferences("prefs",MODE_PRIVATE);

        fcm_token = fcmTokenpref.getString("fcm_token","");
        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        Gson gson = new Gson();
        userObj = gson.fromJson(settings.getString("UserObj",""),UserObj.class);
        name = userObj.getName();
        mail = userObj.getEmail();
        token = settings.getString(TOKEN,DefaultTokenValue);

    }

    private void showError(String title ,String message)
    {

        if (errorlayout.getVisibility() == View.GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorMessage.setText(message);
        erroeTitle.setText(title);
    }

    // PostMethodWithAPI

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
                Toast.makeText(HomePage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomePage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomePage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomePage.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                loadHomePosts(token);
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

 class OnSwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    Context context;

    OnSwipeTouchListener(Context ctx, View mainView) {

        gestureDetector = new GestureDetector(ctx, new GestureListener());
        mainView.setOnTouchListener(this);
        context = ctx;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    void onSwipeRight() {
//        Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
        this.onSwipe.swipeRight();
    }

    void onSwipeLeft() {
        context.startActivity(new Intent(context,SkillPage.class));
        Activity activity = (Activity) context;
        activity.finish();
        activity.overridePendingTransition(0, 0);
        this.onSwipe.swipeLeft();
    }

    void onSwipeTop() {
//        Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
        this.onSwipe.swipeTop();
    }

    void onSwipeBottom() {
//        Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
        this.onSwipe.swipeBottom();
    }

    interface onSwipeListener {
        void swipeRight();

        void swipeTop();

        void swipeBottom();

        void swipeLeft();
    }

    onSwipeListener onSwipe;
}

