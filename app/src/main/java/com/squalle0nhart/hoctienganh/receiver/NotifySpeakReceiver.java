package com.squalle0nhart.hoctienganh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import com.squalle0nhart.hoctienganh.service.NotificationService;

/**
 * Created by squalle0nharta on 4/4/2017.
 */

public class NotifySpeakReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ( NotificationService.sTextToSpeech != null)
        NotificationService.sTextToSpeech.speak(NotificationService.sNotifyWord, TextToSpeech.QUEUE_FLUSH, null);
    }
}