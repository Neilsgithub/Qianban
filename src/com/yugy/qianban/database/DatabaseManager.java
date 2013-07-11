package com.yugy.qianban.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	public DatabaseManager(Context context) {
		databaseHelper = new DatabaseHelper(context);
	}

	public void close() {
		database.close();
	}

	public void setAccount(Account account) {
		database = databaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM account", null);
		ContentValues value = new ContentValues();
		value.put("ck", account.ck);
		value.put("name", account.name);
		value.put("id", account.id);
		value.put("liked", account.liked);
		value.put("banned", account.banned);
		value.put("played", account.played);
		if (cursor.getCount() > 0) {
			database.update("account", value, "_id = ?", new String[] { "1" });
		} else {
			database.insert("account", null, value);
		}
		database.close();
	}

	public Account getAccount() {
		database = databaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM account", null);
		Account account = null;
		if (cursor.moveToFirst()) {
			account = new Account();
			account.ck = cursor.getString(cursor.getColumnIndex("ck"));
			account.name = cursor.getString(cursor.getColumnIndex("name"));
			account.id = cursor.getInt(cursor.getColumnIndex("id"));
			account.liked = cursor.getInt(cursor.getColumnIndex("liked"));
			account.banned = cursor.getInt(cursor.getColumnIndex("banned"));
			account.played = cursor.getInt(cursor.getColumnIndex("played"));
		}
		database.close();
		return account;
	}
	
	public void removeAccount(){
		database = databaseHelper.getWritableDatabase();
		database.delete("account", null, null);
		database.close();
	}
}