package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.servicenames.responseclass.ServiceNamesResponse;

@Dao
public interface ServiceNamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertServiceName(ServiceNamesResponse serviceNamesResponse);

    @Query("SELECT * FROM SERVICE_NAMES WHERE strGroupChildSrno = :groupChildSrNo")
    ServiceNamesResponse getServiceNames(String groupChildSrNo);


}
