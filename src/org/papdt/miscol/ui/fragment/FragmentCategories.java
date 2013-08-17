package org.papdt.miscol.ui.fragment;

import java.util.ArrayList;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.Mistakes;
import org.papdt.miscol.utils.Constants.Databases.Tags;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.CategoryCard;

import com.fima.cardsui.views.CardUI;

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

public class FragmentCategories extends Fragment {
	private static FragmentCategories sInstance;
	private CardUI mCardUI;
	private Button mAddButton;
	private SearchView mSearchView;
	private CategoryCard[] mCategories;
	private DatabaseHelper mDbHelper;

	private final static String TAG = "FragmentMistakes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		this.mDbHelper = DatabaseHelper.getInstance(getActivity());
		super.onCreate(savedInstanceState);
		// addSampleData(); //Tested
	}

	@SuppressWarnings("unused")
	private void addSampleData() {
		Log.d(TAG, "添加测试数据");
		Mistake m = new Mistake("测试", "呵呵呵呵呵");
		m.setGradeName("高一");
		m.setSubjectName("节操");
		m.setTagNames(new String[] { "Demo" });
		m.setTypeName("填空题");
		m.setAnswerText("Xavier");
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
		ArrayList<CategoryCard> allTags = getCategoryCards(mDbHelper
				.getCategoryInfo(Tags.TABLE_NAME, Mistakes.KEY_STRING_TAG_IDS,
						true));
		if (allTags != null) {
			int size = allTags.size();
			Log.d(TAG, "获取到list长度为" + size);
			mCategories = new CategoryCard[size];
			// 初始化数组我求你了...亏我还去掉了迭代器原来是这个坑爹东西
			for (int i = 0; i < size; i++) {
				mCategories[i] = allTags.get(i);
				// FIXED
			}
		} else {
			mCategories = new CategoryCard[3];
			mCategories[0] = new CategoryCard("#第一次月考", 3);
			mCategories[1] = new CategoryCard("#语文测验", 4);
			mCategories[2] = new CategoryCard("#数学第三次周测", 2);
		}
		fillContentsToView();
	}

	public void fillContentAsGradeIndex() {
		ArrayList<CategoryCard> allGrades = getCategoryCards(mDbHelper
				.getCategoryInfo(Grades.TABLE_NAME, Mistakes.KEY_INT_GRADE_ID));
		if (allGrades != null) {
			int size = allGrades.size();
			Log.d(TAG, "获取到list长度为" + size);
			mCategories = new CategoryCard[size];
			for (int i = 0; i < size; i++) {
				mCategories[i] = allGrades.get(i);
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

	private ArrayList<CategoryCard> getCategoryCards(CategoryInfo[] info) {
		if (info.length == 0 || info == null) {
			return null;
		}
		ArrayList<CategoryCard> tags = new ArrayList<CategoryCard>();
		for (CategoryInfo temp : info) {
			Log.d(TAG,
					"添加CategoryCard:" + temp.getName() + " " + temp.getCount());
			tags.add(new CategoryCard(temp.getName(), temp.getCount()));
		}
		return tags;
	}

}
