package io.interact.mohamedbenarbia.benmycontacts.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;
import io.interact.mohamedbenarbia.benmycontacts.Util.FileLogger;
import io.interact.mohamedbenarbia.benmycontacts.Util.NetworkUtility;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

        Log.e(LOG_TAG, "received a new interaction ");

        String data = intent.getStringExtra("interaction");
        try {
            UserInteraction interaction = new UserInteraction(new JSONObject(data));

            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) { // connection is available

                //TODO addInteractionToServer to be moved to this class
                HttpResponse resp= addInteractionToServer(interaction);
                if (resp!=null && resp.getStatusLine().getStatusCode()==SharedAttributes.CREATED_RESPONSE){ // if the interaction push has succeeded
                    // get the response of the server and construct and add the interaction to the cache (the cache that is supposed to e in sync with the server)
                    try {
                        JSONObject respBody = new JSONObject(EntityUtils.toString(resp.getEntity()));
                        UserInteraction uiFromServer = new UserInteraction(respBody.getJSONObject("entity"));
                        Log.e(LOG_TAG,"interaction answered back by the server : "+uiFromServer);
                        //write this interaction to the cache
                        FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS).logLine(uiFromServer.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else  { // if the interaction push has failed
                    Log.e(LOG_TAG,"error occurred when posting to server");
                    //write it to the cache
                    FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS_TRIGGERED).logLine(interaction.toString());
                    Log.e(LOG_TAG, "interaction logged to cache ");

                }
            } else { //if there is no connectivity simply log the interaction
                Log.e(LOG_TAG,"error occurred when posting to server");
                //write it to the cache
                FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS_TRIGGERED).logLine(interaction.toString());
                Log.e(LOG_TAG, "interaction logged to cache ");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HttpResponse addInteractionToServer(UserInteraction interac) {

        JSONObject reqBody = interac.preparePostBody();

        // generate the headers
        HashMap<String,String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, "application/json");
        headers.put("Accept", "application/json");
        //TODO get the auth token
        headers.put("triggerToken", "gfUH43trfdkjg34");

        //TODO modify the the server
        // generate the url for the login service
        String url= SharedAttributes.BASE_MOCK_URL+ SharedAttributes.INTERACTIONS_URI;
        // Post data to server and get Response

        HttpResponse resp = NetworkUtility.postMethod(url, headers, reqBody);

        return resp;
    }

}
