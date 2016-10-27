package net.devcats.squirrel.Handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.devcats.squirrel.Models.Image;
import net.devcats.squirrel.Models.ModelData;
import net.devcats.squirrel.Tasks.DownloadImageTask;
import net.devcats.squirrel.Tasks.UrlRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ModelDataHandler {
    private static final String TAG = ModelDataHandler.class.getSimpleName();

    private static ModelDataHandler modelDataHandler;

    private Context mContext;
    private List<ModelData> mModels = new ArrayList<>();

    private List<ModelDataHandlerCallbacks> mListeners;

    public interface ModelDataHandlerCallbacks {
        void onModelTaskComplete();
    }

    public static ModelDataHandler getInstance(Context context) {
        if (modelDataHandler == null) {
            modelDataHandler = new ModelDataHandler(context);
        }

        return modelDataHandler;
    }

    public ModelDataHandler(Context context) {
        mContext = context;
        mListeners = new ArrayList<>();
    }

    public void addOnTaskCompleteListener(ModelDataHandlerCallbacks listener) {
        mListeners.add(listener);
    }

    private void updateListeners() {
        for (ModelDataHandlerCallbacks listener : mListeners) {
            listener.onModelTaskComplete();
        }
    }

    public void removeOnTaskCompleteListener(ModelDataHandlerCallbacks listener) {
        mListeners.remove(listener);
    }

    public void getModelListFromWeb() {
        try {

            new UrlRequest(mContext, UrlRequest.ACTION_GET_MODEL_LIST, null, new UrlRequest.UrlRequestCallbacks() {
                @Override
                public void onUrlRequestComplete(String result) {
                    parseModelsDataFromJSON(result);
                    updateListeners();
                }
            }).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ModelData> parseModelsDataFromJSON(String json) {
        try {

            JSONArray modelArray = new JSONArray(json);
            mModels = new ArrayList<>(modelArray.length());
            UserHandler.getInstance(mContext).clearSubscribedModels();

            for (int i = 0; i < modelArray.length(); i++) {
                mModels.add(parseModelDataFromJSON(modelArray.getJSONObject(i)));
            }

            return mModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ModelData parseModelDataFromJSON(JSONObject object) {
        try {
            ModelData modelData = new ModelData();
            List<Image> imageList = new ArrayList<>();

            modelData.setID(object.getInt("modelId"));
            modelData.setUsername(object.getString("modelUsername"));
            modelData.setAge(object.getInt("modelAge"));
            modelData.setPartyType(object.getInt("modelPartyType"));
            modelData.setProfileImageUrl(object.getString("modelProfileImageURL"));
            modelData.setImageCount(object.getInt("modelImageCount"));
            modelData.setIsSubscribed(object.getBoolean("modelIsSubscribed"));
            modelData.setStarRating(object.getInt("modelStarRating"));

            // Check if we're subscribed or not
            if (modelData.getIsSubscribed()) {
                UserHandler.getInstance(mContext).addModelToSubscribedList(modelData);
            }

            JSONArray imageArray = object.getJSONArray("modelImageUrlArray");

            for (int i = 0; i < imageArray.length(); i++) {
                JSONObject imageObject = imageArray.getJSONObject(i);

                Image image = new Image();
                image.setImageId(imageObject.getInt("imageId"));
                image.setImageType(imageObject.getInt("imageType"));
                image.setImageURL(imageObject.getString("imageUrl"));

                imageList.add(image);

            }

            modelData.setImageList(imageList);

            return modelData;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ModelData> getModelsList() {
        return mModels;
    }

    public void getModelProfileImage(final ModelData model) {

        if (model != null && model.getProfileImage() == null && !model.isDownloading()) {

            model.setIsDownloading(true);

            Bitmap bitmap = loadProfileImageFromDisk(model.getID());

            if (bitmap != null) {
                model.setProfileImage(bitmap);
                model.setIsDownloading(false);
                updateListeners();
            } else {
                new DownloadImageTask(model.getID(), 0, model.getProfileImageUrl(), new DownloadImageTask.GetImageTaskCallbacks() {
                    @Override
                    public void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap) {
                        model.setProfileImage(bitmap);
                        model.setIsDownloading(false);
                        updateListeners();

                        saveProfileImageToDisk(modelId, bitmap);
                    }
                }).execute();
            }
        }
    }

    public ModelData getModelByID(int modelId) {
        for (ModelData model : mModels) {
            if (model.getID() == modelId) {
                return model;
            }
        }
        return null;
    }

    public int getVisibleModelCount() {
        int count = 0;

        for (ModelData modelData : mModels) {
            if (modelData.isVisible()) {
                count++;
            }
        }

        return count;
    }

    public void saveProfileImageToDisk(final int modelId, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File directory = new File(mContext.getFilesDir(), "model_" + modelId);
                    if (!directory.exists()) {
                        if (!directory.mkdir()) {
                            throw new Exception("Failed to create directory: model_" + modelId);
                        }
                    }

                    File file = new File(directory, "profile_image.png");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Bitmap loadProfileImageFromDisk(int modelId) {
        try {
            File file = new File(mContext.getFilesDir() + "/model_" + modelId, "profile_image.png");
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Profile image for model number " + modelId + " could not be found locally. Downloading from server...");
        }
        return null;
    }

    public void downloadModelImages(ModelData modelData) {

        for (int i = 0; i < modelData.getImageCount(); i++) {

            Image image = modelData.getImageList().get(i);

            if (!image.isDownloading()) {
                image.setIsDownloading(true);

                new DownloadImageTask(modelData.getID(), modelData.getImageList().get(i).getImageId(), modelData.getImageList().get(i).getImageURL(), new DownloadImageTask.GetImageTaskCallbacks() {
                    @Override
                    public void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap) {
                        getModelByID(modelId).getImageByID(imageId).setIsDownloading(false);
                        getModelByID(modelId).getImageByID(imageId).setImageBitmap(bitmap);
                        updateListeners();
                    }
                }).execute();
            }
        }
    }

//
//    public List<ModelData> getModelList() {
//        return mModels;
//    }
//
//    public void getModelById(int modelId, final ModelDataHandlerCallbacks callbacks) {
//        try {
//            JSONObject object = new JSONObject();
//            object.put("action", ACTION_GET_BY_ID);
//            object.put("modelId", modelId);
//
//            new UrlRequest(mContext, Configuration.BASE_URL_PATH, object, new UrlRequest.UrlRequestCallbacks() {
//                @Override
//                public void onUrlRequestComplete(String result) {
//                    callbacks.onUrlRequestComplete(result);
//                }
//            }).execute();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void downloadModelImages(ModelData modelData, final ModelDataHandlerCallbacks callbacks) {
//        for (Image image : modelData.getImageList()) {
//            new DownloadImageTask(modelData.getID(),image.getImageId(), image.getImageURL(), new DownloadImageTask.GetImageTaskCallbacks() {
//                @Override
//                public void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap) {
//                    callbacks.onGetBitmapComplete(modelId, imageId, bitmap);
//                }
//            }).execute();
//        }
//    }
//
//
//
//    public void downloadModelProfileImage(int modelId, String url, final ModelDataHandlerCallbacks callbacks) {
//        new DownloadImageTask(modelId, 0, url, new DownloadImageTask.GetImageTaskCallbacks() {
//            @Override
//            public void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap) {
//                callbacks.onGetBitmapComplete(modelId, imageId, bitmap);
//            }
//        }).execute();
//    }
//
//
//
//    public void getProfileImage(int modelId, final ModelDataHandlerCallbacks callbacks) {
//        for (ModelData model : mModels) {
//            if (model.getID() == modelId) {
//                if (model.getProfileImage() != null) {
//                    callbacks.onGetBitmapComplete(modelId, 0, model.getProfileImage());
//                }
//                break;
//            }
//        }
//
//        new DownloadImageTask(modelId, 0, Configuration.BASE_URL_PATH, new DownloadImageTask.GetImageTaskCallbacks() {
//            @Override
//            public void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap) {
//                callbacks.onGetBitmapComplete(modelId, imageId, bitmap);
//            }
//        }).execute();
//    }
//
//
}