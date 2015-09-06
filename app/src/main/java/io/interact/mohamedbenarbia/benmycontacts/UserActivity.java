package io.interact.mohamedbenarbia.benmycontacts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wissem on 05.09.15.
 */
public class UserActivity {
    private String type, name;

    public UserActivity(String t, String n){
        this.type=t;
        this.name=n;


    }

    public String name() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return this.type + " " + this.name;
    }


}
