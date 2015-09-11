package io.interact.mohamedbenarbia.benmycontacts.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import io.interact.mohamedbenarbia.benmycontacts.UserInteraction;

/**
 * tracks only incoming sms
 */
public class SmsInteractionListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            //incoming call
            String phoneNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            UserInteraction inter = new UserInteraction("", "sms", "", "" + System.currentTimeMillis(), "INBOUND", phoneNumber, "");
            Intent mServiceIntent = new Intent(context, NewInteractionHandlerService.class);
            mServiceIntent.putExtra("interaction",inter.toString());
            context.startService(mServiceIntent);
        }
    }
}
