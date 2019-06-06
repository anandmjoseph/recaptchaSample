package com.anand.recaptchasample.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.anand.recaptchasample.R;
import com.anand.recaptchasample.model.RecaptchaVerifyResponse;
import com.anand.recaptchasample.viewmodel.RecaptchaResponseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafetyNet.getClient(MainActivity.this).verifyWithRecaptcha(getResources().getString(R.string.pubK))
                        .addOnSuccessListener(new SuccessListener())
                        .addOnFailureListener(new FailureListener());
            }
        });
    }

    private void showAlertWithButton(@NonNull String title, @NonNull String message, @NonNull String buttonMessage) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(false).setPositiveButton(buttonMessage, null).create().show();
    }

    private class SuccessListener implements OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse> {

        @Override
        public void onSuccess(final SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {

            String userResponseToken = recaptchaTokenResponse.getTokenResult();
            if (!userResponseToken.isEmpty()) {
                RecaptchaResponseViewModel mViewModel = ViewModelProviders.of(MainActivity.this).get(RecaptchaResponseViewModel.class);
                mViewModel.getmRecaptchaObservable("https://www.google.com", userResponseToken, getApplicationContext().getString(R.string.priK)).observe(MainActivity.this, new Observer<RecaptchaVerifyResponse>() {
                    @Override
                    public void onChanged(@Nullable RecaptchaVerifyResponse recaptchaVerifyResponse) {
                        if (recaptchaVerifyResponse != null && recaptchaVerifyResponse.isSuccess()) {
                            showAlertWithButton("Obie is a human", "Yes Siree, he a human I tell ya", "Well now ain't that nice!");
                        } else {
                            showAlertWithButton("Obie ain't a human", "No Siree, Obie ain't no human at all", "Doggone it!");
                        }
                    }
                });
            }
        }
    }

    private class FailureListener implements OnFailureListener {

        @Override
        public void onFailure(@NonNull Exception e) {
            showAlertWithButton("Obie is Unknown", e.getLocalizedMessage(), "Doggone it!");
        }
    }
}