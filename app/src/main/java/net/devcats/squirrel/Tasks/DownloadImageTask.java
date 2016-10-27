package net.devcats.squirrel.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = DownloadImageTask.class.getSimpleName();

    private GetImageTaskCallbacks mCallbacks;

    private int mModelId;
    private int mImageId;
    private String mUrl;

    public interface GetImageTaskCallbacks {
        void onGetImageTaskComplete(int modelId, int imageId, Bitmap bitmap);
    }

    public DownloadImageTask(int modelId, int imageId, String url, GetImageTaskCallbacks callbacks) {
        mModelId = modelId;
        mImageId = imageId;
        mUrl = url;
        mCallbacks = callbacks;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.i(TAG, "Downloading image: " + mUrl);
        try {
            java.net.URL url = new java.net.URL(mUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        mCallbacks.onGetImageTaskComplete(mModelId, mImageId, result);
    }

}