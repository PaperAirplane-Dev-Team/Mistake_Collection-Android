package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;
import org.papdt.miscol.ui.CategoryCard;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class FragmentMistakes extends Fragment {
	private static FragmentMistakes sInstance;
	private CardUI mCardUI;
	private SearchView mSearchView;
	private CategoryCard[] mCategories;
	private final static String TAG = "FragmentMistakes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mistakes, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
		return v;
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();// 还不能忘了加这句
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fragment_main, menu);
		mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO 完善搜索功能
				MyLogger.i(TAG, "用户搜索关键词: " + arg0);
				return false;
			}
			
		});
		super.onPrepareOptionsMenu(menu);
	}
	
	@Deprecated
	public FragmentMistakes() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	public void fillContentAsTagIndex() {
		mCategories = new CategoryCard[3];
		mCategories[0] = new CategoryCard("#第一次月考", 3);
		mCategories[1] = new CategoryCard("#语文测验", 4);
		mCategories[2] = new CategoryCard("#数学第三次周测", 2);
		fillContentsToView();
	}

	public void fillContentAsSubjectIndex() {
		mCategories = new CategoryCard[3];
		mCategories[0] = new CategoryCard("初二", 3, 2);
		mCategories[1] = new CategoryCard("初三", 10, 2);
		mCategories[2] = new CategoryCard("高一", 8, 3);
		fillContentsToView();
	}

	private void fillContentsToView() {
		mCardUI.clearCards();
		if (mCategories != null) {
			for (CategoryCard cs : mCategories) {
				mCardUI.addCard(cs);
			}
		}
		mCardUI.refresh();
	}

	public static FragmentMistakes getInstance() {
		if (sInstance == null) {
			sInstance = new FragmentMistakes();
		}
		return sInstance;
	}

}
