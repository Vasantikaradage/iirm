package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;

@Dao
public interface CoverageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoverage(CoverageResponse coverageResponse);

    @Query("SELECT * FROM Coverage WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    CoverageResponse getCoverage(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM Coverage")
    void deleteAllCoverage();

    //Coverage Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoverageDetails(CoverageDetailsResponse coverageDetailsResponse);

    @Query("SELECT * FROM COVERAGE_DETAILS WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    CoverageDetailsResponse getCoverageDetails(String oeGrpBasInfoSrNo);


}
