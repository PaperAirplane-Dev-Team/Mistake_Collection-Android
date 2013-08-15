package org.papdt.miscol.ui.fragment;

import java.util.HashSet;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.ActivityAddMistake;
import org.papdt.miscol.ui.dialog.SelectTagsDialog;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.Tags;
import org.papdt.miscol.utils.Intents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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

public class FragmentAddMistake0 extends Fragment implements
		OnItemSelectedListener {

	private LinearLayout mLayout;
	private EditText mEtTitle, mEtDescription;
	private Spinner mSpinnerGrade, mSpinnerSubject;
	private TextView mTvTags;
	private Mistake mMistake;
	private DatabaseHelper mDbHelper;
	private ArrayAdapter<String> mGradeAdapter, mSubjectAdapter;
	public HashSet<String> mTags = new HashSet<String>();
	public HashSet<String> mAllTags = new HashSet<String>();
	public CheckBox[] mCheckBoxes;
	private String mPicPath = null;
	private Drawable mPic = null;
	private int mDeletePicMenuItemId = -1;

	private final static String TAG = "FragmentAddMistake0";
	private static FragmentAddMistake0 sInstance;

	@Deprecated
	public FragmentAddMistake0() {
		mDbHelper = DatabaseHelper.getInstance(getActivity());
		Log.d(TAG, TAG + "被初始化");
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
		default:
			if (item.getItemId() == mDeletePicMenuItemId) {
				setPicture(null);
				getActivity().invalidateOptionsMenu();
			}

		}
		return true;
	}

	private void capturePhoto() {
		Uri uri = Intents.getOutputMediaFileUri(Intents.MEDIA_TYPE_IMAGE);
		mPicPath = uri.getPath();
		startActivityForResult(Intents.CAPTURE_PHOTO_INTENT.putExtra(
				MediaStore.EXTRA_OUTPUT, uri), Intents.RESULT_CAPTURE_IMAGE);

	}

	private void addPhoto() {
		startActivityForResult(Intents.PICK_PHOTO_INTENT,
				Intents.RESULT_PICK_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, String.format(
				"onActivityResult,requestCode = %d ,resultCode = %d ,OK = %d ,data is null = "
						+ (data == null), requestCode, resultCode,
				Activity.RESULT_OK));
		if (requestCode == Intents.RESULT_PICK_IMAGE
				&& resultCode == Activity.RESULT_OK && null != data) {
			Uri image = data.getData();
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(image,
					projection, null, null, null);
			cursor.moveToFirst();
			setPicture(cursor.getString(0));
			cursor.close();
		}
		if (requestCode == Intents.RESULT_CAPTURE_IMAGE
				&& resultCode == Activity.RESULT_OK) {
			setPicture(mPicPath);
		}
	}

	private void setPicture(String path) {
		mPicPath = path;
		if (path != null) {
			Log.i(TAG, "用户选择的Picture Path为" + mPicPath);
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			mPic = Drawable.createFromPath(mPicPath);
			int sideLength = (int) (dm.widthPixels * 0.2);
			mPic.setBounds(0, 0, sideLength, sideLength);
			mEtDescription.setCompoundDrawables(mPic, null, null, null);
			getActivity().invalidateOptionsMenu();
		} else {
			mEtDescription.setCompoundDrawables(null, null, null, null);
			mPic = null;
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

	private boolean isFieldEmpty() {
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
				.equals(getString(R.string.spinner_default))) {
			Toast.makeText(getActivity(),
					String.format(format, getString(R.string.tv_grade)),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		if (((String) mSpinnerSubject.getSelectedItem())
				.equals(getString(R.string.spinner_default))) {
			Toast.makeText(getActivity(),
					String.format(format, getString(R.string.tv_subject)),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
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
						mSubjectAdapter
								.remove(getString(R.string.spinner_default));
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
						mGradeAdapter
								.remove(getString(R.string.spinner_default));
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
		Log.d(TAG, "onItemSelected position: " + position);
		try {
			switch (parent.getId()) {
			case R.id.spinner_grade:
				if (position == mGradeAdapter.getCount() - 1) {
					Log.d(TAG, "addGrade");
					addGrade();
				}
				break;
			case R.id.spinner_subject:
				if (position == mSubjectAdapter.getCount() - 1) {
					Log.d(TAG, "addSubject");
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
