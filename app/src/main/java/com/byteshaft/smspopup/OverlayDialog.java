package com.byteshaft.smspopup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class OverlayDialog extends Activity implements View.OnClickListener {

    public static OverlayDialog self = null;
    Uri photoUri = null;
    String incomingAddress;
    private static EditText messageInputField = null;
    Button cancelButton;
    Button replyButton;

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
        System.out.println("starting activity");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_overlay_dialog);
        System.out.println("activity started");
        self = this;
        ImageView contactImage = (ImageView) findViewById(R.id.img);
        TextView incomingMessageLabel = (TextView) findViewById(R.id.tv);
        TextView contactAddress = (TextView) findViewById(R.id.incomingNumber);
        messageInputField = (EditText) findViewById(R.id.editTextMsg);
        setMessageBoxListener();
        cancelButton = (Button) findViewById(R.id.bCancel);
        replyButton = (Button) findViewById(R.id.bReply);
        cancelButton.setOnClickListener(this);
        replyButton.setOnClickListener(this);
        incomingMessageLabel.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String message = extras.getString("message");
        String name = extras.getString("name");
        String photo = extras.getString("photo");
        incomingAddress = extras.getString("number");

        if (photo != null) {
            photoUri = Uri.parse(photo);
        }

        contactImage.setImageURI(photoUri);
        incomingMessageLabel.setText(message);

        if (name == null) {
            contactAddress.setText(incomingAddress);
        } else {
            contactAddress.setText(name);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCancel:
                finish();
                break;
            case R.id.bReply:
                String replyText = messageInputField.getText().toString();
                sendSms(incomingAddress, replyText );
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

    private void sendSms(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }

    private void setMessageBoxListener() {
        messageInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (messageInputField.getText().toString().isEmpty()) {
                    replyButton.setEnabled(false);
                } else {
                    replyButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
