package de.wpavelev.scorecounter2.model.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Player {

    @PrimaryKey(autoGenerate = true)
    int playerId;

    private String name;

    @Ignore
    private int score;

    private int activePlayer;

    private int qwirkle;

    public Player(String name) {
        this.name = name;
    }


    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQwirkle() {
        return qwirkle;
    }

    public void setQwirkle(int qwirkle) {
        this.qwirkle = qwirkle;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
}
