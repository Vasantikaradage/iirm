package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimInformation {
    public BasicClaimInformation BasicClaimInformation;
    public ClaimMemberInformation ClaimMemberInformation;
    public ClaimIncidentInformation ClaimIncidentInformation;
    public ClaimProcessInformation ClaimProcessInformation;
    public ClaimCashLessInformation ClaimCashlessInformation;
    public ClaimHospitalInformation ClaimHospitalInformation;
    public ClaimAilmentInformation ClaimAilmentInformation;
    public ClaimChargesInformation ClaimChargesInformation;
    public ClaimFileInformation ClaimFileInformation;
    public ClaimPaymentInformation ClaimPaymentInformation;

    public BasicClaimInformation getBasicClaimInformation() {
        return BasicClaimInformation;
    }

    public void setBasicClaimInformation(BasicClaimInformation basicClaimInformation) {
        BasicClaimInformation = basicClaimInformation;
    }

    public ClaimMemberInformation getClaimMemberInformation() {
        return ClaimMemberInformation;
    }

    public void setClaimMemberInformation(ClaimMemberInformation claimMemberInformation) {
        ClaimMemberInformation = claimMemberInformation;
    }

    public ClaimIncidentInformation getClaimIncidentInformation() {
        return ClaimIncidentInformation;
    }

    public void setClaimIncidentInformation(ClaimIncidentInformation claimIncidentInformation) {
        ClaimIncidentInformation = claimIncidentInformation;
    }

    public ClaimProcessInformation getClaimProcessInformation() {
        return ClaimProcessInformation;
    }

    public void setClaimProcessInformation(ClaimProcessInformation claimProcessInformation) {
        ClaimProcessInformation = claimProcessInformation;
    }

    public ClaimCashLessInformation getClaimCashlessInformation() {
        return ClaimCashlessInformation;
    }

    public void setClaimCashlessInformation(ClaimCashLessInformation claimCashlessInformation) {
        ClaimCashlessInformation = claimCashlessInformation;
    }

    public ClaimHospitalInformation getClaimHospitalInformation() {
        return ClaimHospitalInformation;
    }

    public void setClaimHospitalInformation(ClaimHospitalInformation claimHospitalInformation) {
        ClaimHospitalInformation = claimHospitalInformation;
    }

    public ClaimAilmentInformation getClaimAilmentInformation() {
        return ClaimAilmentInformation;
    }

    public void setClaimAilmentInformation(ClaimAilmentInformation claimAilmentInformation) {
        ClaimAilmentInformation = claimAilmentInformation;
    }

    public ClaimChargesInformation getClaimChargesInformation() {
        return ClaimChargesInformation;
    }

    public void setClaimChargesInformation(ClaimChargesInformation claimChargesInformation) {
        ClaimChargesInformation = claimChargesInformation;
    }

    public ClaimFileInformation getClaimFileInformation() {
        return ClaimFileInformation;
    }

    public void setClaimFileInformation(ClaimFileInformation claimFileInformation) {
        ClaimFileInformation = claimFileInformation;
    }

    public ClaimPaymentInformation getClaimPaymentInformation() {
        return ClaimPaymentInformation;
    }

    public void setClaimPaymentInformation(ClaimPaymentInformation claimPaymentInformation) {
        ClaimPaymentInformation = claimPaymentInformation;
    }

    @Override
    public String toString() {
        return "ClaimInformation{" +
                "BasicClaimInformation=" + BasicClaimInformation +
                ", ClaimMemberInformation=" + ClaimMemberInformation +
                ", ClaimIncidentInformation=" + ClaimIncidentInformation +
                ", ClaimProcessInformation=" + ClaimProcessInformation +
                ", ClaimCashlessInformation=" + ClaimCashlessInformation +
                ", ClaimHospitalInformation=" + ClaimHospitalInformation +
                ", ClaimAilmentInformation=" + ClaimAilmentInformation +
                ", ClaimChargesInformation=" + ClaimChargesInformation +
                ", ClaimFileInformation=" + ClaimFileInformation +
                ", ClaimPaymentInformation=" + ClaimPaymentInformation +
                '}';
    }
}
