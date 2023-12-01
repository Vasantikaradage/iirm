package com.indiainsure.android.MB360.insurance.queries.responseclass;

public class QueryFaqModel {
    String Question;
    String answer;
    Boolean expanded;

    public QueryFaqModel(String question, String answer) {
        Question = question;
        this.answer = answer;
        this.expanded = false;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "QueryFaqModel{" +
                "Question='" + Question + '\'' +
                ", answer='" + answer + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
