package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;

@Dao
public interface ClaimsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaims(ClaimsResponse claimsResponse);

    @Query("SELECT * FROM INTIMATE_CLAIMS WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    ClaimsResponse getClaims(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM INTIMATE_CLAIMS")
    void deleteAllClaims();


    //loadPerson
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoadPerson(LoadPersonsIntimationResponse loadPersonsIntimationResponse);

    @Query("SELECT * FROM INTIMATE_CLAIMS_RELATION WHERE oeGrpBasInfoSrNo= :oeGrpBasInfoSrNo")
    LoadPersonsIntimationResponse getLoadPerson(String oeGrpBasInfoSrNo );
}
