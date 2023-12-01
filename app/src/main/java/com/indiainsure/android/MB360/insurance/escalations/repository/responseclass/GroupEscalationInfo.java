package com.indiainsure.android.MB360.insurance.escalations.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupEscalationInfo {
    @SerializedName("PERSON_ID")
    @Expose
    private String personId;
    @SerializedName("ADDRESS_ID")
    @Expose
    private String addressId;
    @SerializedName("EMAIL_ID")
    @Expose
    private String emailId;
    @SerializedName("NUMBER_ID")
    @Expose
    private String numberId;
    @SerializedName("DESCRIPTION")
    @Expose
    private String description;
    @SerializedName("ADDRESS")
    @Expose
    private String address;
    @SerializedName("CONTACT_PERSON")
    @Expose
    private String contactPerson;
    @SerializedName("LANDLINE_NO")
    @Expose
    private String landlineNo;
    @SerializedName("MOBILE_NO")
    @Expose
    private String mobileNo;
    @SerializedName("FAX_NO")
    @Expose
    private String faxNo;
    @SerializedName("EMAIL")
    @Expose
    private String email;
    @SerializedName("ESCALATION")
    @Expose
    private String escalation;
    @SerializedName("ADDITIONAL_TEXT")
    @Expose
    private String additionalText;
    @SerializedName("DISP_EMAIL")
    @Expose
    private String dispEmail;
    @SerializedName("DISP_MOB")
    @Expose
    private String dispMob;
    @SerializedName("DISP_ADD")
    @Expose
    private String dispAdd;
    @SerializedName("DISP_FAX")
    @Expose
    private String dispFax;
    @SerializedName("DISP_EMAIL_HR")
    @Expose
    private String dispEmailHr;
    @SerializedName("DISP_MOB_HR")
    @Expose
    private String dispMobHr;
    @SerializedName("DISP_ADD_HR")
    @Expose
    private String dispAddHr;
    @SerializedName("DISP_FAX_HR")
    @Expose
    private String dispFaxHr;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLandlineNo() {
        return landlineNo;
    }

    public void setLandlineNo(String landlineNo) {
        this.landlineNo = landlineNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEscalation() {
        return escalation;
    }

    public void setEscalation(String escalation) {
        this.escalation = escalation;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public String getDispEmail() {
        return dispEmail;
    }

    public void setDispEmail(String dispEmail) {
        this.dispEmail = dispEmail;
    }

    public String getDispMob() {
        return dispMob;
    }

    public void setDispMob(String dispMob) {
        this.dispMob = dispMob;
    }

    public String getDispAdd() {
        return dispAdd;
    }

    public void setDispAdd(String dispAdd) {
        this.dispAdd = dispAdd;
    }

    public String getDispFax() {
        return dispFax;
    }

    public void setDispFax(String dispFax) {
        this.dispFax = dispFax;
    }

    public String getDispEmailHr() {
        return dispEmailHr;
    }

    public void setDispEmailHr(String dispEmailHr) {
        this.dispEmailHr = dispEmailHr;
    }

    public String getDispMobHr() {
        return dispMobHr;
    }

    public void setDispMobHr(String dispMobHr) {
        this.dispMobHr = dispMobHr;
    }

    public String getDispAddHr() {
        return dispAddHr;
    }

    public void setDispAddHr(String dispAddHr) {
        this.dispAddHr = dispAddHr;
    }

    public String getDispFaxHr() {
        return dispFaxHr;
    }

    public void setDispFaxHr(String dispFaxHr) {
        this.dispFaxHr = dispFaxHr;
    }

    @Override
    public String toString() {
        return "GroupEscalationInfo{" +
                "personId='" + personId + '\'' +
                ", addressId='" + addressId + '\'' +
                ", emailId='" + emailId + '\'' +
                ", numberId='" + numberId + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", landlineNo='" + landlineNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", faxNo='" + faxNo + '\'' +
                ", email='" + email + '\'' +
                ", escalation='" + escalation + '\'' +
                ", additionalText='" + additionalText + '\'' +
                ", dispEmail='" + dispEmail + '\'' +
                ", dispMob='" + dispMob + '\'' +
                ", dispAdd='" + dispAdd + '\'' +
                ", dispFax='" + dispFax + '\'' +
                ", dispEmailHr='" + dispEmailHr + '\'' +
                ", dispMobHr='" + dispMobHr + '\'' +
                ", dispAddHr='" + dispAddHr + '\'' +
                ", dispFaxHr='" + dispFaxHr + '\'' +
                '}';
    }
}
