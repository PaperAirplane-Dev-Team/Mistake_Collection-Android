package org.papdt.miscol.ui.fragment;

import org.papdt.miscol.utils.Intents;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

public abstract class AbsFragmentAddMistake extends Fragment {
	protected String mPicPath = null;
	protected Drawable mPic = null;
	protected EditText mEtDescription;
	protected int mDeletePicMenuItemId = -1;
	public final static String TAG = "AbsFragmentAddMistake";

	protected void setPicture(String path) {
		mPicPath = path;
		if (path != null) {
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

	protected void capturePhoto() {
		Uri uri = Intents.getOutputMediaFileUri(Intents.MEDIA_TYPE_IMAGE);
		mPicPath = uri.getPath();
		startActivityForResult(Intents.CAPTURE_PHOTO_INTENT.putExtra(
				MediaStore.EXTRA_OUTPUT, uri), Intents.RESULT_CAPTURE_IMAGE);

	}

	protected void addPhoto() {
		startActivityForResult(Intents.PICK_PHOTO_INTENT,
				Intents.RESULT_PICK_IMAGE);
	}

	abstract protected boolean isFieldEmpty();

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

}
