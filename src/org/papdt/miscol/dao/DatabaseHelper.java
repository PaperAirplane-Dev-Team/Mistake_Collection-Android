package org.papdt.miscol.dao;

import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.QueryCondition;

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
	
	public DatabaseHelper(Context context, DatabaseErrorHandler handler){
		mDbOpenHelper = DbOpenHelper.getInstance(context, handler);
		mDatabase = mDbOpenHelper.getWritableDatabase();
	}

	public boolean insertMistake(Mistake mistake){
		ContentValues newMistake=new ContentValues();
		//TODO 搞定这些
		return false;
	}
	
	public Mistake[] queryMistake(QueryCondition condition){
		//TODO 实现根据条件查询错题的方法
		return null;
	}
	
	public boolean updateMistake(Mistake mistake){
		//TODO 实现对错题的修改
		return false;
	}
	
	public boolean deleteMistake(Mistake mistake){
		//实现对错题的删除
		return false;
	}
	
}
