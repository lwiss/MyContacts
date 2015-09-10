package io.interact.mohamedbenarbia.benmycontacts.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;

import java.util.Date;

/**
 * Created by wissem on 10.09.15.
 */
public class InteractionsListener extends BroadcastReceiver {
    final String LOG_TAG=InteractionsListener.class.getName();

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private boolean isIncoming;
    private long callStartTime;


    public void onReceive(Context context, Intent intent) {


        String savedNumber=null;
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            Log.e(LOG_TAG, "got action new outgoing call");
        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state, number);
            UserInteraction inter;
            if(isIncoming) {
                inter = new UserInteraction("", "call", "", "" + callStartTime, "INBOUND", savedNumber, "");
            }else {
                inter = new UserInteraction("", "call", "", "" + callStartTime, "OUTBOUND", "",savedNumber);
            }

            Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
            mServiceIntent.putExtra("interaction",inter.toString());
            context.startService(mServiceIntent);

        }
    }




    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = System.currentTimeMillis();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = System.currentTimeMillis();
                }
                break;

        }
        lastState = state;
    }
}
