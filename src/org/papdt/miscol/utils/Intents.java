package org.papdt.miscol.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class Intents {
	public static final int RESULT_PICK_IMAGE = 1024;
	public static final int RESULT_CAPTURE_IMAGE = 1337;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	public static final String TAG = "MediaIntents";
	public static final String APP_NAME = "MisCol";

	public static final Intent CAPTURE_PHOTO_INTENT = new Intent(
			MediaStore.ACTION_IMAGE_CAPTURE);
	public static final Intent PICK_PHOTO_INTENT = new Intent(
			Intent.ACTION_PICK,
			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	public static Uri getOutputMediaFileUri(int type) {
		MyLogger.d(TAG,Uri.fromFile(getOutputMediaFile(type)).getPath());
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				APP_NAME);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				MyLogger.d(TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
}
