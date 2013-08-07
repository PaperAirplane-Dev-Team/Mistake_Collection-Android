package org.papdt.miscol.dao;

import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.utils.Constants.Databases.Grades;
import org.papdt.miscol.utils.Constants.Databases.IDbWithIdAndName;
import org.papdt.miscol.utils.Constants.Databases.Subjects;
import org.papdt.miscol.utils.Constants.Databases.QuestionType;
import org.papdt.miscol.utils.Constants.Databases.Tags;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataItemProcessor {
	
	//TODO 实现照片的存储编号

	public static void processMistake(Mistake mistake, SQLiteDatabase db) {
		mistake.setTypeId(convertItemIntoId(mistake.getTypeName(), db,
				QuestionType.TABLE_NAME));
		mistake.setSubjectId(convertItemIntoId(mistake.getSubjectName(), db,
				Subjects.TABLE_NAME));
		mistake.setGradeId(convertItemIntoId(mistake.getGradeName(), db,
				Grades.TABLE_NAME));
	}

	public static String convertTagsIntoString(Mistake mistake,
			SQLiteDatabase db) {
		StringBuilder sb=new StringBuilder();
		for (String tag : mistake.getTagNames()) {
			int tagId=convertItemIntoId(tag, db, Tags.TABLE_NAME);
			sb.append(tagId+", ");
		}
		return sb.toString();
	}

	private static int convertItemIntoId(String itemName, SQLiteDatabase db,
			String tableName) {
		// 处理Type
		int itemId;
		Cursor cursor = db.rawQuery("SELECT * FROM " + tableName
				+ " WHERE " + IDbWithIdAndName.KEY_STRING_NAME + "=" + itemName, null);
		if (cursor.moveToNext()) {
			itemId = cursor.getInt(1);
			cursor.close();
		} else {
			db.execSQL("INSERT INTO " + IDbWithIdAndName.KEY_STRING_NAME + " ("
					+ IDbWithIdAndName.KEY_STRING_NAME + ") VALUES (" + itemName + ")");
			cursor.close();
			cursor = db.rawQuery("SELECT * FROM " + tableName
					+ " WHERE " + IDbWithIdAndName.KEY_STRING_NAME + "=" + itemName, null);
			cursor.moveToNext();
			itemId = cursor.getInt(1);
		}
		return itemId;
	}
}
