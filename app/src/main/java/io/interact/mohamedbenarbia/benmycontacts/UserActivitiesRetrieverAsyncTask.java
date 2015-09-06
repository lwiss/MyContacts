package io.interact.mohamedbenarbia.benmycontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by wissem on 05.09.15.
 */
public class UserActivitiesRetrieverAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {


    public int offset=0,limit=15;
    public JSONObject filters;
    Activity activity ;

    public UserActivitiesRetrieverAsyncTask(Activity a) {
        this.activity = a;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {


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
        ArrayList<String> activityList = null;
        try {
            JSONObject resBody = new JSONObject(EntityUtils.toString(resp.getEntity()));
            JSONArray jsonActivities = resBody.getJSONArray("data");
            activityList=jsonArray2activitiesList(jsonActivities);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activityList;
    }



    @Override
    protected void onPostExecute(ArrayList<String> o) {
        super.onPostExecute(o);
        //start the list activity
        Intent i = new Intent(activity, ActivitiesDisplay.class);
        i.putStringArrayListExtra("interactions list",o);
        activity.startActivity(i);
    }

    private ArrayList<String> jsonArray2activitiesList(JSONArray jsonArray){
        ArrayList<String> listActivities= new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++) {
            try {
                JSONObject obj= (JSONObject)jsonArray.get(i);
                String t= obj.getString("type");
                String n =((JSONObject) ((JSONArray)obj.get("contacts")).get(0)).getString("displayName");

                UserInteraction ua = new UserInteraction(t,n);
                Log.e("new user activity",ua.toString());
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
}
