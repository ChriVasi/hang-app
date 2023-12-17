package com.example.myhang;



public class Game {
    private long id;
    private String player1Name;
    private String player2Name;
    private String secretWord;
    private boolean user2Played;
    private String lettersUsed;
    private boolean wordFound;

    // Constructor
    public Game(long id, String player1Name, String player2Name, String secretWord, boolean user2Played, String lettersUsed, boolean wordFound) {
        this.id = id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.secretWord = secretWord;
        this.user2Played = user2Played;
        this.lettersUsed = lettersUsed;
        this.wordFound = wordFound;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

    public boolean isUser2Played() {
        return user2Played;
    }

    public void setUser2Played(boolean user2Played) {
        this.user2Played = user2Played;
    }

    public String getLettersUsed() {
        return lettersUsed;
    }

    public void setLettersUsed(String lettersUsed) {
        this.lettersUsed = lettersUsed;
    }

    public boolean isWordFound() {
        return wordFound;
    }

    public void setWordFound(boolean wordFound) {
        this.wordFound = wordFound;
    }
}
