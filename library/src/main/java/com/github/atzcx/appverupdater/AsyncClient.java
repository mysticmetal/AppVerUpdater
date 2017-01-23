/*
 * Copyright 2017 Aleksandr Tarakanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.atzcx.appverupdater;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.github.atzcx.appverupdater.enums.UpdateErrors;
import com.github.atzcx.appverupdater.interfaces.DownloadListener;
import com.github.atzcx.appverupdater.interfaces.RequestListener;
import com.github.atzcx.appverupdater.models.Update;
import com.github.atzcx.appverupdater.utils.DialogUtils;
import com.github.atzcx.appverupdater.utils.UpdaterUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncClient {


    /**
     * Information from the server about new ads app
     */

    public static class StringRequest extends AsyncTask<Void, Void, Update> {

        private Context context;
        private String url;
        private RequestListener listener;
        private OkHttpClient client;
        private Response response;
        private Request request;

        public StringRequest(Context context, String url, RequestListener listener) {
            this.context = context;
            this.url = url;
            this.listener = listener;
            this.client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (listener == null || context == null || client == null) {
                cancel(true);
            } else if (UpdaterUtils.isNetworkAvailable(context)) {
                if (url == null) {
                    listener.onFailure(UpdateErrors.STRING_URL_IS_EMPTY);
                    cancel(true);
                }
            } else {
                listener.onFailure(UpdateErrors.NETWORK_NOT_AVAILABLE);
                cancel(true);
            }

        }

        @Override
        protected Update doInBackground(Void... voids) {

            request = new Request.Builder()
                    .url(this.url)
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.UNKNOWN_HOST_EXCEPTION);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.SOCET_TIMEOUT_EXCEPTION);
            } catch (SocketException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.SOCET_EXCEPTION);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.isSuccessful()) {

                if (response != null) {
                    try {

                        Update updateModel = JSONParser.parse(new JSONObject(response.body().string()));

                        if (updateModel != null) {
                            return updateModel;
                        }

                    } catch (IOException | JSONException ignore) {
                    }
                } else {
                    listener.onFailure(UpdateErrors.FILE_JSON_NO_DATA);
                }

            } else {

                if (response.code() == 404) {
                    listener.onFailure(UpdateErrors.JSON_FILE_IS_MISSING);
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Update updateModel) {
            super.onPostExecute(updateModel);

            if (updateModel != null) {
                listener.onSuccess(updateModel);
            }
        }
    }

    /**
     * Download the updates from the server
     */

    public static class DownloadRequest extends AsyncTask<Void, String, File> {

        private Context context;
        private String url;

        private CharSequence message;
        private String downloadFileName;
        private DownloadListener listener;
        private OkHttpClient client;
        private Response response;
        private Request request;

        private ProgressDialog progressDialog;

        private int count;
        private InputStream input = null;
        private OutputStream output = null;

        public DownloadRequest(Context context, String url, CharSequence message, String downloadFileName, DownloadListener listener) {
            this.context = context;
            this.url = url;
            this.message = message;
            this.downloadFileName = downloadFileName;
            this.listener = listener;
            this.progressDialog = DialogUtils.showDownloadProgressDialog(context, this.message);
            this.client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (listener == null || context == null || client == null) {
                cancel(true);
            } else if (UpdaterUtils.isNetworkAvailable(context)) {
                if (url == null) {
                    cancel(true);
                }
            } else {
                listener.onFailure(UpdateErrors.NETWORK_NOT_AVAILABLE);
                cancel(true);
            }

            this.progressDialog.show();
        }

        @Override
        protected File doInBackground(Void... param) {
            try {

                request = new Request.Builder()
                        .url(this.url)
                        .build();

                response = client.newCall(request).execute();

                if (response.isSuccessful()) {

                    File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");

                    if (SDCardRoot.exists() == false) {
                        SDCardRoot.mkdirs();
                    }

                    File file = new File(SDCardRoot, downloadFileName);

                    int length = (int) response.body().contentLength();

                    input = new BufferedInputStream(response.body().byteStream(), 10 * 1024);

                    output = new FileOutputStream(file);

                    byte data[] = new byte[1024];
                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;

                        publishProgress("" + (int) ((total * 100) / length));

                        output.write(data, 0, count);

                    }

                    if (output != null) {
                        return new File(SDCardRoot, downloadFileName);
                    }

                }

                output.flush();
                output.close();
                input.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.UNKNOWN_HOST_EXCEPTION);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.SOCET_TIMEOUT_EXCEPTION);
            } catch (SocketException e) {
                e.printStackTrace();
                listener.onFailure(UpdateErrors.SOCET_EXCEPTION);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
            this.progressDialog.setMessage(this.message + " - " + Integer.parseInt(progress[0]) + "%");
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            this.progressDialog.dismiss();

            if (file != null) {
                listener.onSuccess(file);
            }

            if (!UpdaterUtils.isNetworkAvailable(context)) {
                listener.onFailure(UpdateErrors.NETWORK_DISCONNECTED);
            }

        }
    }

}
