package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.EnrollMentWindowCountConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "ENROLLMENT_WINDOW_COUNT", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(EnrollMentWindowCountConverter.class)
public class AdminSettingResponse {
    @SerializedName("Group_Admin_Basic_Settings")
    @Expose
    private GroupAdminBasicSettings Group_Admin_Basic_Settings;
    @SerializedName("Group_Relation")
    @Expose
    private List<com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupRelation> GroupRelation = new ArrayList<>();
    @SerializedName("Group_Window_Period_Info")
    @Expose
    private GroupWindowPeriodInfo Group_Window_Period_Info;
    @SerializedName("Premium_EMI_info")
    @Expose
    private List<PremiumEmiInfo> premiumEmiInfo = new ArrayList<>();

    @SerializedName("Enroll_Life_Event_Info")
    @Expose
    private EnrollLifeEventInfo Enroll_Life_Event_Info;
    @SerializedName("Enroll_Topup_Options")
    @Expose
    private EnrollTopupOptions Enroll_Topup_Options;
    @SerializedName("Enrollment_Misc_Info")
    @Expose
    private EnrollmentMiscInfo Enrollment_Misc_Info;
    @SerializedName("Message")
    @Expose
    private Message message;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    @PrimaryKey
    @NonNull
    private  String oeGrpBasInfoSrNo;




    public GroupAdminBasicSettings getGroup_Admin_Basic_Settings() {
        return Group_Admin_Basic_Settings;
    }

    public void setGroup_Admin_Basic_Settings(GroupAdminBasicSettings group_Admin_Basic_Settings) {
        Group_Admin_Basic_Settings = group_Admin_Basic_Settings;
    }



    public GroupWindowPeriodInfo getGroup_Window_Period_Info() {
        return Group_Window_Period_Info;
    }

    public void setGroup_Window_Period_Info(GroupWindowPeriodInfo group_Window_Period_Info) {
        Group_Window_Period_Info = group_Window_Period_Info;
    }

    public EnrollLifeEventInfo getEnroll_Life_Event_Info() {
        return Enroll_Life_Event_Info;
    }

    public void setEnroll_Life_Event_Info(EnrollLifeEventInfo enroll_Life_Event_Info) {
        Enroll_Life_Event_Info = enroll_Life_Event_Info;
    }

    public EnrollTopupOptions getEnroll_Topup_Options() {
        return Enroll_Topup_Options;
    }

    public void setEnroll_Topup_Options(EnrollTopupOptions enroll_Topup_Options) {
        Enroll_Topup_Options = enroll_Topup_Options;
    }

    public EnrollmentMiscInfo getEnrollment_Misc_Info() {
        return Enrollment_Misc_Info;
    }

    public void setEnrollment_Misc_Info(EnrollmentMiscInfo enrollment_Misc_Info) {
        Enrollment_Misc_Info = enrollment_Misc_Info;
    }

    public Message getMessage ()
    {
        return message;
    }

    public void setMessage (Message message)
    {
        this.message = message;
    }

    public List<com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupRelation> getGroupRelation() {
        return GroupRelation;
    }

    public void setGroupRelation(List<com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupRelation> groupRelation) {
        GroupRelation = groupRelation;
    }

    public List<PremiumEmiInfo> getPremiumEmiInfo() {
        return premiumEmiInfo;
    }

    public void setPremiumEmiInfo(List<PremiumEmiInfo> premiumEmiInfo) {
        this.premiumEmiInfo = premiumEmiInfo;
    }

    @Override
    public String toString() {
        return "AdminSettingResponse{" +
                "Group_Admin_Basic_Settings=" + Group_Admin_Basic_Settings +
                ", GroupRelation=" + GroupRelation +
                ", Group_Window_Period_Info=" + Group_Window_Period_Info +
                ", premiumEmiInfo=" + premiumEmiInfo +
                ", Enroll_Life_Event_Info=" + Enroll_Life_Event_Info +
                ", Enroll_Topup_Options=" + Enroll_Topup_Options +
                ", Enrollment_Misc_Info=" + Enrollment_Misc_Info +
                ", message=" + message +
                '}';
    }
}



