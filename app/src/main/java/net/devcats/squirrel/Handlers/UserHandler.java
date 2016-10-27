package net.devcats.squirrel.Handlers;

import android.content.Context;

import net.devcats.squirrel.Models.ModelAlbum;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.Models.UserData;
import net.devcats.squirrel.Utils;

import java.util.ArrayList;
import java.util.List;

public class UserHandler {

    public static final String KEY_SELECTED_MODEL_FILTER = "selected_model_filter";
    public static final String KEY_FILTER_FEMALE = "filter_female";
    public static final String KEY_FILTER_MALE = "filter_male";
    public static final String KEY_FILTER_COUPLE = "filter_couple";
    public static final String KEY_FILTER_GROUP = "filter_group";

    public static final String FILTER_ALL_MODELS = "all_models";
    public static final String FILTER_MY_MODELS = "my_models";

    public static final String FILTER_TRUE = "true";
    public static final String FILTER_FALSE = "false";

    private static UserHandler mInstance;

    private Context mContext;

    private UserData userData;

    public static UserHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserHandler(context);
        }

        return mInstance;
    }

    public UserHandler(Context context) {
        mContext = context;
        userData = new UserData();
    }

    public void parseUserDataFromJson(String json) {

    }

    public int getUserID() {
        return userData.getId();
    }

    public List<ModelData> getSubscribedModels() {
        return userData.getSubscribedModels();
    }

    public void addModelToSubscribedList(ModelData model) {
        userData.getSubscribedModels().add(model);
    }

    public List<ModelAlbum> getUserAlbums() {

        // TODO: TESTING!!! REMOVE!!!
        List<ModelAlbum> albums = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ModelAlbum modelAlbum = new ModelAlbum();

            modelAlbum.setAlbumId(i + 1);
            modelAlbum.setName("Album " + (i + 1));
            modelAlbum.setImageCount(5);
            modelAlbum.setUserId(1);

            albums.add(modelAlbum);
        }

        return albums;

//        return userData.getUserAlbums();
    }

    public void clearSubscribedModels() {
        userData.getSubscribedModels().clear();
    }

    public boolean isSubscribedToModel(int modelID) {

        for (ModelData model : userData.getSubscribedModels()) {
            if (model.getID() == modelID) {
                return true;
            }
        }

        return false;
    }

    public void saveFilterSetting(String key, String value) {
        Utils.saveSetting(mContext, key, value);
    }

    public String getSavedFilterSetting(String key) {
        return Utils.getSetting(mContext, key);
    }

}
