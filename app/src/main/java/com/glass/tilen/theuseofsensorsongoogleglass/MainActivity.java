package com.glass.tilen.theuseofsensorsongoogleglass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.google.android.glass.widget.CardScrollView;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent;
        if(Preferences.startTutorial(this))
            intent = new Intent(this, SensorsActivity.class);
        else
            intent = new Intent(this, SensorsActivity.class); //TODO setProperActivity
        startActivity(intent);
        finish();
    }
}
