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
import com.github.atzcx.appverupdater.utils.UtilsDialog;
import com.github.atzcx.appverupdater.utils.UtilsUpdater;

public class AppVerUpdater {

    private Context context;
    private String url;
    private AsyncRequest.JSONAsyncTask jsonAsyncTask;
    private CharSequence title;
    private CharSequence content;
    private CharSequence contentNotes;
    private CharSequence positiveText;
    private CharSequence negativeText;
    private CharSequence message;
    private boolean viewNotes = false;

    private AlertDialog alertDialog;

    public AppVerUpdater(@NonNull Context context) {
        this.context = context;

        this.title = this.context.getResources().getString(R.string.appverupdate_dialog_title);
        this.content = this.context.getResources().getString(R.string.appverupdater_dialog_content);
        this.contentNotes = this.context.getResources().getString(R.string.appverupdater_dialog_content_notes);
        this.positiveText = this.context.getResources().getString(R.string.appverupdater_dialog_positivetext);
        this.negativeText = this.context.getResources().getString(R.string.appverupdater_dialog_negativetext);
        this.message = this.context.getResources().getString(R.string.appver_updater_progressdialog_message);
    }

    public AppVerUpdater setUpdateJSONUrl(@NonNull String url){
        this.url = url;
        return this;
    }

    public AppVerUpdater setAlertDialogTitle(@StringRes int titleRes) {
        setAlertDialogTitle(this.context.getText(titleRes));
        return this;
    }

    public AppVerUpdater setAlertDialogTitle(@NonNull CharSequence title) {
        this.title = title;
        return this;
    }

    public AppVerUpdater setAlertDialogContent(@StringRes int contentRes) {
        setAlertDialogContent(this.context.getText(contentRes));
        return this;
    }

    public AppVerUpdater setAlertDialogContent(@NonNull CharSequence content) {
        this.content = content;
        return this;
    }

    public AppVerUpdater setAlertDialogPositiveText(@StringRes int positiveTextRes) {
        setAlertDialogPositiveText(this.context.getText(positiveTextRes));
        return this;
    }

    public AppVerUpdater setAlertDialogPositiveText(@NonNull CharSequence positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public AppVerUpdater setAlertDialogNegativeText(@StringRes int negativeTextRes) {
        setAlertDialogNegativeText(this.context.getText(negativeTextRes));
        return this;
    }

    public AppVerUpdater setAlertDialogNegativeText(@NonNull CharSequence negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public AppVerUpdater setProgressDialogMessage(@StringRes int messageRes) {
        setProgressDialogMessage(this.context.getText(messageRes));
        return this;
    }

    public AppVerUpdater setProgressDialogMessage(@NonNull CharSequence message) {
        this.message = message;
        return this;
    }

    public AppVerUpdater setViewNotes(boolean viewNotes){
        this.viewNotes = viewNotes;
        return this;
    }

    public AppVerUpdater build(){
        update();
        return this;
    }

    private void update(){

        jsonAsyncTask = new AsyncRequest.JSONAsyncTask(context, url, new ResponseListener() {
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

    public void stop(){
        if (jsonAsyncTask != null && !jsonAsyncTask.isCancelled()) {
            jsonAsyncTask.cancel(true);
        }
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private CharSequence formatContent(Context context, UpdateModel update){

        if (content != null && contentNotes != null){

            if (this.viewNotes){
                if (update.getNotes() != null && !TextUtils.isEmpty(update.getNotes())){
                    return String.format(String.valueOf(contentNotes), UtilsUpdater.appName(context), update.getVersion(), update.getNotes());
                }
            } else {
                return String.format(String.valueOf(content), UtilsUpdater.appName(context), update.getVersion());
            }

        }

        return content;
    }

}
