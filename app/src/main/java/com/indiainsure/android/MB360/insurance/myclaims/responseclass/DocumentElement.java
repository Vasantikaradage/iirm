package com.indiainsure.android.MB360.insurance.myclaims.responseclass;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.indiainsure.android.MB360.database.converters.MyClaimsConverters.ClaimInformationConverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DocumentElement")
@Entity(tableName = "MY_CLAIMS")
public class DocumentElement {

    @Element(name = "status")
    public String status;
    @Element(name = "ClaimsInformation")
    @TypeConverters(ClaimInformationConverter.class)
    public ClaimsInformation ClaimsInformation;

    @PrimaryKey
    public int oeGrpBasInfoSrNo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClaimsInformation getClaimsInformation() {
        return ClaimsInformation;
    }

    public void setClaimsInformation(ClaimsInformation claimsInformation) {
        ClaimsInformation = claimsInformation;
    }

    @Override
    public String toString() {
        return "DocumentElement{" +
                "status='" + status + '\'' +
                ", ClaimsInformation=" + ClaimsInformation +
                '}';
    }
}
