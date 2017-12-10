package zzzkvidi4.com.testandroidapplication1.syncronization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class TopResults {
    @SerializedName("your_place")
    @Expose
    private int your_place;

    @SerializedName("top10")
    @Expose
    private Top[] top10;

    @SerializedName("your_hight_score")
    @Expose
    private int your_hight_score;

    public int getYour_place() {
        return your_place;
    }

    public Top[] getTop10() {
        return top10;
    }

    public void setTop10(Top[] top10) {
        this.top10 = top10;
    }

    public void setYour_place(int your_place) {
        this.your_place = your_place;
    }

    public int getYour_hight_score() {
        return your_hight_score;
    }

    public void setYour_hight_score(int your_hight_score) {
        this.your_hight_score = your_hight_score;
    }
}
