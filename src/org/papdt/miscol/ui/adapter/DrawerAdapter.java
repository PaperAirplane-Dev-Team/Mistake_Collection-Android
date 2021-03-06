package org.papdt.miscol.ui.adapter;

import org.papdt.miscol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
	private String[] mDatas;
	private Context mCtx;

	public interface IDrawerNames {
		int MAIN = 0; // 要不以后这么样,常量不要那么多,用接口
		int MISTAKES = 1; // fython : 嗯!
		int TEST = 2;
		int FORTH = 3;
		int FIFTH = 4;
		int ABOUT = 5;
		String[] TAGS = { "Welcome", "Mistakes", "Test", "Forth", "Fifth",
				"About" };
	}

	public DrawerAdapter(String[] datas, Context ctx) {
		mDatas = datas;
		mCtx = ctx;
	}

	@Override
	public int getCount() {

		return mDatas.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mDatas[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	static class ViewHolder {
		ImageView ivIcon;
		TextView tvText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mCtx).inflate(
					R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.drawer_item_image);
			holder.tvText = (TextView) convertView
					.findViewById(android.R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvText.setText(mDatas[position]);
		switch (position) {
		// TODO 根据IDrawerName的项目分配图标
		}
		return convertView;
	}

}
