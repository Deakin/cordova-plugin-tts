package au.com.deakin.tts;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;

/*
Cordova Text-to-Speech Plugin
https://github.com/Deakin/cordova-plugin-tts

by VILIC VANE
https://github.com/vilic

Forked from:
https://github.com/kanayo/cordova-plugin-tts
https://github.com/chemerisuk/cordova-plugin-tts

MIT License
*/

public class TTS extends CordovaPlugin implements OnInitListener {

    public static final String ERR_INVALID_OPTIONS = "ERR_INVALID_OPTIONS";
    public static final String ERR_NOT_INITIALIZED = "ERR_NOT_INITIALIZED";
    public static final String ERR_ERROR_INITIALIZING = "ERR_ERROR_INITIALIZING";
    public static final String ERR_UNKNOWN = "ERR_UNKNOWN";

    boolean ttsInitialized = false;
    TextToSpeech tts = null;

    @Override
    public void initialize(CordovaInterface cordova, final CordovaWebView webView) {
        tts = new TextToSpeech(cordova.getActivity().getApplicationContext(), this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                // do nothing
            }

            @Override
            public void onDone(String callbackId) {
                if (!callbackId.equals("")) {
                    CallbackContext context = new CallbackContext(callbackId, webView);
                    context.success();
                }
            }

            @Override
            public void onError(String callbackId) {
                if (!callbackId.equals("")) {
                    CallbackContext context = new CallbackContext(callbackId, webView);
                    context.error(ERR_UNKNOWN);
                }
            }
        });
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {

      if (tts == null) {
        callbackContext.error(ERR_ERROR_INITIALIZING);
        return true;
      }

      if (!ttsInitialized) {
        callbackContext.error(ERR_NOT_INITIALIZED);
        return true;
      }

      if (action.equals("speak")) {
          speak(args, callbackContext);
      } else if (action.equals("stop")) {
          stop(args, callbackContext);
      } else {
          return false;
      }

      return true;
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            tts = null;
        } else {
            // warm up the tts engine with an empty string
            tts.setLanguage(new Locale("en", "US"));

            HashMap<String, String> ttsParams = new HashMap<String, String>();
            ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

            tts.speak("", TextToSpeech.QUEUE_FLUSH, ttsParams);

            ttsInitialized = true;
        }
    }

    private void stop(JSONArray args, CallbackContext callbackContext)
      throws JSONException, NullPointerException {
        tts.stop();
    }
    
    private void speak(JSONArray args, CallbackContext callbackContext)
            throws JSONException, NullPointerException {
        JSONObject params = args.getJSONObject(0);

        if (params == null) {
            callbackContext.error(ERR_INVALID_OPTIONS);
            return;
        }

        String text;
        String locale;
        double rate;
        long preDelay;
        long postDelay;

        if (params.isNull("text")) {
            callbackContext.error(ERR_INVALID_OPTIONS);
            return;
        } else {
            text = params.getString("text");
        }

        if (params.isNull("locale")) {
            locale = "en-US";
        } else {
            locale = params.getString("locale");
        }

        if (params.isNull("rate")) {
            rate = 1.0;
        } else {
            rate = params.getDouble("rate");
        }

        if (params.isNull("preDelay")) {
            preDelay = 0;
        } else {
            preDelay = params.getLong("preDelay");
        }

        if (params.isNull("postDelay")) {
            postDelay = 0;
        } else {
            postDelay = params.getLong("postDelay");
        }

        String[] localeArgs = locale.split("-");
        tts.setLanguage(new Locale(localeArgs[0], localeArgs[1]));
        tts.setSpeechRate((float) rate);

        HashMap<String, String> ttsParams = new HashMap<String, String>();
        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, callbackContext.getCallbackId());

        if( preDelay > 0 ) {
          tts.playSilence(preDelay, TextToSpeech.QUEUE_ADD, ttsParams);
        }

        tts.speak(text, TextToSpeech.QUEUE_ADD, ttsParams );
        
        if( postDelay > 0 ) {
        	tts.playSilence(postDelay, TextToSpeech.QUEUE_ADD, ttsParams);
        }


    }

}