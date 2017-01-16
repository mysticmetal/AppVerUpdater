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

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.github.atzcx.appverupdater.enums.AppVerUpdaterError;
import com.github.atzcx.appverupdater.interfaces.RequestListener;
import com.github.atzcx.appverupdater.models.UpdateModel;
import com.github.atzcx.appverupdater.utils.UtilsDialog;
import com.github.atzcx.appverupdater.utils.UtilsUpdater;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class AppVerUpdater {

    private Context context;
    private String url;
    private StringRequest.newCall stringRequest;

    private CharSequence title_available;
    private CharSequence content_available;
    private CharSequence contentNotes_available;
    private CharSequence positiveText_available;
    private CharSequence negativeText_available;
    private CharSequence title_not_available;
    private CharSequence content_not_available;
    private CharSequence message;
    private CharSequence denied_message;

    private boolean viewNotes = false;
    private boolean showNotUpdate = false;

    private AlertDialog alertDialog;

    public AppVerUpdater(@NonNull Context context) {
        this.context = context;

        this.title_available = this.context.getResources().getString(R.string.appverupdate_update_available);
        this.content_available = this.context.getResources().getString(R.string.appverupdater_content_update_available);
        this.contentNotes_available = this.context.getResources().getString(R.string.appverupdater_notes_update_available);
        this.positiveText_available = this.context.getResources().getString(R.string.appverupdater_positivetext_update_available);
        this.negativeText_available = this.context.getResources().getString(R.string.appverupdater_negativetext_update_available);
        this.title_not_available = this.context.getResources().getString(R.string.appverupdate_not_update_available);
        this.content_not_available = this.context.getResources().getString(R.string.appverupdater_content_not_update_available);
        this.message = this.context.getResources().getString(R.string.appverupdater_progressdialog_message_update_available);
        this.denied_message = context.getResources().getString(R.string.appverupdater_denied_message);
    }

    public AppVerUpdater setUpdateJSONUrl(@NonNull String url) {
        this.url = url;
        return this;
    }

    public AppVerUpdater setShowNotUpdated(boolean showNotUpdate) {
        this.showNotUpdate = showNotUpdate;
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableTitle(@StringRes int titleRes) {
        setAlertDialogUpdateAvailableTitle(this.context.getText(titleRes));
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableTitle(@NonNull CharSequence title) {
        this.title_available = title;
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableContent(@StringRes int contentRes) {
        setAlertDialogUpdateAvailableContent(this.context.getText(contentRes));
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableContent(@NonNull CharSequence content) {
        this.content_available = content;
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailablePositiveText(@StringRes int positiveTextRes) {
        setAlertDialogUpdateAvailablePositiveText(this.context.getText(positiveTextRes));
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailablePositiveText(@NonNull CharSequence positiveText) {
        this.positiveText_available = positiveText;
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableNegativeText(@StringRes int negativeTextRes) {
        setAlertDialogUpdateAvailableNegativeText(this.context.getText(negativeTextRes));
        return this;
    }

    public AppVerUpdater setAlertDialogUpdateAvailableNegativeText(@NonNull CharSequence negativeText) {
        this.negativeText_available = negativeText;
        return this;
    }

    public AppVerUpdater setProgressDialogUpdateAvailableMessage(@StringRes int messageRes) {
        setProgressDialogUpdateAvailableMessage(this.context.getText(messageRes));
        return this;
    }

    public AppVerUpdater setProgressDialogUpdateAvailableMessage(@NonNull CharSequence message) {
        this.message = message;
        return this;
    }

    public AppVerUpdater setAlertDialogNotUpdateAvailableTitle(@StringRes int titleRes) {
        setAlertDialogNotUpdateAvailableTitle(this.context.getText(titleRes));
        return this;
    }

    public AppVerUpdater setAlertDialogNotUpdateAvailableTitle(@NonNull CharSequence title) {
        this.title_not_available = title;
        return this;
    }

    public AppVerUpdater setAlertDialogNotUpdateAvailableContent(@StringRes int contentRes) {
        setAlertDialogNotUpdateAvailableContent(this.context.getText(contentRes));
        return this;
    }

    public AppVerUpdater setAlertDialogNotUpdateAvailableContent(@NonNull CharSequence content) {
        this.content_not_available = content;
        return this;
    }

    public AppVerUpdater setAlertDialogDeniedMessage(@StringRes int denied_messageRes) {
        setAlertDialogDeniedMessage(this.context.getText(denied_messageRes));
        return this;
    }

    public AppVerUpdater setAlertDialogDeniedMessage(@NonNull CharSequence denied_message) {
        this.denied_message = denied_message;
        return this;
    }

    public AppVerUpdater setViewNotes(boolean viewNotes) {
        this.viewNotes = viewNotes;
        return this;
    }

    public AppVerUpdater build() {
        start();
        return this;
    }

    private void start(){
        if (Build.VERSION.SDK_INT >= 23){
            new TedPermission(context)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage(String.valueOf(denied_message))
                    .setDeniedCloseButtonText(android.R.string.ok)
                    .setGotoSettingButton(false)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        } else {
            update();
        }
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            update();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            for (String s : deniedPermissions){
                Log.v(Constans.TAG, "Permission Denied: " + s);
            }
        }
    };

    private void update() {

        stringRequest = new StringRequest.newCall(context, url, new RequestListener() {
            @Override
            public void onSuccess(UpdateModel update) {

                Log.v(Constans.TAG, "Update: " + update);

                if (UtilsUpdater.isUpdateAvailable(UtilsUpdater.appVersion(context), update.getVersion())) {

                    alertDialog = UtilsDialog.showUpdateAvailableDialog(context, title_available, formatContent(context, update), negativeText_available, positiveText_available, message, update.getUrl());
                    alertDialog.show();

                } else if (showNotUpdate) {

                    alertDialog = UtilsDialog.showUpdateNotAvailableDialog(context, title_not_available, content_not_available);
                    alertDialog.show();

                }

            }

            @Override
            public void onFailure(AppVerUpdaterError error) {
                Log.v(Constans.TAG, "Update Exception: " + error);
            }
        });

        stringRequest.execute();

    }

    public void stop() {
        if (stringRequest != null && !stringRequest.isCancelled()) {
            stringRequest.cancel(true);
        }
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private CharSequence formatContent(Context context, UpdateModel update) {

        try {

            if (content_available != null && contentNotes_available != null) {

                if (this.viewNotes) {
                    if (update.getNotes() != null && !TextUtils.isEmpty(update.getNotes())) {
                        return String.format(String.valueOf(contentNotes_available), UtilsUpdater.appName(context), update.getVersion(), update.getNotes());
                    }
                } else {
                    return String.format(String.valueOf(content_available), UtilsUpdater.appName(context), update.getVersion());
                }

            }

        } catch (Exception e) {
            Log.v(Constans.TAG, "formatContent: " + e.getMessage());
        }

        return content_available;
    }

}
