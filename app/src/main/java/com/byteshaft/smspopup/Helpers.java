package com.byteshaft.smspopup;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;

import java.util.List;

public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    void setPopupEnabled(boolean enable) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("enablePopup", enable).apply();
    }

    boolean isPopupEnabled() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("enablePopup", false);
    }

    String getContactNameFromNumber(String phoneNumber) {
        ContentResolver cr = getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.PHOTO_URI}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            SmsReceiver.photo = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }
    boolean isDefaulSmsAppFocused() {
        String defaultApplication = Settings.Secure.getString(getContentResolver(), "sms_default_application");
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        ComponentName rootActivity = task.baseActivity;
        String rootActivityName = rootActivity.getPackageName();
        return rootActivityName.equals(defaultApplication);
    }
}
