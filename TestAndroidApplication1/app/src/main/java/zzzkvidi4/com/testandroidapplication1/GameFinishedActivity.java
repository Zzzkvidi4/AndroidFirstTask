package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import zzzkvidi4.com.testandroidapplication1.onClickListeners.BackToMenuOnClickListener;
import zzzkvidi4.com.testandroidapplication1.onClickListeners.StartGameOnClickListener;

public class GameFinishedActivity extends AppCompatActivity {
    private int difficulty;
    private int id;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        difficulty = intent.getIntExtra("difficulty", 0);
        id = intent.getIntExtra("id", 0);
        score = intent.getIntExtra("score", 0);
        Button tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(new StartGameOnClickListener(id, difficulty, score, false, this, true));
        Button backToMenuBtn = (Button)findViewById(R.id.backToMenuBtn);
        backToMenuBtn.setOnClickListener(new BackToMenuOnClickListener(this, true));
    }
}
