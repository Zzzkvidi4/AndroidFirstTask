package zzzkvidi4.com.testandroidapplication1.database;

import java.sql.Date;

/**
 * Created by Roman on 09.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.database.
 */

public class GameMatch {
    private int idGameMatch;
    private int idGame;
    private int idUser;
    private int score;
    private int difficulty;
    private Date date;

    public Date getDate() {
        return date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getIdGame() {
        return idGame;
    }

    public int getIdGameMatch() {
        return idGameMatch;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getScore() {
        return score;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public void setIdGameMatch(int idGameMatch) {
        this.idGameMatch = idGameMatch;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
