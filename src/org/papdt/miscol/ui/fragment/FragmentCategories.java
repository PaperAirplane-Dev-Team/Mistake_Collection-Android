package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.CategoryCard;
import org.papdt.miscol.ui.InfoCard;

import com.fima.cardsui.views.CardUI;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * 用于显示 [标签]、[年级/科目] 的全部分类的Fragment
 * 
 */
public class FragmentCategories extends AbsFragmentCategories implements
		CategoryInfo.TYPE {

	private final int NULL = -1;

	private static FragmentCategories sInstance;
	private Button mAddButton;
	private SearchView mSearchView;
	private DatabaseHelper mDbHelper;
	private int mCurrentTab = NULL;

	private final static String TAG = "FragmentCategories";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		this.mDbHelper = DatabaseHelper.getInstance(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_categories, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
		mAddButton = (Button) v.findViewById(R.id.btn_add);
		mAddButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), ActivityAddMistake.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
			}

		});
		if (mCurrentTab != NULL) {
			switch (mCurrentTab) {
			case TAGS:
				fillContentAsTagIndex();
				break;
			case GRADES:
				fillContentAsGradeIndex();
				break;
			}
		}
		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
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
			public boolean onQueryTextChange(String newText) {
				return onQueryTextSubmit(newText);
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (TextUtils.isEmpty(query)) {
					if ((mCurrentTab == TAGS)) {
						fillContentAsTagIndex();
					} else {
						fillContentAsGradeIndex();
					}
					return true;
				}

				Log.i(TAG, "用户搜索关键词: " + query);
				CategoryCard[] cards = filtCategory(mCategories, query.trim());
				if (cards.length > 0) {
					mCategories = cards;
					fillContentsToView();
					return true;
					// 不确定要不要return true.
				}
				InfoCard c = new InfoCard(getString(R.string.no_match_title),
						getString(R.string.no_match_category));
				mCardUI.clearCards();
				mCardUI.addCard(c);
				mCardUI.refresh();
				return false;
			}

		});
		super.onPrepareOptionsMenu(menu);
	}

	@Deprecated
	public FragmentCategories() {
		Log.d(TAG, TAG + "被初始化");
	}

	private CategoryCard[] queryCategoryCards(int category) {
		CategoryInfo[] info = mDbHelper.getCategoryInfo(category);
		return processCategoryCard(info);
	}

	/**
	 * Called when TAGS tab is selected.
	 */
	public void fillContentAsTagIndex() {
		mCurrentTab = TAGS;
		CategoryCard[] tags = queryCategoryCards(TAGS);
		if (tags.length > 0) {
			mCategories = tags;
			fillContentsToView();
		} else {
			InfoCard card = new InfoCard(
					getString(R.string.no_category_hint_title),
					getString(R.string.no_tag_hint));
			mCardUI.clearCards();
			mCardUI.addCard(card);
			mCardUI.refresh();
		}
	}

	/**
	 * Called when GRADE tab is selected.
	 */
	public void fillContentAsGradeIndex() {
		mCurrentTab = GRADES;
		CategoryCard[] grades = queryCategoryCards(GRADES);
		if (grades.length > 0) {
			mCategories = grades;
			fillContentsToView();
		} else {
			InfoCard card = new InfoCard(
					getString(R.string.no_category_hint_title),
					getString(R.string.no_subject_hint));
			mCardUI.clearCards();
			mCardUI.addCard(card);
			mCardUI.refresh();
		}
	}

	public static FragmentCategories getInstance() {
		if (sInstance == null) {
			sInstance = new FragmentCategories();
		}
		return sInstance;
	}

	@Override
	public void onClick(View v) {
		showMistakes((CategoryInfo) v.getTag());
	}

	private void showMistakes(CategoryInfo info) {
		QueryCondition c = new QueryCondition();
		switch (info.getType()) {
		case TAGS:
			c.setTagIds(new Integer[] { mDbHelper.convertItemNameToId(
					info.getName(), TAGS) });
			break;
		case GRADES:
			c.setGradeIds(new Integer[] { mDbHelper.convertItemNameToId(
					info.getName(), GRADES) });
			break;
		}
		Mistake[] m = mDbHelper.queryMistakes(c);
		Log.d(TAG, "查询到" + m.length + "个符合条件的Mistake");
		// 将同一分类中的Mistake封装到Fragment的Argument中，并切换至FragmentMistakes
		Bundle b = new Bundle();
		b.putParcelableArray(FragmentMistakes.KEY, m);
		Fragment fragment = (info.getType() == TAGS) ? new FragmentMistakes()
				: new FragmentSubjects();
		fragment.setArguments(b);
		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		getFragmentManager().beginTransaction().addToBackStack(TAG)
				.replace(R.id.fl_content, fragment).commit();
	}

}
