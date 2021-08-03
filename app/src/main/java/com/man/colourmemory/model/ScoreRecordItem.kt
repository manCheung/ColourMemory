package com.man.colourmemory.model

import androidx.room.Ignore

data class ScoreRecordItem(
    var id: Int,
    var name: String,
    var score: Int
) {
    @Ignore
    var rank: Int = 0
}