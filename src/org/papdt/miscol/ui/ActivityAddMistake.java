package org.papdt.miscol.ui;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.fragment.FragmentAddMistake0;
import org.papdt.miscol.ui.fragment.FragmentAddMistake1;
import org.papdt.miscol.utils.MyLogger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ActivityAddMistake extends Activity {
	private Fragment[] mFragments;
	private FragmentTransaction mTransaction;
	private FragmentManager mFragmentManager;
	
	private final static int step = 0;
	
	private final static String TAG = "ActivityMain";
	private final static String[] TAGS = {"First", "Second"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmistake);
		mFragmentManager = getFragmentManager();
		mFragments = new Fragment[2];
		initializeActionBar();
		selectStep(step);
		MyLogger.d(TAG, TAG + "已完成初始化");
	}
	
	private void initializeActionBar(){
		getActionBar().setDisplayShowTitleEnabled(false);
	}
	
	private void selectStep(int step){
		boolean initialized;
		mTransaction = mFragmentManager.beginTransaction();
		for (Fragment fragment : mFragments) {
			hideFragment(fragment);
		}
		if (mFragments[step] == null) {
			MyLogger.d(TAG, "创建新的 Fragment:" + step);
			initialized = false;
			switch (step) {
			case 0:
				mFragments[step] = FragmentAddMistake0.getInstance();
				break;
			case 1:
				mFragments[step] = FragmentAddMistake1.getInstance();
				break;
			default:
				mFragments[step] = new Fragment();
				// TODO 初始化各Fragment
				break;
			}
		} else {
			MyLogger.d(TAG, "已存在Fragment:" + step);
			initialized = true;
		}
		replaceToFragment(mFragments[step], initialized, TAGS[step]);
	}
	
	private void replaceToFragment(Fragment fragment, boolean hasInitialized, String tag) {
		if (!hasInitialized) {
			mTransaction.add(R.id.fl_content, fragment,tag);
		}
		mTransaction.attach(fragment).show(fragment).commit();
		mFragmentManager.popBackStack();
		mFragmentManager.executePendingTransactions();
	}

	private void hideFragment(Fragment fragment) {
		if (fragment != null) {
			mTransaction.hide(fragment);
		}

	}
}