package com.github.atzcx.appverupdater.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.github.atzcx.appverupdater.Constans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilsUpdater {

    public static String appName(Context context){
        return context.getString(context.getApplicationInfo().labelRes);
    }

    public static String currentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }

    public static String appVersion(Context context){
        String version = null;

        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static boolean isUpdateAvailable(String versionOld, String versionNew){
        boolean res = false;

        if (!versionOld.equals("0.0.0.0") && !versionNew.equals("0.0.0.0")){
            res = (versionCompare(versionOld, versionNew)) < 0;
        }

        return res;
    }

    public static void toUpdateApk(Context context, String filePath){
        if (filePath != null){
            Uri uri = Uri.fromFile(new File(filePath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            Log.v(Constans.TAG, "apk update not found");
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static int versionCompare(String versionOld, String versionNew){

        String[] vals1 = versionOld.split("\\.");
        String[] vals2 = versionNew.split("\\.");

        int i = 0;

        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }

}
