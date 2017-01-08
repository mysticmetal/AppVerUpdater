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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncDownload extends AsyncTask<String, String, String> {

    private Context context;
    private CharSequence message;

    private ProgressDialog progressDialog;

    private int count;
    private InputStream input = null;
    private OutputStream output = null;

    public AsyncDownload(Context context, CharSequence message){
        this.context = context;
        this.message = message;
        this.progressDialog = UtilsDialog.showDownloadProgressDialog(context, this.message);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(String... param) {
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(param[0])
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()){

                int lenghtOfFile = (int) response.body().contentLength();

                input = new BufferedInputStream(response.body().byteStream(), 10 * 1024);

                output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/update-" + UtilsUpdater.currentDate() + ".apk");

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    output.write(data, 0, count);

                }

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
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
        this.progressDialog.setMessage( this.message + " - " + Integer.parseInt(progress[0]) + "%");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.progressDialog.dismiss();

        String apkPath = Environment.getExternalStorageDirectory().toString() + "/update-" + UtilsUpdater.currentDate() + ".apk";

        if (apkPath != null){
            UtilsUpdater.toUpdateApk(context, apkPath);
        }

    }
}
