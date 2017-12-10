package zzzkvidi4.com.testandroidapplication1.seqRepeater;

import android.graphics.Color;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import zzzkvidi4.com.testandroidapplication1.R;

/**
 * Created by Red Sky on 08.12.2017.
 */

public class GameField {
    private static final int ELEMS_DISTANCE = 30;
    private static final int SHUFFLE_STEPS = 50;
    private static final int WINCOLOR = Color.GREEN;
    private static final int LOSECOLOR = Color.RED;
    private static final int MAINCOLOR = Color.BLUE;
    public static final int ONE_CARD_DELAY = 1000;
    private int fieldWidth;
    private int fieldHeight;
    private int elemWidth;
    private int elemHeight;
    private SeqRepeaterGameObject[][] field;
    private int[] winSeq;
    private int guessedCount;
    private boolean isTouchable = true;
    private boolean islosed;


    public GameField(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.field = new SeqRepeaterGameObject[fieldHeight][fieldWidth];
        this.winSeq = new int[fieldHeight*fieldWidth];

    }

    public SeqRepeaterGameObject[][] getField() {return field;}

    public int getGuessedCount(){
        return guessedCount;
    }

    public int getElemWidth(int canvasWidth) {
        return (canvasWidth - (fieldWidth + 1) * ELEMS_DISTANCE) / (fieldWidth);
    }
    public int getElemHeight(int canvasHeight) {
        return (canvasHeight - (fieldHeight + 1) * ELEMS_DISTANCE) / (fieldHeight);
    }

    public void initialize(int canvasWidth, int canvasHeight){
        this.islosed = false;
        guessedCount = 0;
        elemWidth = getElemWidth(canvasWidth);
        elemHeight = getElemHeight(canvasHeight);
        int elementCount = fieldWidth * fieldHeight;
        winSeq = new int[elementCount];
        for (int i = 0; i < elementCount; ++i){
            winSeq[i] = i;
        }
        Random rnd = new Random();
        for (int i = 0; i < SHUFFLE_STEPS; ++i){
            int pos1 = rnd.nextInt(elementCount);
            int pos2 = rnd.nextInt(elementCount);
            int tmp = winSeq[pos1];
            winSeq[pos1] = winSeq[pos2];
            winSeq[pos2] = tmp;
        }
        for(int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                SeqRepeaterGameObject elem = new SeqRepeaterGameObject(i*fieldWidth+j, j * (elemWidth + ELEMS_DISTANCE)+ELEMS_DISTANCE, i * (elemHeight + ELEMS_DISTANCE)+ELEMS_DISTANCE, MAINCOLOR);
                field[i][j] = elem;
            }
        }
    }

    public void checkOnTouch(int x, int y){
        if (!isTouchable || islosed){
            return;
        }
        int row = 0;
        while (y > elemHeight) {
            y -= (elemHeight + ELEMS_DISTANCE);
            ++row;
        }
        if (y >= 0) {
            int column = 0;
            while (x > elemWidth) {
                x -= (elemWidth + ELEMS_DISTANCE);
                ++column;
            }
            if ((x >= 0) && (row >= 0)) {
                if (field[row][column].getColor() != WINCOLOR) {
                    if (field[row][column].getId() == winSeq[guessedCount]) {
                        field[row][column].setColor(WINCOLOR);
                        ++guessedCount;
                        this.islosed = false;
                    } else {
                        field[row][column].setColor(LOSECOLOR);
                        this.islosed = true;
                    }
                }
            }
        }
    }

    public boolean gameFinished(){
        return fieldWidth*fieldHeight == guessedCount || islosed;
    }

    public void showWin(){
        for (int i = 0; i < fieldHeight*fieldWidth; ++i){
            int y = winSeq[i] % fieldWidth;
            int x = (winSeq[i] - y) / fieldWidth;
            Timer timerOpen = new Timer();
            Timer timerClose = new Timer();
            timerOpen.schedule(new OpenWin(x,y), i * ONE_CARD_DELAY);
            timerClose.schedule(new HideWin(x,y), (i+1) * ONE_CARD_DELAY);
        }
    }

    private class OpenWin extends TimerTask {
        int x;
        int y;

        public OpenWin(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            field[x][y].setColor(WINCOLOR);
        }
    }

    private class HideWin extends TimerTask {
        int x;
        int y;

        public HideWin(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            field[x][y].setColor(MAINCOLOR);
            isTouchable = true;
        }
    }

}
