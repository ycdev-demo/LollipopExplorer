package me.ycdev.android.demo.lollipopdemo.usage.apps;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.ycdev.android.demo.lollipopdemo.R;
import me.ycdev.android.demo.lollipopdemo.utils.AppLogger;
import me.ycdev.android.lib.common.utils.DateTimeUtils;

public class AppsUsageViewerActivity extends Activity {
    private static final String TAG = "AppsUsageViewerActivity";

    private TextView mTimeSpanView;
    private ListView mListView;
    private AppsUsageViewerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_usage_viewer);

        initViews();
        new LoadTask().execute();
    }

    private void initViews() {
        mTimeSpanView = (TextView) findViewById(R.id.stats_timespan);
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new AppsUsageViewerAdapter(getLayoutInflater());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apps_usage_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_app_name) {
            mAdapter.sort(new AppsUsageItem.AppNameComparator());
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_sort_by_time) {
            mAdapter.sort(new AppsUsageItem.LastStartupComparator());
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_sort_by_time_used) {
            mAdapter.sort(new AppsUsageItem.FgTimeComparator());
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadTask extends AsyncTask<Void, Void, List<AppsUsageItem>> {
        private long mStartTime;
        private long mEndTime;

        @Override
        protected void onPreExecute() {
            // cur month usage
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            mStartTime = calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.MILLISECOND, -1);
            mEndTime = calendar.getTimeInMillis();

            String startStr = DateTimeUtils.getReadableTimeStamp(mStartTime);
            String endStr = DateTimeUtils.getReadableTimeStamp(mEndTime);
            String timeInfo = getString(R.string.apps_usage_timespan, startStr, endStr);
            mTimeSpanView.setText(timeInfo);
        }

        @Override
        protected List<AppsUsageItem> doInBackground(Void... params) {
            //noinspection ResourceType
            UsageStatsManager usageStatsMgr = (UsageStatsManager) getSystemService("usagestats");
            Map<String, UsageStats> usagesList = usageStatsMgr.queryAndAggregateUsageStats(
                    mStartTime, mEndTime);
            AppLogger.i(TAG, "usages stat list size: " + usagesList.size());
            List<AppsUsageItem> results = new ArrayList<AppsUsageItem>();
            PackageManager pm = getPackageManager();
            for (UsageStats usage : usagesList.values()) {
                AppsUsageItem item = new AppsUsageItem();
                item.pkgName = usage.getPackageName();
                item.lastStartup = usage.getLastTimeUsed();
                item.lastStartupStr = getString(R.string.apps_usage_last_startup,
                        DateTimeUtils.generateFileName(item.lastStartup));
                item.fgTime = usage.getTotalTimeInForeground();
                item.fgTimeStr = getString(R.string.apps_usage_fg_time_used,
                        DateTimeUtils.getReadableTimeUsage(item.fgTime));
                item.appName = item.pkgName;
                try {
                    item.appName = pm.getApplicationInfo(item.pkgName, 0).loadLabel(pm).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                results.add(item);
            }
            Collections.sort(results, new AppsUsageItem.AppNameComparator());
            return results;
        }

        @Override
        protected void onPostExecute(List<AppsUsageItem> results) {
            mAdapter.setData(results);
        }
    }

}
