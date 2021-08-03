package com.man.colourmemory.db

import com.man.colourmemory.R
import com.man.colourmemory.model.CardItemModel
import com.man.colourmemory.model.ScoreRecordItem
import java.util.*

interface DatabaseRepositoryImp {

    fun insertScore(scoreDetail: ScoreEntity)

    fun getScores(itemCallback: ItemCallback)

    fun getCurrentRank(score: Int,rankCallback: RankCallback)

    interface ItemCallback {
        fun onItemsResult(items: List<ScoreRecordItem>)
    }

    interface RankCallback {
        fun onRankResult(rank: Int)
    }

}

interface CardRepositoryImp {
    fun getItems(itemCallback: ItemCallback)

    interface ItemCallback {
        fun onItemsResult(itemModels: List<CardItemModel>)
    }
}

class DatabaseRepository constructor(database: RoomDbHelper) :
    DatabaseRepositoryImp, CardRepositoryImp {

    private val rawMemoryDrawableList = listOf(
        R.drawable.colour1,
        R.drawable.colour2,
        R.drawable.colour3,
        R.drawable.colour4,
        R.drawable.colour5,
        R.drawable.colour6,
        R.drawable.colour7,
        R.drawable.colour8
    )

    private var database: RoomDbHelper = database

    override fun insertScore(scoreDetail: ScoreEntity) {
        database.getScoreDao().insert(scoreDetail)
    }

    override fun getScores(itemCallback: DatabaseRepositoryImp.ItemCallback) {
        val list = mutableListOf<ScoreRecordItem>()

        database.getScoreDao().getAll().forEachIndexed {index, it ->
            val scoreItem = ScoreRecordItem(
                id = it.id,
                name = it.name,
                score = it.score
            )
            scoreItem.rank = index + 1
            list.add(scoreItem)
        }

        itemCallback.onItemsResult(list)

    }

    override fun getCurrentRank(score: Int, rankCallback: DatabaseRepositoryImp.RankCallback) {
        rankCallback.onRankResult(database.getScoreDao().getCurrentRank(score))
    }

    override fun getItems(itemCallback: CardRepositoryImp.ItemCallback) {
        val list = mutableListOf<CardItemModel>()

        val duplicateMemoryDrawableList = mutableListOf<Int>()

        (0..1).forEach { _ ->
            duplicateMemoryDrawableList.addAll(rawMemoryDrawableList.toList())
        }

        duplicateMemoryDrawableList.shuffle();

        duplicateMemoryDrawableList.forEach {
            list.add(
                CardItemModel(
                    imageSrc = it,
                    isSelected = false,
                    isMatched = false
                )
            )
        }
        itemCallback.onItemsResult(list)
    }


}
