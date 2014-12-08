package me.ycdev.android.demo.lollipopdemo.usage.events;

import android.app.Activity;
import android.app.usage.UsageEvents;
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

import me.ycdev.android.demo.lollipopdemo.R;
import me.ycdev.android.demo.lollipopdemo.usage.utils.UsageStatsUtils;
import me.ycdev.android.lib.common.utils.DateTimeUtils;

public class UsageEventsViewerActivity extends Activity {
    private static final String TAG = "AppsUsageViewerActivity";

    private TextView mTimeSpanView;
    private ListView mListView;
    private UsageEventsViewerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_usage_viewer);

        initViews();
        if (!UsageStatsUtils.checkUsageStatsPermission(this)) {
            UsageStatsUtils.showPermissionDialog(this);
        }
    }

    private void initViews() {
        mTimeSpanView = (TextView) findViewById(R.id.stats_timespan);
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new UsageEventsViewerAdapter(getLayoutInflater());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UsageStatsUtils.checkUsageStatsPermission(this)) {
            new LoadTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usage_events_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_app_name) {
            mAdapter.sort(new UsageEventsItem.AppNameComparator());
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_sort_by_time) {
            mAdapter.sort(new UsageEventsItem.TimeStampComparator());
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_settings) {
            UsageStatsUtils.launchUsageStatsSettings(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadTask extends AsyncTask<Void, Void, List<UsageEventsItem>> {
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
        protected List<UsageEventsItem> doInBackground(Void... params) {
            //noinspection ResourceType
            UsageStatsManager usageStatsMgr = (UsageStatsManager) getSystemService("usagestats");
            UsageEvents events = usageStatsMgr.queryEvents(mStartTime, mEndTime);
            List<UsageEventsItem> results = new ArrayList<>();
            UsageEvents.Event event = new UsageEvents.Event();
            PackageManager pm = getPackageManager();
            while (events.getNextEvent(event)) {
                UsageEventsItem item = new UsageEventsItem();
                item.pkgName = event.getPackageName();
                item.className = event.getClassName();
                item.type = event.getEventType();
                item.timeStamp = event.getTimeStamp();
                item.appName = item.pkgName;
                try {
                    item.appName = pm.getApplicationInfo(item.pkgName, 0).loadLabel(pm).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                results.add(item);
            }
            Collections.sort(results, new UsageEventsItem.AppNameComparator());
            return results;
        }

        @Override
        protected void onPostExecute(List<UsageEventsItem> resuls) {
            mAdapter.setData(resuls);
        }
    }
}
