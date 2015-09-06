package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by MohamedBenArbia on 05/09/15.
 */


public class FetchContactsAsyncTask extends AsyncTask<Void, Void, Void> {



    private static String URL = "https://api.mycontacts.io/v2/contacts" ;




    public FetchContactsAsyncTask(Context context) {

        this.context = context;



    }


    @Override
    protected Void doInBackground(Void... params) {

        HttpResponse response = fetchContacts();

        Log.d("FETCH CONTACTS", "Response status" + response.getStatusLine().getStatusCode()) ;

        try {
            String resBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonResponse = new JSONObject(resBody) ;
            JSONArray jsonarray = jsonResponse.getJSONArray("data") ;

            jsonarray.length();

            for (int i=0 ; i <jsonarray.length()-1; i ++) {
                JSONObject contactJsonObject = jsonarray.getJSONObject(i) ;
                String firstName = contactJsonObject.getString("firstName") ;
                String lastName = contactJsonObject.getString("lastName") ;

                Object email = contactJsonObject.get("emails") ;


                Object phoneArray = contactJsonObject.get("phoneNumbers") ;



                Log.d("FETCH CONTACTS", "Contact number: " + i + " first name: "+ firstName + " last name: "+lastName+" emails: "+ email+ " phone numbers: "+phoneArray) ;




            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private Context context ;




    private HttpResponse fetchContacts() {
        HttpResponse response = null;

        HttpClient client = HttpClientFactory.getInstance() ;
        HttpGet getRequest = new HttpGet(URL);


       // JSONObject postRequestBody = new JSONObject();

        // String entity to send to the server that contains JSonObject
        //StringEntity stringEntity = null;

        // Form the Json object to send to the server
        try {
            SharedPreferences  setting = this.context.getSharedPreferences(this.context.getString(R.string.preference_file_key), Context.MODE_PRIVATE) ;
            String token = setting.getString(String.valueOf(this.context.getText(R.string.token_key)), null);

            Log.d("FETCH contacts", "Token used: " + token) ;


            // Form the body of the postRequest
            //postRequestBody.put("authToken", token);


           // stringEntity = new StringEntity(postRequestBody.toString());

           // postRequest.setEntity(stringEntity);
            getRequest.setHeader(HTTP.CONTENT_TYPE, "application/json");
            getRequest.setHeader("Accept", "application/json");
            getRequest.setHeader("authToken", token);


            response = client.execute(getRequest);


        }// catch (JSONException e) {
           // e.printStackTrace();
    //    }
    catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }




}
