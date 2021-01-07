package de.wpavelev.scorecounter2.model.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Player;
import de.wpavelev.scorecounter2.model.data.PlayerWithScore;

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

    @Query("SELECT * FROM Player ORDER BY playerId")
    LiveData<List<Player>> getPlayers();

    @Query("SELECT * FROM Player ORDER BY playerId")
    List<Player> getPlayerList();


    @Query("SELECT * FROM Player WHERE playerId = :playerId")
    Player getPlayerById(int playerId);

    @Transaction
    @Query("SELECT * FROM Player")
    LiveData<List<PlayerWithScore>> getPlayerWithScore();


}
