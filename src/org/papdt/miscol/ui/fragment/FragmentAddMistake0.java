package org.papdt.miscol.ui.fragment;

import java.util.HashSet;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.Tags;
import org.papdt.miscol.utils.MyLogger;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentAddMistake0 extends Fragment implements
		OnItemSelectedListener {

	private LinearLayout mLayout;
	private EditText mEtTitle, mEtDescription;
	private Spinner mSpinnerGrade, mSpinnerSubject;
	private TextView mTvTags;
	private Mistake mMistake;
	private DatabaseHelper mDbHelper;
	private ArrayAdapter<String> mGradeAdapter, mSubjectAdapter;
	private HashSet<String> mTags = new HashSet<String>();
	private HashSet<String> mAllTags = new HashSet<String>();
	private CheckBox[] mCheckBoxes;

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
		mGradeAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);
		mSubjectAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item);
		mGradeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSubjectAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_addmistake_first, null);
		mEtTitle = (EditText) mLayout.findViewById(R.id.et_title);
		mEtDescription = (EditText) mLayout.findViewById(R.id.et_description);
		mTvTags = (TextView) mLayout.findViewById(R.id.tv_tags);
		mSpinnerGrade = (Spinner) mLayout.findViewById(R.id.spinner_grade);
		mSpinnerSubject = (Spinner) mLayout.findViewById(R.id.spinner_subject);

		mSpinnerGrade.setAdapter(mGradeAdapter);
		mSpinnerSubject.setAdapter(mSubjectAdapter);

		mSpinnerGrade.setOnItemSelectedListener(this);
		mSpinnerSubject.setOnItemSelectedListener(this);
		fillDatas();
		return mLayout;
	}

	private void showTags() {
		if (mTags.size() != 0) {
			mTvTags.setVisibility(View.VISIBLE);
			StringBuilder sb = new StringBuilder();
			for (String str : mTags) {
				sb.append("#").append(str).append("\n");
			}
			mTvTags.setText(sb);
		} else {
			mTvTags.setVisibility(View.GONE);
		}

	}

	private void fillDatas() {
		mGradeAdapter.clear();
		mSubjectAdapter.clear();
		mAllTags.clear();
		CategoryInfo[] tagInfo = mDbHelper.getCategoryInfo(Tags.TABLE_NAME);
		if (tagInfo != null && tagInfo.length > 0) {
			for (CategoryInfo ci : tagInfo) {
				mAllTags.add(ci.getName());
			}
		}
		mAllTags.addAll(mTags);
		CategoryInfo[] gradeInfo = mDbHelper.getCategoryInfo(Grades.TABLE_NAME);
		CategoryInfo[] subjectInfo = mDbHelper
				.getCategoryInfo(Subjects.TABLE_NAME);
		addCategoryInfoToAdapter(gradeInfo, mGradeAdapter);
		addCategoryInfoToAdapter(subjectInfo, mSubjectAdapter);
		String addCat = getString(R.string.add_category);
		mGradeAdapter.add(addCat);
		mSubjectAdapter.add(addCat);
	}

	private void addCategoryInfoToAdapter(CategoryInfo[] info,
			ArrayAdapter<String> adapter) {
		if (info != null) {
			for (CategoryInfo ci : info) {
				adapter.add(ci.getName());
			}
		} else {
			adapter.add(getString(R.string.spinner_default));
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
			// TODO 添加照片
			break;
		case R.id.action_add_tag:
			MyLogger.d(TAG, "用户触发添加标签操作");
			addTags();
			break;
		}
		return true;
	}

	private void addTags() {
		LinearLayout layout = new LinearLayout(getActivity());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		String[] allTags = (String[]) mAllTags.toArray(new String[]{""});
		if (allTags != null && allTags.length > 0) {
			mCheckBoxes = new CheckBox[allTags.length];
			for (int i = 0; i < mAllTags.size(); i++) {
				mCheckBoxes[i] = new CheckBox(getActivity());
				mCheckBoxes[i].setText(allTags[i]);
				if (mTags.contains(allTags[i])) {
					mCheckBoxes[i].setChecked(true);
				}
				layout.addView(mCheckBoxes[i], params);
			}
		}
		DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					for(CheckBox cb:mCheckBoxes){
						if(cb.isChecked()){
							mTags.add(cb.getText().toString());
						}
					}
					showTags();
					break;
				}
			}
		};
		TextView tvAddTag = new TextView(getActivity());
		tvAddTag.setGravity(Gravity.CENTER);
		tvAddTag.setText(R.string.add_category);
		tvAddTag.setTextAppearance(getActivity(),
				android.R.attr.textAppearanceLarge);
		tvAddTag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final EditText et = new EditText(getActivity());
				et.setHint(R.string.add_tag_hint);
				final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (!TextUtils.isEmpty(et.getText())) {
								mTags.add(et.getText().toString());
								mAllTags.add(et.getText().toString());
								showTags();
							} else {
								Toast.makeText(getActivity(),
										R.string.add_tag_empty,
										Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				};
				new AlertDialog.Builder(getActivity())
						.setTitle(R.string.add_tag_title).setView(et)
						.setPositiveButton(android.R.string.ok, listener)
						.setNegativeButton(android.R.string.cancel, listener)
						.show();
			}
		});
		layout.addView(tvAddTag, params);
		new AlertDialog.Builder(getActivity()).setTitle(R.string.add_tag_title)
				.setView(layout)
				.setPositiveButton(android.R.string.ok, selectListener)
				.setNegativeButton(android.R.string.cancel, selectListener)
				.show();
	}

	private void moveToNextStep() {
		mMistake = new Mistake(mEtTitle.getText().toString(), mEtDescription
				.getText().toString());
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		FragmentAddMistake1 fragment = new FragmentAddMistake1();

		Bundle bundle = new Bundle();
		bundle.putParcelable("Mistake", mMistake);
		fragment.setArguments(bundle);
		transaction.addToBackStack(ActivityAddMistake.TAGS[0])
				.replace(R.id.fl_content, fragment, ActivityAddMistake.TAGS[1])
				.commit();
	}

	private void addSubject() {
		final EditText etSubject = new EditText(getActivity());
		etSubject.setHint(R.string.add_subject_hint);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (!TextUtils.isEmpty(etSubject.getText())) {
						mSubjectAdapter
								.remove(getString(R.string.add_category));
						mSubjectAdapter.add(etSubject.getText().toString());
						mSpinnerSubject
								.setSelection(mSubjectAdapter.getCount() - 1);
					} else {
						Toast.makeText(getActivity(),
								R.string.add_subject_empty, Toast.LENGTH_SHORT)
								.show();
					}
					break;
				}

			}
		};

		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.add_subject_title).setView(etSubject)
				.setPositiveButton(android.R.string.ok, listener)
				.setNegativeButton(android.R.string.cancel, listener).show();
	}

	private void addGrade() {
		final EditText etGrade = new EditText(getActivity());
		etGrade.setHint(R.string.add_grade_hint);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (!TextUtils.isEmpty(etGrade.getText())) {
						mGradeAdapter.remove(getString(R.string.add_category));
						mGradeAdapter.add(etGrade.getText().toString());
						mSpinnerGrade
								.setSelection(mGradeAdapter.getCount() - 1);
					} else {
						Toast.makeText(getActivity(), R.string.add_grade_empty,
								Toast.LENGTH_SHORT).show();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}

			}
		};
		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.add_grade_title)
				.setPositiveButton(android.R.string.ok, listener)
				.setView(etGrade)
				.setNegativeButton(android.R.string.cancel, listener).show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		MyLogger.d(TAG, "onItemSelected position: " + position);
		try {
			switch (parent.getId()) {
			case R.id.spinner_grade:
				if (position == mGradeAdapter.getCount() - 1) {
					MyLogger.d(TAG, "addGrade");
					addGrade();
				}
				break;
			case R.id.spinner_subject:
				if (position == mSubjectAdapter.getCount() - 1) {
					MyLogger.d(TAG, "addSubject");
					addSubject();
				}
				break;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
