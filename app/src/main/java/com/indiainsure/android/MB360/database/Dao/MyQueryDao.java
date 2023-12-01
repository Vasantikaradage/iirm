package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryResponse;

@Dao
public interface MyQueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMyQuery(QueryResponse queryResponse);

    @Query("SELECT * FROM QUERY_TABLE WHERE empSrNo = :empSrNo")
    QueryResponse getQuery(String empSrNo);

    @Query("DELETE FROM QUERY_TABLE")
    void deleteQuery();


    //Query details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQueryDetails(QueryDetails queryDetails);

    @Query("SELECT * FROM QUERY_DETAIL_LIST WHERE custQuerySrNo = :custQuerySrNo")
    QueryDetails getQueryDeatils(String custQuerySrNo);

    @Query("DELETE FROM QUERY_DETAIL_LIST")
    void deleteAllQueryDetails();
}
