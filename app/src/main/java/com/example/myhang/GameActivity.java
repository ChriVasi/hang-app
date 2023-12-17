package com.example.myhang;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private TextView player2NameTextView;
    private EditText guessEditText;
    private Button submitButton;
    private ImageView hangmanImageView;
    private Button startNewButton;
    private TextView guessedLettersTextView;


    private String secretWord;
    private StringBuilder guessedWord;
    private StringBuilder guessedLetters;

    private int remainingAttempts;

    private GameDatabaseHelper databaseHelper;
    private long gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        player2NameTextView = findViewById(R.id.player2NameTextView);
        guessEditText = findViewById(R.id.guessEditText);
        submitButton = findViewById(R.id.submitButton);
        hangmanImageView = findViewById(R.id.hangmanImageView);
        startNewButton = findViewById(R.id.startNewButton);
        guessedLettersTextView = findViewById(R.id.guessedLettersTextView);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String playerName2 = extras.getString("playerName2");
            secretWord = extras.getString("secretWord");
            gameId = extras.getLong("gameId");

            if (playerName2 != null) {
                player2NameTextView.setText(playerName2);
            }
        }

        guessedWord = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            guessedWord.append("-");
        }

        guessEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    s.delete(1, s.length());
                }
            }
        });

        guessedLetters = new StringBuilder();

        remainingAttempts = 7;

        databaseHelper = new GameDatabaseHelper(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guess = guessEditText.getText().toString().toUpperCase();

                if (guess.isEmpty()) {
                    Toast.makeText(GameActivity.this, "Please enter a guess.", Toast.LENGTH_SHORT).show();
                    return;
                }

                char guessedLetter = guess.charAt(0);

                if (guessedLetters.indexOf(String.valueOf(guessedLetter)) >= 0) {
                    Toast.makeText(GameActivity.this, "You have already guessed that letter.", Toast.LENGTH_SHORT).show();
                    return;
                }

                guessedLetters.append(guessedLetter).append(" ");

                boolean correctGuess = false;
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guessedLetter) {
                        guessedWord.setCharAt(i, guessedLetter);
                        correctGuess = true;
                    }
                }

                if (!correctGuess) {
                    remainingAttempts--;
                }

                updateUI();

                if (guessedWord.toString().equals(secretWord)) {
                    Toast.makeText(GameActivity.this, "Congratulations! You won!", Toast.LENGTH_SHORT).show();
                    saveGameStep(guess, true); // Save game step when the player wins
                } else if (remainingAttempts == 0) {
                    Toast.makeText(GameActivity.this, "Game over! You lost. The word was: " + secretWord, Toast.LENGTH_SHORT).show();
                    saveGameStep(guess, false); // Save game step when the player loses
                } else {
                    saveGameStep(guess, false); // Save game step for normal guesses
                }

                guessEditText.setText("");
            }
        });

        startNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }

    private void updateUI() {
        TextView guessedWordTextView = findViewById(R.id.guessedWordTextView);
        TextView attemptsTextView = findViewById(R.id.attemptsTextView);

        guessedWordTextView.setText(guessedWord.toString());
        attemptsTextView.setText("Attempts remaining: " + remainingAttempts);

        guessedLettersTextView.setText("Guessed Letters: " + guessedLetters.toString());

        int drawableResourceId;
        switch (remainingAttempts) {
            case 7:
                drawableResourceId = R.drawable.game0;
                break;
            case 6:
                drawableResourceId = R.drawable.game1;
                break;
            case 5:
                drawableResourceId = R.drawable.game2;
                break;
            case 4:
                drawableResourceId = R.drawable.game3;
                break;
            case 3:
                drawableResourceId = R.drawable.game4;
                break;
            case 2:
                drawableResourceId = R.drawable.game5;
                break;
            case 1:
                drawableResourceId = R.drawable.game6;
                break;
            default:
                drawableResourceId = R.drawable.game7;
                break;
        }

        hangmanImageView.setImageResource(drawableResourceId);
    }

    private void saveGameStep(String guessedletters, boolean isWin) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameDatabaseHelper.COLUMN_USER2_PLAYED, 1);
        values.put(GameDatabaseHelper.COLUMN_LETTERS_USED, guessedLetters.toString());
        values.put(GameDatabaseHelper.COLUMN_WORD_FOUND, isWin ? 1 : 0);


        String selection = GameDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(gameId)};

        int rowsUpdated = db.update(GameDatabaseHelper.TABLE_GAME, values, selection, selectionArgs);

        if (rowsUpdated == 0) {
            Toast.makeText(GameActivity.this, "Error saving game step.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GameActivity.this, "Game step saved.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
