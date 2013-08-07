package org.papdt.miscol.dao;

import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.utils.Constants.Databases;
import org.papdt.miscol.utils.Constants.Databases.Mistakes;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

	private static DatabaseHelper sInstance;
	private DbOpenHelper mDbOpenHelper;
	private SQLiteDatabase mDatabase;

	public static DatabaseHelper getInstance(Context context,
			DatabaseErrorHandler handler) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context, handler);
		}
		return sInstance;
	}

	public DatabaseHelper(Context context, DatabaseErrorHandler handler) {
		mDbOpenHelper = DbOpenHelper.getInstance(context, handler);
		mDatabase = mDbOpenHelper.getWritableDatabase();
	}

	public boolean insertMistake(Mistake mistake) {
		ContentValues valuesToInsert = generateContentValues(mistake, true);
		mDatabase.insert(Databases.Mistakes.TABLE_NAME, null, valuesToInsert);
		return false;
	}

	public Mistake[] queryMistake(QueryCondition condition) {
		// TODO 实现根据条件查询错题的方法
		return null;
	}

	public boolean updateMistake(Mistake mistake) {
		// TODO 实现对错题的修改
		return false;
	}

	public boolean deleteMistake(Mistake mistake) {
		// 实现对错题的删除
		return false;
	}

	private ContentValues generateContentValues(Mistake mistake, boolean isNew) {
		DataItemProcessor.processMistake(mistake, mDatabase);
		ContentValues values = new ContentValues();
		if (isNew) {
			values.put(Mistakes.KEY_DATETIME_ADD_TIME, mistake.getAddTime());
		} else {
			values.put(Mistakes.KEY_DATETIME_LAST_MODIFY_TIME,
					mistake.getLastModifyTime());
		}
		values.put(Mistakes.KEY_STRING_TITLE, mistake.getTitle());
		values.put(Mistakes.KEY_INT_TYPE, mistake.getTypeId());
		values.put(Mistakes.KEY_STRING_QUESTION_TEXT, mistake.getQuestionText());
		if (mistake.getQuestionPhotoId() != -1) {
			values.put(Mistakes.KEY_INT_QUESTION_PHOTO_ID,
					mistake.getQuestionPhotoId());
		}
		values.put(Mistakes.KEY_STRING_ANSWER_TEXT, mistake.getAnswerText());
		if (mistake.getAnswerPhotoId() != -1) {
			values.put(Mistakes.KEY_INT_ANSWER_PHOTO_ID,
					mistake.getAnswerPhotoId());
		}
		if (!isNew && mistake.getLastReviewTime() != null) {
			values.put(Mistakes.KEY_DATETIME_LAST_REVIEW_TIME,
					mistake.getLastReviewTime());
			values.put(Mistakes.KEY_INT_REVIEW_TIMES, mistake.getReviewTimes());
			values.put(Mistakes.KEY_INT_REVIEW_CORRECT_TIMES,
					mistake.getReviewCorrectTimes());
		}
		values.put(Mistakes.KEY_INT_SUBJECT_ID, mistake.getSubjectId());
		values.put(Mistakes.KEY_INT_GRADE_ID, mistake.getGradeId());
		values.put(Mistakes.KEY_STRING_TAG_IDS, DataItemProcessor.convertTagsIntoString(mistake, mDatabase));
		values.put(Mistakes.KEY_INT_IS_ISTARRED, mistake.isStarred() ? 1 : 0);
		return values;
	}

}
