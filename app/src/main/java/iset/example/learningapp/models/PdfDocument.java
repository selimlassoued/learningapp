package iset.example.learningapp.models;

import java.io.Serializable;

public class PdfDocument implements Serializable {
    private int id;
    private String title;
    private String url;
    private long fileSize;
    private boolean isDownloaded;

    public PdfDocument() {}

    public PdfDocument(int id, String title, String url, long fileSize) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.fileSize = fileSize;
        this.isDownloaded = false;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public long getFileSize() { return fileSize; }
    public boolean isDownloaded() { return isDownloaded; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setUrl(String url) { this.url = url; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public void setDownloaded(boolean downloaded) { isDownloaded = downloaded; }

    public String getFormattedSize() {
        if (fileSize < 1024) return fileSize + " B";
        else if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        else return String.format("%.1f MB", fileSize / (1024.0 * 1024));
    }
}
