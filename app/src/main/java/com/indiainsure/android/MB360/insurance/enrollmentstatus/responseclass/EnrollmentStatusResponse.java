package com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass;

public class EnrollmentStatusResponse {

    EnrollmentStatus enrollmentStatus = null;
    Boolean errorState = false;
    Boolean loadingState = true;

    public EnrollmentStatus getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Boolean getErrorState() {
        return errorState;
    }

    public void setErrorState(Boolean errorState) {
        this.errorState = errorState;
    }

    public Boolean getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(Boolean loadingState) {
        this.loadingState = loadingState;
    }

    @Override
    public String toString() {
        return "EnrollmentStatusResponse{" +
                "enrollmentStatus=" + enrollmentStatus +
                ", errorState=" + errorState +
                ", loadingState=" + loadingState +
                '}';
    }
}
