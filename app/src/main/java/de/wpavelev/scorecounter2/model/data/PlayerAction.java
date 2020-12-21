package de.wpavelev.scorecounter2.model.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Beinhaltet eine Aktion, die der User durchführt
 */
@Entity
public class PlayerAction {


    @PrimaryKey(autoGenerate = true)
    int id;

    /**
     * Veränderung eines Wertes bei einem Spieler
     */
    int mValueChange = 0;

    /**
     * Spieler, dessen Wert verändert wird
     */
    int mPlayerChanged = 0;

    /**
     * ID des Wertes, falls nicht der letzte Wert geänder wurde
     */
    int mScoreId = -1;


    @Ignore
    public PlayerAction(int valueChange, int playerChanged) {
        mValueChange = valueChange;
        mPlayerChanged = playerChanged;
    }

    public PlayerAction(int valueChange, int playerChanged, int scoreId) {
        mValueChange = valueChange;
        mPlayerChanged = playerChanged;
        mScoreId = scoreId;
    }

    public int getValueChange() {
        return mValueChange;
    }

    public void setValueChange(int valueChange) {
        mValueChange = valueChange;
    }

    public int getPlayerChanged() {
        return mPlayerChanged;
    }

    public void setPlayerChanged(int playerChanged) {
        mPlayerChanged = playerChanged;
    }

    public int getScoreId() {
        return mScoreId;
    }

    public void setScoreId(int scoreId) {
        mScoreId = scoreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
