package zzzkvidi4.com.testandroidapplication1;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by Roman on 22.10.2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
