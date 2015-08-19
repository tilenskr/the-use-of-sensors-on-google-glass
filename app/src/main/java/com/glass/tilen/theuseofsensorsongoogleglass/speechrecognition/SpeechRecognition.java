package com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

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

    private HelperQueue mHelperQueue;

    // singleton
    private static SpeechRecognition instance = null;

    public static SpeechRecognition getInstance(Context mContext) {
        if (instance == null) {
            instance = new SpeechRecognition(mContext);
        }
        return instance;
    }


    /**
     * keyword constants for mSpeechRecognizer
     **/
    public final static String KEYWORD_VERTICAL = "tutorial_up_down";
    public final static String KEYWORD_HORIZONTAL = "tutorial_left_right";
    public static final String KEYWORD_NAVIGATION_ALL = "navigation_all";
    public static final String KEYWORD_NAVIGATION_BACK = "navigation_back";
    public static final String KEYWORD_NAVIGATION_LEFT_RIGHT_BACK = "navigation_left_right_back";


    public interface SpeechRecognitionCallback {
        /**
         * This method is used when state of SpeechRecognizer changes. It is used in initialization
         * of SpeechRecognizer and for errors in SpeechRecognizer.
         *
         * @param resultText Exception message if there is one otherwise "".
         **/
        void onSpeechStateChanged(String resultText);

        void onSpeechResult(String text);
    }

    private SpeechRecognition(Context mContext) {
        this.mContext = mContext;
        mHandler = new Handler();
        mHelperQueue = new HelperQueue();

    }

    public void setSpeechRecognition(SpeechRecognitionCallback mCallback, boolean active) {
        this.mCallback = mCallback;
        this.active = active;
    }

    public void startSpeechRecognition(final String keywordSearch) {
        // try and catch - not nice, but maybe will catch some bugs
        if(!active)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null)
                        mCallback.onSpeechStateChanged("-1");
                }
            }, 1000);
            return;
        }
        mHandler.removeCallbacksAndMessages(null);
        currentKeywordSearch = keywordSearch;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSpeechRecognizer == null) {
                        mHandler.post(initializeSpeechRecognizer());
                    } else {
                        executePendingAction();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switchSearch(keywordSearch);
                                if (mCallback != null)
                                    mCallback.onSpeechStateChanged("");
                            }
                        }, 1000);
                    }
                } catch (Exception e) {
                    Global.ErrorDebug("SpeechRecogition.startSpeechRecognition(): Exception: " + e);
                    executePendingAction();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startSpeechRecognition(keywordSearch);
                        }
                    }, 1000);
                }
            }
        };
        Runnable runnable = mHelperQueue.addRunnable(run, HelperQueue.INITIALIZE);
        Global.TestDebug("SpeechRecognition.startSpeechRecognition(): runnable" +
                runnable +  ", id: " + HelperQueue.INITIALIZE);
        if (runnable != null)
            mHandler.post(runnable);
    }


    private Runnable initializeSpeechRecognizer() {
        mHandler.removeCallbacksAndMessages(null);
        Global.TestDebug("SpeechRecognition.initializeSpeechRecognizer()");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (active) {
                    if (isInitialized == false)
                        new SetUpSpeechRecognizer().execute();
                    else {
                        executePendingAction();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCallback != null)
                                    mCallback.onSpeechStateChanged("");
                            }
                        }, 1000);

                    }
                    isInitialized = true;
                } else {
                    executePendingAction();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mCallback != null)
                                mCallback.onSpeechStateChanged("-1");
                        }
                    }, 1000);
                }
            }
        };
        return runnable;
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

        nameOfFile = KEYWORD_VERTICAL + ".gram"; //my convention (name + ".gram")
        searchFile = new File(assetsDir, nameOfFile);
        mSpeechRecognizer.addKeywordSearch(KEYWORD_VERTICAL, searchFile);

        nameOfFile = KEYWORD_HORIZONTAL + ".gram"; //my convention (name + ".gram")
        searchFile = new File(assetsDir, nameOfFile);
        mSpeechRecognizer.addKeywordSearch(KEYWORD_HORIZONTAL, searchFile);

        nameOfFile = KEYWORD_NAVIGATION_ALL + ".gram"; //my convention (name + ".gram")
        searchFile = new File(assetsDir, nameOfFile);
        mSpeechRecognizer.addKeywordSearch(KEYWORD_NAVIGATION_ALL, searchFile);

        nameOfFile = KEYWORD_NAVIGATION_BACK + ".gram"; //my convention (name + ".gram")
        searchFile = new File(assetsDir, nameOfFile);
        mSpeechRecognizer.addKeywordSearch(KEYWORD_NAVIGATION_BACK, searchFile);

        nameOfFile = KEYWORD_NAVIGATION_LEFT_RIGHT_BACK + ".gram"; //my convention (name + ".gram")
        searchFile = new File(assetsDir, nameOfFile);
        mSpeechRecognizer.addKeywordSearch(KEYWORD_NAVIGATION_LEFT_RIGHT_BACK, searchFile);
    }

    public String setActive() {
        active = !active;
        mHandler.removeCallbacksAndMessages(null);
        String textToDisplay;
        if (active) {
            Runnable runnable = mHelperQueue.addRunnable(initializeSpeechRecognizer(), HelperQueue.INITIALIZE);
            Global.TestDebug("SpeechRecognition.setActive(): runnable" +
                    runnable +  ", id: " + HelperQueue.INITIALIZE);
            if (runnable != null)
                mHandler.post(runnable);
            textToDisplay = "";
        } else {
            shutdownSpeechRecognition();
            textToDisplay = "-1";
        }
        return textToDisplay;
    }

    public void switchSearch(final String searchName) {
        //TODO set thread on, off for debugging purposes
        //new Thread(new Runnable() {
        //  @Override
        // public void run() {
        currentKeywordSearch = searchName;
        Global.SpeechDebug("SpeechRecognition.switchSearch(): searchName: " + searchName);
        mSpeechRecognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        mSpeechRecognizer.startListening(currentKeywordSearch);
        //  }
        //}).start();

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
            if (mCallback != null)
                mCallback.onSpeechResult(text);
        }
    }

    @Override
    public void onError(Exception e) {
        String exceptionMessage = e.getMessage();
        Global.SpeechDebug("SpeechRecognition.onError(): Exception: " + exceptionMessage);
        if (mCallback != null)
            mCallback.onSpeechStateChanged(exceptionMessage);
    }

    @Override
    public void onTimeout() {
        Global.SpeechDebug("SpeechRecognition.onTimeout()");
        switchSearch(currentKeywordSearch);
    }

    public void cancelCallback()
    {
        mCallback = null;
    }


    public void shutdownSpeechRecognition() {
        // wait for 5 second, if user really quit app or just went to another activity
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                // there is error if we quit app faster than SpeechRecognizer takes to initialize
                                try {
                                    //if (this.isInitialized == true) {
                                    if (mSpeechRecognizer != null) {
                                        mSpeechRecognizer.cancel();
                                        mSpeechRecognizer.shutdown();
                                        mSpeechRecognizer = null;
                                        isInitialized = false;
                                    }
                                    //}
                                } catch (Exception e) {
                                    Global.ErrorDebug("SpeechRecognition.shutdownSpeechRecognition(): Exception: " + e.getMessage());
                                    // if we quit app faster than it takes SpeechRecognizer to initialize,
                                    // there would be error on initialization, so we use Handler
                                    executePendingAction();
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            shutdownSpeechRecognition();
                                        }
                                    }, 500);
                                } finally {
                                    Global.SpeechDebug("SpeechRecognition.shutdownSpeechRecognition(): Shutdown successfully");
                                    executePendingAction();
                                }
                            }
                        };
                        Runnable mRunnable = mHelperQueue.addRunnable(run, HelperQueue.SHUTDOWN);
                        Global.TestDebug("SpeechRecognition.shutdownSpeechRecognition(): runnable" +
                                mRunnable +  ", id: " + HelperQueue.SHUTDOWN);
                        if (mRunnable != null)
                            mHandler.post(mRunnable);
                    }
                }, 5000);
    }

    private class SetUpSpeechRecognizer extends AsyncTask<Void, Void, Exception> {
        @Override
        protected Exception doInBackground(Void... params) {
            Global.SpeechDebug("SpeechRecognition.doInBackground()");
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
            Global.SpeechDebug("SpeechRecognition.onPostExecute(): active: " +active + ", result: " + result
                    + ", Callback: " + mCallback);
            String resultText = "";
            if (!active) {
                if (mCallback != null)
                    mCallback.onSpeechStateChanged("-1");
                executePendingAction();
                shutdownSpeechRecognition();
                return;
            }
            if (result != null) {  // if there is exception
                resultText = result.getMessage();
                Global.SpeechDebug("SpeechRecognition.SetUpSpeechRecognizer.onPostExecute(): Exception: " + resultText);
                if (mCallback != null)
                    mCallback.onSpeechStateChanged(resultText);
                else
                    shutdownSpeechRecognition();
            } else {
                if (mCallback != null) {
                    mCallback.onSpeechStateChanged(resultText);
                    switchSearch(currentKeywordSearch);
                }
                else
                    shutdownSpeechRecognition();
            }
            executePendingAction();
        }
    }

    private void executePendingAction() {
        Runnable runnable = mHelperQueue.poll();
        Global.TestDebug("SpeechRecognition.executePendingAction(): runnable: " +
                runnable);
        if (runnable != null)
            mHandler.post(runnable);
    }
}
