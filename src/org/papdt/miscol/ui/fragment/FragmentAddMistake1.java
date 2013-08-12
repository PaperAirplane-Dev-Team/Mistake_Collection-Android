package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentAddMistake1 extends Fragment {

	private LinearLayout mLayout;
	private final static String TAG = "FragmentAddMistake1";

	public FragmentAddMistake1() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_addmistake_second,
				null);
		return mLayout;
	}
	

}
