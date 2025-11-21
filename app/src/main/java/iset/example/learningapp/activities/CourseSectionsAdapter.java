package iset.example.learningapp.activities;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import iset.example.learningapp.R;
import iset.example.learningapp.models.CourseSection;
import iset.example.learningapp.models.PdfDocument;
import iset.example.learningapp.models.Video;

import java.util.List;

public class CourseSectionsAdapter extends RecyclerView.Adapter<CourseSectionsAdapter.SectionViewHolder> {

    private List<CourseSection> sections;
    private OnSectionInteractionListener listener;

    public interface OnSectionInteractionListener {
        void onPdfDownloadClick(PdfDocument pdf);
        void onVideoClick(Video video);
    }

    public CourseSectionsAdapter(List<CourseSection> sections, OnSectionInteractionListener listener) {
        this.sections = sections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        CourseSection section = sections.get(position);
        holder.bind(section, position);
    }

    @Override
    public int getItemCount() {
        return sections != null ? sections.size() : 0;
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSectionNumber;
        private TextView tvSectionTitle;
        private TextView tvSectionInfo;
        private ImageView ivExpandIcon;
        private LinearLayout layoutHeader;
        private LinearLayout layoutContent;
        private LinearLayout layoutPdf;
        private TextView tvPdfTitle;
        private TextView tvPdfSize;
        private ImageButton btnDownloadPdf;
        private RecyclerView rvVideos;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionNumber = itemView.findViewById(R.id.tvSectionNumber);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            tvSectionInfo = itemView.findViewById(R.id.tvSectionInfo);
            ivExpandIcon = itemView.findViewById(R.id.ivExpandIcon);
            layoutHeader = itemView.findViewById(R.id.layoutSectionHeader);
            layoutContent = itemView.findViewById(R.id.layoutSectionContent);
            layoutPdf = itemView.findViewById(R.id.layoutPdf);
            tvPdfTitle = itemView.findViewById(R.id.tvPdfTitle);
            tvPdfSize = itemView.findViewById(R.id.tvPdfSize);
            btnDownloadPdf = itemView.findViewById(R.id.btnDownloadPdf);
            rvVideos = itemView.findViewById(R.id.rvVideos);
        }

        public void bind(CourseSection section, int position) {
            // Numéro et titre
            tvSectionNumber.setText(String.valueOf(position + 1));
            tvSectionTitle.setText(section.getTitle());

            // Info de la section
            String info = buildSectionInfo(section);
            tvSectionInfo.setText(info);

            // État d'expansion
            updateExpandState(section.isExpanded(), false);

            // Click pour expand/collapse
            layoutHeader.setOnClickListener(v -> {
                section.setExpanded(!section.isExpanded());
                updateExpandState(section.isExpanded(), true);
            });

            // Configuration du PDF
            setupPdfSection(section.getPdfDocument());

            // Configuration des vidéos
            setupVideosRecyclerView(section.getVideos());
        }

        private String buildSectionInfo(CourseSection section) {
            StringBuilder sb = new StringBuilder();
            if (section.getPdfDocument() != null) {
                sb.append("1 PDF");
            }
            int videoCount = section.getTotalVideos();
            if (videoCount > 0) {
                if (sb.length() > 0) sb.append(" • ");
                sb.append(videoCount).append(" vidéo").append(videoCount > 1 ? "s" : "");
            }
            int totalSeconds = section.getTotalDuration();
            if (totalSeconds > 0) {
                if (sb.length() > 0) sb.append(" • ");
                int minutes = totalSeconds / 60;
                sb.append(minutes).append(" min");
            }
            return sb.toString();
        }

        private void updateExpandState(boolean isExpanded, boolean animate) {
            if (animate) {
                // Animation de rotation de l'icône
                float rotation = isExpanded ? 180f : 0f;
                ObjectAnimator.ofFloat(ivExpandIcon, "rotation", rotation)
                        .setDuration(200)
                        .start();

                // Animation du contenu
                if (isExpanded) {
                    layoutContent.setVisibility(View.VISIBLE);
                    layoutContent.setAlpha(0f);
                    layoutContent.animate().alpha(1f).setDuration(200).start();
                } else {
                    layoutContent.animate()
                            .alpha(0f)
                            .setDuration(200)
                            .withEndAction(() -> layoutContent.setVisibility(View.GONE))
                            .start();
                }
            } else {
                ivExpandIcon.setRotation(isExpanded ? 180f : 0f);
                layoutContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                layoutContent.setAlpha(isExpanded ? 1f : 0f);
            }
        }

        private void setupPdfSection(PdfDocument pdf) {
            if (pdf != null) {
                layoutPdf.setVisibility(View.VISIBLE);
                tvPdfTitle.setText(pdf.getTitle());
                tvPdfSize.setText(pdf.getFormattedSize());

                // Icône selon l'état de téléchargement
                btnDownloadPdf.setImageResource(
                        pdf.isDownloaded() ? R.drawable.ic_check : R.drawable.ic_download
                );

                btnDownloadPdf.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPdfDownloadClick(pdf);
                    }
                });

                layoutPdf.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPdfDownloadClick(pdf);
                    }
                });
            } else {
                layoutPdf.setVisibility(View.GONE);
            }
        }

        private void setupVideosRecyclerView(List<Video> videos) {
            if (videos != null && !videos.isEmpty()) {
                rvVideos.setVisibility(View.VISIBLE);
                rvVideos.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                rvVideos.setNestedScrollingEnabled(false);

                VideosAdapter videosAdapter = new VideosAdapter(videos, video -> {
                    if (listener != null) {
                        listener.onVideoClick(video);
                    }
                });
                rvVideos.setAdapter(videosAdapter);
            } else {
                rvVideos.setVisibility(View.GONE);
            }
        }
    }
}