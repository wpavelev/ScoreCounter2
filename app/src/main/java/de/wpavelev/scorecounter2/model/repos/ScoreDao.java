package de.wpavelev.scorecounter2.model.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.stuff.Score;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);

    @Update
    void update(Score score);

    @Delete
    void delete(Score score);


    @Query("DELETE FROM Score")
    void deleteAll();

    @Query("SELECT * FROM Score ORDER BY id")
    LiveData<List<Score>> getAllScores();

    @Query("SELECT * FROM Score WHERE player = :playerId ORDER BY id DESC LIMIT 1")
    LiveData<Score> getLastScore(String playerId);
}