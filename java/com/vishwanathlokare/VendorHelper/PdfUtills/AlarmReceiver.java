package com.vishwanathlokare.VendorHelper.PdfUtills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.provider.Settings;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        MediaPlayer mediaPlayer = MediaPlayer.create(arg0, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();

    }

}
