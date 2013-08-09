package org.papdt.miscol.dao;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.ui.CategoryCard;
import org.papdt.miscol.utils.Constants.Databases;
import org.papdt.miscol.utils.Constants.Databases.Files;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.IDbWithIdAndName;
import org.papdt.miscol.utils.Constants.Databases.Mistakes;
import org.papdt.miscol.utils.Constants.Databases.SqlStatements;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.Tags;
import org.papdt.miscol.utils.MyLogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

	private static DatabaseHelper sInstance;
	private DbOpenHelper mDbOpenHelper;
	private SQLiteDatabase mDatabase;
	private static final String TAG = "DatabaseHelper";
	private boolean hasPrevious = false;
	private StringTokenizer mTokenizer;

	private DatabaseHelper(Context context, DatabaseErrorHandler handler) {
		mDbOpenHelper = DbOpenHelper.getInstance(context, handler);
		mDatabase = mDbOpenHelper.getWritableDatabase();
	}

	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context, new DatabaseErrorHandler() {
				@Override
				public void onCorruption(SQLiteDatabase dbObj) {
					MyLogger.e(TAG, "onCorruption");
				}

			});
		}
		return sInstance;
	}

	/**
	 * 获取全部Tag的CategoryCard ArrayList
	 * 
	 * @return
	 */

	public ArrayList<CategoryCard> getAllTags() {
		CategoryInfo[] info = getCategoryInfo(Tags.TABLE_NAME);
		ArrayList<CategoryCard> tags = new ArrayList<CategoryCard>();
		for (CategoryInfo temp : info) {
			tags.add(new CategoryCard(temp.getName(), temp.getCount()));
		}
		return tags;
	}

	/**
	 * 返回表中所有id-name的映射以及计数
	 * 
	 * @param tableName
	 *            查询的表名
	 * @return 分类信息数组
	 */
	public CategoryInfo[] getCategoryInfo(String tableName) {
		Cursor cursor = mDatabase.rawQuery("SELECT "
				+ IDbWithIdAndName.KEY_INT_ID + ","
				+ IDbWithIdAndName.KEY_STRING_NAME + ","
				+ IDbWithIdAndName.KEY_INT_ITEM_COUNT + " FROM " + tableName,
				null);
		if (cursor.getCount() == 0)
			return null;
		CategoryInfo info[] = new CategoryInfo[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()) {
			info[i].setId(cursor.getInt(0));
			info[i].setName(cursor.getString(1).trim());
			info[i].setCount(cursor.getInt(3));
			i++;
		}
		cursor.close();
		return info;
	}

	/**
	 * 插入错题
	 * 
	 * @param mistake
	 *            要插入的错题
	 * @return 是否成功
	 * @throws MistakeOperationException
	 *             操作失败
	 */
	public boolean insertMistake(Mistake mistake)
			throws MistakeOperationException {
		DataItemProcessor.processMistake(mistake, mDatabase);
		ContentValues valuesToInsert = generateContentValues(mistake, true);
		mDatabase.beginTransaction();
		try {
			mDatabase.insert(Databases.Mistakes.TABLE_NAME, null,
					valuesToInsert);
		} catch (Exception e) {
			mDatabase.endTransaction();
			MyLogger.wtf(TAG, "SQL错误" + e.getMessage());
			throw new MistakeOperationException(mistake, "插入时发生错误，为"
					+ e.getStackTrace());
		}
		MyLogger.d(TAG, "成功插入错题，ID为" + mistake.getId());
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
		return true;
	}

	/**
	 * 按照给出的条件从数据库中搜索错题
	 * 
	 * @param condition
	 *            查询条件(至少有一项)
	 * @return 返回的符合条件的错题(可能为null)
	 */
	public Mistake[] queryMistakesByCondition(QueryCondition condition) {
		String query = "SELECT " + SqlStatements.SELECT_ALL_ITEM + " FROM "
				+ Mistakes.TABLE_NAME + " WHERE "
				+ generateWhereClause(condition);
		Cursor cursor = mDatabase.rawQuery(query, null);
		if (cursor.getCount() == 0)
			return null;
		int i = 0;
		Mistake results[] = new Mistake[cursor.getCount()];
		while (cursor.moveToNext()) {
			results[i++] = generateMistakeWithCursor(cursor);
		}
		cursor.close();
		return results;
	}

	/**
	 * 更新一道错题
	 * 
	 * @param mistake
	 *            修改后的错题
	 * @return 是否成功
	 * @throws MistakeOperationException
	 *             操作失败
	 */
	public boolean updateMistake(Mistake mistake)
			throws MistakeOperationException {
		if (mistake.getId() == -1) {
			throw new MistakeOperationException(mistake, "试图在数据库内更改未保存的错题");
		}
		// TODO 如果有照片修改那么删掉源文件并修改path,在Fragment中完成
		DataItemProcessor.processMistake(mistake, mDatabase);
		ContentValues values = generateContentValues(mistake, false);
		mDatabase.beginTransaction();
		mDatabase.update(Mistakes.TABLE_NAME, values,
				IDbWithIdAndName.KEY_INT_ID + "=" + mistake.getId(), null);
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
		return true;
	}

	/**
	 * 从数据库中删除错题
	 * 
	 * @param mistake
	 *            要删除的错题
	 * @return 是否成功
	 * @throws MistakeOperationException
	 *             操作失败
	 */
	public boolean deleteMistake(Mistake mistake)
			throws MistakeOperationException {

		int id = mistake.getId();
		if (id == -1) {
			throw new MistakeOperationException(mistake, "试图删除未保存于数据库的错题");
		}
		// TODO 删掉照片文件
		DataItemProcessor.replaceOrDeletePhotoIdIfNecessary(
				mistake.getAnswerPhotoId(), null, mDatabase);
		DataItemProcessor.replaceOrDeletePhotoIdIfNecessary(
				mistake.getQuestionPhotoId(), null, mDatabase);
		mDatabase.beginTransaction();
		try {
			mDatabase.execSQL("DELETE FROM " + Mistakes.TABLE_NAME + " WHERE "
					+ IDbWithIdAndName.KEY_INT_ID + "=" + id);
		} catch (SQLException e) {
			mDatabase.endTransaction();
			MyLogger.wtf(TAG, "SQL错误" + e.getMessage());
			throw new MistakeOperationException(mistake, "删除时发生错误，为"
					+ e.getStackTrace());
		}
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
		MyLogger.d(TAG, "成功删除错题,ID为" + id);
		return true;
	}

	private String generateWhereClause(QueryCondition condition) {
		StringBuilder sb = new StringBuilder();
		if (condition.getTitle() != null) {
			sb.append("(" + Mistakes.KEY_STRING_TITLE + " LIKE '"
					+ condition.getTitle() + "%' )");
			hasPrevious = true;
		}
		insertRangeCompareClause(sb, condition.getAddTime(),
				Mistakes.KEY_DATETIME_ADD_TIME);
		insertRangeCompareClause(sb, condition.getLastModifyTime(),
				Mistakes.KEY_DATETIME_LAST_MODIFY_TIME);
		insertRangeCompareClause(sb, condition.getLastReviewTime(),
				Mistakes.KEY_DATETIME_LAST_REVIEW_TIME);
		insertRangeCompareClause(sb, condition.getReviewTimes(),
				Mistakes.KEY_INT_REVIEW_TIMES);
		insertRangeCompareClause(sb, condition.getCorrectRate(),
				Mistakes.KEY_REAL_CORRECT_RATE);
		insertMultipleChoiceClause(sb, condition.getTypeIds(),
				Mistakes.KEY_INT_TYPE_ID, true);
		insertMultipleChoiceClause(sb, condition.getSubjectIds(),
				Mistakes.KEY_INT_SUBJECT_ID, true);
		insertMultipleChoiceClause(sb, condition.getGradeIds(),
				Mistakes.KEY_INT_GRADE_ID, true);
		insertMultipleChoiceClause(sb, condition.getTagIds(),
				Mistakes.KEY_STRING_TAG_IDS, false);
		if (condition.isStarred()) {
			if (hasPrevious) {
				sb.append(" OR ");
			}
			sb.append("(" + Mistakes.KEY_INT_IS_ISTARRED + "=1)");
		}
		return sb.toString();
	}

	private boolean insertRangeCompareClause(StringBuilder sb, Object[] range,
			String key) {
		if (range != null) {
			if (hasPrevious) {
				sb.append(" OR ");
			}
			sb.append("(" + key + " BETWEEN " + range[0] + " AND " + range[1]
					+ " )");
			return true;
		} else {
			return false;
		}
	}

	private boolean insertMultipleChoiceClause(StringBuilder sb,
			Integer[] choices, String key, boolean isExact) {
		if (choices != null) {
			if (hasPrevious) {
				sb.append(" OR ");
			}
			if (isExact) {
				for (Integer s : choices) {
					sb.append(" (" + key + "=" + s.intValue() + ") OR");
				}
			} else {
				for (Integer s : choices) {
					sb.append(" (" + key + " LIKE '%" + s.intValue()
							+ "%' ) OR");
				}
			}
			int length = sb.length();
			sb.delete(length - 3, length - 1);// 删除最后一个OR
			return true;
		} else {
			return false;
		}
	}

	private Mistake generateMistakeWithCursor(Cursor cursor) {
		Mistake mistake = new Mistake();
		mistake.setId(cursor.getInt(0));
		mistake.setAddTime(cursor.getString(1));
		mistake.setLastModifyTime(cursor.getString(2));
		mistake.setTitle(cursor.getString(3));
		mistake.setTypeId(cursor.getInt(4));
		mistake.setQuestionText(cursor.getString(5));
		mistake.setQuestionPhotoId(cursor.getInt(6));
		mistake.setAnswerText(cursor.getString(7));
		mistake.setAnswerPhotoId(cursor.getInt(8));
		mistake.setLastReviewTime(cursor.getString(9));
		mistake.setReviewTimes(cursor.getInt(10));
		mistake.setReviewCorrectTimes(cursor.getInt(11));
		mistake.setCorrectRate(cursor.getDouble(12));
		mistake.setSubjectId(cursor.getInt(13));
		mistake.setGradeId(cursor.getInt(14));
		mistake.setTagIds(convertStringTagIdsToArray(cursor.getString(15)));
		mistake.setStarred(cursor.getInt(16) == 1 ? true : false);
		completeMistake(mistake);
		return mistake;
	}

	private int[] convertStringTagIdsToArray(String ids) {
		mTokenizer = new StringTokenizer(ids, ",");
		int tagIds[] = new int[mTokenizer.countTokens()], i = 0;
		while (mTokenizer.hasMoreTokens()) {
			tagIds[i++] = Integer.valueOf(mTokenizer.nextToken());
		}
		return tagIds;
	}

	private void completeMistake(Mistake mistake) {
		if (mistake.getQuestionPhotoId() != -1) {
			mistake.setQuestionPhotoPath(getNameById(Files.TABLE_NAME,
					mistake.getQuestionPhotoId(), Files.KEY_STRING_PATH));
		}
		if (mistake.getAnswerPhotoId() != -1) {
			mistake.setAnswerPhotoPath(getNameById(Files.TABLE_NAME,
					mistake.getAnswerPhotoId(), Files.KEY_STRING_PATH));
		}
		mistake.setSubjectName(getNameById(Subjects.TABLE_NAME,
				mistake.getSubjectId(), Subjects.KEY_STRING_NAME));
		mistake.setGradeName(getNameById(Grades.TABLE_NAME,
				mistake.getGradeId(), Grades.KEY_STRING_NAME));
		int ids[] = mistake.getTagIds(), i = 0;
		String tags[] = new String[ids.length];
		for (int id : ids) {
			tags[i++] = getNameById(Tags.TABLE_NAME, id, Tags.KEY_STRING_NAME);
		}
		mistake.setTagNames(tags);
	}

	private String getNameById(String tableName, int id, String keyName) {
		Cursor cursor = mDatabase.rawQuery("SELECT " + keyName + " FROM "
				+ tableName + " WHERE " + IDbWithIdAndName.KEY_INT_ID + "="
				+ id, null);
		cursor.moveToNext();
		String result = cursor.getString(0);
		cursor.close();
		return result;
	}

	private ContentValues generateContentValues(Mistake mistake, boolean isNew) {
		ContentValues values = new ContentValues();
		if (isNew) {
			values.put(Mistakes.KEY_DATETIME_ADD_TIME, mistake.getAddTime());
		} else {
			values.put(Mistakes.KEY_DATETIME_LAST_MODIFY_TIME,
					mistake.getLastModifyTime());
		}
		values.put(Mistakes.KEY_STRING_TITLE, mistake.getTitle());
		values.put(Mistakes.KEY_INT_TYPE_ID, mistake.getTypeId());
		values.put(Mistakes.KEY_STRING_QUESTION_TEXT, mistake.getQuestionText());
		values.put(Mistakes.KEY_INT_QUESTION_PHOTO_ID,
				mistake.getQuestionPhotoId());
		values.put(Mistakes.KEY_STRING_ANSWER_TEXT, mistake.getAnswerText());
		values.put(Mistakes.KEY_INT_ANSWER_PHOTO_ID, mistake.getAnswerPhotoId());
		values.put(Mistakes.KEY_DATETIME_LAST_REVIEW_TIME,
				mistake.getLastReviewTime());
		values.put(Mistakes.KEY_INT_REVIEW_TIMES, mistake.getReviewTimes());
		values.put(Mistakes.KEY_INT_REVIEW_CORRECT_TIMES,
				mistake.getReviewCorrectTimes());
		values.put(Mistakes.KEY_REAL_CORRECT_RATE, mistake.getCorrectRate());
		values.put(Mistakes.KEY_INT_SUBJECT_ID, mistake.getSubjectId());
		values.put(Mistakes.KEY_INT_GRADE_ID, mistake.getGradeId());
		values.put(Mistakes.KEY_STRING_TAG_IDS,
				DataItemProcessor.convertTagsIntoString(mistake, mDatabase));
		values.put(Mistakes.KEY_INT_IS_ISTARRED, mistake.isStarred() ? 1 : 0);
		return values;
	}

}
