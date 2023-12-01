package com.indiainsure.android.MB360.utilities.token;

import static com.indiainsure.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;
import static com.indiainsure.android.MB360.BuildConfig.BEARER_TOKEN;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.onboarding.authentication.repository.LoginClient;
import com.indiainsure.android.MB360.onboarding.validation.responseclass.ValidationRequest;
import com.indiainsure.android.MB360.onboarding.validation.responseclass.ValidationResponse;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.rootdetection.CertChecker;
import com.indiainsure.android.MB360.utilities.token.responseclasses.AuthToken;
import com.indiainsure.android.MB360.utilities.token.retrofit.BearerRetrofitClient;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BearerRepository {

    private Application application;
    private Disposable refreshTokenDisposable; // Disposable to manage the token refreshing subscription
    boolean SSL_PINNED = false;

    public BearerRepository(Application application) {
        this.application = application;
    }

    public void startRefreshingToken(String emp_srNo, String person_srNo, String emp_id) {
        // Stop any existing token refreshing subscription
        stopRefreshingToken();

        // Create a new subscription to refresh the token every 5 minutes
        refreshTokenDisposable = Observable.interval(0, 5, TimeUnit.MINUTES)
                .observeOn(Schedulers.io()) // Perform the operation on the IO thread
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> {
                    refreshToken(emp_srNo, person_srNo, emp_id);
                }, Throwable::printStackTrace);
    }

    public void stopRefreshingToken() {
        // Dispose the token refreshing subscription if it's not null
        if (refreshTokenDisposable != null && !refreshTokenDisposable.isDisposed()) {
            refreshTokenDisposable.dispose();
            refreshTokenDisposable = null;
        }
    }

    private void refreshToken(String emp_srNo, String person_srNo, String emp_id) {
        Call<AuthToken> authTokenCall = BearerRetrofitClient.getInstance(application.getApplicationContext()).getBearerApi().refreshToken(emp_srNo, person_srNo, emp_id);


        try {
            // Disable trust manager checks - we'll check the certificate manually ourselves later
            TrustManager[] trustManager = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, trustManager, null);

                    SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(BuildConfig.DOMAIN, Integer.parseInt(BuildConfig.PORT));
                    X509Certificate[] certs = (X509Certificate[]) socket.getSession().getPeerCertificates();
                    X509Certificate leafCertificateserver = (X509Certificate) certs[0];
                    LogMyBenefits.d("CERT", "SERVER CERT: " + leafCertificateserver);


                    try {
                        InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
                        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                        X509Certificate leafCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
                        LogMyBenefits.d("CERT", "CLIENT CERT: " + leafCertificate);

                        if (leafCertificate.equals(leafCertificateserver)) {
                            LogMyBenefits.d("CERT", "VALID CERT");
                            SSL_PINNED = true;
                        } else {
                            LogMyBenefits.d("CERT", "ERROR LEAF: NOT MATCH" + leafCertificate);
                            throw new RuntimeException();
                        }

                        // Use the leafCertificate object to pin the certificate
                    } catch (Exception e) {
                        LogMyBenefits.e("CERT", "ERROR: ", e);
                        e.printStackTrace();
                        if (e instanceof RuntimeException) {
                            throw new RuntimeException("SSL ERROR!");
                        }
                    }


                    Predicate<X509Certificate> certMatch = cert -> CertChecker.Companion.doesCertMatchPin(BuildConfig.CERT, cert);
                    if (!Arrays.stream(certs).anyMatch(certMatch)) {
                        socket.close();
                        // Close the socket immediately without sending a request
                        SSL_PINNED = false;
                        throw new RuntimeException("Unrecognized cert hash.");

                    } else {
                        SSL_PINNED = true;
                        //regular code here

                        authTokenCall.enqueue(new Callback<AuthToken>() {
                            @Override
                            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                try {
                                    LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response);
                                    if (response.code() == 200) {
                                        if (response.body() != null) {
                                            EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());
                                            encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                        }
                                        //saved token token expired
                                        //to refresh token, validate with otp
                                    } else if (response.code() == 401) {
                                        //validate the user again.....
                                        //call thew validate user here
                                        EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());

                                        String OTP = encryptionPreference.getEncryptedDataToken(BuildConfig.AUTH_OTP);
                                        String PHONE_NUMBER = encryptionPreference.getEncryptedDataToken(AUTH_PHONE_NUMBER);
                                        ValidationRequest validationRequest = new ValidationRequest();
                                        validationRequest.setEnteredotp(Integer.parseInt(OTP));
                                        validationRequest.setMobileno(PHONE_NUMBER);

                                        if (!OTP.isEmpty() && !PHONE_NUMBER.isEmpty()) {

                                            Call<ValidationResponse> validationCall = LoginClient.getInstance().getLoginApi().ValidateOTP(validationRequest);
                                            validationCall.enqueue(new Callback<ValidationResponse>() {
                                                @Override
                                                public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
                                                    try {
                                                        if (response.code() == 200) {
                                                            if (response.body() != null) {
                                                                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                            } else {
                                                                //logout
                                                            }

                                                        } else {
                                                            //logout
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        //logout
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ValidationResponse> call, Throwable t) {
                                                    //logout
                                                    t.printStackTrace();

                                                }
                                            });

                                        } else {
                                            //logout
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<AuthToken> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }

                    socket.close();
                } catch (Exception e) {

                    LogMyBenefits.e("SSL", "Error", e);
                    e.printStackTrace();
                    if (e instanceof RuntimeException) {
                        showToast(() -> Toast.makeText(application, "SSL ERROR!", Toast.LENGTH_SHORT).show());
                        throw new RuntimeException("SSL ERROR!");
                    }

                }

            });


        } catch (Throwable e) {
            SSL_PINNED = false;
            e.printStackTrace();
            LogMyBenefits.e("SSL", "Error", e);
        }


    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}