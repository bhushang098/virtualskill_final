/*
 * Copyright (C) 2019 Twilio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twilio.video.app.ui.room;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.twilio.audioswitch.selection.AudioDevice;
import com.twilio.audioswitch.selection.AudioDeviceSelector;
import com.twilio.video.AspectRatio;
import com.twilio.video.CameraCapturer;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalTrackPublicationOptions;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.ScreenCapturer;
import com.twilio.video.StatsListener;
import com.twilio.video.TrackPriority;
import com.twilio.video.TwilioException;
import com.twilio.video.VideoConstraints;
import com.twilio.video.VideoDimensions;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.BodyParamClassClass.MakeClassClass;
import com.twilio.video.app.CanvasAll.CollaborativeDrawingView;
import com.twilio.video.app.CanvasAll.MotionMessage;
import com.twilio.video.app.DetailedChatResponse.Datum;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;
import com.twilio.video.app.MainPages.OtherUserProfile;
import com.twilio.video.app.R;
import com.twilio.video.app.RetrifitClient;
import com.twilio.video.app.SingleUserResponse.Data;
import com.twilio.video.app.adapter.ChatItemAdapter;
import com.twilio.video.app.adapter.StatsListAdapter;
import com.twilio.video.app.base.BaseActivity;
import com.twilio.video.app.data.Preferences;
import com.twilio.video.app.data.api.AuthServiceError;
import com.twilio.video.app.data.api.TokenService;
import com.twilio.video.app.participant.ParticipantViewState;
import com.twilio.video.app.sdk.RoomManager;
import com.twilio.video.app.sdk.VideoTrackViewState;
import com.twilio.video.app.udf.ViewEffect;
import com.twilio.video.app.ui.room.RoomViewEffect.Connected;
import com.twilio.video.app.ui.room.RoomViewEffect.Disconnected;
import com.twilio.video.app.ui.room.RoomViewEffect.ShowConnectFailureDialog;
import com.twilio.video.app.ui.room.RoomViewEffect.ShowTokenErrorDialog;
import com.twilio.video.app.ui.room.RoomViewEvent.ActivateAudioDevice;
import com.twilio.video.app.ui.room.RoomViewEvent.CheckPermissions;
import com.twilio.video.app.ui.room.RoomViewEvent.Disconnect;
import com.twilio.video.app.ui.room.RoomViewEvent.RefreshViewState;
import com.twilio.video.app.ui.room.RoomViewEvent.ScreenTrackRemoved;
import com.twilio.video.app.ui.room.RoomViewEvent.SelectAudioDevice;
import com.twilio.video.app.ui.room.RoomViewEvent.ToggleLocalVideo;
import com.twilio.video.app.ui.room.RoomViewEvent.VideoTrackRemoved;
import com.twilio.video.app.ui.room.RoomViewModel.RoomViewModelFactory;
import com.twilio.video.app.ui.settings.SettingsActivity;
import com.twilio.video.app.util.CameraCapturerCompat;
import com.twilio.video.app.util.InputUtils;
import com.twilio.video.app.util.PermissionUtil;
import com.twilio.video.app.util.StatsScheduler;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.twilio.video.AspectRatio.ASPECT_RATIO_11_9;
import static com.twilio.video.AspectRatio.ASPECT_RATIO_16_9;
import static com.twilio.video.AspectRatio.ASPECT_RATIO_4_3;
import static com.twilio.video.Room.State.CONNECTED;
import static com.twilio.video.app.data.api.AuthServiceError.EXPIRED_PASSCODE_ERROR;
import static com.twilio.video.app.sdk.RoomManagerKt.CAMERA_TRACK_NAME;
import static com.twilio.video.app.sdk.RoomManagerKt.MICROPHONE_TRACK_NAME;
import static com.twilio.video.app.sdk.RoomManagerKt.SCREEN_TRACK_NAME;

public class RoomActivity extends BaseActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int MEDIA_PROJECTION_REQUEST_CODE = 101;
    private static final int STATS_DELAY = 1000; // milliseconds
    private static final String IS_AUDIO_MUTED = "IS_AUDIO_MUTED";
    private static final String IS_VIDEO_MUTED = "IS_VIDEO_MUTED";

    // This will be used instead of real local participant sid,
    // because that information is unknown until room connection is fully established
    private static final String LOCAL_PARTICIPANT_STUB_SID = "";
    private static  String PREFS_NAME = "login_preferences";

    private AspectRatio[] aspectRatios =
            new AspectRatio[] {ASPECT_RATIO_4_3, ASPECT_RATIO_16_9, ASPECT_RATIO_11_9};

    private VideoDimensions[] videoDimensions =
            new VideoDimensions[] {
                    VideoDimensions.CIF_VIDEO_DIMENSIONS,
                    VideoDimensions.VGA_VIDEO_DIMENSIONS,
                    VideoDimensions.WVGA_VIDEO_DIMENSIONS,
                    VideoDimensions.HD_540P_VIDEO_DIMENSIONS,
                    VideoDimensions.HD_720P_VIDEO_DIMENSIONS,
                    VideoDimensions.HD_960P_VIDEO_DIMENSIONS,
                    VideoDimensions.HD_S1080P_VIDEO_DIMENSIONS,
                    VideoDimensions.HD_1080P_VIDEO_DIMENSIONS
            };



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.connect)
    Button connect;

    @BindView(R.id.disconnect)
    ImageButton disconnectButton;


    @BindView(R.id.primary_video)
    ParticipantPrimaryView primaryVideoView;

    @BindView(R.id.remote_video_thumbnails)
    RecyclerView thumbnailRecyclerView;

    @BindView(R.id.local_video_image_button)
    ImageButton localVideoImageButton;

    @BindView(R.id.local_audio_image_button)
    ImageButton localAudioImageButton;

    @BindView(R.id.video_container)
    FrameLayout frameLayout;

    @BindView(R.id.join_room_layout)
    LinearLayout joinRoomLayout;

    @BindView(R.id.room_edit_text)
    ClearableEditText roomEditText;

    @BindView(R.id.join_status_layout)
    LinearLayout joinStatusLayout;

    @BindView(R.id.join_status)
    TextView joinStatusTextView;

    @BindView(R.id.join_room_name)
    TextView joinRoomNameTextView;

    @BindView(R.id.recording_notice)
    TextView recordingNoticeTextView;

    @BindView(R.id.stats_recycler_view)
    RecyclerView statsRecyclerView;

    @BindView(R.id.stats_disabled)
    LinearLayout statsDisabledLayout;

    @BindView(R.id.stats_disabled_title)
    TextView statsDisabledTitleTextView;

    @BindView(R.id.stats_disabled_description)
    TextView statsDisabledDescTextView;

    private MenuItem switchCameraMenuItem;
    private MenuItem pauseVideoMenuItem;
    private MenuItem pauseAudioMenuItem;
    private MenuItem screenCaptureMenuItem;
    private MenuItem settingsMenuItem;
    private MenuItem deviceMenuItem;
    ImageButton drawbtn,chatButton;
    ImageButton chooseColor;
    ImageButton earaser;
    ImageButton previewColor;
    ImageButton pencilButton;
    ImageButton crossDrawBtn;
    String token,userId,classId;
    int localColor =  Color.BLACK;
    float STROKE_WIDTH = 5f;

    private int savedAudioMode = AudioManager.MODE_INVALID;
    private int savedVolumeControlStream;
    private boolean savedIsMicrophoneMute = false;
    private boolean savedIsSpeakerPhoneOn = false;

    private String displayName;
    String roomName;
    private LocalParticipant localParticipant;
    private String localParticipantSid = LOCAL_PARTICIPANT_STUB_SID;
    private Room room;
    private VideoConstraints videoConstraints;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack cameraVideoTrack;
    private boolean restoreLocalVideoCameraTrack = false;
    private LocalVideoTrack screenVideoTrack;
    private CameraCapturerCompat cameraCapturer;
    private ScreenCapturer screenCapturer;

    //ForChats
    List<Datum> detailedChatList = new ArrayList<>();
    ChatItemAdapter chatItemAdapter;
    RecyclerView chatsRecyclerView;


    private final ScreenCapturer.Listener screenCapturerListener =
            new ScreenCapturer.Listener() {
                @Override
                public void onScreenCaptureError(@NonNull String errorDescription) {
                    Timber.e("Screen capturer error: %s", errorDescription);
                    stopScreenCapture();
                    Snackbar.make(
                            primaryVideoView,
                            R.string.screen_capture_error,
                            Snackbar.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onFirstFrameAvailable() {
                    Timber.d("First frame from screen capturer available");
                }
            };

    private StatsScheduler statsScheduler;
    private StatsListAdapter statsListAdapter;
    private Map<String, String> localVideoTrackNames = new HashMap<>();

    @Inject TokenService tokenService;

    @Inject SharedPreferences sharedPreferences;

    @Inject RoomManager roomManager;

    @Inject AudioDeviceSelector audioDeviceSelector;

    /** Coordinates participant thumbs and primary participant rendering. */
    private PrimaryParticipantController primaryParticipantController;
    private static final String DATA_TRACK_MESSAGE_THREAD_NAME = "DataTrackMessages";

    private Boolean isAudioMuted = false;
    private Boolean isVideoMuted = false;
    private ParticipantAdapter participantAdapter;
    private CollaborativeDrawingView collaborativeDrawingView;
    LinearLayout canVasMenu;

    public static void startActivity(Context context, Uri appLink) {
        Intent intent = new Intent(context, RoomActivity.class);
        intent.setData(appLink);
        context.startActivity(intent);
    }

    private RoomViewModel roomViewModel;

    private LocalDataTrack localDataTrack;
    private boolean disconnectedFromOnDestroy;

    // Dedicated thread and handler for messages received from a RemoteDataTrack
    private final HandlerThread dataTrackMessageThread =
            new HandlerThread(DATA_TRACK_MESSAGE_THREAD_NAME);
    private Handler dataTrackMessageThreadHandler;

    // Map used to map remote data tracks to remote participants
    private final Map<RemoteDataTrack, RemoteParticipant> dataTrackRemoteParticipantMap =
            new HashMap<>();

    // Drawing view listener that sends the events on the local data track
    private final CollaborativeDrawingView.Listener drawingViewListener =
            new CollaborativeDrawingView.Listener() {
                @Override
                public void onTouchEvent(int actionEvent, float x, float y) {
                    Log.d(">>ont Touch >>>>", String.format("onTouchEvent: actionEvent=%d, x=%f, y=%f , color=%d",
                            actionEvent, x, y,localColor));
                    boolean actionDown = (actionEvent == MotionEvent.ACTION_DOWN);
                    float normalizedX = x / (float) collaborativeDrawingView.getWidth();
                    float normalizedY = y / (float) collaborativeDrawingView.getHeight();
                    MotionMessage motionMessage = new MotionMessage(actionDown,
                            normalizedX,
                            normalizedY,localColor,STROKE_WIDTH);

                    if (localDataTrack != null) {
                        localDataTrack.send(motionMessage.toJsonString());
                    } else {
                        Log.e("TAG", "Ignoring touch event because data track is release");
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localDataTrack = LocalDataTrack.create(this);
        loadprefs();



        // Start the thread where data messages are received
        dataTrackMessageThread.start();
        dataTrackMessageThreadHandler = new Handler(dataTrackMessageThread.getLooper());

        RoomViewModelFactory factory =
                new RoomViewModelFactory(
                        roomManager, audioDeviceSelector, new PermissionUtil(this));
        roomViewModel = new ViewModelProvider(this, factory).get(RoomViewModel.class);

        if (savedInstanceState != null) {
            isAudioMuted = savedInstanceState.getBoolean(IS_AUDIO_MUTED);
            isVideoMuted = savedInstanceState.getBoolean(IS_VIDEO_MUTED);
        }
        // get Intent Data
        displayName = getIntent().getStringExtra("user_name");
        roomName = getIntent().getStringExtra("room_name");
        token = getIntent().getStringExtra("token");
        classId = getIntent().getStringExtra("classId");

        // So calls can be answered when screen is locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Grab views
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        collaborativeDrawingView = findViewById(R.id.drawing_view_on_video);
        chooseColor = findViewById(R.id.btn_choose_color);
        canVasMenu = findViewById(R.id.lin_lay_canvas_menu);
        drawbtn = findViewById(R.id.local_draw_image_button);
        chatButton = findViewById(R.id.local_chat_button);
        earaser = findViewById(R.id.btn_ereaser);
        previewColor = findViewById(R.id.btn_preview_color);
        pencilButton = findViewById(R.id.btn_pencil);
        crossDrawBtn = findViewById(R.id.btn_cross_raw);


        if(room!=null){
            // drawbtn.setVisibility(View.VISIBLE);

        }

        connectButtonClick();

        drawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collaborativeDrawingView.getVisibility()==View.GONE){
                    collaborativeDrawingView.setVisibility(View.VISIBLE);
                    canVasMenu.setVisibility(View.VISIBLE);
                    if(joinRoomNameTextView.getText()==null)
                        collaborativeDrawingView.setEnabled(false);
                    else
                        collaborativeDrawingView.setEnabled(true);

                    collaborativeDrawingView.setListener(drawingViewListener);
                    drawbtn.setImageResource(R.drawable.ic_baseline_layers_clear_24);
                    if(room!=null){
                        for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                            addRemoteParticipant(remoteParticipant);
                        }
                    }

                }else {
                    collaborativeDrawingView.setVisibility(View.GONE);
                    collaborativeDrawingView.clear();
                    drawbtn.setImageResource(R.drawable.ic_baseline_brush_24);
                    canVasMenu.setVisibility(View.GONE);
                }

            }
        });
        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(RoomActivity.this, 100, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        localColor = color;
                        STROKE_WIDTH = 5f;
                        previewColor.setBackgroundColor(color);
                        collaborativeDrawingView.setSTROKE_WIDTH(STROKE_WIDTH);
                        collaborativeDrawingView.setColor(localColor);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                        localColor = Color.BLACK;
                        STROKE_WIDTH = 5f;
                    }
                });

                dialog.show();
            }
        });

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(RoomActivity.this, 100, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        localColor = color;
                        STROKE_WIDTH = 5f;
                        previewColor.setBackgroundColor(color);
                        collaborativeDrawingView.setSTROKE_WIDTH(STROKE_WIDTH);
                        collaborativeDrawingView.setColor(localColor);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                        localColor = Color.BLACK;
                        STROKE_WIDTH = 5f;
                    }
                });

                dialog.show();
            }
        });

        earaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localColor = Color.WHITE;
                previewColor.setBackgroundColor(localColor);
                STROKE_WIDTH = 20f;
                collaborativeDrawingView.setSTROKE_WIDTH(STROKE_WIDTH);
                collaborativeDrawingView.setColor(localColor);
            }
        });

        crossDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collaborativeDrawingView.setVisibility(View.GONE);
                collaborativeDrawingView.clear();
                drawbtn.setImageResource(R.drawable.ic_baseline_brush_24);
                canVasMenu.setVisibility(View.GONE);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat();
            }
        });



        setupThumbnailRecyclerView();

        // Setup toolbar
        setSupportActionBar(toolbar);

        // Cache volume control stream
        savedVolumeControlStream = getVolumeControlStream();

        // setup participant controller
        primaryParticipantController = new PrimaryParticipantController(primaryVideoView);

        // Setup Activity
        statsScheduler = new StatsScheduler();
        obtainVideoConstraints();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startChat() {
        inflateDetailedMessage(detailedChatList);
        Call<DetailedChatResponse> call = RetrifitClient.getInstance()
                .getChatApi().getDetailedChatList(token,classId,"class");

        call.enqueue(new Callback<DetailedChatResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<DetailedChatResponse> call, Response<DetailedChatResponse> response) {
                Log.d("Chat Response>>", response.raw().toString());
                if(response.body()!=null)
                {

                    detailedChatList = response.body().getMessages().getData();
                    Collections.reverse(detailedChatList);
                    chatItemAdapter.setMessageList(detailedChatList);
                    chatsRecyclerView.smoothScrollToPosition(response.body().getMessages().getData().size());
                    chatItemAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<DetailedChatResponse> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inflateDetailedMessage(List<Datum> data) {
        // ivSelectedImage.setImageURI(Uri.fromFile(coverImage));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popUpView = inflater.inflate(R.layout.chat_details_layout,
                null); // inflating popup layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        PopupWindow mopoup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mopoup.setElevation(5f);
        EditText etMessage = popUpView.findViewById(R.id.et_message);
        Collections.reverse(data);

        ProgressBar progressBar = popUpView.findViewById(R.id.pb_chat_details);
        chatsRecyclerView = popUpView.findViewById(R.id.rec_view_detailed_chat_with_user);
        ImageView ivSend = popUpView.findViewById(R.id.iv_send_chat_message);
        ImageView ivback = popUpView.findViewById(R.id.iv_toggle_chat_on_popup);
        mopoup.setAnimationStyle(android.R.style.Animation_Dialog);
        mopoup.showAtLocation(popUpView, Gravity.TOP, 0, 0);

        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(RoomActivity.this));
        chatsRecyclerView.setAdapter(chatItemAdapter);

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
                if(etMessage.getText().toString().length()>0)
                {
                    hideKeybaord(v);
                    chatsRecyclerView.smoothScrollToPosition(detailedChatList.size());
                    sendmessage(etMessage.getText().toString(),classId,mopoup);
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

    private void sendmessage(String message, String classId,PopupWindow popuseWindow) {

        Call <Map> call = RetrifitClient.getInstance().getChatApi()
                .sendChatMess(token,"class",message,classId,"1");

        Datum messageObj = new Datum();
        messageObj.setContent(message);
        messageObj.setUserId(userId);
        messageObj.setBelongsTo(classId);
        messageObj.setCreatedAt("Now Now");
        int index  = detailedChatList.size();
        detailedChatList.add(index,messageObj);
        chatItemAdapter.notifyItemInserted(index);
        chatsRecyclerView.smoothScrollToPosition(index);

        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.d("Responnse>>Chat ",response.raw().toString());
                if(response.body()!=null)
                {

                }else {
                    detailedChatList.remove(index);
                    chatItemAdapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.d("Failuer>>",t.toString());

            }
        });
    }

    private void loadprefs() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = settings.getString("UserObj", "");
        Data obj = gson.fromJson(json, Data.class);
        userId = obj.getId().toString();
        token = settings.getString("token","");
        chatItemAdapter = new ChatItemAdapter(detailedChatList,getApplicationContext(),String.valueOf(userId));

    }


    private void setupThumbnailRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        thumbnailRecyclerView.setLayoutManager(layoutManager);
        participantAdapter = new ParticipantAdapter();
        participantAdapter
                .getViewHolderEvents()
                .observe(this, (viewEvent) -> roomViewModel.processInput(viewEvent));
        thumbnailRecyclerView.setAdapter(participantAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkIntentURI();

        restoreCameraTrack();

        roomViewModel.processInput(RefreshViewState.INSTANCE);
        roomViewModel.processInput(CheckPermissions.INSTANCE);

        publishLocalTracks();

        updateStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayName = sharedPreferences.getString(Preferences.DISPLAY_NAME, null);
        setTitle(displayName);
    }

    private boolean checkIntentURI() {
        boolean isAppLinkProvided = false;
        Uri uri = getIntent().getData();
        String roomName = new UriRoomParser(new UriWrapper(uri)).parseRoom();
        if (roomName != null) {
            roomEditText.setText(roomName);
            isAppLinkProvided = true;
        }
        return isAppLinkProvided;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(IS_AUDIO_MUTED, isAudioMuted);
        outState.putBoolean(IS_VIDEO_MUTED, isVideoMuted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        // Teardown tracks
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (cameraVideoTrack != null) {
            cameraVideoTrack.release();
            cameraVideoTrack = null;
        }
        if (screenVideoTrack != null) {
            screenVideoTrack.release();
            screenVideoTrack = null;
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean recordAudioPermissionGranted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean writeExternalStoragePermissionGranted =
                    grantResults[2] == PackageManager.PERMISSION_GRANTED;
            boolean permissionsGranted =
                    recordAudioPermissionGranted
                            && cameraPermissionGranted
                            && writeExternalStoragePermissionGranted;

            if (permissionsGranted) {
                roomViewModel.processInput(CheckPermissions.INSTANCE);
                setupLocalMedia();
            } else {
                Snackbar.make(primaryVideoView, R.string.permissions_required, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onStop() {
        removeCameraTrack();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.room_menu, menu);
        settingsMenuItem = menu.findItem(R.id.settings_menu_item);
        // Grab menu items for updating later
        switchCameraMenuItem = menu.findItem(R.id.switch_camera_menu_item);
        pauseVideoMenuItem = menu.findItem(R.id.pause_video_menu_item);
        pauseAudioMenuItem = menu.findItem(R.id.pause_audio_menu_item);
        screenCaptureMenuItem = menu.findItem(R.id.share_screen_menu_item);
        deviceMenuItem = menu.findItem(R.id.device_menu_item);

        requestPermissions();
        roomViewModel.getViewState().observe(this, this::bindRoomViewState);
        roomViewModel.getViewEffects().observe(this, this::bindRoomViewEffects);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_camera_menu_item:
                if (cameraCapturer != null) {
                    cameraCapturer.switchCamera();
                }
                return true;
            case R.id.share_screen_menu_item:
                String shareScreen = getString(R.string.share_screen);

                if (item.getTitle().equals(shareScreen)) {
                    if (screenCapturer == null) {
                        requestScreenCapturePermission();
                    } else {
                        startScreenCapture();
                    }
                } else {
                    stopScreenCapture();
                }
                return true;
            case R.id.device_menu_item:
                displayAudioDeviceList();
                return true;
            case R.id.pause_audio_menu_item:
                toggleLocalAudioTrackState();
                return true;
            case R.id.pause_video_menu_item:
                toggleLocalVideoTrackState();
                return true;
            case R.id.settings_menu_item:
                removeCameraTrack();

                Intent intent = new Intent(RoomActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Snackbar.make(
                        primaryVideoView,
                        R.string.screen_capture_permission_not_granted,
                        Snackbar.LENGTH_LONG)
                        .show();
                return;
            }
            screenCapturer = new ScreenCapturer(this, resultCode, data, screenCapturerListener);
            startScreenCapture();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(collaborativeDrawingView.getVisibility()==View.VISIBLE){
            collaborativeDrawingView.setVisibility(View.INVISIBLE);
        }else {
            roomViewModel.processInput(Disconnect.INSTANCE);
        }

    }

    @OnTextChanged(
            value = R.id.room_edit_text,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(CharSequence text) {
        connect.setEnabled(!TextUtils.isEmpty(text));
    }

    @OnClick(R.id.connect)
    void connectButtonClick() {
        InputUtils.hideKeyboard(this);
        if (!didAcceptPermissions()) {
            Snackbar.make(primaryVideoView, R.string.permissions_required, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        connect.setEnabled(false);
        // obtain room name
        RoomViewEvent.Connect viewEvent = new RoomViewEvent.Connect(displayName, roomName);
        roomViewModel.processInput(viewEvent);

    }

    @OnClick(R.id.disconnect)
    void disconnectButtonClick() {
        callEndClassApi();
        roomViewModel.processInput(Disconnect.INSTANCE);
        stopScreenCapture();
        finish();
    }

    private void callEndClassApi() {
        Call<MakeClassResponse> call = RetrifitClient.getInstance()
                .getClassesApi().endClass(token);

        call.enqueue(new Callback<MakeClassResponse>() {
            @Override
            public void onResponse(Call<MakeClassResponse> call, Response<MakeClassResponse> response) {
                Log.d("End Class>> Response>>", response.raw().toString());

                if(response.body()!=null)
                {
                    if (response.body().getStatus())
                    {
                        Toast.makeText(RoomActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeClassResponse> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.local_audio_image_button)
    void toggleLocalAudio() {
        int icon;
        if (localAudioTrack == null) {
            isAudioMuted = false;
            localAudioTrack = LocalAudioTrack.create(this, true, MICROPHONE_TRACK_NAME);
            if (localParticipant != null && localAudioTrack != null) {
                localParticipant.publishTrack(localAudioTrack);
            }
            icon = R.drawable.ic_mic_white_24px;
            pauseAudioMenuItem.setVisible(true);
            pauseAudioMenuItem.setTitle(
                    localAudioTrack.isEnabled() ? R.string.pause_audio : R.string.resume_audio);
        } else {
            isAudioMuted = true;
            if (localParticipant != null) {
                localParticipant.unpublishTrack(localAudioTrack);
            }
            localAudioTrack.release();
            localAudioTrack = null;
            icon = R.drawable.ic_mic_off_gray_24px;
            pauseAudioMenuItem.setVisible(false);
        }
        localAudioImageButton.setImageResource(icon);
    }

    @OnClick(R.id.local_video_image_button)
    void toggleLocalVideo() {

        if (localParticipant != null)
            roomViewModel.processInput(new ToggleLocalVideo(localParticipant.getSid()));

        if (cameraVideoTrack == null) {
            isVideoMuted = false;

            // add local camera track
            cameraVideoTrack =
                    LocalVideoTrack.create(
                            this,
                            true,
                            cameraCapturer.getVideoCapturer(),
                            videoConstraints,
                            CAMERA_TRACK_NAME);
            if (localParticipant != null && cameraVideoTrack != null) {
                publishVideoTrack(cameraVideoTrack, TrackPriority.LOW);

                // enable video settings
                switchCameraMenuItem.setVisible(cameraVideoTrack.isEnabled());
                pauseVideoMenuItem.setTitle(
                        cameraVideoTrack.isEnabled()
                                ? R.string.pause_video
                                : R.string.resume_video);
                pauseVideoMenuItem.setVisible(true);
            }
        } else {
            isVideoMuted = true;
            // remove local camera track
            cameraVideoTrack.removeRenderer(primaryVideoView);

            if (localParticipant != null) {
                localParticipant.unpublishTrack(cameraVideoTrack);
            }
            cameraVideoTrack.release();
            cameraVideoTrack = null;

            // disable video settings
            switchCameraMenuItem.setVisible(false);
            pauseVideoMenuItem.setVisible(false);
        }

        // update toggle button icon
        localVideoImageButton.setImageResource(
                cameraVideoTrack != null
                        ? R.drawable.ic_videocam_white_24px
                        : R.drawable.ic_videocam_off_gray_24px);

        roomViewModel.processInput(RefreshViewState.INSTANCE);
    }

    private void publishVideoTrack(LocalVideoTrack videoTrack, TrackPriority trackPriority) {
        LocalTrackPublicationOptions localTrackPublicationOptions =
                new LocalTrackPublicationOptions(trackPriority);
        localParticipant.publishTrack(videoTrack, localTrackPublicationOptions);
    }

    private void obtainVideoConstraints() {
        Timber.d("Collecting video constraints...");

        VideoConstraints.Builder builder = new VideoConstraints.Builder();

        // setup aspect ratio
        String aspectRatio = sharedPreferences.getString(Preferences.ASPECT_RATIO, "0");
        if (aspectRatio != null) {
            int aspectRatioIndex = Integer.parseInt(aspectRatio);
            builder.aspectRatio(aspectRatios[aspectRatioIndex]);
            Timber.d(
                    "Aspect ratio : %s",
                    getResources()
                            .getStringArray(R.array.settings_screen_aspect_ratio_array)[
                            aspectRatioIndex]);
        }

        // setup video dimensions
        int minVideoDim = sharedPreferences.getInt(Preferences.MIN_VIDEO_DIMENSIONS, 0);
        int maxVideoDim = sharedPreferences.getInt(Preferences.MAX_VIDEO_DIMENSIONS, 1);

        if (maxVideoDim != -1 && minVideoDim != -1) {
            builder.minVideoDimensions(videoDimensions[minVideoDim]);
            builder.maxVideoDimensions(videoDimensions[maxVideoDim]);
        }

        Timber.d(
                "Video dimensions: %s - %s",
                getResources()
                        .getStringArray(R.array.settings_screen_video_dimensions_array)[
                        minVideoDim],
                getResources()
                        .getStringArray(R.array.settings_screen_video_dimensions_array)[
                        maxVideoDim]);

        // setup fps
        int minFps = sharedPreferences.getInt(Preferences.MIN_FPS, 0);
        int maxFps = sharedPreferences.getInt(Preferences.MAX_FPS, 24);

        if (maxFps != -1 && minFps != -1) {
            builder.minFps(minFps);
            builder.maxFps(maxFps);
        }

        Timber.d("Frames per second: %d - %d", minFps, maxFps);

        videoConstraints = builder.build();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permissionsGranted()) {
                requestPermissions(
                        new String[] {
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSIONS_REQUEST_CODE);
            } else {
                setupLocalMedia();
            }
        } else {
            setupLocalMedia();
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

    /** Initialize local media and provide stub participant for primary view. */
    private void setupLocalMedia() {
        if (localAudioTrack == null && !isAudioMuted) {
            localAudioTrack = LocalAudioTrack.create(this, true, MICROPHONE_TRACK_NAME);
            if (room != null && localParticipant != null)
                localParticipant.publishTrack(localAudioTrack);
        }
        if (cameraVideoTrack == null && !isVideoMuted) {
            setupLocalVideoTrack();
            if (room != null && localParticipant != null)
                publishVideoTrack(cameraVideoTrack, TrackPriority.LOW);
        }
        roomViewModel.processInput(RefreshViewState.INSTANCE);
    }

    /** Create local video track */
    private void setupLocalVideoTrack() {

        // initialize capturer only once if needed
        if (cameraCapturer == null) {
            cameraCapturer =
                    new CameraCapturerCompat(this, CameraCapturer.CameraSource.FRONT_CAMERA);
        }

        cameraVideoTrack =
                LocalVideoTrack.create(
                        this,
                        true,
                        cameraCapturer.getVideoCapturer(),
                        videoConstraints,
                        CAMERA_TRACK_NAME);
        if (cameraVideoTrack != null) {
            localVideoTrackNames.put(
                    cameraVideoTrack.getName(), getString(R.string.camera_video_track));
        } else {
            Snackbar.make(
                    primaryVideoView,
                    R.string.failed_to_add_camera_video_track,
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Render local video track.
     *
     * <p>NOTE: Stub participant is created in controller. Make sure to remove it when connected to
     * room.
     */
    private void renderLocalParticipantStub() {

        VideoTrackViewState cameraTrackViewState =
                cameraVideoTrack != null ? new VideoTrackViewState(cameraVideoTrack, false) : null;
        primaryParticipantController.renderAsPrimary(
                localParticipantSid,
                getString(R.string.you),
                null,
                cameraTrackViewState,
                localAudioTrack == null,
                cameraCapturer != null
                        && cameraCapturer.getCameraSource()
                        == CameraCapturer.CameraSource.FRONT_CAMERA);

        primaryVideoView.showIdentityBadge(false);
    }

    private void updateLayout(RoomViewState roomViewState) {
        int disconnectButtonState = View.GONE;
        int joinRoomLayoutState = View.VISIBLE;
        int joinStatusLayoutState = View.GONE;

        boolean settingsMenuItemState = true;
        boolean screenCaptureMenuItemState = false;

        Editable roomEditable = roomEditText.getText();
        boolean isRoomTextNotEmpty = roomEditable != null && !roomEditable.toString().isEmpty();
        boolean connectButtonEnabled = isRoomTextNotEmpty;

        String roomName = displayName;
        String toolbarTitle = displayName;
        String joinStatus = "";
        int recordingWarningVisibility = View.GONE;

        if (roomViewState.isConnectingLayoutVisible()) {
            disconnectButtonState = View.VISIBLE;
            joinRoomLayoutState = View.GONE;
            joinStatusLayoutState = View.VISIBLE;
            recordingWarningVisibility = View.VISIBLE;
            settingsMenuItemState = false;

            connectButtonEnabled = false;

            if (roomEditable != null) {
                roomName = roomEditable.toString();
            }
            joinStatus = "Joining...";
        }
        if (roomViewState.isConnectedLayoutVisible()) {
            disconnectButtonState = View.VISIBLE;
            joinRoomLayoutState = View.GONE;
            joinStatusLayoutState = View.GONE;
            settingsMenuItemState = false;
            screenCaptureMenuItemState = true;

            connectButtonEnabled = false;

            roomName = roomViewState.getTitle();
            toolbarTitle = roomName;
            joinStatus = "";
        }
        if (roomViewState.isLobbyLayoutVisible()) {
            connectButtonEnabled = isRoomTextNotEmpty;
            screenCaptureMenuItemState = false;
        }

        boolean isMicEnabled = roomViewState.isMicEnabled();
        boolean isCameraEnabled = roomViewState.isCameraEnabled();
        boolean isLocalMediaEnabled = isMicEnabled && isCameraEnabled;
        localAudioImageButton.setEnabled(isLocalMediaEnabled);
        localVideoImageButton.setEnabled(isLocalMediaEnabled);
        int micDrawable =
                isAudioMuted || !isLocalMediaEnabled
                        ? R.drawable.ic_mic_off_gray_24px
                        : R.drawable.ic_mic_white_24px;
        int videoDrawable =
                isVideoMuted || !isLocalMediaEnabled
                        ? R.drawable.ic_videocam_off_gray_24px
                        : R.drawable.ic_videocam_white_24px;
        localAudioImageButton.setImageResource(micDrawable);
        localVideoImageButton.setImageResource(videoDrawable);
        statsListAdapter = new StatsListAdapter(this);
        statsRecyclerView.setAdapter(statsListAdapter);
        statsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        disconnectButton.setVisibility(disconnectButtonState);
        joinRoomLayout.setVisibility(joinRoomLayoutState);
        joinStatusLayout.setVisibility(joinStatusLayoutState);
        connect.setEnabled(connectButtonEnabled);


        setTitle(toolbarTitle);

        joinStatusTextView.setText(joinStatus);
        joinRoomNameTextView.setText(roomName);
        recordingNoticeTextView.setVisibility(recordingWarningVisibility);

        // TODO: Remove when we use a Service to obtainTokenAndConnect to a room
        if (settingsMenuItem != null) {
            settingsMenuItem.setVisible(settingsMenuItemState);
        }

        if (screenCaptureMenuItem != null
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screenCaptureMenuItem.setVisible(screenCaptureMenuItemState);
        }
    }

    private void setTitle(String toolbarTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(toolbarTitle);
        }
    }

    private void setVolumeControl(boolean setVolumeControl) {
        if (setVolumeControl) {
            /*
             * Enable changing the volume using the up/down keys during a conversation
             */
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        } else {
            setVolumeControlStream(savedVolumeControlStream);
        }
    }

    @TargetApi(21)
    private void requestScreenCapturePermission() {
        Timber.d("Requesting permission to capture screen");
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // This initiates a prompt dialog for the user to confirm screen projection.
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(), MEDIA_PROJECTION_REQUEST_CODE);
    }

    private void startScreenCapture() {
        screenVideoTrack = LocalVideoTrack.create(this, true, screenCapturer, SCREEN_TRACK_NAME);

        if (screenVideoTrack != null) {
            screenCaptureMenuItem.setIcon(R.drawable.ic_stop_screen_share_white_24dp);
            screenCaptureMenuItem.setTitle(R.string.stop_screen_share);
            localVideoTrackNames.put(
                    screenVideoTrack.getName(), getString(R.string.screen_video_track));

            if (localParticipant != null) {
                publishVideoTrack(screenVideoTrack, TrackPriority.HIGH);
            }
        } else {
            Snackbar.make(
                    primaryVideoView,
                    R.string.failed_to_add_screen_video_track,
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    private void stopScreenCapture() {
        if (screenVideoTrack != null) {

            roomViewModel.processInput(new ScreenTrackRemoved(localParticipant.getSid()));

            if (localParticipant != null) {
                localParticipant.unpublishTrack(screenVideoTrack);
            }
            screenVideoTrack.release();
            localVideoTrackNames.remove(screenVideoTrack.getName());
            screenVideoTrack = null;
            screenCaptureMenuItem.setIcon(R.drawable.ic_screen_share_white_24dp);
            screenCaptureMenuItem.setTitle(R.string.share_screen);
        }
    }

    private void toggleLocalAudioTrackState() {
        if (localAudioTrack != null) {
            boolean enable = !localAudioTrack.isEnabled();
            localAudioTrack.enable(enable);
            pauseAudioMenuItem.setTitle(
                    localAudioTrack.isEnabled() ? R.string.pause_audio : R.string.resume_audio);
        }
    }

    private void toggleLocalVideoTrackState() {
        if (cameraVideoTrack != null) {
            boolean enable = !cameraVideoTrack.isEnabled();
            cameraVideoTrack.enable(enable);
            pauseVideoMenuItem.setTitle(
                    cameraVideoTrack.isEnabled() ? R.string.pause_video : R.string.resume_video);
        }
    }

    /**
     * Remove the video track and mark the track to be restored when going to the settings screen or
     * going to the background
     */
    private void removeCameraTrack() {
        if (cameraVideoTrack != null) {
            if (localParticipant != null) {
                roomViewModel.processInput(new VideoTrackRemoved(localParticipant.getSid()));
                localParticipant.unpublishTrack(cameraVideoTrack);
            }
            cameraVideoTrack.release();
            restoreLocalVideoCameraTrack = true;
            cameraVideoTrack = null;
        }
    }

    /** Try to restore camera video track after going to the settings screen or background */
    private void restoreCameraTrack() {
        if (restoreLocalVideoCameraTrack) {
            obtainVideoConstraints();
            setupLocalVideoTrack();
            restoreLocalVideoCameraTrack = false;
        }
    }

    private void updateStatsUI(boolean enabled) {
        if (enabled) {
            if (room != null && room.getRemoteParticipants().size() > 0) {
                // show stats
                statsRecyclerView.setVisibility(View.VISIBLE);
                statsDisabledLayout.setVisibility(View.GONE);
            } else if (room != null) {
                // disable stats when there is no room
                statsDisabledTitleTextView.setText(getString(R.string.stats_unavailable));
                statsDisabledDescTextView.setText(
                        getString(R.string.stats_description_media_not_shared));
                statsRecyclerView.setVisibility(View.GONE);
                statsDisabledLayout.setVisibility(View.VISIBLE);
            } else {
                // disable stats if there is room but no participants (no media)
                statsDisabledTitleTextView.setText(getString(R.string.stats_unavailable));
                statsDisabledDescTextView.setText(getString(R.string.stats_description_join_room));
                statsRecyclerView.setVisibility(View.GONE);
                statsDisabledLayout.setVisibility(View.VISIBLE);
            }
        } else {
            statsDisabledTitleTextView.setText(getString(R.string.stats_gathering_disabled));
            statsDisabledDescTextView.setText(getString(R.string.stats_enable_in_settings));
            statsRecyclerView.setVisibility(View.GONE);
            statsDisabledLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateStats() {
        if (statsScheduler.isRunning()) {
            statsScheduler.cancelStatsGathering();
        }
        boolean enableStats = sharedPreferences.getBoolean(Preferences.ENABLE_STATS, false);
        if (enableStats && (room != null) && (room.getState() == CONNECTED)) {
            statsScheduler.scheduleStatsGathering(room, statsListener(), STATS_DELAY);
        }
        updateStatsUI(enableStats);
    }

    private StatsListener statsListener() {
        return statsReports -> {
            // Running on StatsScheduler thread
            if (room != null) {
                statsListAdapter.updateStatsData(
                        statsReports, room.getRemoteParticipants(), localVideoTrackNames);
            }
        };
    }

    private void initializeRoom() {
        if (room != null) {

            setupLocalParticipant(room);

            publishLocalTracks();

            updateStats();
        }
    }

    private void setupLocalParticipant(Room room) {
        localParticipant = room.getLocalParticipant();
        if (localParticipant != null) {
            localParticipantSid = localParticipant.getSid();
        }
    }

    private void publishLocalTracks() {
        if (localParticipant != null) {
            if (cameraVideoTrack != null) {
                Timber.d("Camera track: %s", cameraVideoTrack);
                publishVideoTrack(cameraVideoTrack, TrackPriority.LOW);
            }

            if (localAudioTrack != null) {
                localParticipant.publishTrack(localAudioTrack);
            }
            if(localDataTrack!=null){
                LocalParticipant localParticipant = room.getLocalParticipant();
                localParticipant.publishTrack(localDataTrack);
            }
        }
    }

    private void toggleAudioDevice(boolean enableAudioDevice) {
        setVolumeControl(enableAudioDevice);
        RoomViewEvent viewEvent =
                enableAudioDevice
                        ? ActivateAudioDevice.INSTANCE
                        : RoomViewEvent.DeactivateAudioDevice.INSTANCE;
        roomViewModel.processInput(viewEvent);
    }

    private void bindRoomViewState(RoomViewState roomViewState) {
        Timber.d("RoomViewState: %s", roomViewState);
        deviceMenuItem.setVisible(!roomViewState.getAvailableAudioDevices().isEmpty());
        renderPrimaryView(roomViewState.getPrimaryParticipant());
        renderThumbnails(roomViewState);
        updateLayout(roomViewState);
    }

    private void bindRoomViewEffects(ViewEffect<RoomViewEffect> roomViewEffectWrapper) {
        RoomViewEffect roomViewEffect = roomViewEffectWrapper.getContentIfNotHandled();
        if (roomViewEffect != null) {
            Timber.d("RoomViewEffect: %s", roomViewEffect);
            requestPermissions();
            if (roomViewEffect instanceof Connected) {
                room = ((Connected) roomViewEffect).getRoom();
                toggleAudioDevice(true);
                initializeRoom();
            }
            if (roomViewEffect instanceof Disconnected) {
                localParticipant = null;
                room = null;
                localParticipantSid = LOCAL_PARTICIPANT_STUB_SID;
                updateStats();
                toggleAudioDevice(false);
            }
            if (roomViewEffect instanceof ShowConnectFailureDialog) {
                new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
                        .setTitle(getString(R.string.room_screen_connection_failure_title))
                        .setMessage(getString(R.string.room_screen_connection_failure_message))
                        .setNeutralButton("OK", null)
                        .show();
                toggleAudioDevice(false);
            }
            if (roomViewEffect instanceof ShowTokenErrorDialog) {
                AuthServiceError error = ((ShowTokenErrorDialog) roomViewEffect).getServiceError();
                handleTokenError(error);
            }
        }
    }

    private void renderPrimaryView(ParticipantViewState primaryParticipant) {
        if (primaryParticipant != null) {
            primaryParticipantController.renderAsPrimary(
                    primaryParticipant.getSid(),
                    primaryParticipant.getIdentity(),
                    primaryParticipant.getScreenTrack(),
                    primaryParticipant.getVideoTrack(),
                    primaryParticipant.isMuted(),
                    primaryParticipant.isMirrored());
        } else {
            renderLocalParticipantStub();
        }
    }

    private void renderThumbnails(RoomViewState roomViewState) {
        participantAdapter.submitList(roomViewState.getParticipantThumbnails());
    }

    private void displayAudioDeviceList() {
        RoomViewState viewState = roomViewModel.getViewState().getValue();
        AudioDevice selectedDevice = viewState.getSelectedDevice();
        List<AudioDevice> audioDevices = viewState.getAvailableAudioDevices();

        if (selectedDevice != null && audioDevices != null) {
            int index = audioDevices.indexOf(selectedDevice);

            ArrayList<String> audioDeviceNames = new ArrayList<>();
            for (AudioDevice a : audioDevices) {
                audioDeviceNames.add(a.getName());
            }

            createAudioDeviceDialog(
                    this,
                    index,
                    audioDeviceNames,
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        SelectAudioDevice viewEvent =
                                new SelectAudioDevice(audioDevices.get(i));
                        roomViewModel.processInput(viewEvent);
                    })
                    .show();
        }
    }

    private AlertDialog createAudioDeviceDialog(
            final Activity activity,
            int currentDevice,
            ArrayList<String> availableDevices,
            DialogInterface.OnClickListener audioDeviceClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        builder.setTitle(activity.getString(R.string.room_screen_select_device));

        builder.setSingleChoiceItems(
                availableDevices.toArray(new CharSequence[0]),
                currentDevice,
                audioDeviceClickListener);

        return builder.create();
    }

    private void handleTokenError(AuthServiceError error) {
        int errorMessage =
                error == EXPIRED_PASSCODE_ERROR
                        ? R.string.room_screen_token_expired_message
                        : R.string.room_screen_token_retrieval_failure_message;

        new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
                .setTitle(getString(R.string.room_screen_connection_failure_title))
                .setMessage(getString(errorMessage))
                .setNeutralButton("OK", null)
                .show();
    }

    private boolean didAcceptPermissions() {
        return PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PermissionChecker.PERMISSION_GRANTED;
    }



    private void addRemoteParticipant(final RemoteParticipant remoteParticipant) {
        // Observe remote participant events
        remoteParticipant.setListener(remoteParticipantListener());

        for (final RemoteDataTrackPublication remoteDataTrackPublication :
                remoteParticipant.getRemoteDataTracks()) {
            /*
             * Data track messages are received on the thread that calls setListener. Post the
             * invocation of setting the listener onto our dedicated data track message thread.
             */
            if (remoteDataTrackPublication.isTrackSubscribed()) {
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(remoteParticipant,
                        remoteDataTrackPublication.getRemoteDataTrack()));
            }
        }
    }

    private void addRemoteDataTrack(RemoteParticipant remoteParticipant,
                                    RemoteDataTrack remoteDataTrack) {
        dataTrackRemoteParticipantMap.put(remoteDataTrack, remoteParticipant);
        remoteDataTrack.setListener(remoteDataTrackListener());
    }

    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {
            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {
            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {
            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {
            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {
            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {
            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {
            }

            @Override
            public void onDataTrackSubscribed(final RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              final RemoteDataTrack remoteDataTrack) {
                /*
                 * Data track messages are received on the thread that calls setListener. Post the
                 * invocation of setting the listener onto our dedicated data track message thread.
                 */
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(remoteParticipant, remoteDataTrack));
            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {
            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {
            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {
            }

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }
        };
    }

    private RemoteDataTrack.Listener remoteDataTrackListener() {
        return new RemoteDataTrack.Listener() {
            @Override
            public void onMessage(RemoteDataTrack remoteDataTrack, ByteBuffer byteBuffer) {

            }

            @Override
            public void onMessage(RemoteDataTrack remoteDataTrack, String message) {
                Log.d("Remote Data>>>>>>>>>>>", "onMessage: " + message);
                MotionMessage motionMessage = MotionMessage.fromJson(message);

                if (motionMessage != null) {
                    RemoteParticipant remoteParticipant = dataTrackRemoteParticipantMap
                            .get(remoteDataTrack);
                    int actionEvent = (motionMessage.actionDown) ?
                            (MotionEvent.ACTION_DOWN) :
                            MotionEvent.ACTION_UP;

                    // Process remote drawing event
                    float projectedX = motionMessage.coordinates.first *
                            (float) collaborativeDrawingView.getWidth();
                    float projectedY = motionMessage.coordinates.second *
                            (float) collaborativeDrawingView.getHeight();
                    int color = motionMessage.color;
                    float width = motionMessage.STROKE_WIDTH;
                    collaborativeDrawingView.onRemoteTouchEvent(remoteParticipant,
                            actionEvent,
                            projectedX,
                            projectedY,color,width);
                } else {
                    Log.e("Remote Data>>>>>>>", "Failed to deserialize message: " + message);
                }
            }
        };
    }


}
