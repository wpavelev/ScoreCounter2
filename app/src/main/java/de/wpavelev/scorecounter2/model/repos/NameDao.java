package de.wpavelev.scorecounter2.model.repos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.wpavelev.scorecounter2.model.data.Name;

@Dao
public interface NameDao {
    @Insert
    void insert(Name name);

    @Update
    void update(Name name);

    @Delete
    void delete(Name name);


    @Query("DELETE FROM Name")
    void deleteAll();

    @Query("SELECT * FROM Name ORDER BY name")
    LiveData<List<Name>> getNames();

}
