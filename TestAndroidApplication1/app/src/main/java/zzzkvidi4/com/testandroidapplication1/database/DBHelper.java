package zzzkvidi4.com.testandroidapplication1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Red Sky on 15.11.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "localDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table game ("
                + " id_game integer primary key,"
                + " name text not null,"
                + " display_name text not null,"
                + " description text not null,"
                + " icon blob, "
                + " max_difficulty integer not null" + ");");
        db.execSQL("create table user("
                + "id_user integer primary key, "
                + "id_server_user integer, "
                + "games_revision_date date, "
                + "name text not null, "
                + "surname text not null);");
        db.execSQL("create table game_match ("
                + " id_game_match integer primary key autoincrement,"
                + " id_game integer not null,"
                + " id_user integer not null,"
                + " score integer not null,"
                + " difficulty integer not null,"
                + " date integer not null,"
                + " foreign key (id_game) references game (id_game),"
                + " foreign key (id_user) references user (id_user));");
        ContentValues cv = new ContentValues();
        cv.put("id_game", 1);
        cv.put("name", "Парные карты");
        cv.put("description", "Открывайте карточки парами, пока они не закончатся!");
        cv.put("display_name", "Парные карты");
        cv.put("max_difficulty", 4);
        cv.put("icon", (String)null);
        try {
            long result = db.insertOrThrow("game", null, cv);
        } catch (Exception e){
            e.printStackTrace();
        }
        //cv.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
