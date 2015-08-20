package com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

/**
 * Created by Tilen on 17.8.2015.
 */
public abstract class MultiLayoutActivity extends BaseMultiLayoutActivity {

    /** Navigates to card at given position. */
    private void navigateToCard(int position) {
        if(position >= 0 && position < mCardAdapter.getCount())
            mCardScroller.animate(position, CardScrollView.Animation.NAVIGATION);
    }

    @Override
    public void onSpeechResult(String text) {
        // can not use switch, because string needs to be declared as final
        // this way is better if we would implement localization (probably never)
        if(text.equals(getString(R.string.backward)))
        {
            mAudioManager.playSoundEffect(Sounds.DISMISSED);
            finish();
        }
        else if(text.equals(getString(R.string.left)))
        {
            int position = mCardScroller.getSelectedItemPosition();
            position--;
            navigateToCard(position);
        }
        else if(text.equals(getString(R.string.right)))
        {
            int position = mCardScroller.getSelectedItemPosition();
            position++;
            navigateToCard(position);
        }
    }
}
