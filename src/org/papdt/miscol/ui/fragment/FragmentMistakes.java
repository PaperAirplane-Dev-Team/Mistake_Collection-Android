package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.InfoCard;
import org.papdt.miscol.ui.MistakeCard;
import org.papdt.miscol.utils.Constants.Preferences.FileNames;
import org.papdt.miscol.utils.Constants.Preferences.Keys;
import org.papdt.miscol.utils.SharedPreferencesOperator;

import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.views.CardUI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * 用于显示某一分类中全部Mistake的Fragment
 * 
 */
public class FragmentMistakes extends Fragment implements OnClickListener {
	private CardUI mCardUI;
	private SearchView mSearchView;
	private Mistake[] mMistakes;

	private static final String TAG = "FragmentMistakes";
	public static final String KEY = "Mistakes";

	public FragmentMistakes() {
		Log.d(TAG, TAG + "被初始化");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_categories, null);
		mCardUI = (CardUI) v.findViewById(R.id.view_cardui);
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

	private void fillDatas() {
		mMistakes = (Mistake[]) getArguments().getParcelableArray(KEY);
		mCardUI.clearCards();
		mCardUI.setSwipeable(true);
		if (!(Boolean) SharedPreferencesOperator.read(getActivity(),
				FileNames.GENERAL, Keys.HAS_EVER_STARTED, false)) {
			InfoCard tap = new InfoCard(getString(R.string.do_you_know),
					getString(R.string.tap_to_see_answer));
			InfoCard swipe = new InfoCard(getString(R.string.swipe_to_del),
					getString(R.string.swipe_to_del_demo));
			mCardUI.addCard(tap);
			mCardUI.addCard(swipe);
			SharedPreferencesOperator.write(getActivity(), FileNames.GENERAL,
					Keys.HAS_EVER_STARTED, true);
		}
		for (final Mistake m : mMistakes) {
			MistakeCard card = new MistakeCard(m);
			card.setOnClickListener(this);
			card.setOnCardSwipedListener(new OnCardSwiped() {
				@Override
				public void onCardSwiped() {
					deleteMistake(m);
				}
			});
			mCardUI.addCard(card);
		}
		mCardUI.refresh();
	}

	private void deleteMistake(final Mistake mistake) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface view, int button) {
				switch (button) {
				case DialogInterface.BUTTON_POSITIVE:
					DatabaseHelper dbHelper = DatabaseHelper.getInstance(null);
					try {
						dbHelper.deleteMistake(mistake);
					} catch (MistakeOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO 独立在Thread
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					view.dismiss();
					fillDatas();
					break;
				}

			}
		};
		new AlertDialog.Builder(getActivity()).setTitle(R.string.del_title)
				.setMessage(R.string.del_content)
				.setIcon(android.R.drawable.ic_input_delete)
				.setPositiveButton(android.R.string.yes, listener)
				.setNegativeButton(android.R.string.cancel, listener).show();
	}

	@Override
	public void onClick(View v) {
		Mistake m = (Mistake) v.getTag();
		Bundle args = new Bundle();
		args.putParcelable(KEY, m);
		Fragment fragment = new FragmentMistakeDetail();
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().addToBackStack(TAG)
		.replace(R.id.fl_content, fragment).commit();
	}

}
