package com.byteshaft.smspopup;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.CompoundButton;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity implements Switch.OnCheckedChangeListener{

    private Helpers mHelpers = null;
    private Switch mPopupDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mPopupDialog = (Switch) findViewById(R.id.switch1);
        mPopupDialog.setOnCheckedChangeListener(this);
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
}
