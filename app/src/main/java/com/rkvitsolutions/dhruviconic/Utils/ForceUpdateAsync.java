package com.rkvitsolutions.dhruviconic.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AlertDialog;


import com.rkvitsolutions.dhruviconic.Activity.Splashscreen;
import com.rkvitsolutions.dhruviconic.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

    private String latestVersion;
    private String currentVersion;
    private Context context;

    public ForceUpdateAsync(String currentVersion, Context context) {
        this.currentVersion = currentVersion;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {

            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (latestVersion != null) {
            if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                if (!(context instanceof Splashscreen)) {
                    if (!((Activity) context).isFinishing()) {
                        showForceUpdateDialog();
                    }
                }
            }
        }
        super.onPostExecute(jsonObject);
    }

    public void showForceUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.AppTheme));
        alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + context.getString(R.string.youAreNotUpdatedMessage1));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }
}
