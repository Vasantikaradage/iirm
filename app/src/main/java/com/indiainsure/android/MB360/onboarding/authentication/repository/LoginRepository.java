package com.indiainsure.android.MB360.onboarding.authentication.repository;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.indiainsure.android.MB360.onboarding.authentication.responseclass.LoginEmailRequest;
import com.indiainsure.android.MB360.onboarding.authentication.responseclass.LoginIDResponse;
import com.indiainsure.android.MB360.onboarding.authentication.responseclass.LoginIdRequest;
import com.indiainsure.android.MB360.onboarding.authentication.responseclass.LoginRequest;
import com.indiainsure.android.MB360.onboarding.authentication.responseclass.LoginResponse;
import com.indiainsure.android.MB360.onboarding.validation.responseclass.ValidationEmailRequest;
import com.indiainsure.android.MB360.onboarding.validation.responseclass.ValidationRequest;
import com.indiainsure.android.MB360.onboarding.validation.responseclass.ValidationResponse;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.ResponseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for logging
 * in users
 **/

public class LoginRepository {

    private final MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private final MutableLiveData<LoginIDResponse> loginIDResponseMutableLiveData;
    private final MutableLiveData<ValidationResponse> validationResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    FirebaseCrashlytics crashlytics;

    //todo set and use loading state

    public LoginRepository(Application application) {
        this.loginResponseMutableLiveData = new MutableLiveData<>();
        this.loginIDResponseMutableLiveData = new MutableLiveData<>();
        this.validationResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(false);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();


    }

    /**
     * this function  has the business logic for logging in users with mobile number
     **/
    public MutableLiveData<LoginResponse> loginWithMobileNumber(LoginRequest loginRequest) {
        LogMyBenefits.d("LOGIN-ACTIVITY", loginRequest.getMobileno().toString());
        Call<LoginResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestOTP(loginRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        loginResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        loginResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }else
                {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> loginWithMobileNumber -> Response Code:- " + String.valueOf(response.code()));
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //todo show user why it failed
                loginResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return loginResponseMutableLiveData;
    }

    public MutableLiveData<LoginResponse> loginWithMobileNumberResend(LoginRequest loginRequest) {
        MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
        LogMyBenefits.d("LOGIN-ACTIVITY", loginRequest.getMobileno().toString());
        Call<LoginResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestOTP(loginRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        loginResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        loginResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }else
                {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> loginWithMobileNumber -> Response Code:- " + String.valueOf(response.code()));
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //todo show user why it failed
                loginResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return loginResponseMutableLiveData;
    }

    /**
     * this function  has the business logic for logging in users with email
     **/
    public MutableLiveData<LoginResponse> loginWithEmail(LoginEmailRequest loginEmailRequest) {
        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, loginEmailRequest.getOfficialemailId().toString());
        Call<LoginResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestEmailOTP(loginEmailRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        loginResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        loginResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }else
                {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> loginWithEmail -> Response Code:- " + String.valueOf(response.code()));
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //todo show user why it failed
                loginResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return loginResponseMutableLiveData;
    }

    /**
     * this function  has the business logic for logging in users with id
     **/
    public MutableLiveData<LoginIDResponse> loginWithId(LoginIdRequest loginIdRequest) {
        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, loginIdRequest.toString());

        Call<LoginIDResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestLoginIdOTP(loginIdRequest);
        loginCall.enqueue(new Callback<LoginIDResponse>() {
            @Override
            public void onResponse(Call<LoginIDResponse> call, Response<LoginIDResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "onResponse: " + response.body().toString());
                        loginIDResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.LOGIN_ACTIVITY, "caught Error : ", e);
                        loginIDResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.e(LogTags.LOGIN_ACTIVITY, "error : " + response.code());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> loginWithId -> Response Code:- " + String.valueOf(response.code()));
                    crashlytics.recordException(responseException);

                }
            }

            @Override
            public void onFailure(Call<LoginIDResponse> call, Throwable t) {
                //todo show user why it failed
                loginIDResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });


        return loginIDResponseMutableLiveData;
    }

    /**
     * otp validation is done here
     **/
    public MutableLiveData<ValidationResponse> ValidateOTP(ValidationRequest validationRequest) {
        LogMyBenefits.d("LOGIN-ACTIVITY", validationRequest.getMobileno().toString());
        Call<ValidationResponse> validationCall = LoginClient.getInstance().getLoginApi().ValidateOTP(validationRequest);

        validationCall.enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
                LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "onResponse: " + response);
                if (response.body() != null) {
                    if (response.code() == 200) {
                        try {
                            validationResponseMutableLiveData.postValue(response.body());
                            loadingState.setValue(false);
                            errorState.setValue(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            validationResponseMutableLiveData.postValue(response.body());
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }

                    } else {
                        validationResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);

                        ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> ValidateOTP -> Response Code:- " + String.valueOf(response.code()));
                        crashlytics.recordException(responseException);
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidationResponse> call, Throwable t) {
                //todo show user why it failed
                validationResponseMutableLiveData.postValue(null);
            }
        });

        return validationResponseMutableLiveData;
    }

    public MutableLiveData<ValidationResponse> ValidateOTPWithEmail(ValidationEmailRequest validationEmailRequest) {
        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, validationEmailRequest.getOfficialemailId().toString());
        Call<ValidationResponse> validationCall = LoginClient.getInstance().getLoginApi().ValidateEmailOTP(validationEmailRequest);

        validationCall.enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        validationResponseMutableLiveData.postValue(response.body());
                    } catch (Exception e) {
                        validationResponseMutableLiveData.postValue(null);
                    }

                } else {
                    //TODO something is wrong
                    validationResponseMutableLiveData.postValue(null);


                    ResponseException responseException = new ResponseException("LOGIN-ACTIVITY -> ValidateOTPWithEmail -> Response Code:- " + String.valueOf(response.code()));
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<ValidationResponse> call, Throwable t) {
                //todo show user why it failed
                validationResponseMutableLiveData.postValue(null);
            }
        });

        return validationResponseMutableLiveData;
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponseMutableLiveData;
    }

    public LiveData<ValidationResponse> getValidationResponse() {
        return validationResponseMutableLiveData;
    }

    public LiveData<LoginIDResponse> getLoginIDResponse() {
        return loginIDResponseMutableLiveData;
    }

    public void resetValidationResponse() {
        validationResponseMutableLiveData.setValue(null);
    }

    public void resetLoginResponse() {
        loginResponseMutableLiveData.setValue(null);
    }
}

