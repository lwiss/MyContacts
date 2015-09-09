package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.interact.mohamedbenarbia.benmycontacts.Contacts.Contact;

/** This is a personalized adapter for the contacts
 * Created by MohamedBenArbia on 08/09/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact>{


    private Context context ;
    private List<Contact> contacts ;


    public ContactsAdapter(Context context, List<Contact> objects) {
        super(context, -1, objects);
        this.contacts = objects;
        this.context = context ;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_element_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.contactDisplayName);


        String firstName =  contacts.get(position).getFirstName();
        String lastName = contacts.get(position).getLastName();

        if((firstName!=null && lastName!=null) && (!firstName.isEmpty() || !lastName.isEmpty())) {
            String sourceString =   firstName  + "<b>" + " "+lastName+ "</b> ";
            textView.setText(Html.fromHtml(sourceString));
        }else {
            String sourceString = contacts.get(position).getDisplayName() ;
            textView.setText(sourceString);
        }

        return rowView;
    }


}
