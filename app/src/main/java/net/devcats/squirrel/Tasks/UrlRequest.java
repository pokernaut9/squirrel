package net.devcats.squirrel.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UrlRequest extends AsyncTask<Void, Void, String> {

    public static String URL_MAIN = "http://www.devcats.net/squirrel/index.php";

    public static final String ACTION_VERSION_CHECK = "versionCheck";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_REGISTER = "register";
    public static final String ACTION_GET_MODEL_LIST = "getModels";
    public static final String ACTION_ADD_NEW_ALBUM = "addNewAlbum";

    private Context mContext;
    private UrlRequestCallbacks mCallbacks;
    private String mUrl;
    private JSONObject mParams;
    private String mAction;

    public interface UrlRequestCallbacks {
        void onUrlRequestComplete(String result);
    }

    public UrlRequest(Context context, String action, JSONObject params, UrlRequestCallbacks callbacks) {
        mContext = context;
        mUrl = URL_MAIN;
        mAction = action;
        mParams = params;
        mCallbacks = callbacks;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("userId", "" + UserHandler.getInstance(mContext).getUserID()));
            nameValuePairs.add(new BasicNameValuePair("action", mAction));

            if (mParams != null) {
                // Add each param
                Iterator<String> iterator = mParams.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    nameValuePairs.add(new BasicNameValuePair(key, mParams.getString(key)));
                }
            }

            HttpPost httpPost = new HttpPost(mUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return mContext.getString(R.string.error) + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mCallbacks.onUrlRequestComplete(result);
    }

}