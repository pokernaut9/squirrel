package net.devcats.squirrel.Models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ModelData {

    public static final int FEMALE = 0;
    public static final int MALE = 1;
    public static final int COUPLE = 2;
    public static final int GROUP = 3;

    public final static String MODEL_ID = "model_ID";
    public final static String MODEL_SELECTED_IMAGE_POSITION = "selected_image_position";

    private int id;
    private String username;
    private int age;
    private int partyType;
    private String profileImageUrl;
    private Bitmap profileImage = null;
    private int imageCount;
    private boolean isSubscribed;
    private int starRating;
    private List<Image> imageList = new ArrayList<>();

    private boolean visible = true;
    private boolean isDownloading = false;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPartyType() {
        return partyType;
    }

    public void setPartyType(int partyType) {
        this.partyType = partyType;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public Image getImageByID(int imageID) {
        for (Image image : imageList) {
            if (image.getImageId() == imageID) {
                return image;
            }
        }
        return null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }
}