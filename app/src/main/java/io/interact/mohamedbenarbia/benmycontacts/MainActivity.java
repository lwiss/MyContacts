package io.interact.mohamedbenarbia.benmycontacts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import io.interact.mohamedbenarbia.benmycontacts.services.CacheHandlerService;
import io.interact.mohamedbenarbia.benmycontacts.services.NewInteractionHandlerService;
import io.interact.mohamedbenarbia.benmycontacts.services.OutgoingSmsObserver;
import io.interact.mohamedbenarbia.benmycontacts.services.SmsInteractionListener;


/**
 * Entry ativity represemts the main activity wich include the principle view (Coontacts)
 */

public class MainActivity extends AppCompatActivity {


    private static final String TAG_DEBUG = "MAIN ACTIVITY";
    AlarmManager mAlarmManager;

    int mycounter=100;

    /**
     * Load principle view otherwise lunch LoginActivity if user logged out.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if a token already exists. if not lunch the loginActivity.
        SharedPreferences setting = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        String token = setting.getString(String.valueOf(getText(R.string.token_key)), null) ;

        Log.d(TAG_DEBUG,"Token saved in shared pref: "+ token) ;
        if (token==null) {
            Intent intent = new Intent(this,
                    LoginActivity.class);
            startActivity(intent);
        }

        //register the CacheHandler to wake up periodically in the background
        if (hasAlarmService()) {
            Intent serviceIntent = new Intent(this, CacheHandlerService.class);
            PendingIntent pIntent = PendingIntent.getService(this,
                    456,
                    serviceIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + SharedAttributes.TIME_MILIS_SERVICE_STARTUP_DELAY,
                    SharedAttributes.TIME_MILIS_SERVICE_WAKEUP_INTERVAL,
                    pIntent);
        }
        //register an observer to listen for outgoing sms
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms/out"),true, new OutgoingSmsObserver(new Handler(),this));

    }
   // String interactionID, String t, String n, String timeStamp, String direction, String from, String to


    public void dummyinter(View view) {
        UserInteraction inter = new UserInteraction("wiss", "call","wissem", "1111111111", "OUTBOUND", ""+mycounter, ""+mycounter);
        Intent mServiceIntent = new Intent(this, NewInteractionHandlerService.class);
        mServiceIntent.putExtra("interaction",inter.toString());
        this.startService(mServiceIntent);
        mycounter++;
    }


    public void displayActivities(View view) {
        Intent intent = new Intent(this,
                DisplayInteractions.class);
        startActivity(intent);

    }


    public void displayContacts(View view) {
        Intent intent = new Intent(this,
                DisplayContactsActivity.class);
        startActivity(intent);

    }


    public void logout(View view) {

        LogOutAsyncTask logOutAsyncTask = new LogOutAsyncTask(this);
        logOutAsyncTask.execute();

    }

    /**
     * Verifies {@link AlarmManager} is initialized
     *
     * @return {@code true}, if {@link AlarmManager} is properly initialized, {@code false} otherwise
     */
    private boolean hasAlarmService() {
        if (null == mAlarmManager ) {
            mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        }

        if (null == mAlarmManager) {
            Log.e(TAG_DEBUG, "Failed to get system alarm service");
        }

        return null != mAlarmManager;
    }
}
