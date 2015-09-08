package io.interact.mohamedbenarbia.benmycontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wissem on 08.09.15.
 */

public class MyCustomArrayAdapter extends ArrayAdapter<UserInteraction> {
    private final Context context;


    public MyCustomArrayAdapter(Context context, ArrayList<UserInteraction> aList) {
        super(context, -1, aList);
        this.context = context;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_element_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.interactionDetail);
        textView.setText(this.getItem(position).toString());

        return rowView;
    }

}
