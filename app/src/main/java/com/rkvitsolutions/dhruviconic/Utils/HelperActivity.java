package com.rkvitsolutions.dhruviconic.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.text.DateFormatSymbols;

public class HelperActivity {

    Activity activity;

    public HelperActivity(Activity activity) {
        this.activity = activity;
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public void shareApp() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nDhruv Iconic for Better & Secured Tomorrow. Install the app now\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {

        }

    }

    public void doWhatsApp(String number) {

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + "+91"+number + "&text=Hello Sir");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.whatsapp.w4b");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.getApplicationContext().startActivity(i);

    }

    public void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        //change the number
        activity.startActivity(callIntent);
    }

    @SuppressLint("IntentReset")
    public void sendEmail(String mailId) {

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mailId});
        i.putExtra(Intent.EXTRA_SUBJECT, "Offer Letter");
        i.putExtra(Intent.EXTRA_TEXT   , Constant.MailMsg);
        try {
            activity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void doMsg(String number) {

        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        activity.startActivity(intent);
    }

    public void rateUs() {

        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
    }

    // check version on play store and force update
    public void forceUpdate() {
        PackageManager packageManager = activity.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,activity).execute();
    }
}
