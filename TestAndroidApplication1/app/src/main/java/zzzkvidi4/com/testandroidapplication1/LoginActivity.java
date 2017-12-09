package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    Button authorizeBtn;
    TextView infoMessageTextView;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authorizeBtn = (Button)findViewById(R.id.authorizeBtn);
        authorizeBtn.setOnClickListener(new TryAgainUploadInfoListener());
        infoMessageTextView = (TextView)findViewById(R.id.infoMessageTextView);
    }

    protected void setupUserInfo(String name, String surname, int id){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("id", id);
        editor.apply();
        if (id != -1){
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            Cursor c = db.query("user", null, "id_user = ?", new String[]{Integer.toString(id)}, null, null, null);
            if (c.getCount() == 0) {
                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("id_user", id);
                db.insert("user", null, cv);
            }
            c.close();
            db.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                VKRequest request = new VKRequest("account.getProfileInfo");
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        JSONObject resp = response.json;
                        String name;
                        String surname;
                        int id;
                        try{
                            id = resp.getJSONObject("response").getInt("id");
                            name = resp.getJSONObject("response").getString("first_name");
                            surname = resp.getJSONObject("response").getString("last_name");
                        }
                        catch (JSONException e){
                            name = "Имя";
                            surname = "Фамилия";
                            id = -1;
                        }
                        setupUserInfo(name, surname, id);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishActivity(0);
                    }

                    @Override
                    public void onError(VKError error) {
                        infoMessageTextView.append("Произошла ошибка соединения с серверами vk. Проверьте подключение к интернету.");
                        infoMessageTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        infoMessageTextView.append("Произошла ошибка соединения с серверами vk. Проверьте подключение к интернету.");
                        infoMessageTextView.setVisibility(View.VISIBLE);
                    }
                });
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class TryAgainUploadInfoListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            VKSdk.login(LoginActivity.this, "");
        }
    }
}
