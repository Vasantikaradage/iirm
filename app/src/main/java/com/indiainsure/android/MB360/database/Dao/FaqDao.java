package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;

@Dao
public interface FaqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFAQ(FaqResponse faqResponse);

    @Query("SELECT * FROM FAQ WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    FaqResponse getFAQ(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM FAQ")
    void deleteAllFAQ();
}
