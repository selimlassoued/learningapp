package iset.example.learningapp.models;

import java.io.Serializable;
import java.util.List;

public class CourseSection implements Serializable {
    private int id;
    private String title;
    private int order;
    private PdfDocument pdfDocument;
    private List<Video> videos;
    private boolean isExpanded; // pour l'UI

    public CourseSection() {}

    public CourseSection(int id, String title, int order, PdfDocument pdfDocument, List<Video> videos) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.pdfDocument = pdfDocument;
        this.videos = videos;
        this.isExpanded = false;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getOrder() { return order; }
    public PdfDocument getPdfDocument() { return pdfDocument; }
    public List<Video> getVideos() { return videos; }
    public boolean isExpanded() { return isExpanded; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setOrder(int order) { this.order = order; }
    public void setPdfDocument(PdfDocument pdfDocument) { this.pdfDocument = pdfDocument; }
    public void setVideos(List<Video> videos) { this.videos = videos; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }

    public int getTotalVideos() {
        return videos != null ? videos.size() : 0;
    }

    public int getTotalDuration() {
        if (videos == null) return 0;
        int total = 0;
        for (Video video : videos) {
            total += video.getDuration();
        }
        return total;
    }
}