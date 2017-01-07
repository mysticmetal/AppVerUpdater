package com.github.atzcx.appverupdater;


import com.github.atzcx.appverupdater.models.UpdateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    private static final String KEY_LATEST_VERSION = "latestVersion";
    private static final String KEY_RELEASE_NOTES = "releaseNotes";
    private static final String KEY_URL = "url";

    public static UpdateModel parse(JSONObject jsonObject){

        try {
            UpdateModel updateModel = new UpdateModel();
            updateModel.setVersion(jsonObject.getString(KEY_LATEST_VERSION).trim());
            updateModel.setUrl(jsonObject.getString(KEY_URL));
            JSONArray releaseArr = jsonObject.optJSONArray(KEY_RELEASE_NOTES);
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < releaseArr.length(); ++i) {
                builder.append(releaseArr.getString(i).trim());
                builder.append(System.getProperty("line.separator"));
            }
            updateModel.setNotes(builder.toString());

            return updateModel;

        } catch (JSONException e){
            //Exception Handler
        }


        return null;
    }

}
