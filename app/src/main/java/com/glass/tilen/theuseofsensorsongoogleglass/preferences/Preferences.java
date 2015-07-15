package com.glass.tilen.theuseofsensorsongoogleglass.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

/**
 * Created by Tilen on 14.7.2015.
 */
public class Preferences {

    private final static String SHARED_PREF_NAME = "sharedPref";
    private final static String STATE_TUTORIAL = "isTutorial";

    private static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean startTutorial(Context context)
    {
        SharedPreferences sharedPref = getSharedPreferences(context);
        boolean isTutorial = sharedPref.getBoolean(STATE_TUTORIAL, true);
        return isTutorial;
    }

    public static void setStartActivity(Context context)
    {
        SharedPreferences sharedPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(STATE_TUTORIAL, false);
        editor.commit();
    }

    public static void setScreenOn(Activity activity)
    {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
