package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.MistakeCard;
import org.papdt.miscol.utils.MyLogger;

import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.views.CardUI;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class FargmentMain extends Fragment {

	private CardUI mCardUI;
	private FrameLayout mCardsLayout;
	private Activity mActivity;
	private final static String TAG = "FragmentMain";

	public FargmentMain() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		mCardsLayout = (FrameLayout) inflater.inflate(R.layout.fragment_main,
				null);
		mCardUI = (CardUI) mCardsLayout.findViewById(R.id.view_cardui);
		mCardUI.setSwipeable(true);
		MistakeCard cardWelcome = new MistakeCard(
				mActivity.getString(R.string.welcome),
				mActivity.getString(R.string.welcome_content));
		cardWelcome.setOnCardSwipedListener(new OnCardSwiped() {
			@Override
			public void onCardSwiped() {
				// XXX 不工作
				Toast.makeText(getActivity(), "I'm being swiped!",
						Toast.LENGTH_SHORT).show();
				MyLogger.d(TAG, "being swiped");
				// XXX 不知道为什么没有用于移除Card的相关方法
			}
		});
		mCardUI.addCard(cardWelcome);
		MistakeCard cardHello = new MistakeCard("Hello, World",
				"Swipe to remove this card.");
		cardHello.setOnCardSwipedListener(new OnCardSwiped() {
			@Override
			public void onCardSwiped() {
				Toast.makeText(getActivity(), "I'm being swiped!",
						Toast.LENGTH_SHORT).show();
				// XXX 不知道为什么没有用于移除Card的相关方法
			}
		});
		mCardUI.addCard(cardHello);

		mCardUI.refresh();
		MyLogger.d(TAG, "Card已添加");
		return mCardsLayout;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// FIXME 不工作
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fragment_main, menu);
		super.onPrepareOptionsMenu(menu);
	}

}
