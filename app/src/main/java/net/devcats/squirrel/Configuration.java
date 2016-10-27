package net.devcats.squirrel;

import android.content.Context;
import android.content.pm.PackageManager;

public class Configuration {

    public static final String BASE_URL_PATH = "http://devcats.net/squirrel/";



    public static Configuration mConfiguration;

    public static Configuration getInstance() {
        if (mConfiguration == null) {
            mConfiguration = new Configuration();
        }

        return mConfiguration;
    }

    public Configuration() {

    }


}