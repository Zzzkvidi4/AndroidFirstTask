package zzzkvidi4.com.testandroidapplication1;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

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
import zzzkvidi4.com.testandroidapplication1.syncronization.User;
import zzzkvidi4.com.testandroidapplication1.syncronization.UserInfo;

public class GameOptionActivity extends AppCompatActivity {
    private static final int DEFAULT_DIFFICULTY = 1;
    private int id;
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
    private Button authBtn;
    private TextView scoreTV;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        authBtn = (Button)findViewById(R.id.authorizeBtn);
        authBtn.setVisibility(View.INVISIBLE);
        authBtn.setOnClickListener(new TryAgainUploadInfoListener());
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        authBtn.setTypeface(fontAwesomeFont);
        top10ListView = (ListView)findViewById(R.id.top10ListView);
        difficultySeekBar = (SeekBar) findViewById(R.id.difficultySeekBar);
        scoreTV = (TextView)findViewById(R.id.gameScore);
        loader = (ProgressBar)findViewById(R.id.progress);
        //int color = ContextCompat.getColor(this, R.color.mainFontColor);
        //loader.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        retrieveStaticInformationAboutGame();
        retrieveDynamicInformationAboutGame(DEFAULT_DIFFICULTY);
        difficultySeekBar.setMax(maxDifficulty - 1);
        difficultySeekBar.invalidate();
        difficultySeekBar.setOnSeekBarChangeListener(new OnDifficultySeekBarChanged());
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new StartGameOnClickListener(id, difficultySeekBar.getProgress() + 1, 0, false, this, true));
        TextView gameNameTextView = (TextView)findViewById(R.id.gameNameTextView);
        gameNameTextView.setText(name);
        TextView gameDescriptionTextView = (TextView)findViewById(R.id.gameDescriptionTextView);
        gameDescriptionTextView.setText(description);

    }

    public void retrieveStaticInformationAboutGame(){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        userId = preferences.getInt("id", getResources().getInteger(R.integer.no_user_id));
        token = preferences.getString("token", getResources().getString(R.string.no_token));
        DBOperations op = new DBOperations(new DBHelper(this));
        GameInfo gameInfo = op.getGameInfo(id);
        if (gameInfo == null) {
            finish();
            return;
        }
        name = gameInfo.getName();
        description = gameInfo.getDescription();
        maxDifficulty = gameInfo.getMaxDifficulty();
    }

    public void retrieveDynamicInformationAboutGame(int difficulty){
        if ((token.equals(getResources().getString(R.string.no_token))) || (userId == getResources().getInteger(R.integer.no_user_id))) {
            authBtn.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
            return;
        }
        DBOperations op = new DBOperations(new DBHelper(this));
        maxScore = op.getMaxScore(userId, id, difficulty);
        if (maxScore != -1)
            scoreTV.setText("Ваш рекорд: " + maxScore);
        MindBlowerAPI mindBlowerAPI = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MindBlowerAPI.MIND_BLOWER_SERVER_URL)
                .build()
                .create(MindBlowerAPI.class);
        loader.setVisibility(View.VISIBLE);
        top10ListView.setVisibility(View.INVISIBLE);
        mindBlowerAPI.getTopResults(id, difficulty, "Token " + token).enqueue(new Callback<TopResults>() {
            @Override
            public void onResponse(Call<TopResults> call, Response<TopResults> response) {
                loader.setVisibility(View.INVISIBLE);
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
                        top10ListView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TopResults> call, Throwable t) {
                Toast.makeText(GameOptionActivity.this, "Соединение с сервером недоступно.", Toast.LENGTH_LONG).show();
                top10ListView.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.INVISIBLE);
            }
        });
    }

    protected void setupUserInfo(String name, String surname, int id){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", id);
        editor.apply();
        if (id != -1){
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            Cursor c = db.query("user", null, "id_user = ?", new String[]{Integer.toString(id)}, null, null, null);
            if (c.getCount() == 0) {
                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("id_user", id);
                db.insert("user", null, cv);
            }
            c.close();
            db.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new CustomVKCallback())) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class CustomVKCallback implements VKCallback<VKAccessToken>{
        @Override
        public void onResult(VKAccessToken res) {
            String token = res.accessToken;
            String email = res.email;
            String provider = "vk";
            MindBlowerAPI mindBlowerAPI = new Retrofit.Builder()
                    .baseUrl(MindBlowerAPI.MIND_BLOWER_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MindBlowerAPI.class);
            UserInfo info = new UserInfo();
            info.setEmail(email);
            info.setProvider("vk");
            info.setVk_token(token);
            mindBlowerAPI.getUserToken(info).enqueue(new OnMindBlowerTokenGet());
            VKRequest request = new VKRequest("account.getProfileInfo");
            request.executeWithListener(new CustomVKRequestListener(Integer.parseInt(res.userId)));
        }

        @Override
        public void onError(VKError error) {
            authBtn.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
            // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
        }
    }

    private class OnMindBlowerTokenGet implements Callback<User>{
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.body() != null) {
                preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                preferences.edit().putString("token", response.body().getToken()).apply();
                GameOptionActivity.this.token = response.body().getToken();
                authBtn.setVisibility(View.INVISIBLE);
                retrieveDynamicInformationAboutGame(difficultySeekBar.getProgress() + 1);
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            authBtn.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
        }
    }

    private class CustomVKRequestListener extends VKRequest.VKRequestListener{
        private int userId;

        CustomVKRequestListener(int userId){
            this.userId = userId;
        }

        @Override
        public void onComplete(VKResponse response) {
            JSONObject resp = response.json;
            String name;
            String surname;
            try {
                name = resp.getJSONObject("response").getString("first_name");
                surname = resp.getJSONObject("response").getString("last_name");
            } catch (JSONException e) {
                name = "Имя";
                surname = "Фамилия";
            }
            setupUserInfo(name, surname, userId);
            GameOptionActivity.this.userId = userId;
        }

        @Override
        public void onError(VKError error) {
            authBtn.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
            Toast.makeText(GameOptionActivity.this, "Произошла ошибка соединения с серверами vk. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            authBtn.setVisibility(View.VISIBLE);
            loader.setVisibility(View.INVISIBLE);
            Toast.makeText(GameOptionActivity.this, "Произошла ошибка соединения с серверами vk. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
        }
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
            startBtn.setOnClickListener(new StartGameOnClickListener(id, difficultySeekBar.getProgress() + 1, 0, false, GameOptionActivity.this, true));
            loader.setVisibility(View.VISIBLE);
            int difficulty = seekBar.getProgress() + 1;
            retrieveDynamicInformationAboutGame(difficulty);
        }
    }

    private class TryAgainUploadInfoListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            authBtn.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.VISIBLE);
            VKSdk.login(GameOptionActivity.this, VKScope.EMAIL);
        }
    }
}
