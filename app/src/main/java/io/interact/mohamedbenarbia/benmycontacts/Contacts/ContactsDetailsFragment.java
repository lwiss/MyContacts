package io.interact.mohamedbenarbia.benmycontacts.Contacts;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.interact.mohamedbenarbia.benmycontacts.FetchContactsAsyncTask;
import io.interact.mohamedbenarbia.benmycontacts.R;

/**
 * Created by MohamedBenArbia on 10/09/15.
 */
public class ContactsDetailsFragment extends Fragment {




    private Contact contact ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View detailsView = inflater.inflate(R.layout.contact_details_layout, container, false);

        // Set the initials text view
        TextView contactInitials = (TextView)detailsView.findViewById(R.id.contactInitials);
        contactInitials.setText(this.contact.getContactInitials());

        // Set the contact Name listView
        TextView contactName = (TextView)detailsView.findViewById(R.id.contactName);
        contactName.setText(this.contact.getDisplayName());

        // Set the phone NumbersList View
       ListView phoneNumbersListView = (ListView) detailsView.findViewById(R.id.phoneNumbersListView);
        phoneNumbersListView.setAdapter(new PhoneNumberAdapter(getActivity(), this.contact.getPhoneNumbersList()));

        //Set the email List View

        // Set the phone NumbersList View
        ListView emailListView = (ListView) detailsView.findViewById(R.id.emailsListView);
        emailListView.setAdapter(new EmailAdapter(getActivity(), this.contact.getEmailList()));

        return detailsView ;
    }



    public void setContact (Contact contact) {
        this.contact = contact ;
    }
}

