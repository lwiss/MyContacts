package io.interact.mohamedbenarbia.benmycontacts;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        this.id=interactionID;
        this.type=InteractionType.valueOf(t);
        this.contactName=n;
        this.created=ts;
        this.direction=Direction.valueOf(direc);

    }



    public UserInteraction(String interactionID, String t, String n, String timeStamp, String direction){
        this.id=interactionID;
        this.type=InteractionType.valueOf(t);
        this.contactName=n;
        this.created=Long.getLong(timeStamp);
        this.direction=Direction.valueOf(direction);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }


    public void addInteractionToServer() {

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
