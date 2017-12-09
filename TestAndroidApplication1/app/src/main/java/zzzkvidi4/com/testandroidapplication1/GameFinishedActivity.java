package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import zzzkvidi4.com.testandroidapplication1.database.DBHelper;
import zzzkvidi4.com.testandroidapplication1.database.DBOperations;
import zzzkvidi4.com.testandroidapplication1.database.GameMatch;
import zzzkvidi4.com.testandroidapplication1.onClickListeners.BackToMenuOnClickListener;
import zzzkvidi4.com.testandroidapplication1.onClickListeners.StartGameOnClickListener;

public class GameFinishedActivity extends AppCompatActivity {
    private final static int MATCHES_TO_SHOW_COUNT = 10;
    private int difficulty;
    private int id;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);
        uploadInfo(getIntent());
        Button backToMenuBtn = (Button)findViewById(R.id.backToMenuBtn);
        backToMenuBtn.setOnClickListener(new BackToMenuOnClickListener(this, true));
    }

    public void uploadInfo(Intent intent){
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int userId = preferences.getInt("id", -1);
        difficulty = intent.getIntExtra("difficulty", 0);
        id = intent.getIntExtra("id", 0);
        score = intent.getIntExtra("score", 0);
        if (userId != -1){
            DBOperations operations = new DBOperations(new DBHelper(this));
            operations.addGameScore(id, score, difficulty, userId);
            LineChart chart = (LineChart)findViewById(R.id.chart);
            List<Entry> entries = new ArrayList<>();
            List<GameMatch> lastMatches = operations.getLastMatches(MATCHES_TO_SHOW_COUNT, userId, id, difficulty);
            int position = 0;
            for(GameMatch match: lastMatches){
                entries.add(new Entry(position, Math.abs(match.getScore())));
                ++position;
            }
            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setColor(R.color.colorPrimaryDark);
            chart.setData(new LineData(dataSet));
            chart.invalidate();
        }
        Button tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(new StartGameOnClickListener(id, difficulty, score, false, this, true));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        uploadInfo(intent);
    }
}
