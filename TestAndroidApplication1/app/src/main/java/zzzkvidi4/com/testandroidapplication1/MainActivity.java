package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.vk.sdk.VKSdk;

import zzzkvidi4.com.testandroidapplication1.database.DBHelper;
import zzzkvidi4.com.testandroidapplication1.database.DBOperations;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "Custom error: ";
    private SharedPreferences preferences;
    private TextView infoTextView;
    private ListView selectGameListView;
    private GameActivityFactory gameActivityFactory;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameActivityFactory = GameActivityFactory.getInstance();
        try {
            gameActivityFactory.registerConstructor(0, SpecksGameActivity.class);
        }
        catch (NoSuchMethodException e){
            Log.d(LOG_TAG, "error in factory!");
        }
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        selectGameListView = (ListView)findViewById(R.id.selectGameListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.button_list_item, new String[] {"Парные карты"});
        selectGameListView.setAdapter(arrayAdapter);
        selectGameListView.setOnItemClickListener(new SelectGameItemClickListener());
        Button logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new LogOutOnClickListener());
        if (!VKSdk.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            uploadUserInfo();
        }
        setupGamesInfo();
        dbHelper = new DBHelper(this);
    }

    public void uploadUserInfo(){
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int id = preferences.getInt("id", -1);
        DBOperations op = new DBOperations(new DBHelper(this));
        String userName = op.getUserFIOString(id);
        infoTextView.setText(userName);
    }

    public void setupGamesInfo(){
        preferences = getSharedPreferences("game_info0", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", "Парные карты");
        editor.putString("description", "Открывайте одинаковые карты парами,\nпока они не закончатся!");
        editor.apply();
    }

    private class SelectGameItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, GameOptionActivity.class);
            intent.putExtra("id", l + 1);
            startActivity(intent);
        }
    }

    private class LogOutOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            VKSdk.logout();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
