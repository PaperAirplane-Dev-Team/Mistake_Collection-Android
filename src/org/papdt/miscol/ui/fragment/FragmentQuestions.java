package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentQuestions extends Fragment {
	private static FragmentQuestions INSTANCE;
	private final static String TAG = "FragmentQuestions";
//	XXX 我的View哪去了……
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cards, null);
		return v;
	}
	
	@Deprecated
	public FragmentQuestions() {
		MyLogger.d(TAG, TAG + "被初始化");
	}
	
	public static FragmentQuestions getInstance(){
		if(INSTANCE == null){
			INSTANCE = new FragmentQuestions();
		}
		return INSTANCE;
	}
	
}
