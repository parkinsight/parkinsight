package com.example.parkinsight;

import java.util.Date;

public class Score {
    public Date date;
    public int score;

    public Score(Date date, int score) {
        this.date = date;
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }
}
