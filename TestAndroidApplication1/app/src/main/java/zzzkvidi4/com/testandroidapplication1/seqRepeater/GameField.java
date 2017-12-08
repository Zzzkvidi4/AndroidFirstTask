package zzzkvidi4.com.testandroidapplication1.seqRepeater;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Created by Red Sky on 08.12.2017.
 */

public class GameField {
    private static final int ELEMS_DISTANCE = 10;
    private static final int SHUFFLE_STEPS = 50;
    private int fieldWidth;
    private int fieldHeight;
    private int elemWidth;
    private int elemHeight;
    private SeqRepeaterGameObject[][] field;
    private int[] winSeq;
    private int guessedcount;
    private boolean isTouchable = true;

    public GameField(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.field = new SeqRepeaterGameObject[fieldHeight][fieldWidth];
        this.winSeq = new int[fieldHeight*fieldWidth];
    }

    public SeqRepeaterGameObject[][] getField() {return field;}

    public int getElemWidth(int canvasWidth) {
        return (canvasWidth - (fieldWidth - 1) * ELEMS_DISTANCE) / (fieldWidth);
    }
    public int getElemHeight(int canvasHeight) {
        return (canvasHeight - (fieldHeight - 1) * ELEMS_DISTANCE) / (fieldHeight);
    }

    public void initialize(int canvasWidth, int canvasHeight){
        guessedcount = 0;
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
                SeqRepeaterGameObject elem = new SeqRepeaterGameObject(i*fieldWidth+j, j * (elemWidth + ELEMS_DISTANCE), i * (elemHeight + ELEMS_DISTANCE));
                field[i][j] = elem;
            }
        }
    }


}
