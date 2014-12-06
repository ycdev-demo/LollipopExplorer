package me.ycdev.android.demo.lollipopdemo.usage.apps;

import java.text.Collator;
import java.util.Comparator;

public class AppsUsageItem {
    public String pkgName;
    public String appName;
    public long lastStartup;
    public String lastStartupStr;
    public long fgTime;
    public String fgTimeStr;

    public static class AppNameComparator implements Comparator<AppsUsageItem> {
        private Collator mCollator = Collator.getInstance();

        @Override
        public int compare(AppsUsageItem lhs, AppsUsageItem rhs) {
            return mCollator.compare(lhs.appName, rhs.appName);
        }
    }

    public static class LastStartupComparator implements Comparator<AppsUsageItem> {
        @Override
        public int compare(AppsUsageItem lhs, AppsUsageItem rhs) {
            if (lhs.lastStartup < rhs.lastStartup) {
                return 1;
            } else if (lhs.lastStartup > rhs.lastStartup) {
                return -1;
            }
            return 0;
        }
    }

    public static class FgTimeComparator implements Comparator<AppsUsageItem> {
        @Override
        public int compare(AppsUsageItem lhs, AppsUsageItem rhs) {
            if (lhs.fgTime < rhs.fgTime) {
                return 1;
            } else if (lhs.fgTime > rhs.fgTime) {
                return -1;
            }
            return 0;
        }
    }
}
