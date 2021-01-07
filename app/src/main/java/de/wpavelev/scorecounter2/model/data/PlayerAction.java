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
    int mValueToRestore = 0;

    /**
     * Spieler, dessen Wert verändert wird
     */
    int mPlayerChanged = 0;

    /**
     * ID des Wertes, falls nicht der letzte Wert geänder wurde
     */
    int mScoreId = -1;


    @Ignore
    public PlayerAction(int valueToRestore, int playerChanged) {
        mValueToRestore = valueToRestore;
        mPlayerChanged = playerChanged;
    }

    public PlayerAction(int valueToRestore, int playerChanged, int scoreId) {
        mValueToRestore = valueToRestore;
        mPlayerChanged = playerChanged;
        mScoreId = scoreId;
    }

    public int getValueToRestore() {
        return mValueToRestore;
    }

    public void setValueToRestore(int valueToRestore) {
        mValueToRestore = valueToRestore;
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
