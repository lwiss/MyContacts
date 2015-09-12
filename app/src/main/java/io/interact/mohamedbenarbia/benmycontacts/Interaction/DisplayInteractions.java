package io.interact.mohamedbenarbia.benmycontacts.Interaction;

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

import io.interact.mohamedbenarbia.benmycontacts.R;

/**
 * Created by wissem on 06.09.15.
 */
public class DisplayInteractions extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_interactions);
    }


}
