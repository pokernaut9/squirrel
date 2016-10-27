package net.devcats.squirrel.Models;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ModelAlbum {

    private int albumId;
    private String name;
    private int userId;
    private int imageCount;
    private Bitmap albumImage;
    private List<Image> images;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public Bitmap getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Bitmap albumImage) {
        this.albumImage = albumImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public ModelAlbum parseAlbumFromJSON(String json) {

        try {

            JSONObject object = new JSONObject(json);

            albumId = object.getInt("albumId");
            name = object.getString("name");
            userId = object.getInt("userId");
            imageCount = object.getInt("imageCount");

            JSONArray imagesArray = object.getJSONArray("images");

            for (int i = 0; i < imagesArray.length(); i++) {

                JSONObject imageObject = imagesArray.getJSONObject(i);

                Image image = new Image();
                image.setImageId(imageObject.getInt("imageId"));
                image.setImageType(imageObject.getInt("imageType"));
                image.setImageURL(imageObject.getString("imageUrl"));
                image.setIsDownloading(false);

                images.add(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }

}
