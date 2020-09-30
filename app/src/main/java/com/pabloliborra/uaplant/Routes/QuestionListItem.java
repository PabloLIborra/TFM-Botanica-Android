package com.pabloliborra.uaplant.Routes;

import java.util.List;

public class QuestionListItem {
    private Question question;

    public QuestionListItem(Question question) {
        this.question = question;
    }

    public String getTitle() {
        return this.question.getTitle();
    }

    public List<String> getAnswers() {
        return this.question.getAnswers();
    }

    public String getTrueAnswer() {
        return this.question.getTrueAnswer();
    }
}
