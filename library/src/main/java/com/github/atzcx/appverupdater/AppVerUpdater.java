package com.github.atzcx.appverupdater;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.github.atzcx.appverupdater.enums.AppVerUpdaterError;
import com.github.atzcx.appverupdater.interfaces.ResponseListener;
import com.github.atzcx.appverupdater.models.UpdateModel;
import com.github.atzcx.appverupdater.utils.UtilsAsyncTask;
import com.github.atzcx.appverupdater.utils.UtilsDialog;
import com.github.atzcx.appverupdater.utils.UtilsUpdater;

public class AppVerUpdater {

    private Context context;
    private String url;
    private UtilsAsyncTask.JSONAsyncTask jsonAsyncTask;

    private CharSequence title;
    private CharSequence content;
    private CharSequence positiveText;
    private CharSequence negativeText;
    private CharSequence message;

    private AlertDialog alertDialog;

    public AppVerUpdater(@NonNull Context context) {
        this.context = context;
    }

    public AppVerUpdater setJSONUrl(@NonNull String url){
        this.url = url;
        return this;
    }

    public AppVerUpdater setDialogTitle(@StringRes int titleRes) {
        setDialogTitle(this.context.getText(titleRes));
        return this;
    }

    public AppVerUpdater setDialogTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public AppVerUpdater setDialogContent(@StringRes int contentRes) {
        setDialogContent(this.context.getText(contentRes));
        return this;
    }

    public AppVerUpdater setDialogContent(@NonNull CharSequence content) {
        this.content = content;
        return this;
    }

    public AppVerUpdater setDialogPositiveText(@StringRes int positiveTextRes) {
        setDialogPositiveText(this.context.getText(positiveTextRes));
        return this;
    }

    public AppVerUpdater setDialogPositiveText(@NonNull CharSequence positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public AppVerUpdater setDialogNegativeText(@StringRes int negativeTextRes) {
        setDialogNegativeText(this.context.getText(negativeTextRes));
        return this;
    }

    public AppVerUpdater setDialogNegativeText(@NonNull CharSequence negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public AppVerUpdater setProgressDialogMessage(@StringRes int messageRes) {
        setDialogNegativeText(this.context.getText(messageRes));
        return this;
    }

    public AppVerUpdater setProgressDialogMessage(@NonNull CharSequence message) {
        this.message = message;
        return this;
    }

    public AppVerUpdater build(){
        update();
        return this;
    }

    private void update(){

        jsonAsyncTask = new UtilsAsyncTask.JSONAsyncTask(context, url, new ResponseListener() {
            @Override
            public void onSuccess(UpdateModel update) {

                Log.v(Constans.TAG, "Update: " + update);

                if (UtilsUpdater.isUpdateAvailable(UtilsUpdater.appVersion(context), update.getVersion())){

                    alertDialog = UtilsDialog.showUpdateAvailableDialog(context, title, formatContent(context, update), negativeText, positiveText, message, update.getUrl());
                    alertDialog.show();

                }

            }

            @Override
            public void onFailure(AppVerUpdaterError error) {
                Log.v(Constans.TAG, "Update Exception: " + error);
            }
        });

        jsonAsyncTask.execute();

    }


    private CharSequence formatContent(Context context, UpdateModel update){

        if (content != null){

            if (update.getNotes() != null && !TextUtils.isEmpty(update.getNotes())){
                return String.format(String.valueOf(content), UtilsUpdater.appName(context), update.getVersion(), update.getNotes());
            } else {
                return String.format(String.valueOf(content), UtilsUpdater.appName(context), update.getVersion());
            }

        }

        return content;
    }

}
