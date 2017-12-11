package zzzkvidi4.com.testandroidapplication1.syncronization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("token")
    @Expose
    private String token;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
