package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.interact.mohamedbenarbia.benmycontacts.Contacts.Contact;
import io.interact.mohamedbenarbia.benmycontacts.Util.NetworkUtility;
import io.interact.mohamedbenarbia.benmycontacts.Util.SharedAttributes;

/**
 *
 * AsyncTask used to fetch contacts from server.
 * Created by MohamedBenArbia on 05/09/15.
 */


public class FetchContactsAsyncTask extends AsyncTask< Void, Void, List<Contact>> {


    private static final String DEBUG_TAG = "FETCH CONTACTS" ;

    /**
     * Associated fragment responsible for displaying the contact list.
     */
    private DisplayContactsFragement displayConotactsFragement ;



    public FetchContactsAsyncTask(DisplayContactsFragement fragement) {

        this.displayConotactsFragement = fragement ;


    }



    @Override
    protected void onPreExecute() {
        List<String> contactsFromCache = getListOfDisplayNames(getContactFromCache());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(displayConotactsFragement.getActivity(),
                android.R.layout.simple_list_item_1, contactsFromCache);
        displayConotactsFragement.setListAdapter(adapter);


    }




    @Override
    protected List<Contact> doInBackground(Void... params) {


        // List that contains all contacts
        List<Contact> listOfContacts = new ArrayList<Contact>() ;


        // First, check if an internet connection exists. If it's the case, fetch contacts from the server and display new content if different form the cache. Otherwise, content from the cash will be displayed.

        ConnectivityManager conMan = (ConnectivityManager) this.displayConotactsFragement.getActivity().getSystemService(this.displayConotactsFragement.getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (!(netInfo == null || !netInfo.isConnected())) {

            HttpResponse response = fetchContacts();

            Log.d(DEBUG_TAG, "Response status" + response.getStatusLine().getStatusCode());

            try {
                String resBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(resBody);
                JSONArray jsonarray = jsonResponse.getJSONArray("data");


                for (int i = 0; i < jsonarray.length() - 1; i++) {
                    JSONObject contactJsonObject = jsonarray.getJSONObject(i);

                    // First check if it's a company or a contact;
                    Contact.Type type = Contact.Type.valueOf(contactJsonObject.getString(Contact.TYPE_KEY));


                    //Get the display name of the contact
                    String displayName = contactJsonObject.getString(Contact.DISPLAY_KEY);

                    // Display only contacts with not null display name
                    if (!displayName.equals(null)) {
                        JSONArray emails = null;
                        JSONArray phoneNumbers = null;

                        boolean emailExist = !contactJsonObject.isNull(Contact.EMAIL_KEY);
                        if (emailExist) {
                            emails = contactJsonObject.getJSONArray(Contact.EMAIL_KEY);
                            Log.d(DEBUG_TAG, "Fetchched emails " + emails);
                        }

                        boolean phoneExist = !contactJsonObject.isNull(Contact.PHONE_KEY);
                        if (phoneExist) {
                            phoneNumbers = contactJsonObject.getJSONArray(Contact.PHONE_KEY);
                            Log.d(DEBUG_TAG, "Fetchched phoneNumbers " + phoneNumbers);

                        }

                        Contact contact = null;
                        if (type.equals(Contact.Type.PERSON)) {
                            String firstName = contactJsonObject.getString(Contact.FIRST_NAME_KEY);
                            String lastName = contactJsonObject.getString(Contact.LAST_NAME_KEY);
                            contact = new Contact(Contact.Type.PERSON, firstName, lastName, emails, phoneNumbers, displayName);
                        } else if (type.equals(Contact.Type.COMPANY)) {
                            String companyName = contactJsonObject.getString(Contact.COMPANY_NAME_KEY);
                            contact = new Contact(Contact.Type.COMPANY, companyName, emails, phoneNumbers, displayName);
                        }


                        listOfContacts.add(contact);

                        Log.d(DEBUG_TAG, "Contact fetched number: " + i + " info: " + contact.toString());

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        // TODO: compare the list with the cached version --> if there is change update the cache and return the newest list




        return listOfContacts ;
    }


    /**
     * Used to update the cahce
     * @param newList New list of contacts
     *
     */
    private void updateCache (List<Contact> newList) {
            // TODO: update Cash
    }


    /**
     * Check if the fetched list from the server differs from the one cached.
     * @return
     */
    private boolean checkLists(List<String> listCache, List<String> listServer ) {

        boolean listDifferent = false ;
        for (String contactName: listServer) {

            // Check if there is an element in the list Server that is not present in the list cache
            if(!listCache.contains(contactName)){
                listDifferent = true ;
                break ;
            }


        }
        return listDifferent ;
    }


    /**
     *  Return list of dsiplay names from list of contacts.
     * @param listOfContacts list containing contacts
     * @return list containing display names of the contacts
     */
    private List<String> getListOfDisplayNames (List<Contact> listOfContacts) {
        List<String> listOfContactNames = new ArrayList<String>() ;

        for (Contact contact: listOfContacts) {
            listOfContactNames.add(contact.getFullName()) ;
        }

        return listOfContactNames ;
    }

    private HttpResponse fetchContacts() {

        // Form the string URI
        String uri = SharedAttributes.BASE_URL + SharedAttributes.CONTACTS_URI ;

        // Get token from shared preferences

        SharedPreferences  setting = this.displayConotactsFragement.getActivity().getSharedPreferences(this.displayConotactsFragement.getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE) ;
        String token = setting.getString(String.valueOf(this.displayConotactsFragement.getActivity().getText(R.string.token_key)), null);



        // Form headers in a Hash map
        HashMap<String, String> headers = new HashMap<>() ;
        headers.put(HTTP.CONTENT_TYPE, "application/json") ;
        headers.put("Accept", "application/json") ;
        headers.put("authToken", token) ;



        return NetworkUtility.getMethod(uri,headers) ;


    }

    private List<Contact> getContactFromCache() {


        // Get list of contacts from cache
        List<Contact> contactList = new ArrayList<Contact>() ;
        Contact contact1 = new Contact(Contact.Type.PERSON, "Mou7" , "Eddayne", null, null, "Mou7 Eddayn") ;
        Contact contact2 = new Contact(Contact.Type.PERSON, "Mou71" , "Eddayne", null, null, "Mou71 Eddayn") ;

        Contact contact3 = new Contact(Contact.Type.PERSON, "Mou72" , "Eddayne", null, null, "Mou72 Eddayn") ;

        Contact contact4 = new Contact(Contact.Type.PERSON, "Mou72" , "Eddayne", null, null, "Mou73 Eddayn") ;

        contactList.add(contact1) ;
        contactList.add(contact2) ;
        contactList.add(contact3) ;
        contactList.add(contact4) ;



        return contactList ;
    }


    @Override
    protected void onPostExecute(List<Contact> listOfContactFromServer) {
        // Check if there is a list fetched from the server
        if(!listOfContactFromServer.isEmpty()) {
            // Compare if there is something new in the new list

            List<String> contactNamesFromCache = getListOfDisplayNames(getContactFromCache());
            List<String> contactNamesFromServer = getListOfDisplayNames(listOfContactFromServer) ;

           if(checkLists(contactNamesFromCache,contactNamesFromServer)) {
                updateCache(listOfContactFromServer);

               // Put the updated list
               ArrayAdapter<String> adapter = new ArrayAdapter<String>(displayConotactsFragement.getActivity(),
                       android.R.layout.simple_list_item_1, contactNamesFromServer);
               displayConotactsFragement.setListAdapter(adapter);


           }


        }



    }



}
