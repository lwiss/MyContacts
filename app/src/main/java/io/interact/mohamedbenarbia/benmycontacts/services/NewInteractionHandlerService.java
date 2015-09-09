package io.interact.mohamedbenarbia.benmycontacts.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;
import io.interact.mohamedbenarbia.benmycontacts.Util.FileLogger;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * this service when called tries to push the interaction to the server waits for the server response
 * If the operation has failed then it writes the interaction to the cache
 * Else it dies
 */
public class NewInteractionHandlerService extends IntentService {


    private final String LOG_TAG= NewInteractionHandlerService.class.getName();
    Context context;

    public NewInteractionHandlerService() {
        super(NewInteractionHandlerService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    /**
     *
     * @param intent holds the interaction in a JSON string format (JSONObject.toString())
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(LOG_TAG,"received a new interaction ");

        String data = intent.getStringExtra("interaction");
        try {
            UserInteraction interaction = new UserInteraction(new JSONObject(data));

            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) { // connection is available

                HttpResponse resp=interaction.addInteractionToServer();
                if (resp!=null && resp.getStatusLine().getStatusCode()!=SharedAttributes.CREATED_RESPONSE){ // if the interaction has failed
                    Log.e(LOG_TAG,"error occured when posting to server");
                    //write it to the cache
                    FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS_TRIGGERED).logLine(interaction.toString());
                    Log.e(LOG_TAG, "interaction logged to cache ");

                }
            } else { //simply log the interaction

                Log.e(LOG_TAG,"error occured when posting to server");
                //write it to the cache
                FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS_TRIGGERED).logLine(interaction.toString());
                Log.e(LOG_TAG, "interaction logged to cache ");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
