package zzzkvidi4.com.testandroidapplication1.syncronization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class UserInfo {
    @SerializedName("provider")
    @Expose
    private String provider;


    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("vk_token")
    @Expose
    private String vk_token;

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public String getVk_token() {
        return vk_token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setVk_token(String vk_token) {
        this.vk_token = vk_token;
    }
}
