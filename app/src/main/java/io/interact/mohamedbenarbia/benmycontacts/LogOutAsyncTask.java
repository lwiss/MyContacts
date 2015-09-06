package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.search.SearchAuth;
import io.interact.mohamedbenarbia.benmycontacts.Util.ApplicationGlobals;
import io.interact.mohamedbenarbia.benmycontacts.Util.NetworkUtilities;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Async task that performs log out.
 * Created by MohamedBenArbia on 05/09/15.
 */


public class LogOutAsyncTask extends AsyncTask<Void, Void, Integer> {

    // Tag used for debugging.
    private static final String TAG_DEBUG = "SERVER RESPONSE LOG OUT";


    // Used to contain main activity context to lunch login activity and display a progress dialog for logging out!
    private Context context;

    // Shared preference used to retrieve nad delete authentication token.
    private SharedPreferences setting;


    /**
     *  Progress dialog to show while logging out
     */
    private ProgressDialog progressDialog ;


    public LogOutAsyncTask(Context context) {
        this.context = context;
        setting = this.context.getSharedPreferences(this.context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this.context);


    }



    /**
     * Show a progress dialog with a spinner while logging out
     */
    @Override
    protected void onPreExecute () {

        progressDialog.setMessage(context.getText(R.string.logging_out_message_spinner));
        progressDialog.show();

    }


    /**
     * Perform communication for the server for logging out and return the status code.
     *
     * @param params
     * @return
     */
    @Override
    protected Integer doInBackground(Void... params) {

        // Initialize status code to return
        int status = ApplicationGlobals.INTERNAL_ERROR ;

        //generate the request body
        JSONObject reqBody = this.logoutBody();

        // generate the headers
        HashMap<String,String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, "application/json");

        // generate the url for the login service
        String url= ApplicationGlobals.BASE_URL+ApplicationGlobals.LOGOUT_URI;

        // Post data to server and get Response
        HttpResponse response = NetworkUtilities.postMethod(url, headers, reqBody);

        if(response!= null) {
            status = response.getStatusLine().getStatusCode() ;
            Log.d(TAG_DEBUG, "Returned status code: "+ status ) ;
        }

        return status;
    }


    /**
     * communicate with the server for logging out!
     *
     * @return
     */
    private JSONObject logoutBody() {
        JSONObject rBody = new JSONObject();
        String token = setting.getString(String.valueOf(this.context.getText(R.string.token_key)), null);
        try {
            rBody.put("authToken", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rBody;

    }


    @Override
    protected void onPostExecute(Integer result) {

        Log.d(TAG_DEBUG, "Server responds with result" + result) ;

        // Dismiss progress dialog
        progressDialog.dismiss();


        if(result == ApplicationGlobals.NO_CONTENT) {
            Toast.makeText(this.context,this.context.getText(R.string.logging_out_message),Toast.LENGTH_SHORT).show(); ;
        }

        // Get shared preference preference editor
        SharedPreferences.Editor editor = setting.edit();

        // Delete the authentication token from shared preference
        editor.remove(this.context.getString(R.string.token_key)) ;
        editor.commit() ;

        // Lunch login activity
        Intent intent = new Intent(this.context,
                LoginActivity.class);
        this.context.startActivity(intent);

    }
}