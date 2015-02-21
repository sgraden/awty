package com.example.steven.awty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//NEED TO FIGURE OUT HOW TO CANCEL THE EXISTING ALARM ON RECREATE. EVERYTHING ELSE WORKS!!!

public class AwtyMain extends ActionBarActivity {

    private PendingIntent pendingIntent; //Background intent for the alarm
    //private Intent alarmIntent;
    private boolean started; //Whether the alarm has be started
    private static final int INTENT_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awty_main);

        //Grab the existing alarm based on ID and check if it is already made.
//        alarmIntent = new Intent(AwtyMain.this, AlarmReceiver.class);
//        started = (PendingIntent.getBroadcast(AwtyMain.this, INTENT_ID, alarmIntent,
//                PendingIntent.FLAG_NO_CREATE) != null);
//        Log.i("hello", "" + started);
//        if (started) { //If alarm already exists
//            Log.i("hello", "Alarm is already active");
//            pendingIntent = PendingIntent.getBroadcast(AwtyMain.this, AwtyMain.INTENT_ID, alarmIntent,
//                    PendingIntent.FLAG_NO_CREATE);
//            swapButton(true); //Switch to "Stop" if alarm exists
//        }

        Button startEnd = (Button) findViewById(R.id.begin_end_button);
        startEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvMessage = (TextView) findViewById(R.id.message);     //TV message
                TextView tvPhone = (TextView) findViewById(R.id.phone_number);  //TV phone
                TextView tvFrequency = (TextView) findViewById(R.id.frequency); //TV frequency

                String msgString = tvMessage.getText().toString();          //String for msg
                String phoneNumString = tvPhone.getText().toString();       //String for phone
                String frequencyString = tvFrequency.getText().toString();  //String for frequency
                int freqInt = -1; //Integer frequency of how often Alarm should Toast
                if (frequencyString.length() > 0) { //Check if frequency length > 0
                    freqInt = Integer.parseInt(frequencyString);
                }

                boolean allow = false; // Can we start the alarm?
                if (msgString.length() > 0
                        && validatePhoneNumber(phoneNumString)
                        && freqInt > 0) {
                    allow = true;
                }

                /* Retrieve a PendingIntent that will perform a broadcast */
                Intent alarmIntent = new Intent(AwtyMain.this, AlarmReceiver.class);
                alarmIntent.putExtra("message", phoneNumString + ": " + msgString); //Intent to send to the alarm

                pendingIntent = PendingIntent.getBroadcast(AwtyMain.this, INTENT_ID,
                        alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                if (allow || started) {
                    //Starts alarm with given message and frequency
                    if (!started) {
                        start(freqInt);
                    } else { //cancels alarm based on pendingIntent
                        cancel();
                    }
                } else {
                    Toast.makeText(AwtyMain.this, "Failed: Check inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_awty_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //cancel();
    }


    public void start(int interval) {
        started = true;
        interval = interval * 1000;// * 60; //Converts min to milli
        swapButton(started);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        started = false;
        swapButton(started);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void swapButton(boolean isActive) {
        Log.i("hello", "swapped");
        Button startStop = (Button) findViewById(R.id.begin_end_button);
        if (!isActive) {
            startStop.setText(R.string.start_button_text);
        } else {
            startStop.setText(R.string.stop_button_text);
        }
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }
}
