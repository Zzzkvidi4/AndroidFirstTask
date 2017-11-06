package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SpecksGameActivity extends AppCompatActivity {
    private int difficulty;
    private int score;
    private boolean isFirst;

    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        setContentView(R.layout.activity_specks_game);
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        infoTextView.append("Diff: " + difficulty + "; Score: " + score);
    }

    @Override
    public void onBackPressed() {
    }
}
