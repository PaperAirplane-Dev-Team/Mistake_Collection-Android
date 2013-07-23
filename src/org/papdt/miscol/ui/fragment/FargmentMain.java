package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.MistakeCard;
import org.papdt.miscol.utils.MyLogger;

import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FargmentMain extends Fragment {

	private CardUI mCardUI;
	private LinearLayout mCardsLayout;
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
		mCardsLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main,
				null);
		mCardUI = (CardUI) mCardsLayout.findViewById(R.id.view_cardui);
		mCardUI.setSwipeable(true);
		MistakeCard card = new MistakeCard("Hello, World",
				"Swipe to remove this card.");
		card.setOnCardSwipedListener(new OnCardSwiped() {
			@Override
			public void onCardSwiped() {
				Toast.makeText(getActivity(), "I'm being swiped!",
						Toast.LENGTH_SHORT).show();
				// XXX 不知道为什么没有用于移除Card的相关方法
			}
		});
		mCardUI.addCard(card);
		MyLogger.d(TAG, "Card已添加");
		return card.getView(getActivity());
		/* FIXME
		 * 本来应该是return mCardUI的 谁知道报错Could not find class
		 * 'com.fima.cardsui.objects.CardStack$3', referenced from method
		 * com.fima.cardsui.objects.CardStack.getAnimationListener
		 * 我一路追回去也不知道什么蛋疼的问题
		 */
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// FIXME 不工作
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.fragment_main, menu);
		super.onPrepareOptionsMenu(menu);
	}

}
