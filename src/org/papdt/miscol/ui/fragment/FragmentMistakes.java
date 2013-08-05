package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMistakes extends Fragment {
	private static FragmentMistakes sInstance;
	private final static String TAG = "FragmentQuestions";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		XXX 我的View哪去了……
		return inflater.inflate(R.layout.fragment_mistakes, container,false);
	}
	
	@Deprecated
	public FragmentMistakes() {
		MyLogger.d(TAG, TAG + "被初始化");
	}
	
	public static FragmentMistakes getInstance(){
		if(sInstance == null){
			sInstance = new FragmentMistakes();
		}
		return sInstance;
	}
	
}
