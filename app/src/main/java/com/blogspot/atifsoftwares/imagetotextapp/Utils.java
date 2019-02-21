package com.blogspot.atifsoftwares.imagetotextapp;

import android.app.Activity;
import android.content.Intent;

public class Utils {
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity)
    {
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
