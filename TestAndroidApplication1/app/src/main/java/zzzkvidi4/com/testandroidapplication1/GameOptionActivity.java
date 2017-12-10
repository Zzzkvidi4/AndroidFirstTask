package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zzzkvidi4.com.testandroidapplication1.database.DBHelper;
import zzzkvidi4.com.testandroidapplication1.database.DBOperations;
import zzzkvidi4.com.testandroidapplication1.database.GameInfo;
import zzzkvidi4.com.testandroidapplication1.onClickListeners.StartGameOnClickListener;
import zzzkvidi4.com.testandroidapplication1.syncronization.MindBlowerAPI;
import zzzkvidi4.com.testandroidapplication1.syncronization.Top;
import zzzkvidi4.com.testandroidapplication1.syncronization.TopResults;
import zzzkvidi4.com.testandroidapplication1.syncronization.TopUser;

public class GameOptionActivity extends AppCompatActivity {
    private static final int DEFAULT_DIFFICULTY = 1;
    private long id;
    private int userId;
    private String name;
    private String description;
    private String token;
    private int maxDifficulty;
    private int maxScore;
    private SharedPreferences preferences;
    private ListView top10ListView;
    private Button startBtn;
    private SeekBar difficultySeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);
        Intent intent = getIntent();
        id = (int) intent.getLongExtra("id", 0);
        retrieveStaticInformationAboutGame();
        retrieveDynamicInformationAboutGame(DEFAULT_DIFFICULTY);
        difficultySeekBar = (SeekBar) findViewById(R.id.difficultySeekBar);
        difficultySeekBar.setMax(maxDifficulty - 1);
        difficultySeekBar.invalidate();
        difficultySeekBar.setOnSeekBarChangeListener(new OnDifficultySeekBarChanged());
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new StartGameOnClickListener((int)id - 1, difficultySeekBar.getProgress() + 1, 0, false, this, true));
        TextView gameNameTextView = (TextView)findViewById(R.id.gameNameTextView);
        gameNameTextView.setText(name);
        TextView gameDescriptionTextView = (TextView)findViewById(R.id.gameDescriptionTextView);
        gameDescriptionTextView.setText(description);
        top10ListView = (ListView)findViewById(R.id.top10ListView);
    }

    public void retrieveStaticInformationAboutGame(){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        userId = preferences.getInt("id", getResources().getInteger(R.integer.no_user_id));
        token = preferences.getString("token", getResources().getString(R.string.no_token));
        DBOperations op = new DBOperations(new DBHelper(this));
        GameInfo gameInfo = op.getGameInfo((int)id);
        if (gameInfo == null) {
            finish();
            return;
        }
        description = gameInfo.getDescription();
        maxDifficulty = gameInfo.getMaxDifficulty();
    }

    public void retrieveDynamicInformationAboutGame(int difficulty){
        DBOperations op = new DBOperations(new DBHelper(this));
        maxScore = op.getMaxScore(userId, (int)id, difficulty);
        MindBlowerAPI mindBlowerAPI = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MindBlowerAPI.MIND_BLOWER_SERVER_URL)
                .build()
                .create(MindBlowerAPI.class);
        mindBlowerAPI.getTopResults((int)id, difficulty, "Token " + token).enqueue(new Callback<TopResults>() {
            @Override
            public void onResponse(Call<TopResults> call, Response<TopResults> response) {
                if (response.body() != null){
                    TopResults results = response.body();
                    ArrayList<String> resultList = new ArrayList<>();
                    for (Top top: results.getTop10()){
                        resultList.add(top.getBest_score() + ": " + top.getUser().toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GameOptionActivity.this, android.R.layout.simple_list_item_1, resultList);
                    if (top10ListView != null) {
                        top10ListView.setAdapter(arrayAdapter);
                        top10ListView.invalidate();
                    }
                }
            }

            @Override
            public void onFailure(Call<TopResults> call, Throwable t) {
                Toast.makeText(GameOptionActivity.this, "Соединение с сервером недоступно.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class OnDifficultySeekBarChanged implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            startBtn.setOnClickListener(new StartGameOnClickListener((int)id - 1, difficultySeekBar.getProgress() + 1, 0, false, GameOptionActivity.this, true));
            int difficulty = seekBar.getProgress();
            retrieveDynamicInformationAboutGame(difficulty);
        }
    }
}
