package zzzkvidi4.com.testandroidapplication1.syncronization;

import android.os.AsyncTask;
import android.provider.Settings;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Roman on 09.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public class AsyncAuthorize extends AsyncTask<String, Integer, String> {
    private String token;
    private String email;

    public AsyncAuthorize(String token, String email){
        this.token = token;
        this.email = email;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mind-blower.herokuapp.com/token/obtain/");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            JSONObject params = new JSONObject();
            params.put("provider", "vk");
            params.put("email", email);
            params.put("vk_token", token);
            byte[] paramBytes = params.toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(paramBytes);
            os.flush(); //Ты пропал Хм... Странности (:
            os.close();
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s);
    }
}
