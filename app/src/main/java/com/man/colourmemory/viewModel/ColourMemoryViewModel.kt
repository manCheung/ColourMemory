package com.man.colourmemory.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.man.colourmemory.db.CardRepositoryImp
import com.man.colourmemory.model.CardItemModel
import com.man.colourmemory.db.DatabaseRepository
import com.man.colourmemory.db.DatabaseRepositoryImp
import com.man.colourmemory.db.ScoreEntity
import com.man.colourmemory.model.ScoreRecordItem
import kotlinx.coroutines.launch

class ColourMemoryViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    var listMemoryCardLiveDataModel: MutableLiveData<List<CardItemModel>> = MutableLiveData()
    val score: MutableLiveData<Int> = MutableLiveData()
    val isWin: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: MutableLiveData<Boolean> = MutableLiveData()
    var currentRank: Int = -1;

    private var numOfMatchCardFound = 0

    init {
        isDone.value = false
        isWin.value = false
        score.value = 0
        getCardData()
    }

    private fun getCardData() {
        repository.getItems(object : CardRepositoryImp.ItemCallback {
            override fun onItemsResult(itemModels: List<CardItemModel>) {
                viewModelScope.launch {
                    listMemoryCardLiveDataModel.value = itemModels
                }
            }
        })
    }

    fun matchCardFound() {
        // Update number of matches found
        numOfMatchCardFound++
        // Update score
        score.value = score.value?.plus(2)

        if (numOfMatchCardFound == listMemoryCardLiveDataModel.value!!.count() / 2) {
            isWin.value = true
        }
    }

    fun matchCardNotFound() {
        score.value = score.value?.minus(1)
    }

    fun insertScore(name: String, score: Int) {
        repository.insertScore(getScoreDetails(name, score))
    }


    private fun getScoreDetails(name: String, score: Int): ScoreEntity {
        val scoreEntity = ScoreEntity()

        scoreEntity.name = name
        scoreEntity.score = score

        return scoreEntity
    }

    fun submittedScoreForm() {
        isWin.value = false
        isDone.value = true
    }

    fun getCurrentRank(score: Int) {
        repository.getCurrentRank(score, (object : DatabaseRepositoryImp.RankCallback {
            override fun onRankResult(rank: Int) {
                currentRank = rank + 1
            }

        }))
    }

}