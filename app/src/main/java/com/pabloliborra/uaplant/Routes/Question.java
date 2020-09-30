package com.pabloliborra.uaplant.Routes;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private String title;
    private List<String> answers;
    private String trueAnswer;

    public Question(String title, List<String> answers, String trueAnswer) {
        this.title = title;
        this.answers = answers;
        this.trueAnswer = trueAnswer;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }
}
