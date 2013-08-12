package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
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
import android.widget.Spinner;

public class FragmentAddMistake0 extends Fragment {

	private LinearLayout mLayout;
	private EditText mEditText_title , mEditText_description;
	private Spinner mSpinner_grade , mSpinner_subject;
	private Mistake mMistake;

	private final static String TAG = "FragmentAddMistake0";
	private static FragmentAddMistake0 sInstance;
	
	@Deprecated
	public FragmentAddMistake0() {
		MyLogger.d(TAG, TAG + "被初始化");
	}

	public static FragmentAddMistake0 getInstance() {
		if (sInstance == null)
			sInstance = new FragmentAddMistake0();
		return sInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		getActivity().getActionBar().setSubtitle("步骤 1/2");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_addmistake_first, null);
		mEditText_title = (EditText) mLayout.findViewById(R.id.et_title);
		mEditText_description = (EditText) mLayout.findViewById(R.id.et_description);
		mSpinner_grade = (Spinner) mLayout.findViewById(R.id.spinner_grade);
		mSpinner_subject = (Spinner) mLayout.findViewById(R.id.spinner_subject);
		return mLayout;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.fragment_addmistake_0,
				menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_next:
			MyLogger.d(TAG, "用户触发下一步操作");
			moveToNextStep();
			break;
		case android.R.id.home:
			MyLogger.d(TAG, "用户点击ActionBar的返回按钮");
			getFragmentManager().beginTransaction().detach(this).commit();
			getActivity().finish();
			break;
		case R.id.action_add_photo:
			MyLogger.d(TAG, "用户触发添加照片操作");
			break;
		case R.id.action_add_tag:
			MyLogger.d(TAG, "用户触发添加标签操作");
			break;
		}
		return true;
	}
	
	private void moveToNextStep() {
		mMistake = new
				Mistake(
						mEditText_title.getText().toString(),
						mEditText_description.getText().toString()
				);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FragmentAddMistake1 fragment = new FragmentAddMistake1();

		Bundle bundle = new Bundle();
		bundle.putParcelable("Mistake", mMistake);
		fragment.setArguments(bundle);
		transaction.replace(R.id.fl_content, fragment).addToBackStack(null).commit();
	}
	
	private Mistake getSampleMistake() {
		Mistake m = new Mistake("测试","呵呵呵呵呵");
		m.setGradeName("高一");
		m.setSubjectName("节操");
		m.setTagNames(new String[]{"Demo"});
		m.setTypeName("选择题");
		return m;
	}

}
