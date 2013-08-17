package org.papdt.miscol.dao;

import java.util.StringTokenizer;

import org.papdt.miscol.bean.CategoryInfo;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.bean.QueryCondition;
import org.papdt.miscol.utils.Constants.Databases;
import org.papdt.miscol.utils.Constants.Databases.Files;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.IDbWithIdAndName;
import org.papdt.miscol.utils.Constants.Databases.Mistakes;
import org.papdt.miscol.utils.Constants.Databases.SqlStatements;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.Tags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
					Log.e(TAG, "onCorruption");
				}

			});
		}
		return sInstance;
	}

	public CategoryInfo[] getCategoryInfo(String tableName,
			String itemColumnName) {
		return getCategoryInfo(tableName, itemColumnName, false, false);
	}

	/**
	 * 返回表中所有id-name的映射以及计数
	 * 
	 * @param tableName
	 *            查询的表名
	 * @return 分类信息数组
	 */
	public CategoryInfo[] getCategoryInfo(String tableName,
			String itemColumnName, boolean isTag, boolean isGrade) {
		Log.d(TAG, "从表" + tableName + "获取信息, 在主表中列名为" + itemColumnName
				+ (isTag ? ",是标签" : ",不是标签"));
		Cursor cursor = mDatabase
				.rawQuery("SELECT " + IDbWithIdAndName.KEY_INT_ID + ","
						+ IDbWithIdAndName.KEY_STRING_NAME + " FROM "
						+ tableName, null);
		if (cursor.getCount() == 0) {
			Log.d(TAG, "未能查询到有关信息");
			return new CategoryInfo[0]; // OK
		}
		CategoryInfo info[] = new CategoryInfo[cursor.getCount()];
		Log.d(TAG, "获取到" + info.length + "条记录");
		int i = 0;
		while (cursor.moveToNext()) {
			info[i] = new CategoryInfo();
			// 不初始化永远是个痛,调试半小时
			info[i].setId(cursor.getInt(0));
			info[i].setName(cursor.getString(1).trim());
			info[i].setCount(getItemCount(Mistakes.TABLE_NAME, itemColumnName,
					info[i].getId(), isTag));
			if (isGrade) {
				info[i].setSubCount(getSubjectAmountOfGrade(info[i].getId()));
			}
			i++;
		}
		cursor.close();
		return info;
	}

	private int getSubjectAmountOfGrade(int gradeId) {
		Cursor cursor = mDatabase.rawQuery("SELECT COUNT(DISTINCT "
				+ Mistakes.KEY_INT_SUBJECT_ID + ") FROM " + Mistakes.TABLE_NAME
				+ " WHERE " + Mistakes.KEY_INT_GRADE_ID + "=" + gradeId, null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
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
			Log.wtf(TAG, "SQL错误" + e.getMessage());
			throw new MistakeOperationException(mistake, "插入时发生错误，为"
					+ e.getStackTrace());
		}
		Log.d(TAG, "成功插入错题");
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
	 * 更新一道错题<br />
	 * 对于那些同时具有name和id的属性(年级、科目、tag)如下处理<br />
	 * id和name均不变-不变<br />
	 * 如果要改变，只需要改变name，id和相应的count会自动改变<br />
	 * 对于tags的count处理问题目前我还是有点头疼，因为要执行的查询过于多<br />
	 * 对于照片:<br />
	 * id和path均不变-不变<br />
	 * id不变，path置空(请事先删除照片文件)-删除对应记录<br />
	 * id不变，path改变(仍然做好对原文件的删除)-删除原纪录插入新纪录，并更新Mistake表中相应的id<br />
	 * id=-1,path存在,则为插入照片操作，更新id为插入的记录<br />
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
		// 详见update方法的JavaDoc注释
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
	 * 从数据库中删除错题<br />
	 * 请先删除对应的照片文件
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
			Log.wtf(TAG, "SQL错误" + e.getMessage());
			throw new MistakeOperationException(mistake, "删除时发生错误，为"
					+ e.getStackTrace());
		}
		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();
		Log.d(TAG, "成功删除错题,ID为" + id);
		return true;
	}

	private int getItemCount(String tableName, String itemColumnName,
			int itemId, boolean isTag) {
		Cursor cursor;
		if (!isTag) {
			cursor = mDatabase.rawQuery("SELECT COUNT(" + itemColumnName
					+ ") FROM " + tableName + " WHERE " + itemColumnName + "='"
					+ itemId + "'", null);
		} else {
			cursor = mDatabase.rawQuery("SELECT COUNT(" + itemColumnName
					+ ") FROM " + tableName + " WHERE " + itemColumnName
					+ " LIKE '%" + itemId + "%'", null);
		}
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	private String generateWhereClause(QueryCondition condition) {
		StringBuilder sb = new StringBuilder();
		if (condition.getTitle() != null) {
			sb.append("(" + Mistakes.KEY_STRING_TITLE + " LIKE '%"
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
					sb.append(" (" + key + "='" + s.intValue() + "') OR");
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
		@SuppressWarnings("deprecation")
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
