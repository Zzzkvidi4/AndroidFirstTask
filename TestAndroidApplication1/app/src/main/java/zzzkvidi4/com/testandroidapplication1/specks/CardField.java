package zzzkvidi4.com.testandroidapplication1.specks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;

import zzzkvidi4.com.testandroidapplication1.R;

/**
 * Created by Roman on 07.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.specks.
 */

public class CardField {
    private static final int STATE_NO_SELECTED_CARDS = 1;
    private static final int STATE_ONE_CARD_SELECTED = 2;
    private static final int STATE_TWO_CARDS_SELECTED = 3;
    private CardGameObject[][] cardField;
    private int fieldWidth;
    private int fieldHeight;
    private int cardWidth;
    private int cardHeight;
    private int topMargin;
    private int state;

    CardGameObject firstCard;
    CardGameObject secondCard;

    public CardField(int fieldWidth, int fieldHeight){
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        cardField = new CardGameObject[fieldHeight][fieldWidth];
    }

    public void initialize(int canvasWidth, int canvasHeight, int topMargin, View view) {
        state = STATE_NO_SELECTED_CARDS;
        this.topMargin = topMargin;
        cardWidth = (canvasWidth - (fieldWidth - 1) * 10) / (fieldWidth);
        cardHeight = (canvasHeight - (fieldHeight - 1) * 10) / (fieldHeight);
        int elementCount = fieldWidth * fieldHeight;
        int[] rndArray = new int[elementCount];
        for (int i = 0; i < elementCount; ++i){
            rndArray[i] = -1;
        }
        int setted = 0;
        Random rnd = new Random();
        for (int k = 0; k < fieldWidth * fieldHeight / 2; ++k){
            int firstPos = rnd.nextInt(elementCount);
            while (rndArray[firstPos] != -1) {
                ++firstPos;
                if (firstPos >= elementCount){
                    firstPos = 0;
                }
            }
            rndArray[firstPos] = k;
            ++setted;
            int shift = rnd.nextInt(elementCount - setted) + 1;
            do{
                ++firstPos;
                if (firstPos >= elementCount){
                    firstPos = 0;
                }
                if (rndArray[firstPos] == -1){
                    --shift;
                }
            } while(shift > 0);
            rndArray[firstPos] = k;
            ++setted;
        }
        Bitmap hiddenBitmap = getBitmapFromResources(5, view, cardWidth, cardHeight);
        for(int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                CardGameObject card = new CardGameObject(j * (cardWidth + 10), i * (cardHeight + 10), getBitmapFromResources(rndArray[i * fieldWidth + j], view, cardWidth, cardHeight), hiddenBitmap, rndArray[i * fieldWidth + j]);
                cardField[i][j] = card;
            }
        }
    }

    public void checkOnTouch(int x, int y){
        y = y - topMargin;
        int row = 0;
        while (y > cardHeight) {
            y -= (cardHeight + 10);
            ++row;
        }
        if (y < 0) {
            row = -1;
        } else {
            int column = 0;
            while (x > cardWidth) {
                x -= (cardWidth + 10);
                ++column;
            }
            if (x < 0) {
                column = -1;
            } else if (row >= 0){
                if ((cardField[row][column].getState() != CardGameObject.GUESSED) && (!cardField[row][column].equals(firstCard))) {
                    cardField[row][column].changeVisible();
                }
                checkFieldState(cardField[row][column]);
            }
        }
    }

    public void checkFieldState(CardGameObject card){
        switch (state) {
            case STATE_NO_SELECTED_CARDS:{
                firstCard = card;
                state = STATE_ONE_CARD_SELECTED;
                break;
            }
            case STATE_ONE_CARD_SELECTED: {
                secondCard = card;
                if (firstCard.getType() == secondCard.getType()) {
                    firstCard.setGuessed();
                    secondCard.setGuessed();
                } else {
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    firstCard.setHiiden(true);
                    secondCard.setHiiden(true);
                }
                state = STATE_NO_SELECTED_CARDS;
            }
        }
    }

    public Bitmap getBitmapFromResources(int number, View view, int cardWidth, int cardHeight){
        Bitmap bitmap;
        switch (number){
            case 0:{
                bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.dipper);
                break;
            }
            case 1:{
                bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.stan);
                break;
            }
            case 2:{
                bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.book);
                break;
            }
            default:{
                bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.mable);
                break;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, cardWidth, cardHeight, false);
    }

    public void onDraw(Canvas canvas){
        for (int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                cardField[i][j].onDraw(canvas, topMargin);
            }
        }
    }
}
