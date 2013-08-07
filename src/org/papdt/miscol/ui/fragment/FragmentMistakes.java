package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.utils.MyLogger;
import org.papdt.miscol.ui.ActivityMain;
import org.papdt.miscol.ui.MistakeCard;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMistakes extends Fragment {
	private static FragmentMistakes sInstance;
	private final static String TAG = "FragmentMistakes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		((ActivityMain) getActivity()).initializeTabs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mistakes, null);
		CardUI cardsUI = (CardUI) v.findViewById(R.id.view_cardui);
		CardStack s = new CardStack();
		s.add(new MistakeCard("Hello World","This is a demo card."));
		s.add(new MistakeCard("Hello World","This is a demo card again."));
		cardsUI.addStack(s);
		cardsUI.refresh();
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		((ActivityMain) getActivity()).removeTabs();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();// 还不能忘了加这句
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fragment_main, menu);
		super.onPrepareOptionsMenu(menu);
	}

	@Deprecated
	public FragmentMistakes() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	public void fillContentAsTagIndex() {

	}

	public void fillContentAsSubjectIndex() {

	}

	public static FragmentMistakes getInstance() {
		if (sInstance == null) {
			sInstance = new FragmentMistakes();
		}
		return sInstance;
	}

}
