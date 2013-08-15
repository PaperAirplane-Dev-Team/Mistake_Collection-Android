package org.papdt.miscol.ui;

import org.papdt.miscol.ui.fragment.FragmentCategories;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.util.Log;

public class MistakesTabListener implements ActionBar.TabListener {
	public final static int TAGS = 0;
	public final static int SUBJECTS = 1;

	private final String TAG = "MistakesTabListener";

	private FragmentCategories mFragment;

	public MistakesTabListener(FragmentCategories fragment) {
		mFragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch ((Integer) tab.getTag()) {
		case TAGS:
			Log.d(TAG, "标签 Tab 被选中");
			mFragment.fillContentAsTagIndex();
			break;
		case SUBJECTS:
			Log.d(TAG, "年级/科目 Tab 被选中");
			mFragment.fillContentAsGradeIndex();
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

}