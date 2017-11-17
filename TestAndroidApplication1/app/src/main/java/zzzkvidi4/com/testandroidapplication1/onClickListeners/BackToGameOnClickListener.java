package zzzkvidi4.com.testandroidapplication1.onClickListeners;

import android.view.View;

import zzzkvidi4.com.testandroidapplication1.GamePauseActivity;

/**
 * Created by Roman on 09.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.onClickListeners.
 */
public class BackToGameOnClickListener implements View.OnClickListener {
    private GamePauseActivity gamePauseActivity;

    public BackToGameOnClickListener(GamePauseActivity gamePauseActivity) {
        this.gamePauseActivity = gamePauseActivity;
    }

    @Override
    public void onClick(View view) {
        gamePauseActivity.finish();
    }
}
