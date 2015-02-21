package com.example.steven.awty;

/**
 * Created by Steven on 2/19/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("message");

        // For our recurring task, we'll just display a message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}