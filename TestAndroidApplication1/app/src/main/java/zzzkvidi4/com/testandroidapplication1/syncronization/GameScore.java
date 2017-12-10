package zzzkvidi4.com.testandroidapplication1.syncronization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class GameScore {
    @SerializedName("score")
    @Expose
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
