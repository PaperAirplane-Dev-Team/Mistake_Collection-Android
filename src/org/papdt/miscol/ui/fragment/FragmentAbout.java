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

public class FragmentAbout extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mCardsLayout = (FrameLayout) inflater.inflate(
				R.layout.fragment_main, null);
		CardUI mCardUI = (CardUI) mCardsLayout.findViewById(R.id.view_cardui);
		InfoCard cardWelcome = new InfoCard(
				"我前年买了个大金表",
				"没错这是关于界面。本人表示无力吐槽……发起项目的时候明明好多人参与为毛烂尾了……为毛啊为毛啊！！还有陈晟祺你丫的不写代码无所谓你好歹回复我私信啊！！你明确告诉我你忙也行啊！！不天天洗澡装什么女神啊！！总之现在这个烂摊子只有我在收拾了，Bug特别特别多估计一时半会改不完我也懒得列在这了。有意向帮助我的请在微博联系@姚沛然 :-P\n\nP.S.洗澡之后挖耳朵，湿湿一大坨。");
		mCardUI.addCard(cardWelcome);
		mCardUI.refresh();
		return mCardsLayout;
	}
}
