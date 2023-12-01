package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.indiainsure.android.MB360.database.converters.hospitalConverters.HospitalElementCountConverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DocumentElement")
@Entity(tableName = "HOSPITAL_COUNT" , indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))

public class DocumentElementCount {
    @Element(name = "Hospitals", required = false)
    @TypeConverters(HospitalElementCountConverter.class)
    public HospitalsCount HospitalsCount;

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

    public HospitalsCount getHospitalsCount() {
        return HospitalsCount;
    }

    public void setHospitalsCount(HospitalsCount hospitalsCount) {
        HospitalsCount = hospitalsCount;
    }

    @Override
    public String toString() {
        return "DocumentElementCount{" +
                "HospitalsCount=" + HospitalsCount +
                '}';
    }
}
