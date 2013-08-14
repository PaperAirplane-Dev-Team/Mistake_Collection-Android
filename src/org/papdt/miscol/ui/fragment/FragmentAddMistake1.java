package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FragmentAddMistake1 extends Fragment {

	private LinearLayout mLayout;
	private EditText mEtAnswer;
	private Mistake mMistake;
	private final static String TAG = "FragmentAddMistake1";

	public FragmentAddMistake1() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		mMistake = this.getArguments().getParcelable("Mistake");
		getActivity().getActionBar().setSubtitle(R.string.step_2);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.fragment_addmistake_1,
				menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_finish:
			MyLogger.d(TAG, "用户触发下一步操作");
			moveToNextStep();
			break;
		case android.R.id.home:
			MyLogger.d(TAG, "用户点击ActionBar的返回按钮");
			moveToLastStep();
			break;
		case R.id.action_add_photo:
			MyLogger.d(TAG, "用户触发添加照片操作");
			break;
		}
		return true;
	}
	
	private void moveToLastStep(){
		getActivity().getActionBar().setSubtitle(R.string.step_2);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		getFragmentManager().popBackStack();
		Fragment fragment = FragmentAddMistake0.getInstance();
		transaction.replace(R.id.fl_content, fragment).commit();
	}
	
	private void moveToNextStep(){
		mMistake.setAnswerText(mEtAnswer.getText().toString());
		((ActivityAddMistake) getActivity()).finishAdding(mMistake);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_addmistake_second,
				null);
		mEtAnswer = (EditText) mLayout.findViewById(R.id.et_answer);
		return mLayout;
	}
	

}
