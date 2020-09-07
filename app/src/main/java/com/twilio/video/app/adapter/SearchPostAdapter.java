package com.twilio.video.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.twilio.video.app.R;
import com.twilio.video.app.SearchResponse.Post;
import com.twilio.video.app.SearchResponse.User;
import com.twilio.video.app.util.TimeService;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.SearchPostAdapterViewHolder> {

    List<Post> postList;
    Context context;
    String token;

    public SearchPostAdapter(List<Post> postList, Context context, String token) {
        this.postList = postList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public SearchPostAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new SearchPostAdapter.SearchPostAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPostAdapterViewHolder holder, int position) {

        holder.caption.setText(postList.get(position).getContent());

        try {
            long time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(postList.get(position).getCreatedAt()).getTime();
            holder.timesAgo.setText(new TimeService().getTimeAgo(time, System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO PopUpMenu
                PopupMenu popup = new PopupMenu(context, holder.menuImage);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_on_post);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update_post:
                               // showUpdatePostpopup(postList.get(position));
                                //Toast.makeText(context, "Update Post", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete_post:
                                //Toast.makeText(context, "Delete Post", Toast.LENGTH_SHORT).show();
                                //deletePost(postList.get(position));
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

       // holder.noOfLikes.setText(String.valueOf(postList.get(position).getLikes().size()));
        //holder.noOfComment2.setText(String.valueOf(postList.get(position).getComments().size()) + "  " + " comments");
        if (postList.get(position).getHasImage() == 1) {
            holder.mediaView.setVisibility(View.VISIBLE);
            Glide.with(context).load("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png").listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.mediaView);
        }
        if (postList.get(position).getHasVideo() == 1) {
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.setVideoURI(Uri.parse("https://virtualskill0.s3.ap-southeast-1.amazonaws.com/public/images/profile-picture.png"
                    ));
            holder.videoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    holder.videoView.pause();
                }
            });


        }
        if (postList.get(position).getYoutubeLink() != null) {
            holder.ytVidView.setVisibility(View.VISIBLE);

            holder.ytVidView.initializeWithWebUi(new YouTubePlayerListener() {
                @Override
                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(postList.get(position).getYoutubeLink().toString(), 0);
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

        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class SearchPostAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView userName, timesAgo, noOfLikes, noOfComment2, caption, tvSeeComments;
        CircleImageView userProfile;
        ProgressBar progressBar;
        ImageView mediaView, likeView, menuImage;
        VideoView videoView;
        YouTubePlayerView ytVidView;
        RecyclerView recComments;
        LinearLayout linLayWriteComment;

        public SearchPostAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_user_name_on_post);
            timesAgo = itemView.findViewById(R.id.tv_times_ago_post);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            noOfComment2 = itemView.findViewById(R.id.no_of_comments2);
            caption = itemView.findViewById(R.id.tv_caption_on_post);
            userProfile = itemView.findViewById(R.id.profile_pic_on_post);
            progressBar = itemView.findViewById(R.id.progress_load_media_on_post);
            mediaView = itemView.findViewById(R.id.iv_on_post);
            recComments = itemView.findViewById(R.id.recView_comment_on_post);
            tvSeeComments = itemView.findViewById(R.id.tv_see_comments);
            linLayWriteComment = itemView.findViewById(R.id.lin_lay_write_comment);
            likeView = itemView.findViewById(R.id.iv_like);
            menuImage = itemView.findViewById(R.id.iv_post_menu);
            videoView = itemView.findViewById(R.id.vv_on_post);
            ytVidView = itemView.findViewById(R.id.ytVv_on_post);

        }
    }
}
