package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.InfoCard;

import com.fima.cardsui.views.CardUI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FragmentConstruction extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mCardsLayout = (FrameLayout) inflater.inflate(
				R.layout.fragment_main, null);
		CardUI mCardUI = (CardUI) mCardsLayout.findViewById(R.id.view_cardui);
		InfoCard cardWelcome = new InfoCard("烂尾楼",
				"因为人手不够本页面暂时无法访问…对您造成的不便敬请谅解…等本猿找到结对编程的对象就回来填坑……\n\nP.S.洗澡之后挖耳朵，湿湿一大坨。");
		mCardUI.addCard(cardWelcome);
		mCardUI.refresh();
		return mCardsLayout;
	}
}
