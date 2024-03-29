package com.anand.recaptchasample.viewmodel;

import android.app.Application;

import com.anand.recaptchasample.model.RecaptchaRepository;
import com.anand.recaptchasample.model.RecaptchaVerifyResponse;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RecaptchaResponseViewModel extends AndroidViewModel {

    public RecaptchaResponseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RecaptchaVerifyResponse> getmRecaptchaObservable(@NonNull String baseUrl, @NonNull String response, @NonNull String key) {
        return new RecaptchaRepository().doRecaptchaValidation(baseUrl, response, key);
    }
}