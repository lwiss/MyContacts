package io.interact.mohamedbenarbia.benmycontacts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wissem on 05.09.15.
 */
public class UserInteraction implements Comparable<UserInteraction>{
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





    public UserInteraction(String t, String n, String timeStamp, String direction){
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
        return this.type.toString() + " " + this.contactName;
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
