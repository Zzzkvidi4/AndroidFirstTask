package zzzkvidi4.com.testandroidapplication1;

import android.content.Intent;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView infoTextView;
    private TextView resultMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTextView = (TextView)findViewById(R.id.infoTextView);
        resultMsg = (TextView)findViewById(R.id.resultMsg);
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, "friends,photos");
        } else {
            uploadUserInfo();
        }

    }

    public void uploadUserInfo(){
        infoTextView.setText("Success!");
        VKRequest request = new VKRequest("account.getProfileInfo");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                JSONObject resp = response.json;
                infoTextView.setText(resp.toString());
                resultMsg.setText("Success result!");
            }

            @Override
            public void onError(VKError error) {
                resultMsg.setText("Insuccess result!");
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                resultMsg.setText("Something strange");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                infoTextView.clearComposingText();
                uploadUserInfo();
            }

            @Override
            public void onError(VKError error) {
                infoTextView.clearComposingText();
                infoTextView.setText("Error!");
            }
        }));
    }
}
