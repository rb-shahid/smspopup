/*
 *
 *  *
 *  *  * (C) Copyright 2015 byteShaft Inc.
 *  *  *
 *  *  * All rights reserved. This program and the accompanying materials
 *  *  * are made available under the terms of the GNU Lesser General Public License
 *  *  * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *  *  * http://www.gnu.org/licenses/lgpl-2.1.html
 *  *  *
 *  *  * This library is distributed in the hope that it will be useful,
 *  *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  *  * Lesser General Public License for more details.
 *  *  
 *
 */

package com.byteshaft.smspopup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver  {

    static String photo = null;
    private String number = null;
    private String messageText = null;
    private String contactName = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Helpers helpers = new Helpers(context);
        if (!helpers.isPopupEnabled() || helpers.isDefaultSmsAppFocused()) {
            return;
        }

        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        number = message.getOriginatingAddress();
        messageText = message.getMessageBody();
        contactName = helpers.getContactNameFromNumber(number);

        if (OverlayDialog.isActivityRunning() && OverlayDialog.isReplyBoxEmpty()) {
            OverlayDialog.closeDialog();
            startDialogActivityWithExtras(context);
        } else if(!OverlayDialog.isActivityRunning()) {
            startDialogActivityWithExtras(context);
        }
        // reset static value.
        photo = null;
    }

    private void startDialogActivityWithExtras(Context context) {
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
}

