package org.papdt.miscol.ui.fragment;

import java.util.Arrays;
import java.util.HashSet;
import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.dialog.SelectTagsDialog;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentAddMistake0 extends AbsFragmentAddMistake implements
		OnItemSelectedListener, CategoryInfo.TYPE {

	private LinearLayout mLayout;
	private EditText mEtTitle;
	private Spinner mSpinnerGrade, mSpinnerSubject, mSpinnerType;
	private TextView mTvTags;
	private Mistake mMistake;
	private DatabaseHelper mDbHelper;
	private ArrayAdapter<String> mGradeAdapter, mSubjectAdapter;
	public HashSet<String> mTags = new HashSet<String>();
	public HashSet<String> mAllTags = new HashSet<String>();
	public CheckBox[] mCheckBoxes;

	private final static String TAG = "FragmentAddMistake0";

	public FragmentAddMistake0() {
		mDbHelper = DatabaseHelper.getInstance(getActivity());
		Log.d(TAG, TAG + "被初始化");
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
		mSpinnerType = (Spinner) mLayout.findViewById(R.id.spinner_type);

		mSpinnerGrade.setAdapter(mGradeAdapter);
		mSpinnerSubject.setAdapter(mSubjectAdapter);
		mSpinnerType.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.types)));
		((ArrayAdapter<?>) mSpinnerType.getAdapter())
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mSpinnerGrade.setOnItemSelectedListener(this);
		mSpinnerSubject.setOnItemSelectedListener(this);
		fillDatas();
		assert (mSpinnerGrade.getOnItemSelectedListener() == this);
		return mLayout;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.menu_addmistake_0, menu);
		if (mPic == null) {
			menu.getItem(0).getSubMenu().removeItem(R.id.action_remove_photo);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_next:
			Log.d(TAG, "用户触发下一步操作");
			moveToNextStep();
			break;
		case android.R.id.home:
			Log.d(TAG, "用户点击ActionBar的返回按钮");
			getFragmentManager().beginTransaction().detach(this).commit();
			getActivity().finish();
			break;
		case R.id.action_pick_photo:
			Log.d(TAG, "用户触发选择照片操作");
			addPhoto();
			break;
		case R.id.action_capture_photo:
			Log.d(TAG, "用户触发拍摄照片操作");
			capturePhoto();
			break;
		case R.id.action_add_tag:
			Log.d(TAG, "用户触发添加标签操作");
			addTags();
			break;
		case R.id.action_remove_photo:
			Log.d(TAG, "用户触发清除照片操作");
			setPicture(null);
			getActivity().invalidateOptionsMenu();
			break;
		}
		return true;
	}

	public void showTags() {
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
		if (getArguments() != null) {
			mMistake = getArguments().getParcelable(ActivityAddMistake.KEY);
			if (mMistake.getTagNames() != null
					&& mMistake.getTagNames().length > 0) {
				mTags = new HashSet<String>(Arrays.asList(mMistake
						.getTagNames()));
				showTags();
			}
			mEtDescription.setText(mMistake.getQuestionText());
			mEtTitle.setText(mMistake.getTitle());
			setPicture(mMistake.getQuestionPhotoPath());
		}

		mGradeAdapter.clear();
		mSubjectAdapter.clear();
		mAllTags.clear();
		CategoryInfo[] tagInfo = mDbHelper.getCategoryInfo(TAGS);
		if (tagInfo != null && tagInfo.length > 0) {
			for (CategoryInfo ci : tagInfo) {
				mAllTags.add(ci.getName());
			}
		}
		mAllTags.addAll(mTags);
		CategoryInfo[] gradeInfo = mDbHelper.getCategoryInfo(GRADES);
		CategoryInfo[] subjectInfo = mDbHelper.getCategoryInfo(SUBJECTS);
		addCategoryInfoToAdapter(gradeInfo, mGradeAdapter);
		addCategoryInfoToAdapter(subjectInfo, mSubjectAdapter);
		String addCat = getString(R.string.add_category);
		mGradeAdapter.add(addCat);
		mSubjectAdapter.add(addCat);
		if (mMistake != null) {
			int gradeIndex = mGradeAdapter.getPosition(mMistake.getGradeName());
			int subjectIndex = mSubjectAdapter.getPosition(mMistake
					.getSubjectName());
			if (gradeIndex == -1) {
				mGradeAdapter.remove(getString(R.string.spinner_default));
				addGrade(mMistake.getGradeName());
			} else {
				mSpinnerGrade.setSelection(gradeIndex);
			}
			if (subjectIndex == -1) {
				mSubjectAdapter.remove(getString(R.string.spinner_default));
				addSubject(mMistake.getSubjectName());
			} else {
				mSpinnerSubject.setSelection(subjectIndex);
			}
		}
	}

	private void addCategoryInfoToAdapter(CategoryInfo[] info,
			ArrayAdapter<String> adapter) {
		if (info.length > 0) {
			for (CategoryInfo ci : info) {
				adapter.add(ci.getName());
			}
		} else {
			adapter.add(getString(R.string.spinner_default));
		}
	}

	private void addTags() {
		SelectTagsDialog dialog = new SelectTagsDialog();
		dialog.setFragment(this);
		dialog.show(getFragmentManager(), SelectTagsDialog.TAG);
	}

	private void moveToNextStep() {
		if (isFieldEmpty())
			return;
		mMistake = new Mistake(mEtTitle.getText().toString(), mEtDescription
				.getText().toString());
		if (mPicPath != null)
			mMistake.setQuestionPhotoPath(mPicPath);
		if (mTags.size() > 0)
			mMistake.setTagNames(mTags.toArray(new String[0]));
		mMistake.setSubjectName((String) mSpinnerSubject.getSelectedItem());
		mMistake.setGradeName((String) mSpinnerGrade.getSelectedItem());
		mMistake.setTypeName((String) mSpinnerType.getSelectedItem());
		mMistake.setTypeId(mSpinnerType.getSelectedItemPosition());
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

	@Override
	protected boolean isFieldEmpty() {
		String format = getString(R.string.field_empty);
		if (TextUtils.isEmpty(mEtTitle.getText())) {
			Toast.makeText(getActivity(),
					String.format(format, mEtTitle.getHint()),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		if (TextUtils.isEmpty(mEtDescription.getText())) {
			Toast.makeText(getActivity(),
					String.format(format, mEtDescription.getHint()),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		if (((String) mSpinnerGrade.getSelectedItem())
				.equals(getString(R.string.spinner_default))
				|| ((String) mSpinnerGrade.getSelectedItem())
						.equals(getString(R.string.add_category))) {
			Toast.makeText(getActivity(),
					String.format(format, getString(R.string.tv_grade)),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		if (((String) mSpinnerSubject.getSelectedItem())
				.equals(getString(R.string.spinner_default))
				|| ((String) mSpinnerSubject.getSelectedItem())
						.equals(getString(R.string.add_category))) {
			Toast.makeText(getActivity(),
					String.format(format, getString(R.string.tv_subject)),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	private void openAddSubjectDialog() {
		final EditText etSubject = new EditText(getActivity());
		etSubject.setHint(R.string.add_subject_hint);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (!TextUtils.isEmpty(etSubject.getText())) {
						mSubjectAdapter
								.remove(getString(R.string.spinner_default));
						String text = etSubject.getText().toString();

						if (mSubjectAdapter.getPosition(text) == -1) {
							addSubject(text);
						} else {
							mSpinnerSubject.setSelection(mSubjectAdapter
									.getPosition(text));
						}
					} else {
						Toast.makeText(getActivity(),
								R.string.add_subject_empty, Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					mSpinnerSubject.setSelection(0);
					break;
				}

			}
		};

		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.add_subject_title).setView(etSubject)
				.setPositiveButton(android.R.string.ok, listener)
				.setNegativeButton(android.R.string.cancel, listener).show();
	}

	private void openAddGradeDialog() {
		final EditText etGrade = new EditText(getActivity());
		etGrade.setHint(R.string.add_grade_hint);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (!TextUtils.isEmpty(etGrade.getText())) {
						mGradeAdapter
								.remove(getString(R.string.spinner_default));
						String text = etGrade.getText().toString();
						if (mGradeAdapter.getPosition(text) == -1) {
							addGrade(text);
						} else {
							mSpinnerGrade.setSelection(mGradeAdapter
									.getPosition(text));
						}

					} else {
						Toast.makeText(getActivity(), R.string.add_grade_empty,
								Toast.LENGTH_SHORT).show();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					mSpinnerGrade.setSelection(0);
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

	private void addSubject(String name) {
		mSubjectAdapter.insert(name, 0);
		mSpinnerSubject.setSelection(0);
	}

	private void addGrade(String name) {
		mGradeAdapter.insert(name, 0);
		mSpinnerGrade.setSelection(0);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// FIXME 根本不工作
		Log.d(TAG, "onItemSelected");
		try {
			switch (parent.getId()) {
			case R.id.spinner_grade:
				Log.d(TAG, "mSpinnerGrade onItemSelected position: " + position);
				Log.d(TAG, mGradeAdapter.getItem(position));
				if (mGradeAdapter.getItem(position).equals(
						getString(R.string.add_category)))
					openAddGradeDialog();
				break;
			case R.id.spinner_subject:
				Log.d(TAG, "mSpinnerSubject onItemSelected position: "
						+ position);
				if (mSubjectAdapter.getItem(position).equals(
						getString(R.string.add_category)))
					openAddSubjectDialog();
				break;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		Log.d(TAG, "onNothingSelected.");
	}

}
