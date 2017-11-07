package zzzkvidi4.com.testandroidapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpecksGameActivity extends AppCompatActivity {
    private int difficulty;
    private int score;
    private boolean isFirst;

    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        //setContentView(R.layout.activity_specks_game);
        //infoTextView = (TextView)findViewById(R.id.infoTextView);
        //infoTextView.append("Diff: " + difficulty + "; Score: " + score);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        FrameLayout game = new FrameLayout(this);
        SurfaceView gameView = new GameView(this);
        LinearLayout gameWidgets = new LinearLayout(this);

        Button pauseBtn = new Button(this);
        pauseBtn.setText("Pause");
        pauseBtn.setOnClickListener(new PauseOnClickListener());

        gameWidgets.addView(pauseBtn);

        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);
    }

    @Override
    public void onBackPressed() {
    }

    public class GameView extends SurfaceView{
        private GameThread gameThread;

        public GameView(Context context) {
            super(context);
            this.gameThread = new GameThread(this);
            getHolder().addCallback(new SurfaceHolder.Callback()
            {
                /*** Уничтожение области рисования */
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    boolean retry = true;
                    gameThread.setRunning(false);
                    while (retry)
                    {
                        try
                        {
                            // ожидание завершение потока
                            gameThread.join();
                            retry = false;
                        }
                        catch (InterruptedException e) { }
                    }
                }

                /** Создание области рисования */
                public void surfaceCreated(SurfaceHolder holder)
                {
                    gameThread.setRunning(true);
                    gameThread.start();
                }

                /** Изменение области рисования */
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
                {
                }
            });
        }

        private class GameThread extends Thread {
            private boolean isRunning;
            private GameView view;

            public GameThread(GameView view){
                this.view = view;
            }

            public void setRunning(boolean isRunning){
                this.isRunning = isRunning;
            }

            @Override
            public void run() {
                while (isRunning)
                {
                    Canvas canvas = null;
                    try
                    {
                        // подготовка Canvas-а
                        canvas = view.getHolder().lockCanvas();
                        synchronized (view.getHolder())
                        {
                            //draw(canvas);
                            onDraw(canvas);
                        }
                    }
                    catch (Exception e) { }
                    finally
                    {
                        if (canvas != null)
                        {
                            view.getHolder().unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.RED);
        }
    }

    private class PauseOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
