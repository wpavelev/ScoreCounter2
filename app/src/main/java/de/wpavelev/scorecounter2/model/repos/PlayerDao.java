package de.wpavelev.scorecounter2.model.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Player;

@Dao
public interface PlayerDao {
    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);


    @Query("DELETE FROM Player")
    void deleteAll();

    @Query("SELECT * FROM Player ORDER BY id")
    LiveData<List<Player>> getAllPlayer();

    @Query("SELECT * FROM Player WHERE id = :playerId LIMIT 1")
    LiveData<Player> getPlayer(int playerId);

    @Query("SELECT * FROM Player WHERE name LIKE :playerName LIMIT 1")
    LiveData<Player> getPlayerByName(String playerName);

}
