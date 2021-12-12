package model;

public class NewsFeed {

    private String title, time, description, profImage, mainImage;
    int id, postType;

    public NewsFeed(String title, String time, String description, String profImage, String mainImage, int id, int postType) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.profImage = profImage;
        this.mainImage = mainImage;
        this.id = id;
        this.postType = postType;
    }

    public NewsFeed(String title, String time, String description, String profImage, String mainImage, int postType) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.profImage = profImage;
        this.mainImage = mainImage;
        this.postType = postType;
    }

    public NewsFeed(String title, String time, String description, String profImage, String mainImage) {
        this.title = title;
        this.time = time;
        this.description = description;
        this.profImage = profImage;
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }
}
