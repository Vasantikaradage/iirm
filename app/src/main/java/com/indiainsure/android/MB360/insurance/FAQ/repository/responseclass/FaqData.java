package com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqData {
    @SerializedName("Faq_Sr_No")
    @Expose
    private String faqSrNo;
    @SerializedName("Faq_Question")
    @Expose
    private String faqQuestion;
    @SerializedName("Faq_Ans")
    @Expose
    private String faqAns;
    @SerializedName("Faq_Order")
    @Expose
    private String faqOrder;

    private boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public String getFaqSrNo() {
        return faqSrNo;
    }

    public void setFaqSrNo(String faqSrNo) {
        this.faqSrNo = faqSrNo;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqAns() {
        return faqAns;
    }

    public void setFaqAns(String faqAns) {
        this.faqAns = faqAns;
    }

    public String getFaqOrder() {
        return faqOrder;
    }

    public void setFaqOrder(String faqOrder) {
        this.faqOrder = faqOrder;
    }

    @Override
    public String toString() {
        return "FaqData{" +
                "faqSrNo='" + faqSrNo + '\'' +
                ", faqQuestion='" + faqQuestion + '\'' +
                ", faqAns='" + faqAns + '\'' +
                ", faqOrder='" + faqOrder + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
