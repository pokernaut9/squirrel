package net.devcats.squirrel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import net.devcats.squirrel.Tasks.UrlRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

        mContext = this;

        // Set TextView with version number
        ((TextView) findViewById(R.id.tvVersionNumber)).setText("v" + Utils.getAppVersion(mContext));

        checkVersion();
    }

    private void checkVersion() {
        try {
            JSONObject params = new JSONObject();
            params.put("version", Utils.getAppVersion(mContext));

            new UrlRequest(this, UrlRequest.ACTION_VERSION_CHECK, params, new UrlRequest.UrlRequestCallbacks() {
                @Override
                public void onUrlRequestComplete(String result) {

                    if (result.equals("true")) {
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle(R.string.application_version_error_title)
                                .setMessage(R.string.application_version_error_message)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // TODO: redirect user to app url
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            }).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
