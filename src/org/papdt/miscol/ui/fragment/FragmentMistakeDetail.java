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

/*
 * P.S.洗澡之后挖耳朵，湿湿一大坨。
 * 
 * 乱曰：
 * 呜呼哀哉！感天之错勘贤愚，慨地之不分好歹。时运不齐，命途多舛。良辰未现，伊人难觅。感我痴人，良苦用心，苦待女神呵呵，殚精竭虑，空赢观者滔滔。失魂至此
 * ，独怆然悲叹而已矣。
 */
