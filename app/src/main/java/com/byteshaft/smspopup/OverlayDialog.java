package com.byteshaft.smspopup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class OverlayDialog extends Activity implements View.OnClickListener {

    public static OverlayDialog self = null;
    private Uri photoUri = null;
    private String incomingAddress = null;
    private static EditText messageInputField = null;
    private Helpers mHelpers = null;

    static void closeDialog() {
        if (isActivityRunning()) {
            closeActivity();
        }
    }

    static boolean isActivityRunning() {
        return self != null;
    }

    static boolean isReplyBoxEmpty() {
        return messageInputField.getText().toString().equals("");
    }

    private static void closeActivity() {
        self.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_dialog);
        self = this;
        UiHelpers uiHelpers = new UiHelpers(this);
        messageInputField = (EditText) findViewById(R.id.editTextMsg);
        Button cancelButton = (Button) findViewById(R.id.bCancel);
        Button replyButton = (Button) findViewById(R.id.bReply);
        cancelButton.setOnClickListener(this);
        replyButton.setOnClickListener(this);
        uiHelpers.setMessageBoxTextChangeListener(messageInputField, replyButton);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mHelpers = new Helpers(this);

        String message = mHelpers.getMessageBodyFromBundledExtras(extras);
        String name = mHelpers.getContactNameFromBundledExtras(extras);
        String photo = mHelpers.getContactAvatarFromBundledExtras(extras);
        incomingAddress = mHelpers.getContactNumberFromBundledExtras(extras);

        if (photo != null) {
            photoUri = Uri.parse(photo);
        }
        if (name == null) {
            uiHelpers.setContactDisplay(incomingAddress);
        } else {
            uiHelpers.setContactDisplay(name);
        }

        uiHelpers.setUpMessageBodyView(message);
        uiHelpers.setContactAvatar(photoUri);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.y = Math.round(pixels);
        getWindowManager().updateViewLayout(view, params);
    }

    @Override
    protected void onStop() {
        super.onStop();
        self = null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_dialog, R.anim.slide_out_down);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCancel:
                finish();
                break;
            case R.id.bReply:
                String replyText = messageInputField.getText().toString();
                mHelpers.sendSms(incomingAddress, replyText);
                finish();
                break;
            case R.id.tv:
                Uri smsUri = Uri.parse(String.format("smsto:%s", incomingAddress));
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                startActivity(intent);
                OverlayDialog.closeDialog();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
