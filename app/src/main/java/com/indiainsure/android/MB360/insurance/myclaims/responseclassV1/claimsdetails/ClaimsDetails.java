package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsDetails {

    @SerializedName("LoadDetailedClaimsValues")
    @Expose
    private LoadDetailedClaimsValues loadDetailedClaimsValues;
    @SerializedName("MEMBER_INFORMATION")
    @Expose
    private MemberInformation memberInformation;
    @SerializedName("CLAIM_POLICY_INFORMATION")
    @Expose
    private ClaimPolicyInformation claimPolicyInformation;
    @SerializedName("CLAIM_HOSPITAL_INFORMATION")
    @Expose
    private ClaimHospitalInformation claimHospitalInformation;
    @SerializedName("CLAIM_INCIDENT_INFORMATION")
    @Expose
    private ClaimIncidentInformation claimIncidentInformation;
    @SerializedName("CLAIM_AILMENT_INFORMATION")
    @Expose
    private ClaimAilmentInformation claimAilmentInformation;
    @SerializedName("CLAIM_PROCESS_INFORMATION")
    @Expose
    private ClaimProcessInformation claimProcessInformation;
    @SerializedName("CLAIM_CHARGES_INFORMATION")
    @Expose
    private ClaimChargesInformation claimChargesInformation;
    @SerializedName("CLAIM_PAYMENT_INFORMATION")
    @Expose
    private ClaimPaymentInformation claimPaymentInformation;
    @SerializedName("CLAIM_FILE_DT_INFORMATION")
    @Expose
    private ClaimFileDtInformation claimFileDtInformation;
    @SerializedName("CLAIM_CASHLESS_INFORMATION")
    @Expose
    private ClaimCashlessInformation claimCashlessInformation;
    @SerializedName("message")
    @Expose
    private Message message;

    public LoadDetailedClaimsValues getLoadDetailedClaimsValues() {
        return loadDetailedClaimsValues;
    }

    public void setLoadDetailedClaimsValues(LoadDetailedClaimsValues loadDetailedClaimsValues) {
        this.loadDetailedClaimsValues = loadDetailedClaimsValues;
    }

    public MemberInformation getMemberInformation() {
        return memberInformation;
    }

    public void setMemberInformation(MemberInformation memberInformation) {
        this.memberInformation = memberInformation;
    }

    public ClaimPolicyInformation getClaimPolicyInformation() {
        return claimPolicyInformation;
    }

    public void setClaimPolicyInformation(ClaimPolicyInformation claimPolicyInformation) {
        this.claimPolicyInformation = claimPolicyInformation;
    }

    public ClaimHospitalInformation getClaimHospitalInformation() {
        return claimHospitalInformation;
    }

    public void setClaimHospitalInformation(ClaimHospitalInformation claimHospitalInformation) {
        this.claimHospitalInformation = claimHospitalInformation;
    }

    public ClaimIncidentInformation getClaimIncidentInformation() {
        return claimIncidentInformation;
    }

    public void setClaimIncidentInformation(ClaimIncidentInformation claimIncidentInformation) {
        this.claimIncidentInformation = claimIncidentInformation;
    }

    public ClaimAilmentInformation getClaimAilmentInformation() {
        return claimAilmentInformation;
    }

    public void setClaimAilmentInformation(ClaimAilmentInformation claimAilmentInformation) {
        this.claimAilmentInformation = claimAilmentInformation;
    }

    public ClaimProcessInformation getClaimProcessInformation() {
        return claimProcessInformation;
    }

    public void setClaimProcessInformation(ClaimProcessInformation claimProcessInformation) {
        this.claimProcessInformation = claimProcessInformation;
    }

    public ClaimChargesInformation getClaimChargesInformation() {
        return claimChargesInformation;
    }

    public void setClaimChargesInformation(ClaimChargesInformation claimChargesInformation) {
        this.claimChargesInformation = claimChargesInformation;
    }

    public ClaimPaymentInformation getClaimPaymentInformation() {
        return claimPaymentInformation;
    }

    public void setClaimPaymentInformation(ClaimPaymentInformation claimPaymentInformation) {
        this.claimPaymentInformation = claimPaymentInformation;
    }

    public ClaimFileDtInformation getClaimFileDtInformation() {
        return claimFileDtInformation;
    }

    public void setClaimFileDtInformation(ClaimFileDtInformation claimFileDtInformation) {
        this.claimFileDtInformation = claimFileDtInformation;
    }

    public ClaimCashlessInformation getClaimCashlessInformation() {
        return claimCashlessInformation;
    }

    public void setClaimCashlessInformation(ClaimCashlessInformation claimCashlessInformation) {
        this.claimCashlessInformation = claimCashlessInformation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimsDetails{" +
                "loadDetailedClaimsValues=" + loadDetailedClaimsValues +
                ", memberInformation=" + memberInformation +
                ", claimPolicyInformation=" + claimPolicyInformation +
                ", claimHospitalInformation=" + claimHospitalInformation +
                ", claimIncidentInformation=" + claimIncidentInformation +
                ", claimAilmentInformation=" + claimAilmentInformation +
                ", claimProcessInformation=" + claimProcessInformation +
                ", claimChargesInformation=" + claimChargesInformation +
                ", claimPaymentInformation=" + claimPaymentInformation +
                ", claimFileDtInformation=" + claimFileDtInformation +
                ", claimCashlessInformation=" + claimCashlessInformation +
                ", message=" + message +
                '}';
    }
}
