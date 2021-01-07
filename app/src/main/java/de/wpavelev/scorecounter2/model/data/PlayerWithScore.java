package de.wpavelev.scorecounter2.model.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PlayerWithScore {

    @Embedded
    public Player mPlayer;

    @Relation(parentColumn = "playerId", entityColumn = "player")
    public List<Score> mPlayerScores;




}
