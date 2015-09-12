package io.interact.mohamedbenarbia.benmycontacts.entry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.interact.mohamedbenarbia.benmycontacts.Login.LoginActivity;
import io.interact.mohamedbenarbia.benmycontacts.R;

public class EntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        SharedPreferences setting = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        String token = setting.getString(String.valueOf(getText(R.string.token_key)), null) ;

        Log.d("Entry Activity", "Token saved in shared pref: " + token) ;
        if (token==null) {
            Intent intent = new Intent(this,
                    LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,
                    MainActivity.class);
            startActivity(intent);
        }

        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry, menu);
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
}
