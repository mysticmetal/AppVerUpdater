package com.github.atzcx.appverupdater;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.github.atzcx.appverupdater.utils.UtilsDialog;
import com.github.atzcx.appverupdater.utils.UtilsUpdater;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileFromURL extends AsyncTask<String, String, Void> {

    private ProgressDialog progressDialog;
    private Context context;
    private CharSequence message;

    public DownloadFileFromURL(Context context, CharSequence message){
        this.context = context;
        this.message = message;
        this.progressDialog = UtilsDialog.showDownloadProgressDialog(context, message);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... param) {
        int count;
        try {
            String currentDate = UtilsUpdater.currentDate();

            URL url = new URL(param[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();

            InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/mypkgt-" + currentDate + ".apk");

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.v(Constans.TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.progressDialog.dismiss();

        String apkPath = Environment.getExternalStorageDirectory().toString() + "/mypkgt-" + UtilsUpdater.currentDate() + ".apk";

        if (apkPath != null){
            UtilsUpdater.toUpdateApk(context, apkPath);
        }

    }
}
