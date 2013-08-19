package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class FragmentMistakes extends Fragment {
	private CardUI mCardUI;
	private SearchView mSearchView;

	private static final String TAG = "FragmentMistakes";

	public FragmentMistakes() {
		Log.d(TAG, TAG + "被初始化");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_categories, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
		return v;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();// 还不能忘了加这句
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_categories, menu);
		mSearchView = (SearchView) menu.getItem(0).getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO 完善搜索功能
				Log.i(TAG, "用户搜索关键词: " + arg0);
				return false;
			}

		});
		super.onPrepareOptionsMenu(menu);
	}
}
