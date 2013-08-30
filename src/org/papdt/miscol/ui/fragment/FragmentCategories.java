package org.papdt.miscol.ui.fragment;

import java.util.ArrayList;

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

public class FragmentCategories extends Fragment implements OnClickListener {
	private static FragmentCategories sInstance;
	private CardUI mCardUI;
	private Button mAddButton;
	private SearchView mSearchView;
	private CategoryCard[] mCategories;
	private DatabaseHelper mDbHelper;

	private final static String TAG = "FragmentCategories";

	public static final int TAGS = 0, SUBJECTS = 1, GRADES = 2;

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

	@Deprecated
	public FragmentCategories() {
		Log.d(TAG, TAG + "被初始化");
	}

	public void fillContentAsTagIndex() {
		CategoryInfo[] tagInfo = mDbHelper.getCategoryInfo(Tags.TABLE_NAME);
		ArrayList<CategoryCard> allTags = getCategoryCards(tagInfo);
		if (allTags != null) {
			int size = allTags.size();
			Log.d(TAG, "获取到Tag list长度为" + size);
			mCategories = new CategoryCard[size];
			// 初始化数组我求你了...亏我还去掉了迭代器原来是这个坑爹东西
			for (int i = 0; i < size; i++) {
				mCategories[i] = allTags.get(i);
				mCategories[i].setOnClickListener(this);
				mCategories[i]
						.setmBindedObject(new Object[] { tagInfo[i], TAGS });
			}
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

	public void fillContentAsGradeIndex() {
		CategoryInfo[] gradeInfo = mDbHelper.getCategoryInfo(Grades.TABLE_NAME);
		ArrayList<CategoryCard> allGrades = getCategoryCards(gradeInfo);
		if (allGrades != null) {
			int size = allGrades.size();
			Log.d(TAG, "获取到list长度为" + size);
			mCategories = new CategoryCard[size];
			for (int i = 0; i < size; i++) {
				mCategories[i] = allGrades.get(i);
				mCategories[i].setOnClickListener(this);
				mCategories[i].setmBindedObject(new Object[] { gradeInfo[i],
						GRADES });
			}
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
		// FIXED SQLiteException: near "R" : syntax error
		// 抱歉，当初写Condition的时候没有测试
		Mistake[] m = mDbHelper.queryMistakesByCondition(c);
		Log.d(TAG, "查询到" + m.length + "个符合条件的Mistake");
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
