package com.man.colourmemory.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.man.colourmemory.model.ScoreRecordItem

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ScoreEntity): Long

    @Query("SELECT id, name, score FROM scores order by score desc, id asc")
    fun getAll(): List<ScoreRecordItem>

    @Query("SELECT count(*) FROM scores where score >= :score")
    fun getCurrentRank(score: Int): Int


}