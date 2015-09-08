package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wissem on 06.09.15.
 */
public class DisplayInteractions extends ListActivity {

    private ArrayList<String> ua;

    public DisplayInteractions() {
    }

    public DisplayInteractions(ArrayList<String> ua) {
        this.ua = ua;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_activities_listview);

        Intent i =getIntent();
        this.ua=i.getStringArrayListExtra("interactions list");
        Log.e("list", "" + this.ua.size());
        setListAdapter(new MyCustomArrayAdapter(this, this.ua));
    }


    public class MyCustomArrayAdapter extends ArrayAdapter <String> {
        private final Context context;
        private ArrayList<String> aList;

        public MyCustomArrayAdapter(Context context, ArrayList<String> aList) {
            super(context, -1, aList);
            this.context = context;
            this.aList = aList;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_element_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.activityDetails);
            textView.setText(aList.get(position).toString());
            // change the icon for Windows and iPhone
//            String s = values[position];
//            if (s.startsWith("iPhone")) {
//                imageView.setImageResource(R.drawable.no);
//            } else {
//                imageView.setImageResource(R.drawable.ok);
//            }

            return rowView;
        }
    }



}
