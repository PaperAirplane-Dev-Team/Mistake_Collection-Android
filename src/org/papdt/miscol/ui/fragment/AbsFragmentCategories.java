package org.papdt.miscol.ui.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.ui.CategoryCard;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;

public abstract class AbsFragmentCategories extends Fragment implements OnClickListener {
	protected CardUI mCardUI;
	protected CategoryCard[] mCategories;

	public final static String TAG = "AbsFragmentCategories";
	public static final int TAGS = 0, GRADES = 1;

	protected ArrayList<CategoryCard> getCategoryCards(CategoryInfo[] info) {
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

	protected void fillContentsToView() {
		mCardUI.clearCards();
		if (mCategories != null) {
			for (CategoryCard cs : mCategories) {
				mCardUI.addCard(cs);
			}
		}
		mCardUI.refresh();
	}
	
	protected CategoryCard[] processCategoryCard(CategoryInfo[] info){
		ArrayList<CategoryCard> allCats = getCategoryCards(info);
		CategoryCard[] cats = new CategoryCard[0];
		if (allCats != null) {
			int size = allCats.size();
			Log.d(TAG, "获取到Tag list长度为" + size);
			cats = new CategoryCard[size];
			// 初始化数组我求你了...亏我还去掉了迭代器原来是这个坑爹东西
			for (int i = 0; i < size; i++) {
				cats[i] = allCats.get(i);
				cats[i].setOnClickListener(this);
				cats[i].setmBindedObject(new Object[] { info[i], TAGS });
			}
		}
		return cats;
	}
	
	protected CategoryCard[] filtCategory(CategoryCard[] source,
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

}
