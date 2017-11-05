package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private TextView infoTextView;
    private ListView selectGameListView;
    private GameActivityFactory gameActivityFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameActivityFactory = GameActivityFactory.getInstance();
        try {
            gameActivityFactory.registerConstructor(0, SpecksGameActivity.class);
        }
        catch (NoSuchMethodException e){
        }
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        selectGameListView = (ListView)findViewById(R.id.selectGameListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[] {"Specks"});
        selectGameListView.setAdapter(arrayAdapter);
        selectGameListView.setOnItemClickListener(new selectGameItemClickListener());
        if (!VKSdk.isLoggedIn()) {
            Activity loginActivity = new LoginActivity();
            Intent intent = new Intent(this, loginActivity.getClass());
            startActivity(intent);
        } else {
            uploadUserInfo();
        }
        setupGamesInfo();
    }

    public void uploadUserInfo(){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String name = preferences.getString("name", "Имя");
        String surname = preferences.getString("surname", "Фамилия");
        infoTextView.clearComposingText();
        infoTextView.setText(name + " " + surname);
    }

    public void setupGamesInfo(){
        preferences = getSharedPreferences("game_info0", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", "Specks");
        editor.putString("description", "Choose two similar cards while they are accessible!");
        editor.apply();
    }

    private class selectGameItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Activity gameOptionsActivity = new GameOptionActivity();
            Intent intent = new Intent(MainActivity.this, gameOptionsActivity.getClass());
            intent.putExtra("id", l);
            startActivity(intent);
        }
    }
}
