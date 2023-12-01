package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;

@Dao
public interface UtilitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUtilities(UtilitiesResponse utilitiesResponse);

    @Query("SELECT * FROM UTILITIES WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    UtilitiesResponse getUtilities(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM UTILITIES")
    void deleteAllUtilities();
}
