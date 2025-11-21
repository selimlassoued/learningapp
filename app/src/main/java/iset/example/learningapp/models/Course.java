package iset.example.learningapp.models;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    private int id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private List<CourseSection> sections;
    private Professor professor;
    private boolean isEnrolled;
    private int enrolledCount;
    private float rating;

    public Course() {}

    public Course(int id, String title, String description, String category,
                  String imageUrl, List<CourseSection> sections, Professor professor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.sections = sections;
        this.professor = professor;
        this.isEnrolled = false;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public List<CourseSection> getSections() { return sections; }
    public Professor getProfessor() { return professor; }
    public boolean isEnrolled() { return isEnrolled; }
    public int getEnrolledCount() { return enrolledCount; }
    public float getRating() { return rating; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setSections(List<CourseSection> sections) { this.sections = sections; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setEnrolled(boolean enrolled) { isEnrolled = enrolled; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }
    public void setRating(float rating) { this.rating = rating; }

    // Helper methods
    public int getTotalSections() {
        return sections != null ? sections.size() : 0;
    }

    public int getTotalVideos() {
        if (sections == null) return 0;
        int total = 0;
        for (CourseSection section : sections) {
            total += section.getTotalVideos();
        }
        return total;
    }

    public String getTotalDurationFormatted() {
        if (sections == null) return "0 min";
        int totalSeconds = 0;
        for (CourseSection section : sections) {
            totalSeconds += section.getTotalDuration();
        }
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        if (hours > 0) {
            return hours + "h " + minutes + "min";
        }
        return minutes + " min";
    }
}