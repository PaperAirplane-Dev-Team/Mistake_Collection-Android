package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Answer;
import org.papdt.miscol.bean.Answer.QUESTION_TYPES;
import org.papdt.miscol.bean.Mistake;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FragmentMistakeDetail extends Fragment implements OnClickListener{
	public final static String TAG = "FragmentMistakeDetail";
	public final static String KEY = "Mistake";

	private Mistake mMistake;
	private Answer mAnswer;
	
	private TextView mTvDescription;
	private RadioGroup mRgSelections,mRgJudgement;
	private ViewStub mVsSelect,mVsJudgement;
	private ImageView mBtnDelete,mBtnStar,mBtnEdit,mIvDescription;
	private Button mBtnAdd;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.mistake_detail, null);
		mTvDescription = (TextView) mView.findViewById(R.id.tv_description);
		mBtnDelete = (ImageView) mView.findViewById(R.id.btn_delete);
		mBtnStar = (ImageView) mView.findViewById(R.id.btn_star);
		mBtnEdit = (ImageView) mView.findViewById(R.id.btn_edit);
		mIvDescription = (ImageView) mView.findViewById(R.id.iv_description);
		mVsSelect = (ViewStub) mView.findViewById(R.id.stub_sel);
		mVsJudgement = (ViewStub) mView.findViewById(R.id.stub_jud);
		
		mBtnDelete.setOnClickListener(this);
		mBtnStar.setOnClickListener(this);
		mBtnEdit.setOnClickListener(this);
		
		fillDatas();
		return mView;
	}

	private void fillDatas() {
		mMistake = getArguments().getParcelable(KEY);
		mAnswer = Answer.createAnswerFromJson(mMistake.getAnswerText(), mMistake.getTypeId());
		mTvDescription.setText(mAnswer.getDescription());
		if (mMistake.getAnswerPhotoPath() != null) {
			mIvDescription.setVisibility(View.VISIBLE);
			mIvDescription.setImageDrawable(Drawable.createFromPath(mMistake
					.getQuestionPhotoPath()));
		}
		switch(mMistake.getTypeId()){
		case Answer.QUESTION_TYPES.JUDGE:
			mVsJudgement.inflate();
			mRgJudgement = (RadioGroup) mVsJudgement.findViewById(R.id.rg_judgement);
			mRgJudgement.check(mAnswer.getJudgement() ? R.id.rb_true
					: R.id.rb_false);
			mRgJudgement.setEnabled(false);
			break;
		case QUESTION_TYPES.SELECT:
			mVsSelect.inflate();
			mRgSelections = (RadioGroup)mVsSelect.findViewById(R.id.rg_selections);
			mBtnAdd = (Button)mVsSelect.findViewById(R.id.btn_add);
			mBtnAdd.setVisibility(View.GONE);
			mRgSelections.setEnabled(false);
			String[] selections = mAnswer.getSelections().toArray(new String[0]);
			for(int i = 0;i<selections.length;i++){
				RadioButton rb = new RadioButton(getActivity());
				rb.setText(selections[i]);
				rb.setId(i);
				mRgSelections.addView(rb);
			}
			mRgSelections.check(mAnswer.getSelectionIndex());
			break;
		}
	}

	@Override
	public void onClick(View v) {
		//TODO Unimplemented.
		switch(v.getId()){
		case R.id.btn_delete:
			break;
		case R.id.btn_edit:
			break;
		case R.id.btn_star:
			break;
		}
		
	}
}

/*
 * P.S.洗澡之后挖耳朵，湿湿一大坨。
 * 
 * 乱曰：
 * 呜呼哀哉！感天之错勘贤愚，慨地之不分好歹。时运不齐，命途多舛。良辰未现，伊人难觅。感我痴人，良苦用心，苦待女神呵呵，殚精竭虑，空赢观者滔滔。失魂至此
 * ，独怆然悲叹而已矣。
 */
