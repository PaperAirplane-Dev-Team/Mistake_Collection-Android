package org.papdt.miscol.ui;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MistakeCard extends Card {
	private TextView mTvTitle, mTvDescription;
	private ImageView mIvDescription;
	private Mistake mMistake;

	public MistakeCard(Mistake m) {
		mMistake = m;
		setmBindedObject(mMistake);
	}

	@Override
	public View getCardContent(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.mistake_card,
				null);
		mTvTitle = (TextView) v.findViewById(R.id.tv_card_title);
		mTvDescription = (TextView) v.findViewById(R.id.tv_card_description);
		mIvDescription = (ImageView) v.findViewById(R.id.iv_description);
		mTvTitle.setText(String.format("[%s]%s", mMistake.getTypeName(),
				mMistake.getTitle()));
		// FIXME TypeName is null
		mTvDescription.setText(mMistake.getQuestionText());

		if (mMistake.getQuestionPhotoPath() != null) {
			mIvDescription.setVisibility(View.VISIBLE);
			mIvDescription.setImageDrawable(Drawable.createFromPath(mMistake
					.getQuestionPhotoPath()));
		}
		return v;
	}

}
