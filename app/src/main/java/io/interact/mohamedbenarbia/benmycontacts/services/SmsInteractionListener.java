package io.interact.mohamedbenarbia.benmycontacts.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;

/**
 * tracks only incoming sms
 */
public class SmsInteractionListener extends BroadcastReceiver {
    String LOG_TAG=SmsInteractionListener.class.getName();
   /* @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            //incoming call
            String phoneNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.e(LOG_TAG,"from "+ phoneNumber);
            UserInteraction inter = new UserInteraction("", "sms", "", "" + System.currentTimeMillis(), "INBOUND", phoneNumber, "");
            Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
            mServiceIntent.putExtra("interaction",inter.toString());
            context.startService(mServiceIntent);
        }
    }*/
   @Override
   public void onReceive(Context context, Intent intent)
   {
       //---get the SMS message passed in---
       Bundle bundle = intent.getExtras();
       SmsMessage[] msgs = null;
       String str = "";
       if (bundle != null)
       {
           //---retrieve the SMS message received---
           Object[] pdus = (Object[]) bundle.get("pdus");
           msgs = new SmsMessage[pdus.length];
           for (int i=0; i<msgs.length; i++){
               msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
           }
           //---display the new SMS message---
           Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
           UserInteraction inter = new UserInteraction("", "sms", "", "" + System.currentTimeMillis(), "INBOUND", msgs[0].getOriginatingAddress(), "");
           Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
           mServiceIntent.putExtra("interaction",inter.toString());
           context.startService(mServiceIntent);
       }
   }
}
