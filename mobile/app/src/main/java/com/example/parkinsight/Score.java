package com.example.parkinsight;

import java.util.Date;

public class Score {
    public Date date;
    public String score;

    public Score(Date date, String score) {
        this.date = date;
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }
}
