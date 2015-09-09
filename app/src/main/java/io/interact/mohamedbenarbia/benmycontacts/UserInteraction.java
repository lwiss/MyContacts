package io.interact.mohamedbenarbia.benmycontacts;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import io.interact.mohamedbenarbia.benmycontacts.Util.NetworkUtility;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wissem on 05.09.15.
 */
public class UserInteraction implements Comparable<UserInteraction>{
    /**
     * unique id of the interaction
     */
    private String id;
    /**
     * name of the contact that was involved in this interaction
     */
    private String  contactName;
    /**
     * type of the interaction
     */
    private InteractionType type;
    /**
     * time stamp referring to the date at which the interaction was established
     */
    private long created;
    /**
     * specifies if the interaction was issued or was received
     */
    private Direction direction;
    /**
     * Specifies the end status of the interaction
     */
    private InteractionStatus status;

    /**
     * specifies email address/phone number involved in the interaction
     */
    private String from, to;



    public UserInteraction(JSONObject obj) throws JSONException {
        //get the id of the interaction
        String interactionID=obj.getString("id");
        //get the type of the interaction
        String t= obj.getString("type");
        //get the contactName (i.e the person/company with whom the interaction was established)
        String n =((JSONObject) ((JSONArray)obj.get("contacts")).get(0)).getString("displayName");
        //get the creation date of the interaction
        Long ts = obj.getLong("created");
        //get the direction of the interaction
        String direc = obj.getString("direction");
        String from = obj.getString("from");
        String to = obj.getString("to");

        this.id=interactionID;
        this.type=InteractionType.valueOf(t);
        this.contactName=n;
        this.created=ts;
        this.direction=Direction.valueOf(direc);
        this.from=from;
        this.to=to;

    }



    public UserInteraction(String interactionID, String t, String n, String timeStamp, String direction, String from, String to){
        this.id=interactionID;
        this.type=InteractionType.valueOf(t);
        this.contactName=n;
        this.created=Long.parseLong(timeStamp);
        this.direction=Direction.valueOf(direction);
        this.from=from;
        this.to=to;
    }

    public String name() {
        return contactName;
    }

    public InteractionType getType() {
        return type;
    }

    public String toString() {

        JSONObject obj=new JSONObject();
        try {
            obj.put("id",id);
            obj.put("type",type);
            obj.put("created",created);
            obj.put("contacts",(new JSONArray()).put(new JSONObject().put("displayName",contactName)));
            obj.put("direction",direction);
            obj.put("from",from);
            obj.put("to",to);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * prepares the body the the post request that will add this interaction to the server
     * @return
     */
    private JSONObject preparePostBody(){
        JSONObject res= new JSONObject();
        try {
            res.put("from",from);
            res.put("type",type);
            res.put("created",created);
            res.put("direction",direction);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    public HttpResponse addInteractionToServer() {

        JSONObject reqBody = preparePostBody();

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


    /**
     *  compares two users interactions based on their creation date
     * @param another the user interaction to be compared against
     * @return 0 if equals , 1 if the caller interaction is more recent and -1 if the caller is more
     */
    @Override
    public int compareTo(UserInteraction another) {

        if (this.created==another.created)
            return 0;
        if (this.created > another.created)
            return 1;
        //
        return -1;
    }

    @Override
    public boolean equals(Object other) {
        if (this instanceof UserInteraction && other instanceof UserInteraction) {
            return this.id.equals(((UserInteraction) other).id);
        }
        return false;
    }

    public String getContactName() {
        return contactName;
    }

    public long getCreated() {
        return created;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * different tyoes of interactions
     */
    public enum InteractionType {
        call, im, email, sms
    }

    /**
     * in which direction is the interaction
     */
    public enum Direction {
        OUTBOUND,INBOUND
    }

    /**
     * different end status that the interaction could have
     */
    public enum InteractionStatus {
        CONNECTED, BUSY, NOT_ANSWERED, VOICE_MAIL, UNKNOWN, MISSED
    }

}
