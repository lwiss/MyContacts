package io.interact.mohamedbenarbia.benmycontacts.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;

/**
 * Created by wissem on 10.09.15.
 */
public class CallInteractionsListener extends BroadcastReceiver {
    final String LOG_TAG=CallInteractionsListener.class.getName();

    private long callStartTime;


    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if(state==null) {
                //incoming call
            String phoneNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            UserInteraction inter = new UserInteraction("", "call", "", "" + System.currentTimeMillis(), "INBOUND", phoneNumber, "");
            Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
            mServiceIntent.putExtra("interaction",inter.toString());
            context.startService(mServiceIntent);
        }else if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                //outgoing call
            String phoneNumber=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            UserInteraction inter = new UserInteraction("", "call", "", "" + System.currentTimeMillis(), "OUTBOUND", "",phoneNumber);
            Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
            mServiceIntent.putExtra("interaction",inter.toString());
            context.startService(mServiceIntent);
        }
    }
}
