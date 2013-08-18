package org.papdt.miscol.dao;

import org.papdt.miscol.utils.Constants.Databases;
import org.papdt.miscol.utils.Constants.Databases.*;

import static org.papdt.miscol.utils.Constants.Databases.SqlStatements.*;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	
	private static DbOpenHelper sInstance;
	
	public static DbOpenHelper getInstance(Context context,
			DatabaseErrorHandler handler) {
		if (sInstance == null) {
			sInstance = new DbOpenHelper(context, handler);
		}
		return sInstance;
	}

	@Deprecated
	private DbOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
		super(context, Databases.FILE_NAME, null, Databases.VERSION,
				errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createAllTables(db);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAllTables(db);
		createAllTables(db);

	}
	
	private void createAllTables(SQLiteDatabase db){
		db.execSQL(CREATE + MISTAKES);
		db.execSQL(CREATE + SUBJECTS);
		db.execSQL(CREATE + GRADES);
		db.execSQL(CREATE + TAGS);
		db.execSQL(CREATE + QUESTION_TYPE);
		db.execSQL(CREATE + FILES);
	}
	
	private void dropAllTables(SQLiteDatabase db){
		db.execSQL(DROP + Mistakes.TABLE_NAME);
		db.execSQL(DROP + Subjects.TABLE_NAME);
		db.execSQL(DROP + Grades.TABLE_NAME);
		db.execSQL(DROP + Tags.TABLE_NAME);
		db.execSQL(DROP + QuestionType.TABLE_NAME);
		db.execSQL(DROP + Files.TABLE_NAME);
	}

}
