package com.github.atzcx.appverupdater.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.atzcx.appverupdater.Constans;
import com.github.atzcx.appverupdater.JSONParser;
import com.github.atzcx.appverupdater.enums.AppVerUpdaterError;
import com.github.atzcx.appverupdater.interfaces.ResponseListener;
import com.github.atzcx.appverupdater.models.UpdateModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UtilsAsyncTask {

    public static class JSONAsyncTask extends AsyncTask<Void, Void, UpdateModel> {

        private Context context;
        private String url;
        private ResponseListener listener;
        private OkHttpClient client;
        private Response response = null;
        private Request request;

        public JSONAsyncTask(Context context, String url, ResponseListener listener){
            this.context = context;
            this.url = url;
            this.listener = listener;
            this.client = new OkHttpClient();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (listener == null || context == null || client == null){
                cancel(true);
            } else if (UtilsUpdater.isNetworkAvailable(context)){
                if (url == null){
                    listener.onFailure(AppVerUpdaterError.URL_IS_EMPTY);
                    cancel(true);
                }
            } else {
                listener.onFailure(AppVerUpdaterError.NETWORK_NOT_AVAILABLE);
                cancel(true);
            }

        }

        @Override
        protected UpdateModel doInBackground(Void... voids) {

            request = new Request.Builder()
                    .url(this.url)
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                Log.v(Constans.TAG, e.getMessage());
            }

            if (response.code() != 404){

                if (response != null){
                    try {

                        UpdateModel updateModel = JSONParser.parse(new JSONObject(response.body().string()));

                        if (updateModel != null){
                            return updateModel;
                        }

                    } catch (IOException | JSONException e) {
                        Log.v(Constans.TAG, "JSON Exception: " + e.getMessage());
                    }
                } else {
                    listener.onFailure(AppVerUpdaterError.JSON_IS_EMPTY);
                }

            } else {
                listener.onFailure(AppVerUpdaterError.NOT_JSON_FILE_TO_SERVER);
            }

            return null;
        }

        @Override
        protected void onPostExecute(UpdateModel updateModel) {
            super.onPostExecute(updateModel);

            if (listener != null){
                listener.onSuccess(updateModel);
            }
        }
    }

}
