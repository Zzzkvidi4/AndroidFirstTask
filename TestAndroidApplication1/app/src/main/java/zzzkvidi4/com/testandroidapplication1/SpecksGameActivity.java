package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zzzkvidi4.com.testandroidapplication1.gameEngine.GameController;
import zzzkvidi4.com.testandroidapplication1.gameEngine.GameSurfaceHolderCallback;
import zzzkvidi4.com.testandroidapplication1.specks.CardFieldController;

public class SpecksGameActivity extends AppCompatActivity {
    private int difficulty;
    private int score;
    private boolean isFirst;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        id = intent.getIntExtra("id", -1);
        setContentView(R.layout.activity_specks_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button pauseBtn = (Button)findViewById(R.id.pauseGameBtn);
        pauseBtn.setOnClickListener(new PauseOnClickListener());
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        pauseBtn.setTypeface(fontAwesomeFont);
        SurfaceView view = (SurfaceView)findViewById(R.id.gameSurfaceView);
        GameController controller = new CardFieldController(this, difficulty, getResources());
        GameSurfaceHolderCallback gameSurfaceHolderCallback = new GameSurfaceHolderCallback(view.getHolder(), controller);
        view.getHolder().addCallback(gameSurfaceHolderCallback);
        view.setOnTouchListener(new CardsOnTouchListener(controller));

    }

    @Override
    public void onBackPressed() {
    }

    private class CardsOnTouchListener implements View.OnTouchListener{
        private GameController controller;

        CardsOnTouchListener(GameController controller){
            this.controller = controller;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            controller.processTouch(motionEvent);
            return true;
        }
    }

    private class PauseOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SpecksGameActivity.this, GamePauseActivity.class);
            startActivity(intent);
        }
    }
}
