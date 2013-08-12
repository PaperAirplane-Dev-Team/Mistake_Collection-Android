package org.papdt.miscol.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.utils.MyLogger;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.CategoryCard;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class FragmentCategories extends Fragment {
	private static FragmentCategories sInstance;
	private CardUI mCardUI;
	private LinearLayout mAddButton;
	private SearchView mSearchView;
	private CategoryCard[] mCategories;
	private DatabaseHelper mDbHelper;

	private final static String TAG = "FragmentMistakes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		this.mDbHelper = DatabaseHelper.getInstance(getActivity());
		super.onCreate(savedInstanceState);
//		addSampleData();
	}

	private void addSampleData() {
		MyLogger.d(TAG, "添加测试数据");
		Mistake m = new Mistake("测试","呵呵呵呵呵");
		m.setGradeName("高一");
		m.setSubjectName("节操");
		m.setTagNames(new String[]{"Demo"});
		try {
			mDbHelper.insertMistake(m);
		} catch (MistakeOperationException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mistakes, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
		mAddButton = (LinearLayout) v.findViewById(R.id.ll_addmistake);
		mAddButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent =
						new Intent(
								getActivity().getApplicationContext(),
								ActivityAddMistake.class
								);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
			}
			
		});
		return v;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();// 还不能忘了加这句
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fragment_main, menu);
		mSearchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

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
	public FragmentCategories() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	public void fillContentAsTagIndex() {
		ArrayList<CategoryCard> allTags = mDbHelper.getAllTags();
		if (allTags != null) {
			Iterator<CategoryCard> iterator = allTags.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				mCategories[i++] = iterator.next();
			}
		} else {
			mCategories = new CategoryCard[3];
			mCategories[0] = new CategoryCard("#第一次月考", 3);
			mCategories[1] = new CategoryCard("#语文测验", 4);
			mCategories[2] = new CategoryCard("#数学第三次周测", 2);
		}
		fillContentsToView();
	}

	public void fillContentAsSubjectIndex() {
		ArrayList<CategoryCard> allSubjects = mDbHelper.getAllGrades();
		if (allSubjects != null) {
			Iterator<CategoryCard> iterator = allSubjects.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				mCategories[i++] = iterator.next();
			}
		} else {
			mCategories = new CategoryCard[3];
			mCategories[0] = new CategoryCard("初二", 3, 2);
			mCategories[1] = new CategoryCard("初三", 10, 2);
			mCategories[2] = new CategoryCard("高一", 8, 3);
		}
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

	public static FragmentCategories getInstance() {
		if (sInstance == null) {
			sInstance = new FragmentCategories();
		}
		return sInstance;
	}

}
