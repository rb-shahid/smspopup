package com.byteshaft.smspopup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends Activity implements Switch.OnCheckedChangeListener, Button.OnClickListener{

    private Helpers mHelpers = null;
    private Switch mPopupDialog = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mPopupDialog = (Switch) findViewById(R.id.switch1);
        mPopupDialog.setOnCheckedChangeListener(this);
        Button close = (Button) findViewById(R.id.bClose);
        close.setOnClickListener(this);
        mHelpers = new Helpers(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHelpers.isPopupEnabled()) {
            mPopupDialog.setChecked(true);
        } else {
            mPopupDialog.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch1:
                if (isChecked) {
                    mHelpers.setPopupEnabled(true);
                } else {
                    mHelpers.setPopupEnabled(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bClose:
            finish();
                Toast.makeText(this,"Setting saved ",Toast.LENGTH_SHORT).show();
            break;

        }

    }
}
