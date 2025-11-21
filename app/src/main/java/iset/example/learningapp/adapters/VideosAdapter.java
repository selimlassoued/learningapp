package iset.example.learningapp.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import iset.example.learningapp.R;
import iset.example.learningapp.models.Video;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<Video> videos;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }

    public VideosAdapter(List<Video> videos, OnVideoClickListener listener) {
        this.videos = videos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDuration;
        private TextView tvOrder;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivVideoThumbnail);
            tvTitle = itemView.findViewById(R.id.tvVideoTitle);
            tvDuration = itemView.findViewById(R.id.tvVideoDuration);
            tvOrder = itemView.findViewById(R.id.tvVideoOrder);
        }

        public void bind(Video video) {
            tvTitle.setText(video.getTitle());
            tvDuration.setText(video.getFormattedDuration());
            tvOrder.setText("Vidéo " + video.getOrder());

            // Charger thumbnail avec Glide
            if (video.getThumbnailUrl() != null && !video.getThumbnailUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(video.getThumbnailUrl())
                        .transform(new RoundedCorners(16))
                        .placeholder(R.drawable.bg_video_thumbnail)
                        .into(ivThumbnail);
            }

            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            });
        }
    }
}