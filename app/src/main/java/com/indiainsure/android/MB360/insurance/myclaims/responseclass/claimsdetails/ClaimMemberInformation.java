package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimMemberInformation {
    public String BeneficiaryName;
    public String EmployeeName;
    public String EmployeeNo;
    public String Age;
    public String Relation;
    public String DateOfBirth;
    public String Gender;
    public String Grade;
    public String PlantDepartment;
    public String City;
    public String SumInsured;
    public String TPAId;

    public String getBeneficiaryName() {
        return BeneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getEmployeeNo() {
        return EmployeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        EmployeeNo = employeeNo;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getRelation() {
        return Relation;
    }

    public void setRelation(String relation) {
        Relation = relation;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getPlantDepartment() {
        return PlantDepartment;
    }

    public void setPlantDepartment(String plantDepartment) {
        PlantDepartment = plantDepartment;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSumInsured() {
        return SumInsured;
    }

    public void setSumInsured(String sumInsured) {
        SumInsured = sumInsured;
    }

    public String getTPAId() {
        return TPAId;
    }

    public void setTPAId(String TPAId) {
        this.TPAId = TPAId;
    }

    @Override
    public String toString() {
        return "ClaimMemberInformation{" +
                "BeneficiaryName='" + BeneficiaryName + '\'' +
                ", EmployeeName='" + EmployeeName + '\'' +
                ", EmployeeNo=" + EmployeeNo +
                ", Age=" + Age +
                ", Relation='" + Relation + '\'' +
                ", DateOfBirth='" + DateOfBirth + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Grade='" + Grade + '\'' +
                ", PlantDepartment='" + PlantDepartment + '\'' +
                ", City='" + City + '\'' +
                ", SumInsured=" + SumInsured +
                ", TPAId='" + TPAId + '\'' +
                '}';
    }
}
