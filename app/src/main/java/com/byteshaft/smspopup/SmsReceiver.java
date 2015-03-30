package com.byteshaft.smspopup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver  {
    static String photo = null;
    String number;
    String messageText;
    SmsMessage message;
    String contactName;

    @Override
    public void onReceive(Context context, Intent intent) {
        Helpers helpers = new Helpers(context);
        if (!helpers.isPopupEnabled()) {
            return;
        }

        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        number = message.getOriginatingAddress();
        messageText = message.getMessageBody();
        contactName = helpers.getContactNameFromNumber(number);

        if (OverlayDialog.isActivityRunning() && OverlayDialog.isReplyBoxEmpty()) {
            OverlayDialog.closeDialog();
            Intent intent1 = new Intent(context, OverlayDialog.class);
            if (photo != null) {
                intent1.putExtra("photo", photo);
            }
            intent1.putExtra("message", messageText);
            intent1.putExtra("name", contactName);
            intent1.putExtra("number", number);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if(!OverlayDialog.isActivityRunning()) {
            Intent intent1 = new Intent(context, OverlayDialog.class);
            if (photo != null) {
                intent1.putExtra("photo", photo);
            }
            intent1.putExtra("message", messageText);
            intent1.putExtra("name", contactName);
            intent1.putExtra("number", number);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

        messageText = null;
        contactName = null;
        number = null;
        photo = null;
    }
}

