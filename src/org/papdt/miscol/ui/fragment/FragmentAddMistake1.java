//FIXME 移除选项

package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Answer;
import org.papdt.miscol.bean.Answer.QUESTION_TYPES;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.ui.ActivityAddMistake;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FragmentAddMistake1 extends AbsFragmentAddMistake implements
		OnClickListener {

	private LinearLayout mLayout;
	private Mistake mMistake;
	private Answer mAnswer;
	private RadioGroup mRgJudgement, mRgSels;
	private Button mBtnAddSel;

	private final static LayoutParams mParams = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private final static String TAG = "FragmentAddMistake1";

	public FragmentAddMistake1() {
		Log.d(TAG, TAG + "被初始化");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		getActivity().getActionBar().setSubtitle(R.string.step_2);
		mMistake = getArguments().getParcelable(ActivityAddMistake.KEY);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.menu_addmistake_1, menu);
		if (mPicPath == null)
			menu.getItem(0).getSubMenu().removeItem(R.id.action_remove_photo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_finish:
			Log.d(TAG, "用户触发下一步操作");
			moveToNextStep();
			break;
		case android.R.id.home:
			Log.d(TAG, "用户点击ActionBar的返回按钮");
			moveToLastStep();
			break;
		case R.id.action_pick_photo:
			Log.d(TAG, "用户触发选择照片操作");
			addPhoto();
			break;
		case R.id.action_capture_photo:
			Log.d(TAG, "用户触发拍摄照片操作");
			capturePhoto();
			break;
		default:
			if (item.getItemId() == mDeletePicMenuItemId) {
				setPicture(null);
				getActivity().invalidateOptionsMenu();
			}
		}
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_addmistake_second, null);
		mEtDescription = (EditText) mLayout.findViewById(R.id.et_answer);
		if (mMistake.getAnswerPhotoPath() != null)
			setPicture(mMistake.getAnswerPhotoPath());
		String[] types = getResources().getStringArray(R.array.types);
		String typeName = mMistake.getTypeName();
		int type = (typeName.equals(types[0])) ? QUESTION_TYPES.SELECT
				: ((typeName.equals(types[1]) ? QUESTION_TYPES.FILL
						: QUESTION_TYPES.JUDGE));
		if (mMistake.getAnswerText() != null) {
			mAnswer = Answer.createAnswerFromJson(mMistake.getAnswerText(),
					type);
			mEtDescription.setText(mAnswer.getDescription());
		} else {
			mAnswer = new Answer(type);
		}
		switch (type) {
		case QUESTION_TYPES.JUDGE:
			ViewStub judgementStub = (ViewStub) mLayout
					.findViewById(R.id.stub_jud);
			View judgementView = judgementStub.inflate();
			mRgJudgement = (RadioGroup) judgementView
					.findViewById(R.id.rg_judgement);
			mRgJudgement.check(mAnswer.getJudgement() ? R.id.rb_true
					: R.id.rb_false);
			break;
		case QUESTION_TYPES.SELECT:
			ViewStub selectStub = (ViewStub) mLayout
					.findViewById(R.id.stub_sel);
			View selectView = selectStub.inflate();
			mRgSels = (RadioGroup) selectView.findViewById(R.id.rg_selections);
			mBtnAddSel = (Button) selectView.findViewById(R.id.btn_add);
			mBtnAddSel.setOnClickListener(this);
			int i = 0;
			for (String selection : mAnswer.getSelections()) {
				RadioButton rb = new RadioButton(getActivity());
				rb.setId(i);
				rb.setText(selection);
				i++;
				mRgSels.addView(rb, 0, mParams);
			}
			mRgSels.check(mAnswer.getSelectionIndex());
			break;
		}
		return mLayout;
	}

	private void moveToLastStep() {
		getActivity().getActionBar().setSubtitle(R.string.step_2);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		getFragmentManager().popBackStack();
		Fragment fragment = new FragmentAddMistake0();
		fragment.setArguments(createBundle());
		transaction.replace(R.id.fl_content, fragment).commit();
	}

	private Bundle createBundle() {
		Bundle arg = new Bundle();
		mAnswer.setDescription(mEtDescription.getText().toString());
		switch (mAnswer.getType()) {
		case QUESTION_TYPES.JUDGE:
			mAnswer.setJudgement((mRgJudgement.getCheckedRadioButtonId() == R.id.rb_true) ? true
					: false);
			break;
		case QUESTION_TYPES.SELECT:
			int length = mRgSels.getChildCount();
			for (int i = 0; i < length; i++) {
				RadioButton rb = (RadioButton) mRgSels.getChildAt(i);
				mAnswer.addSelection(rb.getText().toString());
			}
			mAnswer.setSelectionIndex(mRgSels.getCheckedRadioButtonId());
			break;
		}
		if (mPicPath != null) {
			mMistake.setAnswerPhotoPath(mPicPath);
		}
		mMistake.setAnswerText(mAnswer.toString());
		arg.putParcelable(ActivityAddMistake.KEY, mMistake);
		return arg;

	}

	private void moveToNextStep() {
		mMistake.setAnswerText(mEtDescription.getText().toString());
		createBundle();
		((ActivityAddMistake) getActivity()).finishAdding(mMistake);
	}

	@Override
	protected boolean isFieldEmpty() {
		if (mAnswer.getType() == QUESTION_TYPES.SELECT) {
			if (mRgSels.getChildCount() <= 1) {
				Toast.makeText(getActivity(), R.string.selection_not_enough,
						Toast.LENGTH_SHORT).show();
				return true;
			}
			if (mRgSels.getCheckedRadioButtonId() == -1) {
				Toast.makeText(getActivity(), R.string.add_selection_empty,
						Toast.LENGTH_SHORT).show();
				return true;
			}

		}
		return TextUtils.isEmpty(mEtDescription.getText());
	}

	@Override
	public void onClick(View arg0) {
		final EditText et = new EditText(getActivity());
		et.setHint(R.string.add_selection_hint);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (arg1 == DialogInterface.BUTTON_POSITIVE) {
					if (TextUtils.isEmpty(et.getText())) {
						Toast.makeText(getActivity(),
								R.string.add_selection_empty,
								Toast.LENGTH_SHORT).show();
					} else {
						RadioButton rb = new RadioButton(getActivity());
						rb.setText(et.getText());
						mRgSels.addView(rb, mParams);
					}
				}
			}
		};
		new AlertDialog.Builder(getActivity()).setTitle(R.string.add_selection)
				.setView(et)
				.setNegativeButton(android.R.string.cancel, listener)
				.setPositiveButton(android.R.string.ok, listener).show();

	}

}
