package org.papdt.miscol.ui;

import org.papdt.miscol.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class InfoCard extends Card {

	private View mCardContent;
	private TextView mTvCardTitle, mTvCardDescription;
	private String mDescription;

	public InfoCard(String title, String description) {
		super(title);
		mDescription = description;

	}

	@Override
	public View getCardContent(Context context) {
		mCardContent = View.inflate(context, R.layout.mistake_card, null);
		mTvCardTitle = (TextView) mCardContent.findViewById(R.id.tv_card_title);
		mTvCardDescription = (TextView) mCardContent
				.findViewById(R.id.tv_card_description);
		mTvCardTitle.setText(title);
		mTvCardDescription.setText(mDescription);
		return mCardContent;
	}

}
