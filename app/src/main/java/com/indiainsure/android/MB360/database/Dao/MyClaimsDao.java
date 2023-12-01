package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;

@Dao
public interface MyClaimsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocumentElement(DocumentElement documentElement);

    @Query("SELECT * FROM MY_CLAIMS")
    DocumentElement getDocumentElement();

    //claims details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimDetails(ClaimsDetails claimsDetails);

    @Query("SELECT * FROM MY_CLAIMS_DETAIL_TABLE WHERE claimSrNo=:claimSrNo")
    ClaimsDetails getClaimDeatils(String claimSrNo);
}
