package io.interact.mohamedbenarbia.benmycontacts.entry;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import io.interact.mohamedbenarbia.benmycontacts.Contacts.Contact;
import io.interact.mohamedbenarbia.benmycontacts.Contacts.ContactsDetailsFragment;
import io.interact.mohamedbenarbia.benmycontacts.Contacts.DisplayContactsFragement;
import io.interact.mohamedbenarbia.benmycontacts.Interaction.DisplayInteractions;
import io.interact.mohamedbenarbia.benmycontacts.Interaction.DisplayInteractionsFragment;
import io.interact.mohamedbenarbia.benmycontacts.LogOutAsyncTask;
import io.interact.mohamedbenarbia.benmycontacts.Login.LoginActivity;
import io.interact.mohamedbenarbia.benmycontacts.R;
import io.interact.mohamedbenarbia.benmycontacts.TabListener;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import io.interact.mohamedbenarbia.benmycontacts.services.CacheHandlerService;
import io.interact.mohamedbenarbia.benmycontacts.services.OutgoingSmsObserver;
import io.interact.mohamedbenarbia.benmycontacts.services.SmsService;


/**
 * Entry ativity represemts the main activity wich include the principle view (Coontacts)
 */

public class MainActivity extends Activity implements DisplayContactsFragement.OnFragmentInteractionListener {


    private static final String TAG_DEBUG = "MAIN ACTIVITY";

    private ActionBar actionBar ;

    AlarmManager mAlarmManager;


    /**
     * Load principle view otherwise lunch LoginActivity if user logged out.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //actionBar.setCustomView(R.layout.main_activity_action_bar_layout);
        //actionBar.setDisplayShowCustomEnabled(true);

        // Check if a token already exists. if not lunch the loginActivity.
        SharedPreferences setting = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        String token = setting.getString(String.valueOf(getText(R.string.token_key)), null) ;

        Log.d(TAG_DEBUG, "Token saved in shared pref: " + token) ;
        if (token==null) {
            Intent intent = new Intent(this,
                    LoginActivity.class);
            startActivity(intent);
        }


        // Customise action bar
         actionBar = getActionBar() ;
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(getText(R.string.app_name));


        // Activate the navigation mode of the tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        Log.d(TAG_DEBUG, "On created entered");

        ActionBar.Tab tabContacts = actionBar.newTab()
                .setIcon(R.drawable.contacts_icon_off).setTabListener(new TabListener<DisplayContactsFragement>(
                        this, (String) getText(R.string.contacts_tab_tag), DisplayContactsFragement.class)) ;

        actionBar.addTab(tabContacts);

        ActionBar.Tab tabInteractions = actionBar.newTab()
                .setIcon(R.drawable.interactions_icon_off).setTabListener(new TabListener<DisplayInteractionsFragment>(
                        this, (String) getText(R.string.interactions_tab_tag), DisplayInteractionsFragment.class)) ;
        actionBar.addTab(tabInteractions);


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
        //start the service that will launch the contentObserver watching the outgoing sms

        Intent intent = new Intent(this, SmsService.class);
        startService(intent);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void displayActivities(View view) {
        Intent intent = new Intent(this,
                DisplayInteractions.class);
        startActivity(intent);

    }


/*
    @Override
    public void onBackPressed() {

        Log.d(TAG_DEBUG,"Back button pressed") ;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        super.onBackPressed();
    }
*/

    public void logout(MenuItem menuItem) {

        LogOutAsyncTask logOutAsyncTask = new LogOutAsyncTask(this);
        logOutAsyncTask.execute();

    }

    @Override
    public void onFragmentInteraction(Contact contact) {

        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        ContactsDetailsFragment contactDetailsFragment = new ContactsDetailsFragment();
        contactDetailsFragment.setContact(contact);
        fragmentTransaction
                .replace(android.R.id.content, contactDetailsFragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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
