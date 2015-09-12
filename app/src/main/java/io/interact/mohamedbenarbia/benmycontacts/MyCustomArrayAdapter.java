package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        TextView textView = (TextView) rowView.findViewById(R.id.interactionType);
        textView.setText(this.getItem(position).getType().toString()); textView.setBackgroundColor(Color.CYAN);

        textView = (TextView) rowView.findViewById(R.id.interactionDirection);
        textView.setText(this.getItem(position).getDirection().toString()); textView.setBackgroundColor(Color.RED);

        textView = (TextView) rowView.findViewById(R.id.interactionTime);
        textView.setText(getCurrentTimeStamp(this.getItem(position).getCreated())); textView.setBackgroundColor(Color.YELLOW);

        textView = (TextView) rowView.findViewById(R.id.interactionName);
        textView.setText(this.getItem(position).getContactName()); textView.setBackgroundColor(Color.MAGENTA);

        return rowView;
    }

    public static String getCurrentTimeStamp(long timeStamp) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(timeStamp);
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public class InteractionComparator implements Comparator<UserInteraction> {

        @Override
        public int compare(UserInteraction lhs, UserInteraction rhs) {
            return -lhs.compareTo(rhs);
        }
    }

}
