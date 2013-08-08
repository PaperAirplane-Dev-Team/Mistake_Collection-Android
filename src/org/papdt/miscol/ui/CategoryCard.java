package org.papdt.miscol.ui;

import org.papdt.miscol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class CategoryCard extends Card {
	private final static int IS_A_CAT_OF_TAGS = -1;
	private String mTitle;
	private int mMistakeAmount;
	private int mSubjectAmount;
	private View mCardContent;

	public CategoryCard(String title, int mistakeAmount, int subjectAmount) {
		super(title);
		this.mTitle = title;
		this.mMistakeAmount = mistakeAmount;
		this.mSubjectAmount = subjectAmount;
	}

	public CategoryCard(String title, int mistakeAmount) {
		super(title);
		this.mTitle = title;
		this.mMistakeAmount = mistakeAmount;
		this.mSubjectAmount = IS_A_CAT_OF_TAGS;
	}

	@Override
	public View getCardContent(Context context) {
		mCardContent = LayoutInflater.from(context).inflate(
				R.layout.categoty_card, null);
		TextView tvTitle = (TextView) mCardContent
				.findViewById(R.id.tv_card_title);
		TextView tvDescription = (TextView) mCardContent
				.findViewById(R.id.tv_card_description);
		tvTitle.setText(mTitle);
		String description = (mSubjectAmount == IS_A_CAT_OF_TAGS) ? String
				.format(context.getString(R.string.card_description),
						mMistakeAmount) : String.format(
				context.getString(R.string.card_description_for_subject),
				mMistakeAmount, mSubjectAmount);
		tvDescription.setText(description);
		return mCardContent;
	}

	@Override
	protected int getCardLayout() {
		return R.layout.stack_card_empty;
	}

	@Override
	protected int getLastCardLayout() {
		return R.layout.stack_card_empty;
	}

	@Override
	protected int getFirstCardLayout() {
		return R.layout.stack_card_empty;
	}

}
