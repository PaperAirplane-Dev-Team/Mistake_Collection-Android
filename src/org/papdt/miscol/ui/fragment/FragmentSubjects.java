package org.papdt.miscol.ui.fragment;

import java.util.HashMap;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.CategoryInfo.TYPE;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.CategoryCard;
import org.papdt.miscol.ui.InfoCard;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * 用于显示某一Grade中全部Subject的Fragment
 * 
 */
public class FragmentSubjects extends AbsFragmentCategories implements TYPE {
	private SearchView mSearchView;
	private String mGradeName;

	private static final String TAG = "FragmentSubjects";

	public FragmentSubjects() {
		Log.d(TAG, TAG + "被初始化");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_categories, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
		Button btnAdd = (Button) v.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), ActivityAddMistake.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Mistake m = new Mistake();
				m.setGradeName(mGradeName);
				Bundle b = new Bundle();
				b.putParcelable(ActivityAddMistake.KEY, m);
				getActivity().startActivity(intent);
			}

		});
		fillDatas();
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
				// TODO 完善搜索功能
				Log.i(TAG, "用户搜索关键词: " + query);
				if(TextUtils.isEmpty(query)){
					//等Android 支持Java 8了这可以优化一下
					fillDatas();
					return true;
				}
				CategoryCard[] cards = filtCategory(mCategories, query.trim());
				if (cards.length > 0) {
					mCategories = cards;
					fillContentsToView();
					return true;
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

	private void fillDatas() {
		Mistake[] args = (Mistake[]) getArguments().getParcelableArray(KEY);
		HashMap<String, CategoryInfo> subjects = new HashMap<String, CategoryInfo>();
		for (Mistake m : args) {
			if (subjects.containsKey(m.getSubjectName())) {
				subjects.get(m.getSubjectName()).addCount();
			} else {
				CategoryInfo ci = new CategoryInfo();
				ci.setName(m.getSubjectName());
				ci.setId(m.getSubjectId());
				ci.setCount(1);
				subjects.put(ci.getName(), ci);
			}
		}
		mCategories = processCategoryCard(subjects.values().toArray(
				new CategoryInfo[0]));
		fillContentsToView();
	}

	@Override
	public void onClick(View v) {
		CategoryInfo info = (CategoryInfo) v.getTag();
		QueryCondition c = new QueryCondition();
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity());
		c.setGradeIds(new Integer[]{dbHelper.convertItemNameToId(info.getName(), SUBJECTS)});
		Mistake[] m = dbHelper.queryMistakes(c);
		Bundle b = new Bundle();
		b.putParcelableArray(KEY, m);
		Fragment fragment = new FragmentMistakes();
		fragment.setArguments(b);
		getFragmentManager().beginTransaction().addToBackStack(TAG)
		.replace(R.id.fl_content, fragment).commit();
	}
}
