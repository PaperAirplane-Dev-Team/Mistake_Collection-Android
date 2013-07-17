package org.papdt.miscol.adapter;

import org.papdt.miscol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

	private Context mContext;
	private String[] mDrawerItemNames;

	public DrawerAdapter(Context context, String[] drawerItemNames) {
		mContext = context;
		mDrawerItemNames = drawerItemNames;
	}

	@Override
	public int getCount() {
		return mDrawerItemNames.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mDrawerItemNames[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.tvDrawerText = (TextView) convertView
					.findViewById(R.id.tv_drawer_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDrawerText.setText(mDrawerItemNames[position]);
		return convertView;
	}

	private static class ViewHolder {
		TextView tvDrawerText;
	}
}
