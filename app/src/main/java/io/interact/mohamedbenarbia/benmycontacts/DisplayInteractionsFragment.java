package io.interact.mohamedbenarbia.benmycontacts;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wissem on 08.09.15.
 */
public class DisplayInteractionsFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        (new UserInteractionsRetrieverAsyncTask(this)).execute();
    }


}
