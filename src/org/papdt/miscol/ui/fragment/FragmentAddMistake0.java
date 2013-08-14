package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.MyLogger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FragmentAddMistake0 extends Fragment implements OnItemSelectedListener{

	private LinearLayout mLayout;
	private EditText mEtTitle , mEtDescription;
	private Spinner mSpinnerGrade , mSpinnerSubject;
	private Mistake mMistake;
	private DatabaseHelper mDbHelper;
	private ArrayAdapter<String> mGradeAdapter,mSubjectAdapter;

	private final static String TAG = "FragmentAddMistake0";
	private static FragmentAddMistake0 sInstance;
	
	@Deprecated
	public FragmentAddMistake0() {
		mDbHelper = DatabaseHelper.getInstance(getActivity());
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
		getActivity().getActionBar().setSubtitle(R.string.step_1);
		mGradeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
		mSubjectAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
		mGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_addmistake_first, null);
		mEtTitle = (EditText) mLayout.findViewById(R.id.et_title);
		mEtDescription = (EditText) mLayout.findViewById(R.id.et_description);
		mSpinnerGrade = (Spinner) mLayout.findViewById(R.id.spinner_grade);
		mSpinnerSubject = (Spinner) mLayout.findViewById(R.id.spinner_subject);
		
		mSpinnerGrade.setAdapter(mGradeAdapter);
		mSpinnerSubject.setAdapter(mSubjectAdapter);
				
		mSpinnerGrade.setOnItemSelectedListener(this);
		mSpinnerSubject.setOnItemSelectedListener(this);
		
		fillDatas();
		return mLayout;
	}

	private void fillDatas() {
		CategoryInfo[] gradeInfo = mDbHelper.getCategoryInfo(Grades.TABLE_NAME);
		CategoryInfo[] subjectInfo = mDbHelper.getCategoryInfo(Subjects.TABLE_NAME);
		addCategoryInfoToAdapter(gradeInfo,mGradeAdapter);
		addCategoryInfoToAdapter(subjectInfo,mSubjectAdapter);
		String addCat = getString(R.string.add_category);
		mGradeAdapter.add(addCat);
		mSubjectAdapter.add(addCat);
	}

	private void addCategoryInfoToAdapter(CategoryInfo[] info,
			ArrayAdapter<String> adapter) {
			if(info!=null){
				for(CategoryInfo ci:info){
					adapter.add(ci.getName());
				}
			}	
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
						mEtTitle.getText().toString(),
						mEtDescription.getText().toString()
				);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FragmentAddMistake1 fragment = new FragmentAddMistake1();

		Bundle bundle = new Bundle();
		bundle.putParcelable("Mistake", mMistake);
		fragment.setArguments(bundle);
		transaction.addToBackStack(ActivityAddMistake.TAGS[0]).replace(R.id.fl_content, fragment,ActivityAddMistake.TAGS[1]).commit();
	}

	private void addSubject() {
		// TODO 添加科目
		
	}

	private void addGrade() {
		//TODO 添加年级
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch(view.getId()){
		case R.id.spinner_grade:
			if(position==mGradeAdapter.getCount()-1){
				addGrade();
			}
			break;
		case R.id.spinner_subject:
			if(position==mSubjectAdapter.getCount()-1){
				addSubject();
			}
			break;
		}		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
