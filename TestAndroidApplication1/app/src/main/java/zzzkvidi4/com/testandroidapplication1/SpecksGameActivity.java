package zzzkvidi4.com.testandroidapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import zzzkvidi4.com.testandroidapplication1.specks.CardField;
import zzzkvidi4.com.testandroidapplication1.specks.CardFieldController;
import zzzkvidi4.com.testandroidapplication1.specks.CardGameObject;
import zzzkvidi4.com.testandroidapplication1.tools.BitmapTools;

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
        //SurfaceView gameView = new GameView(this, 2, 3);

        //game.addView(gameView);
        //game.addView(gameWidgets);
        SurfaceView view = (SurfaceView)findViewById(R.id.gameSurfaceView);
        GameController controller = new CardFieldController(this, 2, 3, getResources());
        GameView gameView = new GameView(view.getHolder(), controller);
        view.getHolder().addCallback(gameView);
        view.setOnTouchListener(new CardsOnTouchListener(controller));
        //setContentView(R.layout.activity_specks_game);
    }

    @Override
    public void onBackPressed() {
    }

    public class GameView implements SurfaceHolder.Callback {
        private GameThread gameThread;
        private GameController controller;

        public GameView(SurfaceHolder holder, GameController controller) {
            //controller = new CardFieldController(SpecksGameActivity.this, fieldWidth, fieldHeight, getResources());
            this.controller = controller;
            this.gameThread = new GameThread(holder);
        }

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

        private class GameThread extends Thread {
            private boolean isRunning;
            private SurfaceHolder holder;

            GameThread(SurfaceHolder holder){
                this.holder = holder;
            }

            void setRunning(boolean isRunning){
                this.isRunning = isRunning;
            }

            @Override
            public void run() {
                Canvas canvas = holder.lockCanvas();
                controller.initializeField(canvas.getWidth(), canvas.getHeight());
                holder.unlockCanvasAndPost(canvas);
                controller.startGameCycle();
                while (isRunning)
                {
                    canvas = null;
                    try
                    {
                        // подготовка Canvas-а
                        canvas = holder.lockCanvas();
                        synchronized (holder)
                        {
                            draw(canvas);
                            controller.draw(canvas);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (canvas != null)
                        {
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
                finish();
            }
        }

        public void draw(Canvas canvas) {
            //super.draw(canvas);
            canvas.drawColor(Color.parseColor("#d7e8ef"));
        }
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
