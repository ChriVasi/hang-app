package com.example.myhang;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText player1NameEditText;
    private EditText player2NameEditText;
    private EditText secretWordEditText;

    private Button startGameButton;
    private Button showResultsButton;
    private GameDatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new GameDatabaseHelper(this);

        player1NameEditText = findViewById(R.id.player1NameEditText);
        player2NameEditText = findViewById(R.id.player2NameEditText);
        secretWordEditText = findViewById(R.id.secretWordEditText);
        startGameButton = findViewById(R.id.startGameButton);
        showResultsButton = findViewById(R.id.showResultsButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player1Name = player1NameEditText.getText().toString();
                String player2Name = player2NameEditText.getText().toString();
                String secretWord = secretWordEditText.getText().toString();



                if (!player1Name.isEmpty() && !player2Name.isEmpty() && !secretWord.isEmpty()) {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    long gameId = saveGameElements(db, player1Name, player2Name, secretWord);

                    if (gameId != -1) {
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("gameId", gameId);
                        intent.putExtra("playerName2", player2Name);
                        intent.putExtra("secretWord", secretWord);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to save game elements to database.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter both player names and a secret word.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });
    }

    private long saveGameElements(SQLiteDatabase db, String player1Name, String player2Name, String secretWord) {
        ContentValues values = new ContentValues();
        values.put(GameDatabaseHelper.COLUMN_PLAYER1_NAME, player1Name);
        values.put(GameDatabaseHelper.COLUMN_PLAYER2_NAME, player2Name);
        values.put(GameDatabaseHelper.COLUMN_SECRET_WORD, secretWord);
        values.put(GameDatabaseHelper.COLUMN_USER2_PLAYED, 0);
        values.put(GameDatabaseHelper.COLUMN_LETTERS_USED, "");
        values.put(GameDatabaseHelper.COLUMN_WORD_FOUND, "Word Found");

        long gameId = db.insert(GameDatabaseHelper.TABLE_GAME, null, values);
        return gameId;
    }
}
