package com.byteshaft.smspopup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainActivity extends Activity implements CheckBox.OnCheckedChangeListener,
        View.OnClickListener {

    private Helpers mHelpers = null;
    private CheckBox mPopupDialogCheckbox = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mPopupDialogCheckbox = (CheckBox) findViewById(R.id.switch1);
        mPopupDialogCheckbox.setOnCheckedChangeListener(this);
        Button close = (Button) findViewById(R.id.bClose);
        close.setOnClickListener(this);
        mHelpers = new Helpers(this);
        setFinishOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHelpers.isPopupEnabled()) {
            mPopupDialogCheckbox.setChecked(true);
        } else {
            mPopupDialogCheckbox.setChecked(false);
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
            break;

        }

    }
}
