package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

    private TextView infoTextView;
    LinearLayout gameWidgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        id = intent.getIntExtra("id", -1);
        setContentView(R.layout.activity_specks_game);
        //infoTextView = (TextView)findViewById(R.id.infoTextView);
        //infoTextView.append("Diff: " + difficulty + "; Score: " + score);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //gameWidgets = new LinearLayout(this);

        Button pauseBtn = (Button)findViewById(R.id.pauseGameBtn);
        //pauseBtn.setText("Пауза");
        //pauseBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.button), null, null, null);
        //pauseBtn.setBackground(Drawable.createFromPath("D:\\Projects\\AndroidFirstTask\\TestAndroidApplication1\\app\\src\\main\\res\\drawable\\button.xml"));
        pauseBtn.setOnClickListener(new PauseOnClickListener());

        //gameWidgets.addView(pauseBtn);

        //FrameLayout game = new FrameLayout(this);
        //SurfaceView gameSurfaceHolderCallback = new GameSurfaceHolderCallback(this, 2, 3);

        //game.addView(gameSurfaceHolderCallback);
        //game.addView(gameWidgets);
        SurfaceView view = (SurfaceView)findViewById(R.id.gameSurfaceView);
        GameController controller = new CardFieldController(this, 2, 3, getResources());
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
