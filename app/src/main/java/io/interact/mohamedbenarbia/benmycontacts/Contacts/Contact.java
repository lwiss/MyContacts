package io.interact.mohamedbenarbia.benmycontacts.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Models a contact which can be a person or a company.
 * Created by MohamedBenArbia on 06/09/15.
 */
public class Contact implements Comparable<Contact> {

    public static String TYPE_KEY = "contactType" ;
    public static String FIRST_NAME_KEY = "firstName" ;
    public static String LAST_NAME_KEY="lastName" ;
    public static String COMPANY_NAME_KEY  = "companyName" ;
    public static String PHONE_KEY="phoneNumbers" ;
    public static String EMAIL_KEY="emails" ;
    public static String DISPLAY_KEY="displayName" ;

    @Override
    public int compareTo(Contact another) {

        return this.displayName.toLowerCase().compareTo(another.displayName.toLowerCase()) ;
    }


    public enum Type {
        COMPANY,PERSON
    }
    
    /**
     * Contact's first name
     */
    private String firstName ;

    /**
     * Contact's last name
     */
    private String lastName ;

    /**
     * Company Name in case of a company
     */
    private String companyName ;

    /**
     * Contact's emails. Presented by a JSONArray
     */

    private JSONArray emails  ;


    /**
     * Contact's phone numbers. Same thing as emails.
     */

    private JSONArray phoneNumbers  ;


    /**
     * The contact type. Person or company.
     */
    private Type type ;


    /**
     * The display name of the contact. Will be used as a identifier of a contact.
     */
    private String displayName ;




    public Contact(Type type , String firstName , String lastName, JSONArray emails, JSONArray phoneNumbers, String displayName) {

        this.type = type ;

        if(firstName!=null) {
            this.firstName = firstName;
        } else {
            this.firstName = "" ;
        }
        if(lastName!=null) {
            this.lastName = lastName ;
        }else  {
            this.lastName = "" ;
        }

        this.emails = emails ;
        this.phoneNumbers = phoneNumbers ;

        this.displayName = displayName  ;

    }


    public Contact(Type type , String companyName, JSONArray emails, JSONArray phoneNumbers, String displayName) {

        this.type = type ;

        this.companyName = companyName ;

        this.emails = emails ;
        this.phoneNumbers = phoneNumbers ;


        this.displayName = displayName ;
    }


    @Override
    public String toString() {
        String contactInfo = "" ;

        contactInfo = "First Name: "+this.firstName +" Last Name: "+this.lastName   ;

        if(this.emails!=null) {

        contactInfo = contactInfo + " emails:" + this.emails ;

        }


        if(this.phoneNumbers!=null) {

            contactInfo = contactInfo + " emails:" + this.phoneNumbers ;

        }

        return contactInfo  ;
    }



    public String getFirstName() {
        return firstName;
    }


    public JSONArray getEmails() {
        return emails;
    }

    public String getLastName() {
        return lastName;
    }

    public JSONArray getPhoneNumbers() {
        return phoneNumbers;
    }


    public String getFullName() {


        return  this.displayName ;

    }


    /**
     * Return a JSON object that represent the contact.
     * @return
     */
    public JSONObject getJson() {
        JSONObject contactJSON = new JSONObject() ;
        try {
            contactJSON.put(TYPE_KEY,this.type.toString()) ;
            contactJSON.put(FIRST_NAME_KEY,this.firstName) ;
            contactJSON.put(LAST_NAME_KEY, this.lastName) ;
            contactJSON.put(COMPANY_NAME_KEY,this.companyName) ;
            contactJSON.put(EMAIL_KEY, this.emails) ;
            contactJSON.put(PHONE_KEY,this.phoneNumbers) ;
            contactJSON.put(DISPLAY_KEY,this.displayName) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contactJSON ;

    }



}
