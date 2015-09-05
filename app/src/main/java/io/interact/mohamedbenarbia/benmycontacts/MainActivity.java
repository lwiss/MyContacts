package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * Entry ativity represemts the main activity wich include the principle view (Coontacts)
 */

public class MainActivity extends AppCompatActivity {


    private static final String TAG_DEBUG = "MAIN ACTIVITY";

    // Shared prefrerence used to set and delete the authentication token.
    private SharedPreferences setting ;

    /**
     * Load principle view otherwise lunch LoginActivity if user logged out.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if a token already exists. if not lunch the loginActivity.
        setting = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        String token = setting.getString(String.valueOf(getText(R.string.token_key)), null) ;

        Log.d(TAG_DEBUG,"Token saved in shared pref: "+ token) ;
        if (token==null) {
            Intent intent = new Intent(this,
                    LoginActivity.class);
            startActivity(intent);
        }



    }



    public void displayActivities(View view) {
    }


    public void displayContacts(View view) {
    }


    public void logout(View view) {

        // Get shared preference preference
        SharedPreferences.Editor editor = setting.edit();

        // Delete the authentication token from shared preference
        editor.remove(getString(R.string.token_key)) ;
        editor.commit() ;

        // Lunch login activity
        Intent intent = new Intent(this,
                LoginActivity.class);
        startActivity(intent);

    }
}
