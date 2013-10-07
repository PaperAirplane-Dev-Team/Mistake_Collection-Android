package org.papdt.miscol.dao;

import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.utils.Constants.Databases.Files;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.IDbWithIdAndName;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.QuestionType;
import org.papdt.miscol.utils.Constants.Databases.Tags;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataItemProcessor {
	private static final String[] EMPTY_ARRAY = new String[] {};

	public static void processMistake(Mistake mistake, SQLiteDatabase db) {
		mistake.setTypeId(convertItemIntoId(mistake.getTypeName(), db,
				QuestionType.TABLE_NAME));
		mistake.setSubjectId(convertItemIntoId(mistake.getSubjectName(), db,
				Subjects.TABLE_NAME));
		mistake.setGradeId(convertItemIntoId(mistake.getGradeName(), db,
				Grades.TABLE_NAME));
		// 以上三项都是必须的
		mistake.setAnswerPhotoId(replaceOrDeletePhotoIdIfNecessary(
				mistake.getAnswerPhotoId(), mistake.getAnswerPhotoPath(), db));
		mistake.setQuestionPhotoId(replaceOrDeletePhotoIdIfNecessary(
				mistake.getQuestionPhotoId(), mistake.getQuestionPhotoPath(),
				db));
	}

	public static int replaceOrDeletePhotoIdIfNecessary(int id, String path,
			SQLiteDatabase db) {
		int result = -1;
		if (id != -1 && path == null) {
			db.execSQL("DELETE FROM " + Files.TABLE_NAME + " WHERE "
					+ Files.KEY_STRING_PATH + "='" + path + "'");
		} else if (id != -1 && path != null) {
			int temp = convertItemIntoId(path, db, Files.TABLE_NAME,
					Files.KEY_STRING_PATH);
			if (temp == id) {
				result = id;
			} else {
				db.execSQL("DELETE FROM " + Files.TABLE_NAME + " WHERE "
						+ Files.KEY_INT_ID + "=" + id);
				result = temp;
			}
		} else if (id == -1 && path != null) {
			result = convertItemIntoId(path, db, Files.TABLE_NAME,
					Files.KEY_STRING_PATH,
					new String[] { Files.KEY_STRING_TYPE },
					new String[] { getFileTypeFromPath(path) });
		}
		return result;
	}

	private static String getFileTypeFromPath(String path) {
		String[] arr = path.split("\\.");
		return arr[arr.length - 1];
	}

	public static String convertTagsIntoString(Mistake mistake,
			SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		for (String tag : mistake.getTagNames()) {
			int tagId = convertItemIntoId(tag, db, Tags.TABLE_NAME);
			sb.append(tagId + ",");
		}
		return sb.toString();
	}

	public static int convertItemIntoId(String itemName, SQLiteDatabase db,
			String tableName) {
		return convertItemIntoId(itemName, db, tableName,
				IDbWithIdAndName.KEY_STRING_NAME);
	}

	private static int convertItemIntoId(String itemName, SQLiteDatabase db,
			String tableName, String itemColumnName, String[] moreColumnNames,
			String[] moreItemNames) {
		if (moreColumnNames.length != moreItemNames.length)
			throw new IllegalArgumentException();
		int itemId;
		Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE "
				+ itemColumnName + "='" + itemName + "'", null);
		if (cursor.moveToNext()) {
			itemId = cursor.getInt(0);
			cursor.close();
		} else {
			cursor.close();
			String statement = "INSERT INTO " + tableName + " ("
					+ itemColumnName + "%s ) VALUES ( '" + itemName + "'%s )";
			if (moreItemNames.length > 0) {
				StringBuilder sbItems = new StringBuilder();
				StringBuilder sbColumns = new StringBuilder();
				for (int i = 0; i < moreColumnNames.length; i++) {
					sbItems.append(",'");
					sbItems.append(moreItemNames[i]);
					sbItems.append("'");
					sbColumns.append(",");
					sbColumns.append(moreColumnNames[i]);
				}
				statement = String.format(statement, sbColumns.toString(),
						sbItems.toString());
			} else {
				statement = String.format(statement, "", "");
			}
			db.execSQL(statement);
			cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE "
					+ itemColumnName + "='" + itemName + "'", null);
			cursor.moveToNext();
			itemId = cursor.getInt(0);
		}
		return itemId;
	}

	private static int convertItemIntoId(String itemName, SQLiteDatabase db,
			String tableName, String itemColumnName) {
		return convertItemIntoId(itemName, db, tableName, itemColumnName,
				EMPTY_ARRAY, EMPTY_ARRAY);
	}

}
