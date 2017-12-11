package zzzkvidi4.com.testandroidapplication1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Red Sky on 11.12.2017.
 */

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Button logout = (Button)findViewById(R.id.YesLogoutBtn);
        //logout.setOnClickListener(new LogOutOnClickListener(this, false));
        Button cancel = (Button)findViewById(R.id.NoLogoutBtn);
        //cancel.setOnClickListener(new BackOnClickListener(this));
    }
}
