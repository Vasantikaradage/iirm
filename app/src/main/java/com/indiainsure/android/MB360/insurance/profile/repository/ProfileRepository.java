package com.indiainsure.android.MB360.insurance.profile.repository;

import static com.indiainsure.android.MB360.BuildConfig.BEARER_TOKEN;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.ProfileDao;
import com.indiainsure.android.MB360.insurance.profile.repository.retrofit.ProfileRetrofitClient;
import com.indiainsure.android.MB360.insurance.profile.response.Message;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.FileUtil;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.MediaTypes;
import com.indiainsure.android.MB360.utilities.UtilMethods;
import com.indiainsure.android.MB360.utilities.token.responseclasses.AuthToken;
import com.indiainsure.android.MB360.utilities.token.retrofit.BearerRetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    Application application;
    private final MutableLiveData<ProfileResponse> profileMutableLiveData;
    private final MutableLiveData<Boolean> error;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<Message> documentUploadResponse;
    private AppDatabase appDatabase;
    private ProfileDao profileDao;

    public final MutableLiveData<Boolean> reloginState;


    public ProfileRepository(Application application) {
        this.application = application;
        profileMutableLiveData = new MutableLiveData<>();
        this.error = new MutableLiveData<>(false);
        this.loading = new MutableLiveData<>(true);
        documentUploadResponse = new MutableLiveData<>();
        appDatabase = AppDatabase.getInstance(application);
        profileDao = appDatabase.profileDao();
        this.reloginState = new MutableLiveData<>(false);


    }


    public MutableLiveData<ProfileResponse> getProfile(String groupChild, String oeGrpBasInfoSrNo, String EmployeeSrNo) {
        try {
            Call<ProfileResponse> profileCall = ProfileRetrofitClient.getInstance(application.getApplicationContext()).getProfileApi().getProfile(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChild), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfoSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(EmployeeSrNo), application.getString(R.string.pass_phrase)));

            Callback<ProfileResponse> callback = new Callback<ProfileResponse>() {

                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                            profileMutableLiveData.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo);

                            profileDao.insertProfile(response.body());

                        } catch (Exception e) {
                            e.printStackTrace();
                            profileMutableLiveData.setValue(null);
                            error.setValue(true);
                            loading.setValue(false);
                            LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "onCatch: ", e);
                        }

                    } else {
                        LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                        profileMutableLiveData.setValue(response.body());
                        error.setValue(true);
                        loading.setValue(false);
                    }

                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "onFailure: ", t);
                    try {

                        LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onFailure: " + profileDao.getProfile(oeGrpBasInfoSrNo).toString());
                        profileMutableLiveData.setValue(profileDao.getProfile(oeGrpBasInfoSrNo));
                        loading.setValue(false);
                        error.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        profileMutableLiveData.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
                    }

                }

            };

            profileCall.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                            profileMutableLiveData.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo);

                            profileDao.insertProfile(response.body());

                        } catch (Exception e) {
                            e.printStackTrace();
                            profileMutableLiveData.setValue(null);
                            error.setValue(true);
                            loading.setValue(false);
                            LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "onCatch: ", e);
                        }

                    } else if (response.code() == 401 || response.code() == 400) {

                        EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());
                        try {


                            Call<AuthToken> authTokenCall = BearerRetrofitClient.getInstance(application.getApplicationContext()).getBearerApi().
                                    refreshToken(AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO), application.getString(R.string.pass_phrase)),
                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO), application.getString(R.string.pass_phrase)),
                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO), application.getString(R.string.pass_phrase)));

                            authTokenCall.enqueue(new Callback<AuthToken>() {
                                @Override
                                public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                    if (response.code() == 200) {
                                        LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response.body().getAuthToken());
                                        if (response.body() != null) {
                                            if (!response.body().getAuthToken().isEmpty()) {
                                                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                profileCall.clone().enqueue(callback);
                                            }else {
                                                reloginState.setValue(true);
                                            }
                                        }else {
                                            reloginState.setValue(true);
                                        }

                                    } else {
                                        reloginState.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<AuthToken> call, Throwable t) {
                                    call.cancel();
                                    LogMyBenefits.e("REFRESH-TOKEN", "onFailure: ", t);
                                    t.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                        profileMutableLiveData.setValue(response.body());
                        error.setValue(true);
                        loading.setValue(false);
                    }

                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "onFailure: ", t);
                    try {

                        LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "onFailure: " + profileDao.getProfile(oeGrpBasInfoSrNo).toString());
                        profileMutableLiveData.setValue(profileDao.getProfile(oeGrpBasInfoSrNo));
                        loading.setValue(false);
                        error.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        profileMutableLiveData.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return profileMutableLiveData;
    }

    public MutableLiveData<ProfileResponse> getProfileData() {
        return profileMutableLiveData;
    }

    public MutableLiveData<Message> uploadDocuments(Uri fileUri, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String docType, String docNo) {

        try {
            File file = FileUtil.from(application.getApplicationContext(), fileUri);

            JSONObject map = new JSONObject();
            map.put("strGRoupChildSrno", UtilMethods.checkSpecialCharacters(groupChildSrNo));
            map.put("strOegrpbasinfsrno", UtilMethods.checkSpecialCharacters(oeGrpBasInfoSrNo));
            map.put("strEmpSrno", UtilMethods.checkSpecialCharacters(employeeSrNo));
            map.put("strDocType", docType);
            map.put("strDocNo", docNo);

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            final MediaType MEDIA_TYPE = MediaTypes.fromFile(file);

            builder.addFormDataPart(file.getName(), FileUtil.getFileName(application.getApplicationContext(), Uri.fromFile(file)),
                    RequestBody.create(MEDIA_TYPE, file));

            builder.addFormDataPart("QueryData", map.toString());
            RequestBody uploadRequest = builder.build();

            Call<Message> uploadCall = ProfileRetrofitClient.getInstance(application.getApplicationContext()).getProfileApi().uploadDocuments(uploadRequest);

            uploadCall.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    try {
                        if (response.code() == 200) {
                            LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "OnResponse : " + response.body().toString());
                            documentUploadResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);

                        } else {
                            LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "OnResponse : " + response.toString());
                            documentUploadResponse.setValue(response.body());
                            error.setValue(false);
                            loading.setValue(false);
                            Toast.makeText(application, "Something went wrong! " + response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "CaughtError: ", e);
                        documentUploadResponse.setValue(null);
                        error.setValue(false);
                        loading.setValue(false);
                        Toast.makeText(application, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "onFailure: ", t);
                    documentUploadResponse.setValue(null);
                    error.setValue(false);
                    loading.setValue(false);
                    Toast.makeText(application, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (JSONException | IOException e) {
            e.printStackTrace();
            LogMyBenefits.e(LogTags.PROFILE_ACTIVITY, "CaughtError: ", e);
            documentUploadResponse.setValue(null);
            error.setValue(false);
            loading.setValue(false);
            Toast.makeText(application, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }


        return documentUploadResponse;
    }
}
