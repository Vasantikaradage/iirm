package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimProcLayoutInfo {
    @SerializedName("CLM_PROC_LAYOUT_INF_SR_NO")
    @Expose
    private String clmProcLayoutInfSrNo;
    @SerializedName("LAYOUT_PART")
    @Expose
    private String layoutPart;
    @SerializedName("LAYOUT_PART_VISIBILITY")
    @Expose
    private String layoutPartVisibility;
    @SerializedName("LAYOUT_OF_PRODUCT")
    @Expose
    private String layoutOfProduct;
    @SerializedName("LAYOUT_OF_CLAIM_PROC")
    @Expose
    private String layoutOfClaimProc;
    @SerializedName("GROUPCHILDSRNO")
    @Expose
    private String groupchildsrno;
    @SerializedName("OE_GRP_BAS_INF_SR_NO")
    @Expose
    private String oeGrpBasInfSrNo;

    public String getClmProcLayoutInfSrNo() {
        return clmProcLayoutInfSrNo;
    }

    public void setClmProcLayoutInfSrNo(String clmProcLayoutInfSrNo) {
        this.clmProcLayoutInfSrNo = clmProcLayoutInfSrNo;
    }

    public String getLayoutPart() {
        return layoutPart;
    }

    public void setLayoutPart(String layoutPart) {
        this.layoutPart = layoutPart;
    }

    public String getLayoutPartVisibility() {
        return layoutPartVisibility;
    }

    public void setLayoutPartVisibility(String layoutPartVisibility) {
        this.layoutPartVisibility = layoutPartVisibility;
    }

    public String getLayoutOfProduct() {
        return layoutOfProduct;
    }

    public void setLayoutOfProduct(String layoutOfProduct) {
        this.layoutOfProduct = layoutOfProduct;
    }

    public String getLayoutOfClaimProc() {
        return layoutOfClaimProc;
    }

    public void setLayoutOfClaimProc(String layoutOfClaimProc) {
        this.layoutOfClaimProc = layoutOfClaimProc;
    }

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

    @Override
    public String toString() {
        return "ClaimProcLayoutInfo{" +
                "clmProcLayoutInfSrNo='" + clmProcLayoutInfSrNo + '\'' +
                ", layoutPart='" + layoutPart + '\'' +
                ", layoutPartVisibility='" + layoutPartVisibility + '\'' +
                ", layoutOfProduct='" + layoutOfProduct + '\'' +
                ", layoutOfClaimProc='" + layoutOfClaimProc + '\'' +
                ", groupchildsrno='" + groupchildsrno + '\'' +
                ", oeGrpBasInfSrNo='" + oeGrpBasInfSrNo + '\'' +
                '}';
    }
}
