package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.indiainsure.android.MB360.database.converters.MyClaimsConverters.MyClaimsDetailsConverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DocumentElement")
@Entity(tableName = "MY_CLAIMS_DETAIL_TABLE", indices = @Index(value = {"claimSrNo"}, unique = true))
public class ClaimsDetails {

    @Element(name = "status")
    public String status;
    @Element(name = "ClaimInformation")
    @TypeConverters(MyClaimsDetailsConverter.class)
    public ClaimInformation ClaimInformation;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String claimSrNo;

    @NonNull
    public String getClaimSrNo() {
        return claimSrNo;
    }

    public void setClaimSrNo(@NonNull String claimSrNo) {
        this.claimSrNo = claimSrNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimInformation getClaimInformation() {
        return ClaimInformation;
    }

    public void setClaimInformation(com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimInformation claimInformation) {
        ClaimInformation = claimInformation;
    }

    @Override
    public String toString() {
        return "ClaimsDetails{" +
                "status='" + status + '\'' +
                ", ClaimInformation=" + ClaimInformation +
                '}';
    }
}
