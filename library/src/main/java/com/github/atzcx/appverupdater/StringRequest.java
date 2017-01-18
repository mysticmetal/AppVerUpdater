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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.atzcx.appverupdater.enums.AppVerUpdaterError;
import com.github.atzcx.appverupdater.interfaces.RequestListener;
import com.github.atzcx.appverupdater.models.UpdateModel;
import com.github.atzcx.appverupdater.utils.UtilsUpdater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StringRequest {

    public static class newCall extends AsyncTask<Void, Void, UpdateModel> {

        private Context context;
        private String url;
        private RequestListener listener;
        private OkHttpClient client;
        private Response response;
        private Request request;

        public newCall(Context context, String url, RequestListener listener) {
            this.context = context;
            this.url = url;
            this.listener = listener;
            this.client = new OkHttpClient();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (listener == null || context == null || client == null) {
                cancel(true);
            } else if (UtilsUpdater.isNetworkAvailable(context)) {
                if (url == null) {
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
            } catch (IOException ignore) {
            }

            if (response.isSuccessful()){

                if (response != null){
                    try {

                        UpdateModel updateModel = JSONParser.parse(new JSONObject(response.body().string()));

                        if (updateModel != null){
                            return updateModel;
                        }

                    } catch (IOException | JSONException ignore) {
                    }
                } else {
                    listener.onFailure(AppVerUpdaterError.JSON_IS_EMPTY);
                }

            } else {

                if (response.code() == 404){
                    listener.onFailure(AppVerUpdaterError.NOT_JSON_FILE_TO_SERVER);
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(UpdateModel updateModel) {
            super.onPostExecute(updateModel);

            if (updateModel != null) {
                listener.onSuccess(updateModel);
            }
        }
    }

}
