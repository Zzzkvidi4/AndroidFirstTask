package zzzkvidi4.com.testandroidapplication1.database;

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
                + "id_game integer primary key autoincrement,"
                + "name text not null,"
                + "display_name text not null,"
                + "description text not null,"
                + "icon blob not null" + ");");
        db.execSQL("create table user("
                + "id_user integer primary key, "
                + "id_server_user integer, "
                + "games_revision_date date, "
                + "name text not null, "
                + "surname text not null);");
        db.execSQL("create table game_match ("
                + "id_game_match integer primary key autoincrement,"
                + " id_game integer not null,"
                + " id_user integer not null,"
                + " score integer not null,"
                + " difficulty integer not null,"
                + " date integer not null,"
                + " foreign key (id_game) references game (id_game),"
                + " foreign key (id_user) references user (id_user));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
