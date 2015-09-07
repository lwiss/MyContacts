package io.interact.mohamedbenarbia.benmycontacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import android.widget.Toast;
import io.interact.mohamedbenarbia.benmycontacts.Util.FileLogger;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import io.interact.mohamedbenarbia.benmycontacts.Util.NetworkUtility;
import org.apache.http.HttpResponse;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by wissem on 05.09.15.
 */
public class UserInteractionsRetrieverAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {


    public int offset=0,limit=15;
    public JSONObject filters;
    Activity activity ;

    public UserInteractionsRetrieverAsyncTask(Activity a) {
        this.activity = a;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {



        //read the list of interaction from the cache
        try {
            ArrayList<UserInteraction> interactionsListCached = fromCahce2InteractionList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //check if there is a connection or not
        ConnectivityManager conMan = (ConnectivityManager) this.activity.getSystemService(this.activity.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) { // connection is available  ==> get interactions from the server then compare it to the list retrieved from the cache

            JSONObject reqBody = this.interactionRequestBody();

            // generate the headers
            HashMap<String,String> headers = new HashMap<>();
            headers.put(HTTP.CONTENT_TYPE, "application/json");
            headers.put("Accept", "application/json");
            //TODO get the auth token
            headers.put("authToken", "gfUH43trfdkjg34");

            //TODO modify the the server
            // generate the url for the login service
            String url= SharedAttributes.BASE_MOCK_URL+ SharedAttributes.INTERACTIONS_LIST_URI;
            // Post data to server and get Response

            HttpResponse resp = NetworkUtility.postMethod(url, headers, reqBody);

            try {
                JSONObject resBody = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONArray jsonInteractions = resBody.getJSONArray("data");
                //interactionsStringList= jsonArray2InteractionsList(jsonInteractions);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


        return new ArrayList<>();
    }



    @Override
    protected void onPostExecute(ArrayList<String> o) {
        super.onPostExecute(o);
        //start the list activity
        Intent i = new Intent(activity, DisplayInteractions.class);
        i.putStringArrayListExtra("interactions list",o);
        activity.startActivity(i);
    }

    /**
     *
     * @param jsonArray
     * @return
     */
    private ArrayList<String> jsonArray2InteractionsList(JSONArray jsonArray){
        ArrayList<String> listActivities= new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++) {
            try {
                JSONObject obj= (JSONObject)jsonArray.get(i);

                UserInteraction ua = new UserInteraction(obj);

                Log.e("new user activity", ua.toString());
                listActivities.add(ua.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return listActivities;

    }
    /**
     * Post the email and password to the server
     *
     * @return the http respsonse of the server. If an internal error occurs return null ;
     */
    private JSONObject interactionRequestBody() {

        JSONObject postRequestBody = new JSONObject();
        // Form the Json object to send to the server
        try {

            // Form the body of the postRequest
            postRequestBody.put("offset", this.offset);
            postRequestBody.put("limit", this.limit);
            this.filters=new JSONObject();
            this.filters.put("defaultOperator","OR");
            this.filters.put("filters", new JSONArray());
            postRequestBody.put("filters", this.filters);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postRequestBody;
    }

    /**
     *
     * @return a list of string each string correspond to a JSONObject
     */
    private ArrayList<UserInteraction> fromCahce2InteractionList() throws JSONException {
        ArrayList<UserInteraction> res=new ArrayList<>();
        ArrayList < String > l = (ArrayList < String >) FileLogger.getInstance(SharedAttributes.NAME_FILE_USER_INTERACTIONS).fetchStringList();
        Iterator<String> it = l.iterator();
        while (it.hasNext()) {
            JSONObject obj=new JSONObject(it.next());
            res.add(new UserInteraction(obj));
        }
        return res;
    }




}
