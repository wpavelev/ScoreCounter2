package de.wpavelev.scorecounter2.model.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Score;

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

    @Query("SELECT * FROM Score ORDER BY scoreId DESC")
    LiveData<List<Score>> getScores();

    @Query("SELECT * FROM Score ORDER BY scoreId DESC")
    List<Score> getScoreList();

    @Query("SELECT * FROM Score WHERE scoreId = :id")
    Score getScore(int id);



}
