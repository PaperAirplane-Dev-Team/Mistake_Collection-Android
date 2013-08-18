package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.ui.ActivityAddMistake;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FragmentAddMistake1 extends AbsFragmentAddMistake {

	private LinearLayout mLayout;
	private Mistake mMistake;
	private final static String TAG = "FragmentAddMistake1";

	public FragmentAddMistake1() {
		Log.d(TAG, TAG + "被初始化");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		mMistake = this.getArguments().getParcelable("Mistake");
		getActivity().getActionBar().setSubtitle(R.string.step_2);
		mMistake = getArguments().getParcelable(ActivityAddMistake.KEY);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.menu_addmistake_1, menu);
		if(mPicPath==null)
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

	private void moveToLastStep() {
		getActivity().getActionBar().setSubtitle(R.string.step_2);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		getFragmentManager().popBackStack();
		Bundle arg = new Bundle();
		arg.putParcelable(ActivityAddMistake.KEY, mMistake);
		Fragment fragment = new FragmentAddMistake0();
		fragment.setArguments(arg);
		transaction.replace(R.id.fl_content, fragment).commit();
	}


	
	private void moveToNextStep() {
		mMistake.setAnswerText(mEtDescription.getText().toString());
		if (mPicPath != null) {
			mMistake.setAnswerPhotoPath(mPicPath);
		}
		((ActivityAddMistake) getActivity()).finishAdding(mMistake);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_addmistake_second, null);
		mEtDescription = (EditText) mLayout.findViewById(R.id.et_answer);
		return mLayout;
	}

	@Override
	protected boolean isFieldEmpty() {
		return TextUtils.isEmpty(mEtDescription.getText());
	}

}
