package me.ycdev.android.demo.lollipopdemo.usage.events;

import java.text.Collator;
import java.util.Comparator;

public class UsageEventsItem {
    public String appName;
    public String pkgName;
    public String className;
    public int type;
    public long timeStamp;

    public static class AppNameComparator implements Comparator<UsageEventsItem> {
        private Collator mCollator = Collator.getInstance();
        private TimeStampComparator mTimeComparator = new TimeStampComparator();

        @Override
        public int compare(UsageEventsItem lhs, UsageEventsItem rhs) {
            int result = mCollator.compare(lhs.appName, rhs.appName);
            if (result == 0) {
                result = mTimeComparator.compare(lhs, rhs);
            }
            return result;
        }
    }

    public static class TimeStampComparator implements Comparator<UsageEventsItem> {
        @Override
        public int compare(UsageEventsItem lhs, UsageEventsItem rhs) {
            if (lhs.timeStamp < rhs.timeStamp) {
                return 1;
            } else if (lhs.timeStamp > rhs.timeStamp) {
                return -1;
            }
            return 0;
        }
    }

}
