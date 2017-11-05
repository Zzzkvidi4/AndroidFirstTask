package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private TextView infoTextView;
    private ListView selectGameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        selectGameListView = (ListView)findViewById(R.id.selectGameListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.button_list_item, new String[] {"game 1", "game 2", "game 3"});
        selectGameListView.setAdapter(arrayAdapter);
        if (!VKSdk.isLoggedIn()) {
            Activity loginActivity = new LoginActivity();
            Intent intent = new Intent(this, loginActivity.getClass());
            startActivity(intent);
        } else {
            uploadUserInfo();
        }
    }

    public void uploadUserInfo(){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String name = preferences.getString("name", "Имя");
        String surname = preferences.getString("surname", "Фамилия");
        infoTextView.clearComposingText();
        infoTextView.setText(name + " " + surname);
    }
}
