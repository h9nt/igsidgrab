package com.vardxg.instatest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

// Fuck u if u rev & change anything bitch
public class msec {
    public static  void sec(Context context) {
        if (!isCurrentApp(context)) {
            System.exit(0);
        } else {

        }
    }

    private static boolean isCurrentApp(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String currentPackageName = packageInfo.packageName;
            return currentPackageName.equals("com.vardxg.instatest");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
