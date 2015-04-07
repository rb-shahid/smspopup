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

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UiHelpers {

    private OverlayDialog mOverlayDialog = null;

    public UiHelpers(OverlayDialog overlayDialog) {
        mOverlayDialog = overlayDialog;
    }

    void setContactAvatar(Uri photoUri) {
        ImageView contactImage = (ImageView) mOverlayDialog.findViewById(R.id.img);
        contactImage.setImageURI(photoUri);
    }

    void setContactDisplay(String contactDisplay) {
        TextView contactAddress = (TextView) mOverlayDialog.findViewById(R.id.incomingNumber);
        contactAddress.setText(contactDisplay);
    }

    void setUpMessageBodyView(String messageBody) {
        TextView incomingMessageLabel = (TextView) mOverlayDialog.findViewById(R.id.tv);
        incomingMessageLabel.setOnClickListener(mOverlayDialog);
        incomingMessageLabel.setText(messageBody);
    }

    void setMessageBoxTextChangeListener(
            final EditText messageInputField,final Button replyButton) {

        messageInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isReplyBoxEmpty = messageInputField.getText().toString().isEmpty();
                replyButton.setEnabled(!isReplyBoxEmpty);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
