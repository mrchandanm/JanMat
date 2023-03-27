package com.prabitra.janmat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.prabitra.janmat.DateFormatting;
import com.prabitra.janmat.Models.PollsModel;
import com.prabitra.janmat.Models.PollsPostModel;
import com.prabitra.janmat.R;
import com.prabitra.janmat.UploadPollActivity;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PollsPostAdapter extends RecyclerView.Adapter<PollsPostAdapter.ViewHolder> {
    View view;
    Context mContext;
    ArrayList<PollsPostModel> pollsPostModelslist;

    public PollsPostAdapter(Context mContext, ArrayList<PollsPostModel> pollsPostModelslist) {
        this.mContext = mContext;
        this.pollsPostModelslist = pollsPostModelslist;
    }

    @NonNull
    @Override
    public PollsPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(mContext).inflate(R.layout.cart_poll_post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollsPostAdapter.ViewHolder holder, int position) {

        long time=pollsPostModelslist.get(position).getTime();
        String formattedDate = DateFormatting.getFormattedDate(time);
        holder.pollPostTitle.setText(pollsPostModelslist.get(position).getTitle());
        holder.userName.setText(pollsPostModelslist.get(position).getUserName());
        holder.time.setText(formattedDate);

        holder.setData(position,pollsPostModelslist.get(position).getPollsModels(),pollsPostModelslist.get(position).getPollId());

if(!pollsPostModelslist.get(holder.getAdapterPosition()).getYoutubevideoId().isEmpty()) {
    holder.youTubePlayerView.setVisibility(View.VISIBLE);
    holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
            String videoId = pollsPostModelslist.get(holder.getAbsoluteAdapterPosition()).getYoutubevideoId();
            youTubePlayer.loadVideo(videoId, 0);
            youTubePlayer.pause();
        }
    });
}
else{
    holder.youTubePlayerView.setVisibility(View.GONE);
}
ArrayList<String> imagelist=new ArrayList<>();
imagelist=pollsPostModelslist.get(holder.getAdapterPosition()).getPhotos();
if(imagelist.size()==0 || imagelist==null){
    holder.postviewpagercardview.setVisibility(View.GONE);
}
else{
    holder.postviewpagercardview.setVisibility(View.VISIBLE);
    DisplayUploadImageViewPagerAdapter viewPagerAdapter=new DisplayUploadImageViewPagerAdapter(mContext,pollsPostModelslist.get(holder.getAdapterPosition()).getPhotos());
    holder.postviewpager.setAdapter(viewPagerAdapter);
}
    }

    @Override
    public int getItemCount() {
        return pollsPostModelslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView pollRecycelrView;
        TextView pollPostTitle,userName,time;
        PollsAdapter pollsAdapter;
        YouTubePlayerView youTubePlayerView;
        ViewPager postviewpager;
        CardView postviewpagercardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pollRecycelrView=itemView.findViewById(R.id.Poll_RecyclerView);
            pollPostTitle=itemView.findViewById(R.id.cart_poll_post_title);
            userName=itemView.findViewById(R.id.cart_post_username);
            time=itemView.findViewById(R.id.cart_post_date_tv);
            youTubePlayerView=itemView.findViewById(R.id.youtube_player_view);
            postviewpager=itemView.findViewById(R.id.cart_post_view_pager);
            postviewpagercardview=itemView.findViewById(R.id.cart_post_view_pager_cardView);
        }

        public void setData(int position,ArrayList<PollsModel> pollsModelslist,String pollId) {
            pollRecycelrView.setLayoutManager(new LinearLayoutManager(mContext));
            pollsAdapter=new PollsAdapter(pollsModelslist,mContext,pollId);
            pollRecycelrView.setAdapter(pollsAdapter);

        }
    }
}
