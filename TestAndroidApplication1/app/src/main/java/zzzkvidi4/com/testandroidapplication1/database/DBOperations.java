package zzzkvidi4.com.testandroidapplication1.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import zzzkvidi4.com.testandroidapplication1.database.DBHelper;

/**
 * Created by Roman on 09.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.
 */

public class DBOperations {
    private static final String UNAUTHORIZED_USER = "Анонимный игрок";
    public static final int NO_SCORES = -1;
    private SQLiteDatabase db;

    public DBOperations(DBHelper helper){
        db = helper.getWritableDatabase();
    }

    public String getUserFIOString(int user_id){
        String[] columns = new String[] {"name", "surname"};
        String selection = "id_user = ?";
        String[] selectionValues = new String[] {Integer.toString(user_id)};
        Cursor cursor = db.query("user", columns, selection, selectionValues, null, null, null);
        int colIndexName = cursor.getColumnIndex("name");
        int colIndexSurname = cursor.getColumnIndex("surname");
        if (!cursor.moveToFirst()){
            cursor.close();
            return UNAUTHORIZED_USER;
        } else {
            String name = cursor.getString(colIndexName);
            String surname = cursor.getString(colIndexSurname);
            cursor.close();
            return name + " " + surname;
        }
    }

    public Bitmap getUserIcon(int user_id){
        String[] columns = new String[] {"icon"};
        String selection = "id_user = ?";
        String[] selectionValues = new String[] {Integer.toString(user_id)};
        Cursor cursor = db.query("user", columns, selection, selectionValues, null, null, null);
        int colIndexIcon = cursor.getColumnIndex("icon");
        if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }
        byte[] iconBytes = cursor.getBlob(colIndexIcon);
        return BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
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
                gameMatches.add(0, match);
                --size;
            } while (size > 0 && cursor.moveToPrevious());
        }
        cursor.close();
        return gameMatches;
    }

    public int getMaxScore(int id_user, int id_game, int difficulty){
        String[] columns = new String[]{"score"};
        String selection = "id_user = ? and id_game = ? and difficulty = ?";
        String[] selectionValues = new String[]{Integer.toString(id_user), Integer.toString(id_game), Integer.toString(difficulty)};
        Cursor cursor = db.query("game_match", columns, selection, selectionValues, null, null, "score");
        int colIndexScore = cursor.getColumnIndex("score");
        if (cursor.moveToLast()){
            int score = cursor.getInt(colIndexScore);
            cursor.close();
            return score;
        } else {
            cursor.close();
            return NO_SCORES;
        }
    }

    public GameInfo getGameInfo(int gameId){
        String selection = "id_game = ?";
        String[] selectionValues = new String[] {Integer.toString(gameId)};
        Cursor cursor = db.query("game", null, selection, selectionValues, null, null, null);
        if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }
        GameInfo gameInfo = new GameInfo();
        int colIndexName = cursor.getColumnIndex("name");
        int colIndexDescription = cursor.getColumnIndex("description");
        int colIndexDisplayName = cursor.getColumnIndex("display_name");
        int colIndexMaxDifficulty = cursor.getColumnIndex("max_difficulty");
        gameInfo.setName(cursor.getString(colIndexName));
        gameInfo.setDescription(cursor.getString(colIndexDescription));
        gameInfo.setDisplayName(cursor.getString(colIndexDisplayName));
        gameInfo.setMaxDifficulty(cursor.getInt(colIndexMaxDifficulty));
        return gameInfo;
    }
}
