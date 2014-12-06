package me.ycdev.android.demo.lollipopdemo.usage.apps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.ycdev.android.demo.lollipopdemo.R;
import me.ycdev.android.lib.common.ui.base.ListAdapterBase;

public class AppsUsageViewerAdapter extends ListAdapterBase<AppsUsageItem> {
    public AppsUsageViewerAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    protected int getItemResId() {
        return R.layout.apps_usage_item;
    }

    @Override
    protected ViewHolderBase createViewHolder(View itemView, int position) {
        return new ViewHolder(itemView, position);
    }

    @Override
    protected void bindView(AppsUsageItem item, ViewHolderBase holder) {
        ViewHolder vh = (ViewHolder) holder;
        vh.appNameView.setText(item.appName);
        vh.pkgNameView.setText(item.pkgName);
        vh.lastStartupView.setText(item.lastStartupStr);
        vh.fgTimeView.setText(item.fgTimeStr);
    }

    private static class ViewHolder extends ViewHolderBase {
        public TextView appNameView;
        public TextView pkgNameView;
        public TextView lastStartupView;
        public TextView fgTimeView;

        public ViewHolder(View itemView, int position) {
            super(itemView, position);
        }

        @Override
        protected void findViews() {
            appNameView = (TextView) itemView.findViewById(R.id.app_name);
            pkgNameView = (TextView) itemView.findViewById(R.id.pkg_name);
            lastStartupView = (TextView) itemView.findViewById(R.id.last_startup);
            fgTimeView = (TextView) itemView.findViewById(R.id.fg_time);
        }
    }
}
