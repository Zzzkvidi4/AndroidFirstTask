package zzzkvidi4.com.testandroidapplication1.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import zzzkvidi4.com.testandroidapplication1.database.DBHelper;

/**
 * Created by Roman on 09.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.
 */

public class DBOperations {
    private SQLiteDatabase db;

    public DBOperations(DBHelper helper){
        db = helper.getWritableDatabase();
    }

    public void addGameScore(int id_game, int score, int difficulty, int id_user){
        ContentValues cv = new ContentValues();
        cv.put("id_game", id_game);
        cv.put("id_user", id_user);
        cv.put("score", score);
        cv.put("difficulty", difficulty);
        cv.put("date", Calendar.getInstance().getTime().getTime());
        db.insert("game_match", null, cv);
    }

    public ArrayList<GameMatch> getLastMatches(int size, int id_user, int id_game, int difficulty){
        String selection = "id_user = ? and id_game = ? and difficulty = ?";
        String[] selectionValues = new String[]{Integer.toString(id_user), Integer.toString(id_game), Integer.toString(difficulty)};
        Cursor cursor = db.query("game_match", null, selection, selectionValues, null, null, "date");
        int colIndexScore = cursor.getColumnIndex("score");
        int colIndexDifficulty = cursor.getColumnIndex("difficulty");
        int colIndexDate = cursor.getColumnIndex("date");
        ArrayList<GameMatch> gameMatches = new ArrayList<>();
        if (cursor.moveToLast()){
            do {
                GameMatch match = new GameMatch();
                match.setDate(new Date(cursor.getLong(colIndexDate)));
                match.setIdGame(id_game);
                match.setDifficulty(cursor.getInt(colIndexDifficulty));
                match.setIdUser(id_user);
                match.setScore(cursor.getInt(colIndexScore));
                gameMatches.add(match);
                --size;
            } while (size > 0 && cursor.moveToPrevious());
        }
        cursor.close();
        return gameMatches;
    }
}
