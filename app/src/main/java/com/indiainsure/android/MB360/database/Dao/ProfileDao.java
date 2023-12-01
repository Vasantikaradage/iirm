package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(ProfileResponse  profileResponse);

    @Query("SELECT * FROM PROFILE WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    ProfileResponse getProfile(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM PROFILE")
    void deleteProfile();


}
