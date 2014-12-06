package me.ycdev.android.demo.lollipopdemo.usage.events;

import android.app.usage.UsageEvents;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.ycdev.android.demo.lollipopdemo.R;
import me.ycdev.android.lib.common.ui.base.ListAdapterBase;
import me.ycdev.android.lib.common.utils.DateTimeUtils;

public class UsageEventsViewerAdapter extends ListAdapterBase<UsageEventsItem> {
    public UsageEventsViewerAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    protected int getItemResId() {
        return R.layout.usage_events_item;
    }

    @Override
    protected ViewHolderBase createViewHolder(View itemView, int position) {
        return new ViewHolder(itemView, position);
    }

    @Override
    protected void bindView(UsageEventsItem item, ViewHolderBase holder) {
        ViewHolder vh = (ViewHolder) holder;
        vh.appNameView.setText(item.appName);
        vh.pkgNameView.setText(item.pkgName);
        vh.classNameView.setText(item.className);
        if (item.type == UsageEvents.Event.CONFIGURATION_CHANGE) {
            vh.eventTypeView.setText(R.string.usage_events_type_config);
        } else if (item.type == UsageEvents.Event.MOVE_TO_FOREGROUND) {
            vh.eventTypeView.setText(R.string.usage_events_type_goto_fg);
        } else if (item.type == UsageEvents.Event.MOVE_TO_BACKGROUND) {
            vh.eventTypeView.setText(R.string.usage_events_type_goto_bg);
        } else if (item.type == UsageEvents.Event.NONE) {
            vh.eventTypeView.setText(R.string.usage_events_type_none);
        }
        vh.timeStampView.setText(DateTimeUtils.getReadableTimeStamp(item.timeStamp));
    }

    private static class ViewHolder extends ViewHolderBase {
        public TextView appNameView;
        public TextView pkgNameView;
        public TextView classNameView;
        public TextView eventTypeView;
        public TextView timeStampView;

        public ViewHolder(View itemView, int position) {
            super(itemView, position);
        }

        @Override
        protected void findViews() {
            appNameView = (TextView) itemView.findViewById(R.id.app_name);
            pkgNameView = (TextView) itemView.findViewById(R.id.pkg_name);
            classNameView = (TextView) itemView.findViewById(R.id.class_name);
            eventTypeView = (TextView) itemView.findViewById(R.id.event_type);
            timeStampView = (TextView) itemView.findViewById(R.id.time_stamp);
        }
    }
}
