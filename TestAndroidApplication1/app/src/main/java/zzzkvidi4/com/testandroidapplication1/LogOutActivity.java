package zzzkvidi4.com.testandroidapplication1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKSdk;

/**
 * Created by Red Sky on 11.12.2017.
 */

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Button logout = (Button)findViewById(R.id.YesLogoutBtn);
        logout.setOnClickListener(new LogOutOnClickListener());
        Button cancel = (Button)findViewById(R.id.NoLogoutBtn);
        cancel.setOnClickListener(new BackOnClickListener());
    }

    private class LogOutOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            VKSdk.logout();
            SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
            preferences.edit().clear().apply();
            finish();
        }
    }

    private class BackOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
