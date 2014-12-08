package me.ycdev.android.demo.lollipopdemo.usage.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import me.ycdev.android.demo.lollipopdemo.R;

public class UsageStatsUtils {
    public static boolean checkUsageStatsPermission(Context cxt) {
        // noinspection ResourceType
        AppOpsManager appOpsMgr = (AppOpsManager) cxt.getSystemService(Context.APP_OPS_SERVICE);
        int result = appOpsMgr.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                cxt.getApplicationInfo().uid, cxt.getPackageName());
        return result == AppOpsManager.MODE_ALLOWED;
    }

    public static void showPermissionDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.usage_stats_perm_request_title);
        builder.setMessage(R.string.usage_stats_perm_request_message);
        builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchUsageStatsSettings(activity);
            }
        });
        builder.setNegativeButton(R.string.button_no, null);
        builder.create().show();
    }

    public static void launchUsageStatsSettings(Context cxt) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        cxt.startActivity(intent);
    }
}
