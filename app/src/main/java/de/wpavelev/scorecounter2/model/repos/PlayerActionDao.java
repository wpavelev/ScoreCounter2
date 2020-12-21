package de.wpavelev.scorecounter2.model.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Name;
import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.PlayerAction;

@Dao
public interface PlayerActionDao {
    @Insert
    void insert(PlayerAction playerAction);

    @Update
    void update(PlayerAction playerAction);

    @Delete
    void delete(PlayerAction playerAction);

    @Query("DELETE FROM PlayerAction")
    void deleteAll();

    @Query("SELECT * FROM PlayerAction ORDER BY id")
    LiveData<List<PlayerAction>> getAllPlayerActions();

    @Query(("SELECT * FROM PlayerAction ORDER BY id DESC LIMIT 1"))
    PlayerAction getLastAction();
}
