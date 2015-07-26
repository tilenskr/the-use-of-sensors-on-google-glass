package com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition;

import android.content.Context;
import android.os.AsyncTask;

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
    private String[] keywordSearches;
    private String currentKeywordSearch;

    public interface SpeechRecognitionCallback {
        void onSpeechResult(String text);
        /** exception if there is one or "" **/
        void onSpeechInitialized(String resultText);
    }

    public SpeechRecognition(Context mContext, SpeechRecognitionCallback mCallback, String... keywordSearches) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.keywordSearches = keywordSearches;
        this.currentKeywordSearch = keywordSearches[0];// !priority - always take the first one that is named in constructor
        new SetUpSpeechRecognizer().execute();
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
        for(String keywordSearch : keywordSearches)
        {
            nameOfFile = keywordSearch + ".gram"; //my convention (name + ".gram")
            searchFile = new File(assetsDir, nameOfFile);
            mSpeechRecognizer.addKeywordSearch(keywordSearch, searchFile);
        }

    }


    public void switchSearch(String searchName) {
        currentKeywordSearch = searchName;
        Global.SpeechDebug("SpeechRecognition.switchSearch(): searchName: " + searchName);
        mSpeechRecognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        mSpeechRecognizer.startListening(currentKeywordSearch);
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
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Global.SpeechDebug("SpeechRecognition.onPartialResult(): Speeched Text: " + text);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Global.SpeechDebug("SpeechRecognition.onResult(): Speeched Text: " + text);
            mCallback.onSpeechResult(text);
        }
    }

    @Override
    public void onError(Exception e) {
        Global.SpeechDebug("SpeechRecognition.onError(): Exception: " + e.getMessage());
    }

    @Override
    public void onTimeout() {
        Global.SpeechDebug("SpeechRecognition.onTimeout()");
        switchSearch(currentKeywordSearch);
    }

    public void shutdownSpeechRecognition()
    {
        mSpeechRecognizer.cancel();
        mSpeechRecognizer.shutdown();
    }

    private class SetUpSpeechRecognizer extends AsyncTask<Void, Void, Exception> {
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(mContext);
                File assetDir = assets.syncAssets();
                setupRecognizer(assetDir);
            } catch (IOException e) {
                Global.SpeechDebug("SpeechRecognition.SetUpSpeechRecognizer.doInBackground(): Exception: " + e.getMessage());
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            String resultText = "";
            if (result != null) {  // if there is exception
                resultText = result.getMessage();
                Global.SpeechDebug("SpeechRecognition.SetUpSpeechRecognizer.onPostExecute(): Exception: " + resultText);
                mCallback.onSpeechInitialized(resultText);
            } else {
                mCallback.onSpeechInitialized(resultText);
                switchSearch(currentKeywordSearch);
            }
        }
    }
}
