package me.ycdev.android.demo.lollipopdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import me.ycdev.android.demo.lollipopdemo.usage.apps.AppsUsageViewerActivity;
import me.ycdev.android.demo.lollipopdemo.usage.events.UsageEventsViewerActivity;
import me.ycdev.android.demo.lollipopdemo.utils.Constants;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void initViews() {
        Button appsUsageViewer = (Button) findViewById(R.id.usage_apps_viewer);
        appsUsageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AppsUsageViewerActivity.class);
            }
        });

        Button usageEventsViewer = (Button) findViewById(R.id.usage_events_viewer);
        usageEventsViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UsageEventsViewerActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_notify) {
            testNotification();
        }

        return super.onOptionsItemSelected(item);
    }

    private Notification getSimpleNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("simple title");
        builder.setContentText("simple text");
        return builder.build();
    }

    private void testNotification() {
        Bitmap appIcon = ((BitmapDrawable) getDrawable(R.drawable.ic_launcher)).getBitmap();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("test title");
        builder.setContentText("test text");
        builder.setSubText("Set the third line of text in the platform notification template.");
        builder.setStyle(new Notification.BigTextStyle(builder).bigText("Helper class for generating" +
                " large-format notifications that include a lot of text." +
                " Here's how you'd set the BigTextStyle on a notification."));
        builder.setSmallIcon(android.R.drawable.stat_notify_chat);
        builder.addAction(android.R.drawable.ic_menu_send, "send", null);
        builder.setLargeIcon(appIcon);
        builder.setNumber(5);
        // the following two lines to make the notification to be a "hands-up"
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setVibrate(new long[] {1, 2, 3, 4});
        builder.setColor(0xffaa0000); // new API
        builder.setPublicVersion(getSimpleNotification()); // new API
        builder.setVisibility(Notification.VISIBILITY_SECRET); // new API
        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(Constants.NOTIFICATION_TAG_APP, Constants.NOTIFICATION_ID_MAIN_TEST, notification);

    }
}
