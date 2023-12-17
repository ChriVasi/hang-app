package com.example.myhang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_game_database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GAME = "game";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER1_NAME = "player1_name";
    public static final String COLUMN_PLAYER2_NAME = "player2_name";
    public static final String COLUMN_SECRET_WORD = "secret_word";
    public static final String COLUMN_USER2_PLAYED = "user2_played";
    public static final String COLUMN_LETTERS_USED = "letters_used";
    public static final String COLUMN_WORD_FOUND = "word_found";

    private static final String CREATE_TABLE_GAME = "CREATE TABLE " + TABLE_GAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PLAYER1_NAME + " TEXT,"
            + COLUMN_PLAYER2_NAME + " TEXT,"
            + COLUMN_SECRET_WORD + " TEXT,"
            + COLUMN_USER2_PLAYED + " INTEGER,"
            + COLUMN_LETTERS_USED + " TEXT,"
            + COLUMN_WORD_FOUND + " TEXT"
            + ")";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
