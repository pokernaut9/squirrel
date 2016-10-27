package net.devcats.squirrel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.zip.Inflater;

public class Utils {

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void saveSetting(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSetting(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static void log(String message) {
        log("Squirrel", message);
    }

    public static void log(String tag, String message) {
        Log.i(tag, message);
    }

    public static void makeToast(Context context, String message) {
        makeToast(context, message, Toast.LENGTH_LONG);
    }

    public static void makeToast(Context context, String message, int toastLength) {
        Toast.makeText(context, message, toastLength).show();
    }

    public static AlertDialog showWaitSpinner(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(R.layout.dialog_please_wait);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        return dialog;
    }

}
