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


public class AwtyMain extends ActionBarActivity {

    private PendingIntent pendingIntent;
    private boolean started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awty_main);

        Button startEnd = (Button) findViewById(R.id.begin_end_button);
        //startEnd.setEnabled(false);
        started = false;

        startEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvMessage = (TextView) findViewById(R.id.message);
                TextView tvPhone = (TextView) findViewById(R.id.phone_number);
                TextView tvFrequency = (TextView) findViewById(R.id.frequency);

                //if (message.getText() != "" && phone.getText() != "" && frequency.getText() != "") {
                    /* Retrieve a PendingIntent that will perform a broadcast */
                    Intent alarmIntent = new Intent(AwtyMain.this, AlarmReceiver.class);
                    String message = tvMessage.getText().toString();
                    String phoneNum = tvPhone.getText().toString();

                    alarmIntent.putExtra("message", phoneNum + ": " + message);
                    pendingIntent = PendingIntent.getBroadcast(AwtyMain.this, 0, alarmIntent, 0);


                    //Starts alarm with given message and frequency
                    if (!started) {
                        start(Integer.parseInt(tvFrequency.getText().toString()));
                    } else {
                        cancel();
                    }
                //}

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


    public void start(int interval) {
        started = true;
        interval = interval * 1000 * 60; //Converts min to milli

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.i("hello", "Started: " + interval);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        started = false;
        Log.i("hello", "canceled");

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }
}
