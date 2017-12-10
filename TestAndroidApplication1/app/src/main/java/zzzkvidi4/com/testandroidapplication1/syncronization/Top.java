package zzzkvidi4.com.testandroidapplication1.syncronization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class Top {
    @SerializedName("user")
    @Expose
    private TopUser user;

    @SerializedName("best_score")
    @Expose
    private int best_score;

    public int getBest_score() {
        return best_score;
    }

    public TopUser getUser() {
        return user;
    }

    public void setBest_score(int best_score) {
        this.best_score = best_score;
    }

    public void setUser(TopUser user) {
        this.user = user;
    }
}
