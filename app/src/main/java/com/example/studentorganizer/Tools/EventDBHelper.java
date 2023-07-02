package com.example.studentorganizer.Tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentorganizer.Tools.EventContract.*;

import androidx.annotation.Nullable;

public final class EventDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "studentOrganizer3.db";
    public static final int DATABASE_VERSION = 3;

    public EventDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                EventEntry.TABLE_NAME + "(" +
                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EventEntry.COLUMN_LESSON + " TEXT NOT NULL, " +
                EventEntry.COLUMN_PROFESSOR + " TEXT NOT NULL, " +
                EventEntry.COLUMN_SELECTED_DAY + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_START_HOUR + " TEXT NOT NULL, " +
                EventEntry.COLUMN_START_MINUTE + " TEXT NOT NULL, " +
                EventEntry.COLUMN_END_HOUR + " TEXT NOT NULL, " +
                EventEntry.COLUMN_END_MINUTE + " TEXT NOT NULL, " +
                EventEntry.COLUMN_SELECTED_REMINDER_HOUR + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_REMINDER_ID + " INTEGER NOT NULL " +
                ");";
        db.execSQL(SQL_CREATE_LESSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(db);
    }
}
