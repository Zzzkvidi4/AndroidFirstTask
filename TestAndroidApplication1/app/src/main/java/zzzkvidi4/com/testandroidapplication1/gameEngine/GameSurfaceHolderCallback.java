package zzzkvidi4.com.testandroidapplication1.gameEngine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import zzzkvidi4.com.testandroidapplication1.SpecksGameActivity;

/**
 * Created by Roman on 07.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.gameEngine.
 */
public class GameSurfaceHolderCallback implements SurfaceHolder.Callback {
    private GameThread gameThread;
    private GameController controller;

    public GameSurfaceHolderCallback(SurfaceHolder holder, GameController controller) {
        //controller = new CardFieldController(SpecksGameActivity.this, fieldWidth, fieldHeight, getResources());
        this.controller = controller;
        this.gameThread = new GameThread(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                // ожидание завершение потока
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Создание области рисования
     */
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
    }

    /**
     * Изменение области рисования
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    private class GameThread extends Thread {
        private boolean isRunning;
        private SurfaceHolder holder;

        GameThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            controller.initializeField(canvas.getWidth(), canvas.getHeight());
            holder.unlockCanvasAndPost(canvas);
            controller.startGameCycle();
            while (isRunning) {
                canvas = null;
                try {
                    // подготовка Canvas-а
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        draw(canvas);
                        controller.draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    private void draw(Canvas canvas) {
        //super.draw(canvas);
        canvas.drawColor(Color.parseColor("#d7e8ef"));
    }
}
