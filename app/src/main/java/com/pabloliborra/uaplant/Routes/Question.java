package com.pabloliborra.uaplant.Routes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pabloliborra.uaplant.Utils.DataConverter;
import com.pabloliborra.uaplant.Utils.ListStringConverter;

import java.io.Serializable;
import java.util.List;

@Entity
public class Question implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String title;
    @TypeConverters(ListStringConverter.class)
    private List<String> answers;
    private String trueAnswer;

    private long activityId;

    public Question(String title, List<String> answers, String trueAnswer, long activityId) {
        this.title = title;
        this.answers = answers;
        this.trueAnswer = trueAnswer;
        this.activityId = activityId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }
}
