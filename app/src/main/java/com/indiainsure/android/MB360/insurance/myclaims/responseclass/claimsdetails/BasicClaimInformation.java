package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class BasicClaimInformation {
    public int ClaimSrNo;
    public String ClaimNo;
    public int UniqueClaimNo;
    public String GroupCode;
    public int EmployeeNo;
    public int EmployeeSrNo;


    public int getClaimSrNo() {
        return ClaimSrNo;
    }

    public void setClaimSrNo(int claimSrNo) {
        ClaimSrNo = claimSrNo;
    }

    public String getClaimNo() {
        return ClaimNo;
    }

    public void setClaimNo(String claimNo) {
        ClaimNo = claimNo;
    }

    public int getUniqueClaimNo() {
        return UniqueClaimNo;
    }

    public void setUniqueClaimNo(int uniqueClaimNo) {
        UniqueClaimNo = uniqueClaimNo;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public int getEmployeeNo() {
        return EmployeeNo;
    }

    public void setEmployeeNo(int employeeNo) {
        EmployeeNo = employeeNo;
    }

    public int getEmployeeSrNo() {
        return EmployeeSrNo;
    }

    public void setEmployeeSrNo(int employeeSrNo) {
        EmployeeSrNo = employeeSrNo;
    }

    @Override
    public String toString() {
        return "BasicClaimInformation{" +
                "ClaimSrNo=" + ClaimSrNo +
                ", ClaimNo='" + ClaimNo + '\'' +
                ", UniqueClaimNo=" + UniqueClaimNo +
                ", GroupCode='" + GroupCode + '\'' +
                ", EmployeeNo=" + EmployeeNo +
                ", EmployeeSrNo=" + EmployeeSrNo +
                '}';
    }
}
