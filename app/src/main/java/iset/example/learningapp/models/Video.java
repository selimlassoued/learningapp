package iset.example.learningapp.models;

import java.io.Serializable;

public class Video implements Serializable {
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;
    private int duration; // en secondes
    private int order;

    public Video() {}

    public Video(int id, String title, String url, String thumbnailUrl, int duration, int order) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.order = order;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public int getDuration() { return duration; }
    public int getOrder() { return order; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setUrl(String url) { this.url = url; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setOrder(int order) { this.order = order; }

    // Helper method pour formater la durée
    public String getFormattedDuration() {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%d:%02d", minutes, seconds);
    }
}