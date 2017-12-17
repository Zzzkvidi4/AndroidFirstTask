package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import zzzkvidi4.com.testandroidapplication1.database.DBHelper;
import zzzkvidi4.com.testandroidapplication1.database.DBOperations;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "Custom error: ";
    private SharedPreferences preferences;
    private TextView infoTextView;
    private ListView selectGameListView;
    private GameActivityFactory gameActivityFactory;
    private Button logoutBtn;
    private ImageView infoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        setContentView(R.layout.activity_main);
        GameActivityFactory gameActivityFactory = GameActivityFactory.getInstance();
        try {
            gameActivityFactory.registerConstructor(1, SpecksGameActivity.class);
            gameActivityFactory.registerConstructor(2, SeqRepeaterGameActivity.class);
        }
        catch (NoSuchMethodException e){
            Log.d(LOG_TAG, "error in factory!");
        }
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        ListView selectGameListView = (ListView)findViewById(R.id.selectGameListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.button_list_item, new String[] {"Парные карты", "Повтори за мной"});
        selectGameListView.setAdapter(arrayAdapter);
        selectGameListView.setOnItemClickListener(new SelectGameItemClickListener());
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new LogOutOnClickListener());
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        logoutBtn.setTypeface(fontAwesomeFont);
        infoIV = (ImageView)findViewById(R.id.infoImageView);
        infoIV.setImageResource(R.drawable.anon);
        uploadUserInfo();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        uploadUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadUserInfo();
    }

    public void uploadUserInfo(){
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int id = preferences.getInt("id", getResources().getInteger(R.integer.no_user_id));
        if (id == getResources().getInteger(R.integer.no_user_id)){
            logoutBtn.setVisibility(View.INVISIBLE);
        } else{
            logoutBtn.setVisibility(View.VISIBLE);
        }
        DBOperations op = new DBOperations(new DBHelper(this));
        String userName = op.getUserFIOString(id);
        Bitmap userIcon = op.getUserIcon(id);
        infoTextView.setText(userName);
        if (userIcon != null)
            infoIV.setImageBitmap(userIcon);
        else
            infoIV.setImageResource(R.drawable.anon);
    }

    private class SelectGameItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, GameOptionActivity.class);
            intent.putExtra("id", (int)l + 1);
            startActivity(intent);
        }
    }

    private class LogOutOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, LogOutActivity.class);
            startActivity(intent);
        }
    }
}
