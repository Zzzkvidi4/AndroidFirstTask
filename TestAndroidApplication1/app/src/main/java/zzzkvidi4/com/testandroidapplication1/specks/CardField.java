package zzzkvidi4.com.testandroidapplication1.specks;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import zzzkvidi4.com.testandroidapplication1.R;

/**
 * Created by Roman on 07.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.specks.
 */

public class CardField {
    private static final int STATE_NO_SELECTED_CARDS = 1;
    private static final int STATE_ONE_CARD_SELECTED = 2;
    private static final int STATE_TWO_CARDS_SELECTED = 3;
    public static final int[] IMAGE_IDS = new int[] {R.drawable.book, R.drawable.dipper, R.drawable.stan, R.drawable.mable};
    public static final int CARDS_DISTANCE = 10;
    public static final int SHUFFLE_STEPS = 50;
    private CardGameObject[][] cardField;
    private int fieldWidth;
    private int fieldHeight;
    private int cardWidth;
    private int cardHeight;
    private int state;
    private int openedCards;
    private boolean isTouchable = true;
    private int mistakes = 0;

    public CardGameObject[][] getCardField(){
        return cardField;
    }

    private CardGameObject firstCard;
    private CardGameObject secondCard;

    public CardField(int fieldWidth, int fieldHeight){
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        cardField = new CardGameObject[fieldHeight][fieldWidth];
    }

    public int getCardWidth(int canvasWidth){
        return (canvasWidth - (fieldWidth - 1) * CARDS_DISTANCE) / (fieldWidth);
    }

    public int getCardHeight(int canvasHeight){
        return (canvasHeight - (fieldHeight - 1) * CARDS_DISTANCE) / (fieldHeight);
    }

    public int getMistakes() {
        return mistakes;
    }

    public void initialize(int canvasWidth, int canvasHeight) {
        state = STATE_NO_SELECTED_CARDS;
        openedCards = 0;

        cardWidth = getCardWidth(canvasWidth);
        cardHeight = getCardHeight(canvasHeight);
        int elementCount = fieldWidth * fieldHeight;
        int[] rndArray = new int[elementCount];
        for (int i = 0; i < elementCount; ++i){
            rndArray[i] = (i + 2) / 2;
        }
        Random rnd = new Random();
        for (int i = 0; i < SHUFFLE_STEPS; ++i){
            int pos1 = rnd.nextInt(elementCount);
            int pos2 = rnd.nextInt(elementCount);
            int tmp = rndArray[pos1];
            rndArray[pos1] = rndArray[pos2];
            rndArray[pos2] = tmp;
        }
        for(int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                CardGameObject card = new CardGameObject(j * (cardWidth + CARDS_DISTANCE), i * (cardHeight + CARDS_DISTANCE), rndArray[i * fieldWidth + j], 0, rndArray[i * fieldWidth + j]);
                cardField[i][j] = card;
            }
        }
    }

    public void checkOnTouch(int x, int y){
        if (!isTouchable){
            return;
        }
        int row = 0;
        while (y > cardHeight) {
            y -= (cardHeight + CARDS_DISTANCE);
            ++row;
        }
        if (y >= 0) {
            int column = 0;
            while (x > cardWidth) {
                x -= (cardWidth + CARDS_DISTANCE);
                ++column;
            }
            if ((x >= 0) && (row >= 0)) {
                if ((cardField[row][column].getState() != CardGameObject.GUESSED) && (!cardField[row][column].equals(firstCard))) {
                    cardField[row][column].changeVisible();
                    checkFieldState(cardField[row][column]);
                }
            }
        }
    }

    private void checkFieldState(CardGameObject card){
        switch (state) {
            case STATE_NO_SELECTED_CARDS:{
                firstCard = card;
                state = STATE_ONE_CARD_SELECTED;
                break;
            }
            case STATE_ONE_CARD_SELECTED: {
                secondCard = card;
                if (firstCard.getType() == secondCard.getType() && !firstCard.equals(secondCard)) {
                    firstCard.setGuessed();
                    secondCard.setGuessed();
                    openedCards += 2;
                    state = STATE_NO_SELECTED_CARDS;
                    firstCard = null;
                } else {
                    ++mistakes;
                    isTouchable = false;
                    Timer timer = new Timer();
                    timer.schedule(new HideWrongCards(), 2000);
                }
            }
        }
    }

    public boolean gameFinished(){
        return fieldWidth*fieldHeight == openedCards;
    }

    public void setFieldHidden(boolean hidden){
        for (int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                cardField[i][j].setHidden(hidden);
            }
        }
    }

    private class HideWrongCards extends TimerTask{
        @Override
        public void run() {
            firstCard.setHidden(true);
            secondCard.setHidden(true);
            firstCard = null;
            secondCard = null;
            state = STATE_NO_SELECTED_CARDS;
            isTouchable = true;
        }
    }
}
