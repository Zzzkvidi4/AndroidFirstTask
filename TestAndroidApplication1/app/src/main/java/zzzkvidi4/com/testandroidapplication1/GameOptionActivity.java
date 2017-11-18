package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import zzzkvidi4.com.testandroidapplication1.onClickListeners.StartGameOnClickListener;

public class GameOptionActivity extends AppCompatActivity {
    private long id;
    private String name;
    private String description;
    private int maxDifficulty;
    private int currentDifficulty;
    private int previousScore;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);
        Intent intent = getIntent();
        id = (int) intent.getLongExtra("id", 0);
        retrieveInformationAboutGame();
        SeekBar difficultySeekBar = (SeekBar) findViewById(R.id.difficultySeekBar);
        difficultySeekBar.setProgress(currentDifficulty);
        difficultySeekBar.setMax(maxDifficulty);
        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new StartGameOnClickListener((int)id, difficultySeekBar.getProgress(), previousScore, false, this));
        TextView gameNameTextView = (TextView)findViewById(R.id.gameNameTextView);
        gameNameTextView.setText(name);
        TextView gameDescriptionTextView = (TextView)findViewById(R.id.gameDescriptionTextView);
        gameDescriptionTextView.setText(description);
    }

    public void retrieveInformationAboutGame(){
        preferences = getSharedPreferences("game_info" + id, MODE_PRIVATE);
        name = preferences.getString("name", "");
        description = preferences.getString("description", "");
        maxDifficulty = preferences.getInt("maxDifficulty", 0);
        currentDifficulty = preferences.getInt("currentDifficulty", 0);
        previousScore = preferences.getInt("previousScore", 0);
    }

}
