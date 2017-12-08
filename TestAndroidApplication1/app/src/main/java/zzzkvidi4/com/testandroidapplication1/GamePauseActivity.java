package zzzkvidi4.com.testandroidapplication1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import zzzkvidi4.com.testandroidapplication1.onClickListeners.BackToGameOnClickListener;
import zzzkvidi4.com.testandroidapplication1.onClickListeners.BackToMenuOnClickListener;

public class GamePauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pause);
        Button backToMenuBtn = (Button)findViewById(R.id.backToMenuBtn);
        backToMenuBtn.setOnClickListener(new BackToMenuOnClickListener(this, false));
        Button backToGameBtn = (Button)findViewById(R.id.backToGameBtn);
        backToGameBtn.setOnClickListener(new BackToGameOnClickListener(this));
    }

}
