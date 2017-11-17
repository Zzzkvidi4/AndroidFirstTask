package zzzkvidi4.com.testandroidapplication1.onClickListeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

import zzzkvidi4.com.testandroidapplication1.GameActivityFactory;

import static zzzkvidi4.com.testandroidapplication1.MainActivity.LOG_TAG;

/**
 * Created by Roman on 09.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.
 */

public class StartGameOnClickListener implements View.OnClickListener {
    private int id;
    private int difficulty;
    private int score;
    private boolean isFirstLaunch;
    private Activity activity;

    public StartGameOnClickListener(int id, int difficulty, int score, boolean isFirstLaunch, Activity activity){
        this.id = id;
        this.difficulty = difficulty;
        this.score = score;
        this.isFirstLaunch = isFirstLaunch;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        try {
            GameActivityFactory gameActivityFactory = GameActivityFactory.getInstance();
            Activity gameActivity = gameActivityFactory.createNewActivity(id);
            Intent intent = new Intent(activity, gameActivity.getClass());
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("score", score);
            intent.putExtra("isFirst", isFirstLaunch);
            intent.putExtra("id", id);
            activity.startActivity(intent);
        }
        catch(InvocationTargetException e){
            Log.d(LOG_TAG, "error in method invoke!");
        }
        catch(InstantiationException e){
            Log.d(LOG_TAG, "error in instantiation!");
        }
        catch (IllegalAccessException e){
            Log.d(LOG_TAG, "error in access to constructor!");
        }
    }
}
