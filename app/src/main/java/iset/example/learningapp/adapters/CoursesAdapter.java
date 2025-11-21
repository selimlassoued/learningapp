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
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import iset.example.learningapp.R;
import iset.example.learningapp.models.Course;
import iset.example.learningapp.models.Professor;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> courses;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CoursesAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private ImageView ivCourseImage;
        private TextView tvCourseTitle;
        private TextView tvCourseCategory;
        private TextView tvProfessorName;
        private TextView tvCourseStats;
        private TextView tvRating;
        private ImageView ivProfessorImage;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardCourse);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseCategory = itemView.findViewById(R.id.tvCourseCategory);
            tvProfessorName = itemView.findViewById(R.id.tvProfessorName);
            tvCourseStats = itemView.findViewById(R.id.tvCourseStats);
            tvRating = itemView.findViewById(R.id.tvRating);
            ivProfessorImage = itemView.findViewById(R.id.ivProfessorImage);
        }

        public void bind(Course course) {
            tvCourseTitle.setText(course.getTitle());
            tvCourseCategory.setText(course.getCategory());

            // Stats du cours
            String stats = course.getTotalSections() + " sections • " +
                    course.getTotalVideos() + " vidéos • " +
                    course.getTotalDurationFormatted();
            tvCourseStats.setText(stats);

            // Rating
            tvRating.setText(String.format("%.1f", course.getRating()));

            // Professeur
            Professor prof = course.getProfessor();
            if (prof != null) {
                tvProfessorName.setText(prof.getName());

                Glide.with(itemView.getContext())
                        .load(prof.getImageUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_person_placeholder)
                        .into(ivProfessorImage);
            }

            // Image du cours
            Glide.with(itemView.getContext())
                    .load(course.getImageUrl())
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.bg_video_thumbnail)
                    .into(ivCourseImage);

            // Click listener
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            });
        }
    }
}