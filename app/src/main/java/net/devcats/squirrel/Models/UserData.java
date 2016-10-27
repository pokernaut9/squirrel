package net.devcats.squirrel.Models;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static final String TAG = UserData.class.getSimpleName();

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<ModelData> subscribedModels;
    private List<ModelAlbum> userAlbums;

    public UserData() {
        id = 0;
        firstName = "";
        lastName = "";
        email = "";
        password = "";
        subscribedModels = new ArrayList<>();
    }

    public static String getTAG() {
        return TAG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ModelData> getSubscribedModels() {
        return subscribedModels;
    }

    public void setSubscribedModels(List<ModelData> subscribedModels) {
        this.subscribedModels = subscribedModels;
    }

    public List<ModelAlbum> getUserAlbums() {
        return userAlbums;
    }

    public void setUserAlbums(List<ModelAlbum> userAlbums) {
        this.userAlbums = userAlbums;
    }
}
