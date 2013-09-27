package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMistakeDetail extends Fragment {
	private final static String TAG = "FragmentMistakeDetail";
	private final static String KEY = "Mistake";
	
	private Mistake mMistake;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mistake_detail, null);
		fillDatas();
		return v;
	}

	private void fillDatas() {
		// TODO Auto-generated method stub

	}
}
