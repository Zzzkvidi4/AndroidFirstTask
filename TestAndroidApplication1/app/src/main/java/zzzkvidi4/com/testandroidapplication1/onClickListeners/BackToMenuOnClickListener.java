package zzzkvidi4.com.testandroidapplication1.onClickListeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import zzzkvidi4.com.testandroidapplication1.MainActivity;

/**
 * Created by Roman on 09.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.onClickListeners.
 */

public class BackToMenuOnClickListener implements View.OnClickListener {
    private Activity activity;
    private boolean isActivityFinishing;

    public BackToMenuOnClickListener(Activity activity, boolean isActivityFinishing){
        this.activity = activity;
        this.isActivityFinishing = isActivityFinishing;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        if (isActivityFinishing){
            activity.finish();
        }
    }
}
