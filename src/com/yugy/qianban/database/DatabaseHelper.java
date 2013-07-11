package com.yugy.qianban.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "qianban.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		// 初始化account表
		arg0.execSQL("CREATE TABLE IF NOT EXISTS account"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "ck TEXT, "
				+ "name TEXT, " + "id INTEGER, "
				+ "liked INTEGER, " + "banned INTEGER, "
				+ "played INTEGER)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("ALTER TABLE account ADD COLUMN other STRING");
	}

}