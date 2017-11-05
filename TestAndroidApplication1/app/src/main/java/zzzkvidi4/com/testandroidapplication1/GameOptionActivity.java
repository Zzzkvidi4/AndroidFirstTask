package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.lang.reflect.InvocationTargetException;

import static zzzkvidi4.com.testandroidapplication1.MainActivity.LOG_TAG;

public class GameOptionActivity extends AppCompatActivity {
    private long id;
    private GameActivityFactory gameActivityFactory;
    private String name;
    private String description;
    private int maxDifficulty;
    private int currentDifficulty;
    private int previousScore;

    private SharedPreferences preferences;
    private SeekBar difficultySeekBar;
    private Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);
        Intent intent = getIntent();
        gameActivityFactory = GameActivityFactory.getInstance();
        id = (int) intent.getLongExtra("id", 0);
        retrieveInformationAboutGame();
        difficultySeekBar = (SeekBar) findViewById(R.id.difficultySeekBar);
        difficultySeekBar.setProgress(currentDifficulty);
        difficultySeekBar.setMax(maxDifficulty);
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new StartGameOnClickListener());
    }

    public void retrieveInformationAboutGame(){
        preferences = getSharedPreferences("game_info" + id, MODE_PRIVATE);
        name = preferences.getString("name", "");
        description = preferences.getString("description", "");
        maxDifficulty = preferences.getInt("maxDifficulty", 0);
        currentDifficulty = preferences.getInt("currentDifficulty", 0);
        previousScore = preferences.getInt("previousScore", 0);
    }

    private class StartGameOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                Activity gameActivity = gameActivityFactory.createNewActivity((int) id);
                Intent intent = new Intent(GameOptionActivity.this, gameActivity.getClass());
                intent.putExtra("difficulty", difficultySeekBar.getProgress());
                intent.putExtra("score", previousScore);
                intent.putExtra("isFirst", false);
                startActivity(intent);
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
}
