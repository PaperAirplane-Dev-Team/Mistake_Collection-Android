package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.InfoCard;
import com.fima.cardsui.views.CardUI;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FragmentMain extends Fragment {

	private CardUI mCardUI;
	private FrameLayout mCardsLayout;
	private LinearLayout mHintLayout;
	private Activity mActivity;
	private final static String TAG = "FragmentMain";
	private static FragmentMain sInstance;

	@Deprecated
	public FragmentMain() {
		Log.d(TAG, TAG + "被初始化");
	}

	public static FragmentMain getInstance() {
		if (sInstance == null)
			sInstance = new FragmentMain();
		return sInstance;
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
		showHint();
		// 添加介绍卡片
		mCardUI = (CardUI) mCardsLayout.findViewById(R.id.view_cardui);
		InfoCard cardWelcome = new InfoCard(
				mActivity.getString(R.string.welcome),
				mActivity.getString(R.string.welcome_content));
		mCardUI.addCard(cardWelcome);
		mCardUI.refresh();
		return mCardsLayout;
	}

	/**
	 * 显示滑动提示
	 */
	public void showHint() {
		mHintLayout = (LinearLayout) mCardsLayout.findViewById(R.id.ll_hint);
		ImageView ivHint = (ImageView) mHintLayout.findViewById(R.id.iv_hint);
		AnimationDrawable hintAnimation = (AnimationDrawable) ivHint
				.getDrawable();
		hintAnimation.start();
		mHintLayout.setVisibility(View.VISIBLE);

	}

	/**
	 * 关闭滑动提示
	 */
	public void hideHint() {
		if (mHintLayout != null) {
			mHintLayout.setVisibility(View.GONE);
		}
	}

}
