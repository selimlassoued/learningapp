package iset.example.learningapp.models;

import java.io.Serializable;

public class Professor implements Serializable {
    private int id;
    private String name;
    private String speciality;
    private int experience; // en années
    private String imageUrl;

    public Professor() {}

    public Professor(int id, String name, String speciality, int experience, String imageUrl) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.experience = experience;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpeciality() { return speciality; }
    public int getExperience() { return experience; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
