package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Async task that performs log out.
 * Created by MohamedBenArbia on 05/09/15.
 */


public class LogOutAsyncTask extends AsyncTask<Void, Void, Integer> {

    // Tag used for debugging.
    private static final String TAG_DEBUG = "SERVER RESPONSE LOG OUT";


    // Log out  Server URL.
    private static final String LogOut_SERVER_URL = "https://api.mycontacts.io/v2/logout";

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
        int status = StatusCodes.INTERNAL_ERROR ;

        HttpResponse response = logout();


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
    private HttpResponse logout() {
        HttpResponse response = null;


        HttpClient client = HttpClientFactory.getInstance();
        HttpPost postRequest = new HttpPost(LogOut_SERVER_URL);

        JSONObject postRequestBody = new JSONObject();

        // String entity to send to the server that contains JSonObject
        StringEntity stringEntity = null;

        // Form the Json object to send to the server
        try {

            // Retreive token form shared preferences
            String token = setting.getString(String.valueOf(this.context.getText(R.string.token_key)), null);

            if (token != null) {
                // Form the body of the postRequest
                postRequestBody.put("authToken", token);


                stringEntity = new StringEntity(postRequestBody.toString());

                // Set the entity to JSON
                postRequest.setEntity(stringEntity);
                postRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");

                response = client.execute(postRequest);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;

    }


    @Override
    protected void onPostExecute(Integer result) {

        Log.d(TAG_DEBUG, "Server responds with result" + result) ;

        // Dismiss progress dialog
        progressDialog.dismiss();


        if(result == StatusCodes.NO_CONTENT) {
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
