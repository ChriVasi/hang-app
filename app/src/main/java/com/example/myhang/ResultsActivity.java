package com.example.myhang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView resultsTextView;
    private GameDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsTextView = findViewById(R.id.resultsTextView);
        databaseHelper = new GameDatabaseHelper(this);

        displayResults();
    }

    private void displayResults() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                GameDatabaseHelper.COLUMN_PLAYER1_NAME,
                GameDatabaseHelper.COLUMN_PLAYER2_NAME,
                GameDatabaseHelper.COLUMN_SECRET_WORD,
                GameDatabaseHelper.COLUMN_USER2_PLAYED,
                GameDatabaseHelper.COLUMN_LETTERS_USED,
                GameDatabaseHelper.COLUMN_WORD_FOUND
        };

        String sortOrder = GameDatabaseHelper.COLUMN_ID + " ASC";

        Cursor cursor = db.query(
                GameDatabaseHelper.TABLE_GAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        StringBuilder resultsBuilder = new StringBuilder();

        while (cursor.moveToNext()) {
            String playerName1 = cursor.getString(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_PLAYER1_NAME));
            String playerName2 = cursor.getString(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_PLAYER2_NAME));
            String secretWord = cursor.getString(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_SECRET_WORD));
            int user2Played = cursor.getInt(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_USER2_PLAYED));
            String lettersUsed = cursor.getString(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_LETTERS_USED));
            int wordFound = cursor.getInt(cursor.getColumnIndexOrThrow(GameDatabaseHelper.COLUMN_WORD_FOUND));

            resultsBuilder.append("Player 1: ").append(playerName1).append("\n");
            resultsBuilder.append("Secret Word: ").append(secretWord).append("\n");
            resultsBuilder.append("Player 2: ").append(playerName2).append("\n");
            resultsBuilder.append("User 2 Played: ").append(user2Played == 1).append("\n");
            resultsBuilder.append("Letters Used: ").append(lettersUsed).append("\n");

            String wordFoundStatus = (wordFound == 1) ? "WINNER" : "LOSER";
            resultsBuilder.append("Word Found: ").append(wordFoundStatus).append("\n");


            resultsBuilder.append("\n");
        }

        cursor.close();

        resultsTextView.setText(resultsBuilder.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
