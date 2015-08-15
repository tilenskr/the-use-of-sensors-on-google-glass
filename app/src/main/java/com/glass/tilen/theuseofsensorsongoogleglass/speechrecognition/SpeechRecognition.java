package com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Tilen on 25.7.2015.
 */
public class SpeechRecognition implements RecognitionListener {
    private Context mContext;
    private SpeechRecognizer mSpeechRecognizer;
    private SpeechRecognitionCallback mCallback;
    private String currentKeywordSearch;
    private boolean isInitialized = false;
    private Handler mHandler = null;
    private boolean active;

    // singleton
    private static SpeechRecognition instance = null;
    public static SpeechRecognition getInstance(Context mContext) {
        if(instance == null) {
            instance = new SpeechRecognition(mContext);
        }
        return instance;
    }


    /**
     * keyword constants for mSpeechRecognizer
     **/
    public static final String KEYWORD_NAVIGATION_ALL = "navigation_all";

    public interface SpeechRecognitionCallback {
        /**
         * This method is used when state of SpeechRecognizer changes. It is used in initialization
         * of SpeechRecognizer and for errors in SpeechRecognizer.
         * @param resultText Exception message if there is one otherwise "".
         **/
        void onSpeechStateChanged(String resultText);
        void onSpeechResult(String text);
    }

    private SpeechRecognition(Context mContext) {
        this.mContext = mContext;
        mHandler = new Handler();

    }

    public void setSpeechRecognition(SpeechRecognitionCallback mCallback, boolean active)
    {
        this.mCallback = mCallback;
        this.active = active;
    }
    
    public void startSpeechRecognition(final String keywordSearch) {
        // try and catch - not nice, but maybe will catch some bugs
        try {
            mHandler.removeCallbacksAndMessages(null);
            this.currentKeywordSearch = keywordSearch;
            if (mSpeechRecognizer == null)
                initializeSpeechRecognizer();
            else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switchSearch(keywordSearch);
                        if (mCallback != null)
                            mCallback.onSpeechStateChanged("");
                    }
                }, 1000);
            }
        }
        catch(Exception e)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSpeechRecognition(keywordSearch);
                }
            }, 1000);
        }
    }

    private void initializeSpeechRecognizer()
    {
        if(active) {
            if (this.isInitialized == false)
                new SetUpSpeechRecognizer().execute();
            this.isInitialized = true;
        }
        else
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mCallback != null)
                        mCallback.onSpeechStateChanged(mContext.getString(R.string.speak_disabled));
                }
            }, 1000);
        }


    }
    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        mSpeechRecognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold((float) 1e-40)
                .getRecognizer();
        mSpeechRecognizer.addListener(this);
        File searchFile;
        String nameOfFile;
            nameOfFile = KEYWORD_NAVIGATION_ALL + ".gram"; //my convention (name + ".gram")
            searchFile = new File(assetsDir, nameOfFile);
            mSpeechRecognizer.addKeywordSearch(KEYWORD_NAVIGATION_ALL, searchFile);


    }

    public String setActive()
    {
        active = !active;
        String textToDisplay;
        if(active) {
            initializeSpeechRecognizer();
            textToDisplay = "";
        }
        else {
            shutdownSpeechRecognition();
            textToDisplay = mContext.getString(R.string.speak_disabled);
        }
        return textToDisplay;
    }

    public void switchSearch(final String searchName) {
        //TODO set thread on, off for debugging purposes
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentKeywordSearch = searchName;
                Global.SpeechDebug("SpeechRecognition.switchSearch(): searchName: " + searchName);
                mSpeechRecognizer.stop();
                // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
                mSpeechRecognizer.startListening(currentKeywordSearch);
            }
        }).start();

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
        Global.SpeechDebug("SpeechRecognition.onEndOfSpeech()");
        switchSearch(currentKeywordSearch);
    }

    @Override
    public void onPartialResult(final Hypothesis hypothesis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (hypothesis != null) {
                    String text = hypothesis.getHypstr();
                    text = text.trim();
                    Global.SpeechDebug("SpeechRecognition.onPartialResult(): Speeched Text: " + text);
                }
            }
        }).start();
        }


    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            text = text.trim();
            Global.SpeechDebug("SpeechRecognition.onResult(): Speeched Text: " + text);
            if(mCallback != null)
                mCallback.onSpeechResult(text);
        }
    }

    @Override
    public void onError(Exception e) {
        String exceptionMessage = e.getMessage();
        Global.SpeechDebug("SpeechRecognition.onError(): Exception: " + exceptionMessage);
        if(mCallback != null)
            mCallback.onSpeechStateChanged(exceptionMessage);
    }

    @Override
    public void onTimeout() {
        Global.SpeechDebug("SpeechRecognition.onTimeout()");
        switchSearch(currentKeywordSearch);
    }


    public void shutdownSpeechRecognition()
    {
        mCallback = null;
        // wait for 5 second, if user really quit app or just went to another activity
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        // there is error if we quit app faster than SpeechRecognizer takes to initialize
                        try {
                            //if (this.isInitialized == true) {
                            if(mSpeechRecognizer != null) {
                                mSpeechRecognizer.cancel();
                                mSpeechRecognizer.shutdown();
                                mSpeechRecognizer = null;
                                isInitialized = false;
                            }
                            //}
                        }
                        catch (Exception e)
                        {
                            Global.ErrorDebug("SpeechRecognition.shutdownSpeechRecognition(): Exception: " + e.getMessage());
                            // if we quit app faster than it takes SpeechRecognizer to initialize,
                            // there would be error on initialization, so we use Handler
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    shutdownSpeechRecognition();
                                }
                            }, 500);
                        }
                        finally {
                            Global.SpeechDebug("SpeechRecognition.shutdownSpeechRecognition(): Shutdown successfully");
                        }
                    }
                }, 5000);


    }

    private class SetUpSpeechRecognizer extends AsyncTask<Void, Void, Exception> {
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(mContext);
                File assetDir = assets.syncAssets();
                setupRecognizer(assetDir);
            } catch (IOException e) {
                Global.ErrorDebug("SpeechRecognition.SetUpSpeechRecognizer.doInBackground(): Exception: " + e.getMessage());
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            String resultText = "";
            if(!active)
            {
                    if(mCallback!= null)
                mCallback.onSpeechStateChanged(mContext.getString(R.string.speak_disabled));
                shutdownSpeechRecognition();
                return;
            }
            if (result != null) {  // if there is exception
                resultText = result.getMessage();
                Global.SpeechDebug("SpeechRecognition.SetUpSpeechRecognizer.onPostExecute(): Exception: " + resultText);
                if(mCallback != null)
                    mCallback.onSpeechStateChanged(resultText);
                else
                    shutdownSpeechRecognition();
            } else {
                Global.SpeechDebug("SpeechRecognition.SetUpSpeechRecognizer.onPostExecute(): Successful initialization");
                if(mCallback != null)
                    mCallback.onSpeechStateChanged(resultText);
                else
                    shutdownSpeechRecognition();
                switchSearch(currentKeywordSearch);
            }
        }
    }
}
