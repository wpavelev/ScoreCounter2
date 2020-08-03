package de.wpavelev.scorecounter2.model.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {

    @PrimaryKey(autoGenerate = true)
    int id;

    private String name;

    private int score;

    private int lastScore;


    public Player(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }
}
