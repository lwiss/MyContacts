package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * AsyncTask used to perform communication with the server to authenticate.
 * Created by MohamedBenArbia on 01/09/15.
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, Integer> {

    private static final String TAG_DEBUG = "SERVER RESPONSE LOGIN";


    // Define code states sent by Server
    private static final int SUCCESS_RESPONSE = 200;
    private static final int NOT_FOUND__RESPONSE = 404;
    private static final int UNAUTHORIZED__RESPONSE = 401;

    // Used if an internal error occured during  login (i.e JSON exception)
    private static final int INTERNAL_ERROR = 100;

    // Used if the user is not found
    private static final int USER_NOT_FOUND = 301;

    // Used if the pass word is incorrect
    private static final int INCORRECT_PASSWORD = 302;


    // Authentication Server URL
    private static final String AUTHENTICATION_SERVER_URL = "https://api.mycontacts.io/v2/login";

    // User not found messgae
    private static final String USER_NOT_FOUND_MESSAGE = "login.user.not_found";
    private static final String INCORRECT_PASSWORD_MESSAGE = "login.user.invalid_password";


    /**
     * Email and password used for authentication
     */
    private String email;
    private String password;

    // context for showing messages on the GUI
    private Context context;


    public LoginAsyncTask(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;

    }

    /**
     * Perform the communication with the server and return the status code.
     *
     * @param params
     * @return Status of the login operation (Success, Unauthorized, IncorrectPassword ...)
     */
    @Override
    protected Integer doInBackground(Void... params) {

        // Post data to server and get Response
        HttpResponse response = postLoginDataToServer();
        int statusLogin = getStatus(response);


        // Save the token to shared preferences if successful connection
        if(statusLogin== SUCCESS_RESPONSE) {
            String resBody = null ;
            try {
                resBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(resBody) ;
                JSONObject tokenObject = jsonResponse.getJSONObject("token") ;
                String token = tokenObject.getString("authToken") ;
                Log.d(TAG_DEBUG, "Token returned " + token );

                // Put the token in the shared preferences
                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(context.getString(R.string.token_key), token);
                editor.commit();



            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return statusLogin;
    }


    /**
     * Post the email and password to the server
     *
     * @return the http respsonse of the server. If an internal error occurs return null ;
     */
    private HttpResponse postLoginDataToServer() {
        HttpResponse response = null;

        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(AUTHENTICATION_SERVER_URL);

        JSONObject postRequestBody = new JSONObject();

        // String entity to send to the server that contains JSonObject
        StringEntity stringEntity = null;

        // Form the Json object to send to the server
        try {

            // Form the body of the postRequest
            postRequestBody.put("username", this.email);
            postRequestBody.put("password", this.password);
            postRequestBody.put("client", "Apriary");

            stringEntity = new StringEntity(postRequestBody.toString());

            postRequest.setEntity(stringEntity);
            postRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");

            response = client.execute(postRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    /**
     * Return the corresponding status depending on the received response from the server. Sould return one of the status defined in the class above.
     *
     * @param response Http repsonse received from the server
     * @return corresponding status ;
     */
    private int getStatus(HttpResponse response) {


        // Return internal error status by default (if response null or an exception occurred) ;
        int statusLogin = INTERNAL_ERROR;

        if (response != null) {
            // Get status code response
            statusLogin = response.getStatusLine().getStatusCode();

            // Log the status for debug
            Log.d(TAG_DEBUG, "status Line" + response.getStatusLine().toString());
            Log.e(TAG_DEBUG, "Replied with status code " + statusLogin); //prints the response's status code


            // Determine if it's an undefined user or a wrong password
            if (statusLogin == UNAUTHORIZED__RESPONSE) {

                String resBody = null;
                try {
                    resBody = EntityUtils.toString(response.getEntity());
                    JSONObject jsonResponse = new JSONObject(resBody) ;
                    String message = jsonResponse.getString("message") ;

                    Log.d(TAG_DEBUG, "Replied with entity " + message);

                    // Put corresponding status with respect to the received message
                    if (message.equals(USER_NOT_FOUND_MESSAGE)) statusLogin = USER_NOT_FOUND;
                    else if (message.equals(INCORRECT_PASSWORD_MESSAGE))
                        statusLogin = INCORRECT_PASSWORD;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }
        return statusLogin;
    }

    @Override
    protected void onPostExecute(Integer result) {

        Log.d(TAG_DEBUG, "argumennt in OnpostExecute" + result);

        switch (result) {
            case INTERNAL_ERROR: {
                Toast.makeText(context, context.getText(R.string.internal_error_message), Toast.LENGTH_LONG).show();
                break ;

            }
            case SUCCESS_RESPONSE: {
                Toast.makeText(context, context.getText(R.string.login_success_message), Toast.LENGTH_LONG).show();
                // In case of successful log in: dismiss login activity
                ((LoginActivity)context).finish();
                break ;

            }
            case USER_NOT_FOUND: {
                Toast.makeText(context, context.getText(R.string.user_not_found_error_message), Toast.LENGTH_LONG).show();
                break ;

            }
            case INCORRECT_PASSWORD: {
                Toast.makeText(context, context.getText(R.string.incorrect_password_error_message), Toast.LENGTH_LONG).show();
                break ;

            }
            case NOT_FOUND__RESPONSE: {
                Toast.makeText(context, context.getText(R.string.not_found_error_message), Toast.LENGTH_LONG).show();
                break ;

            }

        }



    }

}
