package com.github.atzcx.appverupdater.utils;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.github.atzcx.appverupdater.AsyncDownload;

public class UtilsDialog {

    public static AlertDialog showUpdateAvailableDialog(final Context context, CharSequence title, final CharSequence content, CharSequence btnNegative, CharSequence btnPositive, final CharSequence message, final String url) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AsyncDownload download = new AsyncDownload(context, message);
                        download.execute(url);
                    }
                })
                .setNegativeButton(btnNegative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }


    public static ProgressDialog showDownloadProgressDialog(Context context, CharSequence message){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;

    }

}
