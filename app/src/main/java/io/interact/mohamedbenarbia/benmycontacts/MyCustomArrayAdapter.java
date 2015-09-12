package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import io.interact.mohamedbenarbia.benmycontacts.Interaction.UserInteraction;

/**
 * Created by wissem on 08.09.15.
 */

public class MyCustomArrayAdapter extends ArrayAdapter<UserInteraction> {
    private final Context context;


    public MyCustomArrayAdapter(Context context, ArrayList<UserInteraction> aList) {
        super(context, -1, aList);
        this.context = context;
        this.sort(new InteractionComparator());


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_element_layout, parent, false);

        UserInteraction interaction = this.getItem(position);

        ImageView interactionDirection = (ImageView) rowView.findViewById(R.id.interactionDirection);
        ImageView interactionType = (ImageView) rowView.findViewById(R.id.interactionType);
        String numberInvolved = "";

        if (interaction.getDirection().equals(UserInteraction.Direction.OUTBOUND)) {
            interactionDirection.setImageResource(R.drawable.outgoing_icon);
            numberInvolved = interaction.getTo();
            if (interaction.getType().equals(UserInteraction.InteractionType.sms)) {
                interactionType.setImageResource(R.drawable.sms_outgoing_icon);
            } else if (interaction.getType().equals(UserInteraction.InteractionType.call)) {
                interactionType.setImageResource(R.drawable.phone_outgoing_icon);

            }

        } else {
            interactionDirection.setImageResource(R.drawable.incoming_icon);
            numberInvolved = interaction.getFrom();

            if (interaction.getType().equals(UserInteraction.InteractionType.sms)) {
                interactionType.setImageResource(R.drawable.sms_ingoing_icon);
            } else if (interaction.getType().equals(UserInteraction.InteractionType.call)) {
                interactionType.setImageResource(R.drawable.call_incoming_icon);

            }

        }


        TextView interactionTime = (TextView) rowView.findViewById(R.id.interactionTime);
        String time = getCurrentTimeStamp(interaction.getCreated());
        interactionTime.setText(time);

        TextView interactionName = (TextView) rowView.findViewById(R.id.interactionName);


        String contactName = "Unknown Contact";
        if (!interaction.getContactName().isEmpty()) {
            contactName = interaction.getContactName();
        }
        String details = contactName + "<br>" +
                "<small>" + numberInvolved + "</small>";
        interactionName.setText(Html.fromHtml(details));

        return rowView;
    }

    public static String getCurrentTimeStamp(long timeStamp) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(timeStamp);
        String strDate = sdfDate.format(now);

        // Get only the hour of creation
        strDate = strDate.substring(11, 16);
        return strDate;
    }

    public class InteractionComparator implements Comparator<UserInteraction> {

        @Override
        public int compare(UserInteraction lhs, UserInteraction rhs) {
            return -lhs.compareTo(rhs);
        }
    }

}
