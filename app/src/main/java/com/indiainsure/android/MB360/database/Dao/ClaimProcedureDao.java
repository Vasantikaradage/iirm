package com.indiainsure.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;

@Dao
public interface ClaimProcedureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimsProcedureLayoutInfo(ClaimsProcedureLayoutInfoResponse claimsProcedureLayoutInfoResponse);

    @Query("SELECT * FROM CLAIM_PROCEDURE_LAYOUT_INFO WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo AND layoutOfClaim= :layoutOfClaim")
    ClaimsProcedureLayoutInfoResponse getClaimsProcedureLayoutInfo(String oeGrpBasInfoSrNo,String layoutOfClaim);

    @Query("DELETE FROM CLAIM_PROCEDURE_LAYOUT_INFO")
    void deleteAllClaimsProcedureLayout();


    // EmergencyContact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimProcedureEmergencyContact(ClaimProcedureEmergencyContactResponse claimProcedureEmergencyContactResponse);

    @Query("SELECT * FROM CLAIM_PROCEDURE_EMERGENCY_CONTACT WHERE tpa_code = :tpa_code")
    ClaimProcedureEmergencyContactResponse getClaimsProcedureEmergencyContact(String tpa_code);

    //load image
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimProcedureImage(ClaimProcedureImageResponse claimProcedureImageResponse);

    @Query("SELECT * FROM CLAIM_PROCEDURE_IMAGE WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo AND layoutOfClaim= :layoutOfClaim")
    ClaimProcedureImageResponse getClaimsProcedureImage(String oeGrpBasInfoSrNo,String layoutOfClaim);

    //ClaimProcedureLayoutInstruction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimProcedureLayoutInstruction(ClaimProcedureLayoutInstructionInfo claimProcedureLayoutInstructionInfo);

    @Query("SELECT * FROM CLAIM_PROCEDURE_LAYOUT_INSTRUCTION WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo AND layoutOfClaim = :layoutOfClaim")
    ClaimProcedureLayoutInstructionInfo getClaimProcedureLayoutInstruction(String oeGrpBasInfoSrNo,String layoutOfClaim);


    // ClaimProcedureTextPath
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimProcedureTextPath(ClaimProcedureTextResponse claimProcedureTextResponse);

    @Query("SELECT * FROM CLAIM_PROCEDURE_TEXT_PATH WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    ClaimProcedureTextResponse getClaimProcedureTextPath(String oeGrpBasInfoSrNo);
}
