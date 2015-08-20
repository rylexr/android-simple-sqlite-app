package com.tinbytes.simplesqliteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Date;

public class SimpleSqliteActivity extends AppCompatActivity {
  private static final String TAG = SimpleSqliteActivity.class.getName();
  private static final String DATABASE_NAME = "samplesimplesqlite.db";
  private static final String CREATE_AUTHOR_TABLE_STATEMENT =
      "CREATE TABLE IF NOT EXISTS author (" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
          "firstname TEXT," +
          "lastname TEXT" +
          ");";
  private static final String CREATE_BOOK_TABLE_STATEMENT =
      "CREATE TABLE IF NOT EXISTS book (" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
          "title TEXT," +
          "date_added DATE," +
          "author_id INTEGER NOT NULL CONSTRAINT author_id REFERENCES author(id) ON DELETE CASCADE" +
          ");";

  private SQLiteDatabase database;
  private long newBookID1;
  private long newBookID2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_sqlite);

    findViewById(R.id.bCreateDatabase).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createDatabase();
      }
    });
    findViewById(R.id.bInsertData).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        insertData();
      }
    });
    findViewById(R.id.bListData).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listData();
      }
    });
    findViewById(R.id.bUpdateData).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateData();
      }
    });
    findViewById(R.id.bDeleteData).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteData();
      }
    });
    findViewById(R.id.bDeleteDatabase).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteDatabase();
      }
    });
  }

  private void createDatabase() {
    database = openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    database.execSQL(CREATE_AUTHOR_TABLE_STATEMENT);
    database.execSQL(CREATE_BOOK_TABLE_STATEMENT);
    Log.d(TAG, "---DATABASE samplesimplesqlite.db CREATED---");
  }

  private void insertData() {
    ContentValues values = new ContentValues();
    values.put("firstname", "J.K.");
    values.put("lastname", "Rowling");
    long newAuthorID = database.insert("author", null, values);

    values = new ContentValues();
    values.put("title", "Harry Potter and the Philosopher's Stone");
    values.put("date_added", new Date().getTime());
    values.put("author_id", newAuthorID);
    newBookID1 = database.insert("book", null, values);

    values = new ContentValues();
    values.put("title", "Harry PottAr and the Chamber of Secrets");
    values.put("date_added", new Date().getTime());
    values.put("author_id", newAuthorID);
    newBookID2 = database.insert("book", null, values);

    Log.d(TAG, "---DATA INSERTED---");
  }

  private void listData() {
    Log.d(TAG, "---AUTHORS---");
    Cursor c = database.query("author", null, null, null, null, null, null);
    while (c.moveToNext()) {
      Log.d(TAG, "Id: " + c.getLong(c.getColumnIndex("id")));
      Log.d(TAG, "Firstname: " + c.getString(c.getColumnIndex("firstname")));
      Log.d(TAG, "Lastname: " + c.getString(c.getColumnIndex("lastname")));
    }
    c.close();

    Log.d(TAG, "---BOOKS---");
    c = database.query("book", null, null, null, null, null, null);
    while (c.moveToNext()) {
      Log.d(TAG, "Id: " + c.getLong(c.getColumnIndex("id")));
      Log.d(TAG, "Title: " + c.getString(c.getColumnIndex("title")));
      Log.d(TAG, "Date Added: " + new Date(c.getLong(c.getColumnIndex("date_added"))));
      Log.d(TAG, "Author Id: " + c.getLong(c.getColumnIndex("author_id")));
    }
    c.close();
  }

  private void updateData() {
    ContentValues values = new ContentValues();
    values.put("title", "Harry Potter and the Chamber of Secrets");
    database.update("book", values, "id=?", new String[]{String.valueOf(newBookID2)});
    Log.d(TAG, "---DATA UPDATED---");
  }

  private void deleteData() {
    database.delete("book", "id=?", new String[]{String.valueOf(newBookID1)});
    Log.d(TAG, "---DATA DELETED---");
  }

  private void deleteDatabase() {
    database.close();
    deleteDatabase(DATABASE_NAME);
    Log.d(TAG, "---DATABASE DELETED---");
  }
}


