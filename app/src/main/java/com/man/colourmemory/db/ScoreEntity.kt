package com.man.colourmemory.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
class ScoreEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @NonNull
    var name: String = ""

    @NonNull
    var score: Int = 0

}
