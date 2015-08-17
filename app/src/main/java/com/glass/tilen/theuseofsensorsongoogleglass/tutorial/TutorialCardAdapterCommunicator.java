package com.glass.tilen.theuseofsensorsongoogleglass.tutorial;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.interface_.BaseCardCommunicator;

/**
 * Created by Tilen on 17.8.2015.
 */
public interface TutorialCardAdapterCommunicator extends BaseCardCommunicator {
    TutorialCardAdapter.TutorialCard getItem(int i);
    void insertCardWithoutAnimation(TutorialCardAdapter.TutorialCard mTutorialCard);
}
