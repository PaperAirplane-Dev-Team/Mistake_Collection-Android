package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentAddMistake0 extends Fragment {

	private LinearLayout mLayout;
	private final static String TAG = "FragmentAddMistake0";
	private static FragmentAddMistake0 sInstance;

	@Deprecated
	public FragmentAddMistake0() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	public static FragmentAddMistake0 getInstance() {
		if (sInstance == null)
			sInstance = new FragmentAddMistake0();
		return sInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_addmistake_first,
				null);
		return mLayout;
	}
	

}
