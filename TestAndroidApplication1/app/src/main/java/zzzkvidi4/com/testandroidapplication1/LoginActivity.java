package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

    protected void setupUserInfo(String name, String surname){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.apply();
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
                        try{
                            name = resp.getJSONObject("response").getString("first_name");
                            surname = resp.getJSONObject("response").getString("last_name");
                        }
                        catch (JSONException e){
                            name = "Имя";
                            surname = "Фамилия";
                        }
                        setupUserInfo(name, surname);
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
