package com.indiainsure.android.MB360.insurance.profile.response;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.ProfileConverter;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "PROFILE", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(ProfileConverter.class)
public class ProfileResponse {

    @SerializedName("UserPersonalDetails")
    @Expose
    private UserPersonalDetails userPersonalDetails;
    @SerializedName("UserAddressDetails")
    @Expose
    private UserAddressDetails userAddressDetails;
    @SerializedName("UserOfficialDetails")
    @Expose
    private UserOfficialDetails userOfficialDetails;
    @SerializedName("UserDocumentsDetails")
    @Expose
    private List<UserDocumentsDetail> userDocumentsDetails = new ArrayList<>();
    @SerializedName("UserProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("UserSocialDetails")
    @Expose
    private UserSocialDetails userSocialDetails;
    @SerializedName("UserNomineeDetails")
    @Expose
    private List<UserNomineeDetails> userNomineeDetails = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;


    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public UserPersonalDetails getUserPersonalDetails() {
        return userPersonalDetails;
    }

    public void setUserPersonalDetails(UserPersonalDetails userPersonalDetails) {
        this.userPersonalDetails = userPersonalDetails;
    }

    public UserAddressDetails getUserAddressDetails() {
        return userAddressDetails;
    }

    public void setUserAddressDetails(UserAddressDetails userAddressDetails) {
        this.userAddressDetails = userAddressDetails;
    }

    public UserOfficialDetails getUserOfficialDetails() {
        return userOfficialDetails;
    }

    public void setUserOfficialDetails(UserOfficialDetails userOfficialDetails) {
        this.userOfficialDetails = userOfficialDetails;
    }

    public List<UserDocumentsDetail> getUserDocumentsDetails() {
        return userDocumentsDetails;
    }

    public void setUserDocumentsDetails(List<UserDocumentsDetail> userDocumentsDetails) {
        this.userDocumentsDetails = userDocumentsDetails;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public UserSocialDetails getUserSocialDetails() {
        return userSocialDetails;
    }

    public void setUserSocialDetails(UserSocialDetails userSocialDetails) {
        this.userSocialDetails = userSocialDetails;
    }

    public List<UserNomineeDetails> getUserNomineeDetails() {
        return userNomineeDetails;
    }

    public void setUserNomineeDetails(List<UserNomineeDetails> userNomineeDetails) {
        this.userNomineeDetails = userNomineeDetails;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "ProfileResponse{" +
                "userPersonalDetails=" + userPersonalDetails +
                ", userAddressDetails=" + userAddressDetails +
                ", userOfficialDetails=" + userOfficialDetails +
                ", userDocumentsDetails=" + userDocumentsDetails +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                ", userSocialDetails=" + userSocialDetails +
                ", userNomineeDetails=" + userNomineeDetails +
                ", message=" + message +
                '}';
    }
}

