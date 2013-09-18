package org.papdt.miscol.ui.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.Tags;
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
public class FragmentCategories extends Fragment implements OnClickListener {
	private static FragmentCategories sInstance;
	private CardUI mCardUI;
	private Button mAddButton;
	private SearchView mSearchView;
	private CategoryCard[] mCategories;
	private DatabaseHelper mDbHelper;
	private int mCurrentTab;

	private final static String TAG = "FragmentCategories";

	public static final int TAGS = 0, GRADES = 1;

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
				return (onQueryTextSubmit(newText));
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
				CategoryCard[] cards = filtCategory(
						queryCategoryCards(mCurrentTab), query.trim());
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

			private CategoryCard[] filtCategory(CategoryCard[] source,
					String query) {
				CategoryCard[] result = new CategoryCard[0];
				HashSet<CategoryCard> matchedCat = new HashSet<CategoryCard>();
				for (CategoryCard cat : source) {
					if (cat.getTitle().toLowerCase(Locale.getDefault())
							.contains(query.toLowerCase(Locale.getDefault())))
						matchedCat.add(cat);
				}
				result = matchedCat.toArray(result);
				return result;
			}

		});
		super.onPrepareOptionsMenu(menu);
	}

	@Deprecated
	public FragmentCategories() {
		Log.d(TAG, TAG + "被初始化");
	}

	private String getTableName(int category) {
		return (category == TAGS) ? Tags.TABLE_NAME : Grades.TABLE_NAME;
	}

	private CategoryCard[] queryCategoryCards(int category) {
		String tableName = getTableName(category);
		CategoryInfo[] info = mDbHelper.getCategoryInfo(tableName);
		ArrayList<CategoryCard> allTags = getCategoryCards(info);
		CategoryCard[] cats = new CategoryCard[0];
		if (allTags != null) {
			int size = allTags.size();
			Log.d(TAG, "获取到Tag list长度为" + size);
			cats = new CategoryCard[size];
			// 初始化数组我求你了...亏我还去掉了迭代器原来是这个坑爹东西
			for (int i = 0; i < size; i++) {
				cats[i] = allTags.get(i);
				cats[i].setOnClickListener(this);
				cats[i].setmBindedObject(new Object[] { info[i], TAGS });
			}
		}
		return cats;
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

	private ArrayList<CategoryCard> getCategoryCards(CategoryInfo[] info) {
		if (info.length == 0 || info == null) {
			return null;
		}
		ArrayList<CategoryCard> tags = new ArrayList<CategoryCard>();
		for (CategoryInfo temp : info) {
			if (temp.getSubCount() == CategoryInfo.NULL) {
				Log.d(TAG, "添加CategoryCard, 名称:" + temp.getName() + ",数量"
						+ temp.getCount());
				tags.add(new CategoryCard(temp.getName(), temp.getCount()));
			} else {
				Log.d(TAG, "添加CategoryCard, 名称:" + temp.getName() + ",数量"
						+ temp.getCount() + ",科目数量" + temp.getSubCount());
				tags.add(new CategoryCard(temp.getName(), temp.getCount(), temp
						.getSubCount()));
			}
		}
		return tags;
	}

	@Override
	public void onClick(View v) {
		Object[] objs = (Object[]) v.getTag();
		showMistakes((CategoryInfo) objs[0], (Integer) objs[1]);
	}

	private void showMistakes(CategoryInfo info, int type) {
		QueryCondition c = new QueryCondition();
		switch (type) {
		case TAGS:
			c.setTagIds(new Integer[] { mDbHelper.convertItemNameToId(
					info.getName(), Tags.TABLE_NAME) });
			break;
		case GRADES:
			c.setGradeIds(new Integer[] { mDbHelper.convertItemNameToId(
					info.getName(), Grades.TABLE_NAME) });
			break;
		}
		Mistake[] m = mDbHelper.queryMistakesByCondition(c);
		Log.d(TAG, "查询到" + m.length + "个符合条件的Mistake");
		// 将同一分类中的Mistake封装到Fragment的Argument中，并切换至FragmentMistakes
		// TODO 对于[年级/科目]下的分类，点击之后应进入[科目]划分的分类列表
		Bundle b = new Bundle();
		b.putParcelableArray(FragmentMistakes.KEY, m);
		Fragment fragment = new FragmentMistakes();
		fragment.setArguments(b);
		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		getFragmentManager().beginTransaction().addToBackStack(TAG)
				.replace(R.id.fl_content, fragment).commit();
	}

}
