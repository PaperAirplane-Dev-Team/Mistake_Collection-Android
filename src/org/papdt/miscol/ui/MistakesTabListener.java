package org.papdt.miscol.ui;

import org.papdt.miscol.ui.fragment.FragmentMistakes;
import org.papdt.miscol.utils.MyLogger;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.FragmentTransaction;

public class MistakesTabListener implements ActionBar.TabListener {
	public final static int TAGS = 0;
	public final static int SUBJECTS = 1;

	private final String TAG = "MistakesTabListener";

	private FragmentMistakes mFragment;

	public MistakesTabListener(FragmentMistakes fragment) {
		mFragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch ((Integer) tab.getTag()) {
		case TAGS:
			MyLogger.d(TAG, "标签 Tab 被选中");
			mFragment.fillContentAsTagIndex();
			break;
		case SUBJECTS:
			MyLogger.d(TAG, "年级/科目 Tab 被选中");
			mFragment.fillContentAsSubjectIndex();
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

}